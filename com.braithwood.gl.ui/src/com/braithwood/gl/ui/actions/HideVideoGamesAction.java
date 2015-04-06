package com.braithwood.gl.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.parts.LibraryPart.HideVideoGamesFilter;

public class HideVideoGamesAction extends GLAction {

	private final HideVideoGamesFilter filter;
	private boolean hide;

	public HideVideoGamesAction(HideVideoGamesFilter filter) {
		super(Action.AS_CHECK_BOX);

		this.filter = filter;
		this.hide = false;
		setChecked(hide);
		filter.setActive(hide);
	}

	@Override
	protected String getActionLabel() {
		return "Hide Video Games";
	}

	@Override
	protected String getActionToolTipText() {
		return "Hide video games";
	}

	@Override
	protected ImageDescriptor getActionImageDescriptor() {
		return GameLibraryUIActivator.getImageDescriptor("icons/actions/hide-video-games12.png");
	}

	@Override
	protected void doRun() {
		hide = !hide;
		setChecked(hide);
		filter.setActive(hide);
	}
}