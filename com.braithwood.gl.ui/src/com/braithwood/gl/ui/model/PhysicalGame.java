package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class PhysicalGame extends Game {

	private int minimumAge;
	private int maximumAge;
	private int minimumPlaytime;
	private int maximumPlaytime;

	public PhysicalGame() {
		super();
	}

	@XmlAttribute(name = "minimumAge", required = true)
	public int getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(int minimumAge) {
		this.minimumAge = minimumAge;
	}

	@XmlAttribute(name = "maximumAge", required = true)
	public int getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}

	@XmlAttribute(name = "minimumPlaytime", required = true)
	public int getMinimumPlaytime() {
		return minimumPlaytime;
	}

	public void setMinimumPlaytime(int minimumPlaytime) {
		this.minimumPlaytime = minimumPlaytime;
	}

	@XmlAttribute(name = "maximumPlaytime", required = true)
	public int getMaximumPlaytime() {
		return maximumPlaytime;
	}

	public void setMaximumPlaytime(int maximumPlaytime) {
		this.maximumPlaytime = maximumPlaytime;
	}

	protected int compareTo(Game o) {
		return super.compareTo(o);
	}
}