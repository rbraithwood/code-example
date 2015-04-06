package com.braithwood.gl.ui.parts;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.contributions.InfoContributionItem;
import com.braithwood.gl.ui.custom.CustomWorkIndicator;
import com.braithwood.gl.ui.custom.PrettySectionPart;

public abstract class GLSectionPart extends PrettySectionPart {

	protected InfoContributionItem infoItem;
	protected Composite stackComposite;
	protected StackLayout stackLayout;
	protected Composite mainComposite;
	protected Composite workingComposite;
	protected CustomWorkIndicator workingIndicator;

	public GLSectionPart(Composite parent, FormToolkit toolkit) {
		this(parent, toolkit, false);
	}

	public GLSectionPart(Composite parent, FormToolkit toolkit, boolean noMargin) {
		super(parent, toolkit, noMargin);

		getSection().setText(getSectionTitle());

		createSectionContents(parent, toolkit);

		String infoKey = getSectionInfoKey();
		if (infoKey != null) {
			infoItem = new InfoContributionItem(infoKey, getSection());
		} else {
			infoItem = null;
		}

		ToolBarManager manager = getSection().getToolBarManager();
		initializeToolbar(manager);
		if (infoItem != null) {
			manager.add(new Separator());
			manager.add(infoItem);
		}
		manager.update(true);

		initializeMenu();
	}

	protected abstract String getSectionInfoKey();

	protected abstract String getSectionTitle();

	protected abstract void createSectionContents(Composite parent, FormToolkit toolkit);

	protected abstract void initializeToolbar(ToolBarManager manager);

	protected void initializeMenu() {
		// for subclasses to override
	}

	protected String ensureNotNull(String string) {
		if (string == null)
			return "";
		return string;
	}

	protected void createStackComposite(Composite parent, FormToolkit toolkit) {
		stackComposite = toolkit.createComposite(getSection(), SWT.NONE);
		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		stackComposite.setLayoutData(gd);
		toolkit.paintBordersFor(stackComposite);
	}

	protected void createMainComposite(Composite parent, FormToolkit toolkit) {
		mainComposite = toolkit.createComposite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComposite.setLayout(layout);
		toolkit.paintBordersFor(mainComposite);
	}

	protected void createWorkingComposite(Composite parent, FormToolkit toolkit) {
		workingComposite = toolkit.createComposite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		workingComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		workingComposite.setLayout(layout);
		toolkit.paintBordersFor(workingComposite);

		workingIndicator = new CustomWorkIndicator(workingComposite, 96, 96, 10);
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		workingIndicator.setLayoutData(gd);
	}

	protected void updateOnUIThread(final Text text, final String string) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				text.setText(string);
			}
		});
	}

	protected void updateOnUIThread(final Label label, final String string) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				label.setText(string);
			}
		});
	}

	// protected void updateOnUIThread(final CustomHyperlinksWithDetails hyperlink, final String title) {
	// Display.getDefault().asyncExec(new Runnable() {
	//
	// @Override
	// public void run() {
	// hyperlink.setTitle(title);
	// }
	// });
	// }

	protected void setInputOnUIThread(final Viewer viewer, final Object input) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setInput(input);
			}
		});
	}

	protected void setSelectionOnUIThread(final Viewer viewer, final IStructuredSelection selection) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setSelection(selection);
			}
		});
	}

	protected final class ShowMainCompositeRunnable implements Runnable {

		public ShowMainCompositeRunnable() {
			super();
		}

		@Override
		public void run() {
			if (stackLayout.topControl == null || !stackLayout.topControl.equals(mainComposite)) {
				stackLayout.topControl = mainComposite;
				workingIndicator.setWorking(false);
				stackComposite.layout(true, true);
			}
		}
	}

	protected final class ShowWorkingCompositeRunnable implements Runnable {

		public ShowWorkingCompositeRunnable() {
			super();
		}

		@Override
		public void run() {
			if (stackLayout.topControl == null || !stackLayout.topControl.equals(workingComposite)) {
				stackLayout.topControl = workingComposite;
				workingIndicator.setWorking(true);
				stackComposite.layout(true, true);
			}
		}
	}
}