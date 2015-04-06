package com.braithwood.gl.ui.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class VideoGame extends Game implements Comparable<Game> {

	private Set<System> systems;
	private GameRating rating;

	public VideoGame() {
		super();
	}

	@XmlAttribute(required = true)
	public GameRating getRating() {
		return rating;
	}

	public void setRating(GameRating rating) {
		this.rating = rating;
	}

	@XmlElement(name = "system")
	public Set<System> getSystems() {
		return systems;
	}

	public void setSystems(Set<System> systems) {
		this.systems = systems;
	}

	@Override
	public int compareTo(Game o) {
		return super.compareTo(o);
	}
}