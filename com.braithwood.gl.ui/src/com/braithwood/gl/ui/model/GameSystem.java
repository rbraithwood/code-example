package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlEnum(String.class)
@XmlType(name = "type")
public enum GameSystem {

	@XmlEnumValue("ps3")
	PS3("PlayStation 3"),

	@XmlEnumValue("ps4")
	PS4("PlayStation 4"),

	@XmlEnumValue("wii")
	WII("Wii"),

	@XmlEnumValue("wii_u")
	WII_U("Wii U"),

	@XmlEnumValue("xbox")
	XBOX("Xbox"),

	@XmlEnumValue("xbox_360")
	XBOX_360("Xbox 360"),

	@XmlEnumValue("ios")
	IOS("iOS Compatible"),

	@XmlEnumValue("android")
	ANDROID("Android Compatible"),

	@XmlEnumValue("pc")
	PC("PC Compatible");

	private String label;

	private GameSystem(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}