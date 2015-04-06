package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlEnum(String.class)
@XmlType(name = "genre")
public enum Genre {

	@XmlEnumValue("children")
	CHILDREN("Children"),

	@XmlEnumValue("family")
	FAMILY("Family"),

	@XmlEnumValue("strategy")
	STRATEGY("Strategy"),

	@XmlEnumValue("action")
	ACTION("Action"),

	@XmlEnumValue("rpg")
	RPG("Role PLaying Game (RPG)"),

	@XmlEnumValue("mmo")
	MMO("Massively Multiplayer Online (MMO)"),

	@XmlEnumValue("fantasy")
	FANTASY("Fantasy"),

	@XmlEnumValue("fps")
	FPS("First Person Shooter (FPS)"),

	@XmlEnumValue("defense")
	DEFENSE("Defense"),

	@XmlEnumValue("platformer")
	PLATFORMER("Platformer"),

	@XmlEnumValue("rts")
	RTS("Real Time Strategu (RTS)"),

	@XmlEnumValue("sim")
	SIM("Simulation");

	private String label;

	private Genre(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}