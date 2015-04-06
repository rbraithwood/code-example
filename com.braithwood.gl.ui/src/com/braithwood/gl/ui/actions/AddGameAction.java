package com.braithwood.gl.ui.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.dialogs.AddGameDialog;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.services.GameLibraryServices;

public class AddGameAction extends GLAction {

	private final TableViewer viewer;

	public AddGameAction(TableViewer viewer) {
		super();

		this.viewer = viewer;
	}

	@Override
	protected String getActionLabel() {
		return "Add Game";
	}

	@Override
	protected String getActionToolTipText() {
		return "Add game to library";
	}

	@Override
	protected ImageDescriptor getActionImageDescriptor() {
		return GameLibraryUIActivator.getImageDescriptor("icons/actions/add12.png");
	}

	@Override
	protected void doRun() {
		AddGameDialog dialog = new AddGameDialog(viewer.getTable().getShell());
		dialog.create();

		if (dialog.open() == Window.OK) {
			final Game game = dialog.getGame();

			Job job = new Job("add game") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					GameLibraryServices.getInstance().addGame(game);

					viewer.getTable().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							viewer.refresh();
						}
					});

					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}
	}
}