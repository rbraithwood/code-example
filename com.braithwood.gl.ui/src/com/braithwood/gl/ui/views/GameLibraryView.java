package com.braithwood.gl.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import com.braithwood.gl.ui.parts.GameDetailsPart;
import com.braithwood.gl.ui.parts.JumpstartPart;
import com.braithwood.gl.ui.parts.LibraryPart;

public class GameLibraryView extends ViewPart {

	public static final String ID = "com.braithwood.gl.ui.view";

	private ManagedForm managedForm;
	private LibraryPart libraryPart;
	private GameDetailsPart detailsPart;
	private JumpstartPart jumpstartPart;

	@Override
	public void createPartControl(Composite parent) {
		managedForm = new ManagedForm(parent);
		managedForm.setContainer(this);

		ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		form.getBody().setLayout(layout);

		FormToolkit toolkit = managedForm.getToolkit();

		Composite body = toolkit.createComposite(managedForm.getForm().getBody(), SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		body.setLayout(layout);
		toolkit.paintBordersFor(body);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 800;
		gd.heightHint = 600;
		body.setLayoutData(gd);

		libraryPart = new LibraryPart(body, toolkit);
		gd = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd.widthHint = 200;
		gd.heightHint = 600;
		libraryPart.getSection().setLayoutData(gd);
		managedForm.addPart(libraryPart);

		detailsPart = new GameDetailsPart(body, toolkit, libraryPart);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		detailsPart.getSection().setLayoutData(gd);
		managedForm.addPart(detailsPart);

		jumpstartPart = new JumpstartPart(body, toolkit, libraryPart);
		gd = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd.widthHint = 300;
		gd.heightHint = 600;
		jumpstartPart.getSection().setLayoutData(gd);
		managedForm.addPart(jumpstartPart);
	}

	@Override
	public void setFocus() {
		managedForm.setFocus();
	}

	@Override
	public void dispose() {
		managedForm.dispose();
		managedForm = null;

		super.dispose();
	}
}