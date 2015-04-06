package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class GLEntity {

	private String label;

	public GLEntity() {
		super();
	}

	@XmlAttribute(name = "label", required = true)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	protected int compareTo(GLEntity o) {
		if (label == null) {
			if (o.label == null) {
				return 0;
			} else {
				return 1;
			}
		}

		return label.compareTo(o.label);
	}
}