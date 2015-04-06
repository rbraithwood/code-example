package com.braithwood.gl.ui.model;

import javax.xml.bind.annotation.XmlAttribute;

public class System implements Comparable<System> {

	private GameSystem system;

	public System() {
		super();
	}

	@XmlAttribute(name = "type", required = true)
	public GameSystem getSystem() {
		return system;
	}

	public void setSystem(GameSystem system) {
		this.system = system;
	}

	@Override
	public int compareTo(System o) {
		if (system == null) {
			if (o.system == null) {
				return 0;
			} else {
				return 1;
			}
		}

		return system.getLabel().compareTo(o.system.getLabel());
	}
}