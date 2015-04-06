package com.braithwood.gl.ui.custom;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class PrettySectionPart extends AbstractFormPart {

	private PrettySection section;

	public PrettySectionPart(PrettySection section) {
		super();

		this.section = section;
	}

	public PrettySectionPart(Composite parent, FormToolkit toolkit) {
		super();

		this.section = new PrettySection(parent, toolkit);
	}

	public PrettySectionPart(Composite parent, FormToolkit toolkit, boolean noMargin) {
		super();

		this.section = new PrettySection(parent, toolkit, noMargin);
	}

	public PrettySection getSection() {
		return section;
	}

	@Override
	public void setFocus() {
		Control client = section.getClient();
		if (client != null)
			client.setFocus();
	}

	public boolean isFocusControl() {
		Control client = section.getClient();
		if (client == null)
			return false;
		return client.isFocusControl();
	}
}