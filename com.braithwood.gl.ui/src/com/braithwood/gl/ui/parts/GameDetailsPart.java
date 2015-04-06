package com.braithwood.gl.ui.parts;

import java.text.MessageFormat;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.model.BoardGame;
import com.braithwood.gl.ui.model.CardGame;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.model.GameRating;
import com.braithwood.gl.ui.model.GameSystem;
import com.braithwood.gl.ui.model.Genre;
import com.braithwood.gl.ui.model.System;
import com.braithwood.gl.ui.model.VideoGame;
import com.braithwood.gl.ui.services.GameLibraryServices;

public class GameDetailsPart extends AbstractGameDetailsPart implements ISelectionChangedListener {

	private static final String SECTION_TITLE_PATTERN = "{0} for {1}";

	private PersistJob job;

	public GameDetailsPart(Composite parent, FormToolkit toolkit, LibraryPart part) {
		super(parent, toolkit);

		part.getViewer().addSelectionChangedListener(this);
	}

	@Override
	protected String getSectionTitle() {
		return "Game Details";
	}

	@Override
	protected String getSectionInfoKey() {
		return "gl.game.details";
	}

	@Override
	protected void persistChanges() {
		if (job == null) {
			job = new PersistJob();
		}

		job.cancel();
		job.schedule(2000);
	}

	private void applyChangesToModel(BoardGame game) {
		String string = boardLabelText.getText().trim();
		game.setLabel(string);

		IStructuredSelection selection = (IStructuredSelection) boardGenreViewer.getSelection();
		game.setGenre((Genre) selection.getFirstElement());

		string = boardPublisherText.getText().trim();
		game.setPublisher(string);

		string = boardMinimumPlayersText.getText().trim();
		int value;
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumPlayers(value);

		string = boardMaximumPlayersText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumPlayers(value);

		string = boardMinimumAgeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumAge(value);

		string = boardMaximumAgeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumAge(value);

		string = boardMinimumPlaytimeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumPlaytime(value);

		string = boardMaximumPlaytimeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumPlaytime(value);
	}

	private void applyChangesToModel(CardGame game) {
		String string = cardLabelText.getText().trim();
		game.setLabel(string);

		IStructuredSelection selection = (IStructuredSelection) cardGenreViewer.getSelection();
		game.setGenre((Genre) selection.getFirstElement());

		string = cardPublisherText.getText().trim();
		game.setPublisher(string);

		string = cardMinimumPlayersText.getText().trim();
		int value;
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumPlayers(value);

		string = cardMaximumPlayersText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumPlayers(value);

		string = cardMinimumAgeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumAge(value);

		string = cardMaximumAgeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumAge(value);

		string = cardMinimumPlaytimeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumPlaytime(value);

		string = cardMaximumPlaytimeText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumPlaytime(value);
	}

	private void applyChangesToModel(VideoGame game) {
		String string = videoLabelText.getText().trim();
		game.setLabel(string);

		IStructuredSelection selection = (IStructuredSelection) videoGenreViewer.getSelection();
		game.setGenre((Genre) selection.getFirstElement());

		string = videoPublisherText.getText().trim();
		game.setPublisher(string);

		string = videoMinimumPlayersText.getText().trim();
		int value;
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMinimumPlayers(value);

		string = videoMaximumPlayersText.getText().trim();
		try {
			value = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			value = Integer.MIN_VALUE;
		}
		game.setMaximumPlayers(value);

		selection = (IStructuredSelection) videoRatingViewer.getSelection();
		game.setRating((GameRating) selection.getFirstElement());

		Set<com.braithwood.gl.ui.model.System> systems = new TreeSet<com.braithwood.gl.ui.model.System>();
		Object[] elements = videoSystemsViewer.getCheckedElements();
		for (Object next : elements) {
			GameSystem gs = (GameSystem) next;

			System system = new System();
			system.setSystem(gs);
			systems.add(system);
		}
		game.setSystems(systems);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (selection == null || selection.isEmpty()) {
			getSection().setText(getSectionTitle());
			activateInvalidSelectionComposite();
			return;
		}

		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof Game) {
			currentGame = (Game) firstElement;
		} else {
			currentGame = null;
		}

		if (currentGame instanceof BoardGame) {
			performValidation = false;
			try {
				updateSectionForPage();
			} finally {
				performValidation = true;
			}

			validateBoardGame();
			activateBoardGameComposite();
			return;
		}
		if (currentGame instanceof CardGame) {
			performValidation = false;
			try {
				updateSectionForPage();
			} finally {
				performValidation = true;
			}

			activateCardGameComposite();
			validateCardGame();
			return;
		}
		if (currentGame instanceof VideoGame) {
			performValidation = false;
			try {
				updateSectionForPage();
			} finally {
				performValidation = true;
			}

			activateVideoGameComposite();
			validateVideoGame();
			return;
		}

		activateInvalidSelectionComposite();
	}

	public void updateSectionForPage() {
		String title = MessageFormat.format(SECTION_TITLE_PATTERN, getSectionTitle(), currentGame.getLabel());
		getSection().setText(title);

		if (currentGame instanceof BoardGame) {
			resetCardGameComposite();
			resetVideoGameComposite();
			updateForBoardGame((BoardGame) currentGame);
		} else if (currentGame instanceof CardGame) {
			resetBoardGameComposite();
			resetVideoGameComposite();
			updateForCardGame((CardGame) currentGame);
		} else if (currentGame instanceof VideoGame) {
			resetBoardGameComposite();
			resetCardGameComposite();
			updateForVideoGame((VideoGame) currentGame);
		}
	}

	private void updateForBoardGame(BoardGame game) {
		boardLabelText.setText(ensureNotNull(game.getLabel()));

		Genre genre = game.getGenre();
		if (genre == null) {
			boardGenreViewer.setSelection(new StructuredSelection());
		} else {
			boardGenreViewer.setSelection(new StructuredSelection(genre));
		}

		boardPublisherText.setText(ensureNotNull(game.getPublisher()));
		boardMinimumPlayersText.setText(game.getMinimumPlayers() + "");
		boardMaximumPlayersText.setText(game.getMaximumPlayers() + "");
		boardMinimumAgeText.setText(game.getMinimumAge() + "");
		boardMaximumAgeText.setText(game.getMaximumAge() + "");
		boardMinimumPlaytimeText.setText(game.getMinimumPlaytime() + "");
		boardMaximumPlaytimeText.setText(game.getMaximumPlaytime() + "");
	}

	private void resetBoardGameComposite() {
		boardLabelText.setText("");
		boardGenreViewer.setSelection(new StructuredSelection());
		boardPublisherText.setText("");
		boardMinimumPlayersText.setText("");
		boardMaximumPlayersText.setText("");
		boardMinimumAgeText.setText("");
		boardMaximumAgeText.setText("");
		boardMinimumPlaytimeText.setText("");
		boardMaximumPlaytimeText.setText("");
	}

	private void updateForCardGame(CardGame game) {
		cardLabelText.setText(ensureNotNull(game.getLabel()));

		Genre genre = game.getGenre();
		if (genre == null) {
			cardGenreViewer.setSelection(new StructuredSelection());
		} else {
			cardGenreViewer.setSelection(new StructuredSelection(genre));
		}

		cardPublisherText.setText(ensureNotNull(game.getPublisher()));
		cardMinimumPlayersText.setText(game.getMinimumPlayers() + "");
		cardMaximumPlayersText.setText(game.getMaximumPlayers() + "");
		cardMinimumAgeText.setText(game.getMinimumAge() + "");
		cardMaximumAgeText.setText(game.getMaximumAge() + "");
		cardMinimumPlaytimeText.setText(game.getMinimumPlaytime() + "");
		cardMaximumPlaytimeText.setText(game.getMaximumPlaytime() + "");
	}

	private void resetCardGameComposite() {
		cardLabelText.setText("");
		cardGenreViewer.setSelection(new StructuredSelection());
		cardPublisherText.setText("");
		cardMinimumPlayersText.setText("");
		cardMaximumPlayersText.setText("");
		cardMinimumAgeText.setText("");
		cardMaximumAgeText.setText("");
		cardMinimumPlaytimeText.setText("");
		cardMaximumPlaytimeText.setText("");
	}

	private void updateForVideoGame(VideoGame game) {
		videoLabelText.setText(ensureNotNull(game.getLabel()));

		Genre genre = game.getGenre();
		if (genre == null) {
			videoGenreViewer.setSelection(new StructuredSelection());
		} else {
			videoGenreViewer.setSelection(new StructuredSelection(genre));
		}

		videoGenreViewer.setSelection(new StructuredSelection(game.getGenre()));

		videoPublisherText.setText(ensureNotNull(game.getPublisher()));
		videoMinimumPlayersText.setText(game.getMinimumPlayers() + "");
		videoMaximumPlayersText.setText(game.getMaximumPlayers() + "");

		GameRating rating = game.getRating();
		if (rating == null) {
			videoRatingViewer.setSelection(new StructuredSelection());
		} else {
			videoRatingViewer.setSelection(new StructuredSelection(rating));
		}

		if (game.getSystems() == null) {
			videoSystemsViewer.setCheckedElements(new Object[0]);
		} else {
			SortedSet<GameSystem> systems = new TreeSet<GameSystem>();
			for (com.braithwood.gl.ui.model.System next : game.getSystems()) {
				systems.add(next.getSystem());
			}
			videoSystemsViewer.setCheckedElements(systems.toArray());
		}
	}

	private void resetVideoGameComposite() {
		videoLabelText.setText("");
		videoGenreViewer.setSelection(new StructuredSelection());
		videoPublisherText.setText("");
		videoMinimumPlayersText.setText("");
		videoMaximumPlayersText.setText("");
		videoRatingViewer.setSelection(new StructuredSelection());
		videoSystemsViewer.setSelection(new StructuredSelection());
	}

	private final class PersistJob extends Job {

		public PersistJob() {
			super("persist changes");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					if (currentGame instanceof BoardGame) {
						applyChangesToModel((BoardGame) currentGame);
					} else if (currentGame instanceof CardGame) {
						applyChangesToModel((CardGame) currentGame);
					} else if (currentGame instanceof VideoGame) {
						applyChangesToModel((VideoGame) currentGame);
					}
				}
			});

			GameLibraryServices.getInstance().saveLibrary();

			return Status.OK_STATUS;
		}
	}
}