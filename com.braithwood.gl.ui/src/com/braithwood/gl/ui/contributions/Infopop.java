package com.braithwood.gl.ui.contributions;

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "infopop")
public class Infopop {

	private SortedSet<InfopopEntry> entries;

	public Infopop() {
		super();
	}

	@XmlElement(name = "entry")
	public SortedSet<InfopopEntry> getEntries() {
		return entries;
	}

	public void setEntries(SortedSet<InfopopEntry> entries) {
		this.entries = entries;
	}
}