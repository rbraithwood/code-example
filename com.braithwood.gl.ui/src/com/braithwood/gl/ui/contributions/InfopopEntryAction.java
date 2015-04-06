package com.braithwood.gl.ui.contributions;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class InfopopEntryAction implements Comparable<InfopopEntryAction> {

	private String id;
	private int order;
	private String imageLocation;
	private String description;

	public InfopopEntryAction() {
		super();
	}

	@XmlAttribute(name = "id", required = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "order", required = true)
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@XmlAttribute(name = "image", required = true)
	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(InfopopEntryAction other) {
		return order - other.order;
	}
}