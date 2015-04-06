package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class Game extends GLEntity {

	private String publisher;
	private Genre genre;
	private int minimumPlayers;
	private int maximumPlayers;

	public Game() {
		super();
	}

	@XmlAttribute(name = "publisher", required = true)
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@XmlAttribute(required = true)
	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@XmlAttribute(name = "minimumPlayers", required = true)
	public int getMinimumPlayers() {
		return minimumPlayers;
	}

	public void setMinimumPlayers(int minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}

	@XmlAttribute(name = "maximumPlayers", required = true)
	public int getMaximumPlayers() {
		return maximumPlayers;
	}

	public void setMaximumPlayers(int maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}

	protected int compareTo(Game o) {
		return super.compareTo(o);
	}
}