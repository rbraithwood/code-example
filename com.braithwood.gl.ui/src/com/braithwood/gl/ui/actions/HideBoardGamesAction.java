package com.braithwood.gl.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.parts.LibraryPart.HideBoardGamesFilter;

public class HideBoardGamesAction extends GLAction {

	private final HideBoardGamesFilter filter;
	private boolean hide;

	public HideBoardGamesAction(HideBoardGamesFilter filter) {
		super(Action.AS_CHECK_BOX);

		this.filter = filter;
		this.hide = false;
		setChecked(hide);
		filter.setActive(hide);
	}

	@Override
	protected String getActionLabel() {
		return "Hide Board Games";
	}

	@Override
	protected String getActionToolTipText() {
		return "Hide board games";
	}

	@Override
	protected ImageDescriptor getActionImageDescriptor() {
		return GameLibraryUIActivator.getImageDescriptor("icons/actions/hide-board-games12.png");
	}

	@Override
	protected void doRun() {
		hide = !hide;
		setChecked(hide);
		filter.setActive(hide);
	}
}