package com.braithwood.gl.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class GLAction extends Action {

	public GLAction() {
		this(Action.AS_PUSH_BUTTON);
	}

	public GLAction(int style) {
		super("", style);

		setText(getActionLabel());
		setToolTipText(getActionToolTipText());
		setImageDescriptor(getActionImageDescriptor());
	}

	protected abstract String getActionLabel();

	protected abstract String getActionToolTipText();

	protected abstract ImageDescriptor getActionImageDescriptor();

	protected abstract void doRun();

	@Override
	public final void run() {
		doRun();
	}
}