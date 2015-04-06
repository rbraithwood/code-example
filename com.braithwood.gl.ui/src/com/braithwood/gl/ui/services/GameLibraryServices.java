package com.braithwood.gl.ui.services;

import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.model.BoardGame;
import com.braithwood.gl.ui.model.CardGame;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.model.GameLibrary;
import com.braithwood.gl.ui.model.VideoGame;

public class GameLibraryServices {

	private static GameLibraryServices INSTANCE;

	private GameLibrary library;
	private SortedSet<Game> games;

	// singleton
	private GameLibraryServices() {
		super();

		loadLibrary();
	}

	public static GameLibraryServices getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GameLibraryServices();
		}
		return INSTANCE;
	}

	public SortedSet<Game> getGames() {
		if (games == null) {
			populateGames();
		}
		return games;
	}

	private void clearGames() {
		games = null;
	}

	public void addGame(Game game) {
		if (game instanceof BoardGame) {
			library.getBoardGames().add((BoardGame) game);
		} else if (game instanceof CardGame) {
			library.getCardGames().add((CardGame) game);
		} else if (game instanceof VideoGame) {
			library.getVideoGames().add((VideoGame) game);
		}

		saveLibrary();
		clearGames();
	}

	public void deleteGames(Set<Game> toDelete) {
		SortedSet<BoardGame> boardGames = library.getBoardGames();
		boardGames.removeAll(toDelete);

		SortedSet<CardGame> cardGames = library.getCardGames();
		cardGames.removeAll(toDelete);

		SortedSet<VideoGame> videoGames = library.getVideoGames();
		videoGames.removeAll(toDelete);

		clearGames();
	}

	private void populateGames() {
		games = new TreeSet<Game>();
		if (library != null) {
			games.addAll(library.getBoardGames());
			games.addAll(library.getCardGames());
			games.addAll(library.getVideoGames());
		}
	}

	private void loadLibrary() {
		String userHome = System.getProperty("user.home");
		File glHome = new File(userHome, "GameLibrary");

		File data = new File(glHome, "gl-data.xml");
		if (data.exists()) {
			try {
				JAXBContext context = JAXBContext.newInstance(GameLibrary.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();

				library = (GameLibrary) unmarshaller.unmarshal(data);
			} catch (JAXBException e) {
				library = null;
			}
		} else {
			loadDefaultLibrary();
			saveLibrary();
		}
	}

	private void loadDefaultLibrary() {
		URL fileLocation = GameLibraryUIActivator.getDefault().getBundle().getResource("data/gl-data.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(GameLibrary.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			library = (GameLibrary) unmarshaller.unmarshal(fileLocation);
		} catch (JAXBException e) {
			library = null;
		}
	}

	public void saveLibrary() {
		String userHome = System.getProperty("user.home");
		File glHome = new File(userHome, "GameLibrary");
		glHome.mkdirs();

		File data = new File(glHome, "gl-data.xml");

		try {
			JAXBContext context = JAXBContext.newInstance(GameLibrary.class);
			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(library, data);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}