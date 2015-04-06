package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlEnum(String.class)
@XmlType(name = "rating")
public enum GameRating {

	@XmlEnumValue("rp")
	RATING_PENDING("RP - Rating Pending"),

	@XmlEnumValue("ec")
	EARLY_CHILDHOOD("EC - Early Childhood"),

	@XmlEnumValue("e")
	EVERYONE("E - Everyone"),

	@XmlEnumValue("e10plus")
	EVERYONE_TEN_PLUS("E10+ - Everyone 10+"),

	@XmlEnumValue("t")
	TEEN("T - Teen"),

	@XmlEnumValue("m")
	MATURE("M - Mature"),

	@XmlEnumValue("ao")
	ADULTS_ONLY("AO - Adults Only");

	private String label;

	private GameRating(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}