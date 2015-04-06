package com.braithwood.gl.ui.model;

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gl-library")
public class GameLibrary {

	private SortedSet<BoardGame> boardGames;
	private SortedSet<CardGame> cardGames;
	private SortedSet<VideoGame> videoGames;

	public GameLibrary() {
		super();
	}

	@XmlElement(name = "board-game")
	public SortedSet<BoardGame> getBoardGames() {
		return boardGames;
	}

	public void setBoardGames(SortedSet<BoardGame> boardGames) {
		this.boardGames = boardGames;
	}

	@XmlElement(name = "card-game")
	public SortedSet<CardGame> getCardGames() {
		return cardGames;
	}

	public void setCardGames(SortedSet<CardGame> cardGames) {
		this.cardGames = cardGames;
	}

	@XmlElement(name = "video-game")
	public SortedSet<VideoGame> getVideoGames() {
		return videoGames;
	}

	public void setVideoGames(SortedSet<VideoGame> videoGames) {
		this.videoGames = videoGames;
	}
}