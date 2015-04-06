package com.braithwood.gl.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.parts.LibraryPart.HideCardGamesFilter;

public class HideCardGamesAction extends GLAction {

	private final HideCardGamesFilter filter;
	private boolean hide;

	public HideCardGamesAction(HideCardGamesFilter filter) {
		super(Action.AS_CHECK_BOX);

		this.filter = filter;
		this.hide = false;
		setChecked(hide);
		filter.setActive(hide);
	}

	@Override
	protected String getActionLabel() {
		return "Hide Card Games";
	}

	@Override
	protected String getActionToolTipText() {
		return "Hide card games";
	}

	@Override
	protected ImageDescriptor getActionImageDescriptor() {
		return GameLibraryUIActivator.getImageDescriptor("icons/actions/hide-card-games12.png");
	}

	@Override
	protected void doRun() {
		hide = !hide;
		setChecked(hide);
		filter.setActive(hide);
	}
}