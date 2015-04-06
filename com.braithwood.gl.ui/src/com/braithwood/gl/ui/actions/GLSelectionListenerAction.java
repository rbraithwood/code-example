package com.braithwood.gl.ui.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.BaseSelectionListenerAction;

public abstract class GLSelectionListenerAction extends BaseSelectionListenerAction {

	public GLSelectionListenerAction() {
		super("");

		setText(getActionLabel());
		setToolTipText(getActionToolTipText());
		setImageDescriptor(getActionImageDescriptor());
	}

	protected abstract String getActionLabel();

	protected abstract String getActionToolTipText();

	protected abstract ImageDescriptor getActionImageDescriptor();

	protected abstract boolean canRun(IStructuredSelection selection);

	protected abstract void doRun();

	@Override
	public final void run() {
		doRun();
	}

	@Override
	protected final boolean updateSelection(IStructuredSelection selection) {
		return canRun(selection);
	}

	public void updateEnablement() {
		setEnabled(canRun(getStructuredSelection()));
	}
}