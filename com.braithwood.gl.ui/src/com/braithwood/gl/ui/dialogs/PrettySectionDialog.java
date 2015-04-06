package com.braithwood.gl.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class PrettySectionDialog extends Dialog {

	protected FormToolkit toolkit;

	public PrettySectionDialog(Shell shell) {
		super(shell);

		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	@Override
	protected void configureShell(Shell newShell) {
		Layout layout = getLayout();
		if (layout != null) {
			newShell.setLayout(layout);
		}

		newShell.setText(getDialogTitle());
		newShell.setSize(getDialogSize());
	}

	protected abstract Point getDialogSize();

	protected abstract String getDialogTitle();

	protected Control createDialogArea(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		ScrolledForm sform = toolkit.createScrolledForm(parent);
		sform.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sform.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});

		ManagedForm mform = new ManagedForm(toolkit, sform);
		Composite body = mform.getForm().getBody();
		body.setLayout(new GridLayout());
		body.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createFormContent(body, mform);

		applyDialogFont(sform.getBody());

		return sform;
	}

	protected abstract void createFormContent(Composite parent, IManagedForm mform);

	protected Control createButtonBar(Composite parent) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Label sep = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		sep.setLayoutData(gd);
		Control bar = super.createButtonBar(parent);
		parent.setBackground(toolkit.getColors().getBackground());
		bar.setBackground(toolkit.getColors().getBackground());
		return bar;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	public boolean close() {
		preClose();

		boolean result = super.close();

		postClose();

		return result;
	}

	protected void preClose() {
		// for subclasses to implement if they want
	}

	protected void postClose() {
		// for subclasses to implement if they want
	}
}