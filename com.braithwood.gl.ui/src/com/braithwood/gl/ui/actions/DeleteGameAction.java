package com.braithwood.gl.ui.actions;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.services.GameLibraryServices;

@SuppressWarnings("unchecked")
public class DeleteGameAction extends GLSelectionListenerAction {

	private final TableViewer viewer;

	public DeleteGameAction(TableViewer viewer) {
		super();

		this.viewer = viewer;
	}

	@Override
	protected String getActionLabel() {
		return "Delete Game";
	}

	@Override
	protected String getActionToolTipText() {
		return "Delete selected games";
	}

	@Override
	protected ImageDescriptor getActionImageDescriptor() {
		return GameLibraryUIActivator.getImageDescriptor("icons/actions/delete12.png");
	}

	@Override
	protected void doRun() {
		IStructuredSelection selection = getStructuredSelection();
		if (!canRun(selection))
			return;

		String confirmMessage;
		if (selection.size() > 1) {
			String pattern = "Are you sure you want to delete the {0} selected games?";
			confirmMessage = MessageFormat.format(pattern, selection.size());
		} else {
			String pattern = "Are you sure you want to delete {0}?";
			Object element = selection.getFirstElement();
			confirmMessage = MessageFormat.format(pattern, ((Game) element).getLabel());
		}

		if (MessageDialog.openConfirm(viewer.getTable().getShell(), "Confirm Delete", confirmMessage)) {
			Set<Game> toDelete = new TreeSet<Game>();
			toDelete.addAll(selection.toList());

			GameLibraryServices.getInstance().deleteGames(toDelete);
			viewer.refresh();
		}
	}

	@Override
	protected boolean canRun(IStructuredSelection selection) {
		if (selection.isEmpty())
			return false;

		boolean result = true;
		for (Object next : selection.toList()) {
			if (!(next instanceof Game)) {
				result = false;
				break;
			}
		}
		return result;
	}
}