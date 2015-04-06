package com.braithwood.gl.ui.contributions;

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class InfopopEntry implements Comparable<InfopopEntry> {

	private String id;
	private String title;
	private String body;
	private SortedSet<InfopopEntryAction> actions;

	public InfopopEntry() {
		super();
	}

	@XmlAttribute(name = "id", required = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "title", required = true)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "body", required = true)
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@XmlElement(name = "action")
	public SortedSet<InfopopEntryAction> getActions() {
		return actions;
	}

	public void setActions(SortedSet<InfopopEntryAction> actions) {
		this.actions = actions;
	}

	@Override
	public int compareTo(InfopopEntry other) {
		return id.compareTo(other.id);
	}
}