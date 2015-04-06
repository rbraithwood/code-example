package com.braithwood.gl.ui.dialogs;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.model.BoardGame;
import com.braithwood.gl.ui.model.CardGame;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.model.GameRating;
import com.braithwood.gl.ui.model.GameSystem;
import com.braithwood.gl.ui.model.Genre;
import com.braithwood.gl.ui.model.System;
import com.braithwood.gl.ui.model.VideoGame;
import com.braithwood.gl.ui.parts.AbstractGameDetailsPart;

public class AddGameDialog extends PrettySectionDialog {

	private DialogSectionPart part;

	public AddGameDialog(Shell shell) {
		super(shell);

		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	@Override
	protected Point getDialogSize() {
		return new Point(500, 550);
	}

	@Override
	protected String getDialogTitle() {
		return "Add Game to Library";
	}

	@Override
	protected void createFormContent(Composite parent, IManagedForm mform) {
		part = new DialogSectionPart(parent, toolkit, this);
		part.getSection().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mform.addPart(part);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		part.validate();
	}

	private void setOKButtonEnablement(boolean enabled) {
		getButton(IDialogConstants.OK_ID).setEnabled(enabled);
	}

	@Override
	protected void preClose() {
		super.preClose();

		part.createGame();
	}

	public Game getGame() {
		return part.getGame();
	}

	private static class DialogSectionPart extends AbstractGameDetailsPart {

		private Game game;
		private AddGameDialog dialog;
		private Button boardRadio;
		private Button cardRadio;
		private Button videoRadio;

		public DialogSectionPart(Composite parent, FormToolkit toolkit, AddGameDialog dialog) {
			super(parent, toolkit);

			this.dialog = dialog;
		}

		protected void createSectionContents(Composite parent, FormToolkit toolkit) {
			performValidation = true;

			Composite composite = toolkit.createComposite(getSection(), SWT.NONE);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.verticalSpacing = 15;
			composite.setLayout(layout);
			toolkit.paintBordersFor(composite);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.widthHint = 500;
			gd.heightHint = 500;
			composite.setLayoutData(gd);

			Composite radioComposite = toolkit.createComposite(composite, SWT.NONE);
			layout = new GridLayout(3, true);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			radioComposite.setLayout(layout);
			toolkit.paintBordersFor(radioComposite);
			gd = new GridData(SWT.CENTER, SWT.FILL, true, false);
			gd.widthHint = 300;
			radioComposite.setLayoutData(gd);

			boardRadio = toolkit.createButton(radioComposite, "Board Game", SWT.RADIO);
			gd = new GridData(SWT.FILL, SWT.FILL, false, false);
			boardRadio.setLayoutData(gd);
			boardRadio.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					activateBoardGameComposite();
					validateBoardGame();
				}
			});

			cardRadio = toolkit.createButton(radioComposite, "Card Game", SWT.RADIO);
			gd = new GridData(SWT.FILL, SWT.FILL, false, false);
			cardRadio.setLayoutData(gd);
			cardRadio.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					activateCardGameComposite();
					validateCardGame();
				}
			});

			videoRadio = toolkit.createButton(radioComposite, "Video Game", SWT.RADIO);
			gd = new GridData(SWT.FILL, SWT.FILL, false, false);
			videoRadio.setLayoutData(gd);
			videoRadio.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					activateVideoGameComposite();
					validateVideoGame();
				}
			});

			Label separator = toolkit.createSeparator(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			gd = new GridData(SWT.FILL, SWT.FILL, true, false);
			gd.verticalIndent = -10;
			separator.setLayoutData(gd);

			stackComposite = toolkit.createComposite(composite, SWT.NONE);
			stackLayout = new StackLayout();
			stackComposite.setLayout(stackLayout);
			gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			stackComposite.setLayoutData(gd);
			toolkit.paintBordersFor(stackComposite);

			createBoardGameComposite(toolkit);
			createCardGameComposite(toolkit);
			createVideoGameComposite(toolkit);

			boardRadio.setSelection(true);
			activateBoardGameComposite();

			getSection().setClient(composite);
		}

		@Override
		protected String getSectionTitle() {
			return "Add Game";
		}

		@Override
		protected String getSectionInfoKey() {
			return "gl.dialog.add.game";
		}

		protected void validate() {
			boolean isValid;
			if (boardRadio.getSelection()) {
				isValid = validateBoardGame();
			} else if (cardRadio.getSelection()) {
				isValid = validateCardGame();
			} else if (videoRadio.getSelection()) {
				isValid = validateVideoGame();
			} else {
				isValid = false;
			}

			dialog.setOKButtonEnablement(isValid);
		}

		@Override
		protected boolean validateBoardGame() {
			if (!boardRadio.getSelection())
				return false;

			boolean isValid = super.validateBoardGame();
			dialog.setOKButtonEnablement(isValid);
			return isValid;
		}

		@Override
		protected boolean validateCardGame() {
			if (!cardRadio.getSelection())
				return false;

			boolean isValid = super.validateCardGame();
			dialog.setOKButtonEnablement(isValid);
			return isValid;
		}

		@Override
		protected boolean validateVideoGame() {
			if (!videoRadio.getSelection())
				return false;

			boolean isValid = super.validateVideoGame();
			dialog.setOKButtonEnablement(isValid);
			return isValid;
		}

		public void createGame() {
			if (boardRadio.getSelection()) {
				game = new BoardGame();
				applyChangesToModel((BoardGame) game);
			} else if (cardRadio.getSelection()) {
				game = new CardGame();
				applyChangesToModel((CardGame) game);
			} else if (videoRadio.getSelection()) {
				game = new VideoGame();
				applyChangesToModel((VideoGame) game);
			} else {
				game = null;
			}
		}

		public Game getGame() {
			return game;
		}

		@Override
		protected void persistChanges() {
			// do nothing
		}

		private void applyChangesToModel(BoardGame game) {
			String string = boardLabelText.getText().trim();
			game.setLabel(string);

			IStructuredSelection selection = (IStructuredSelection) boardGenreViewer.getSelection();
			game.setGenre((Genre) selection.getFirstElement());

			string = boardPublisherText.getText().trim();
			game.setPublisher(string);

			string = boardMinimumPlayersText.getText().trim();
			int value;
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumPlayers(value);

			string = boardMaximumPlayersText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumPlayers(value);

			string = boardMinimumAgeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumAge(value);

			string = boardMaximumAgeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumAge(value);

			string = boardMinimumPlaytimeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumPlaytime(value);

			string = boardMaximumPlaytimeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumPlaytime(value);
		}

		private void applyChangesToModel(CardGame game) {
			String string = cardLabelText.getText().trim();
			game.setLabel(string);

			IStructuredSelection selection = (IStructuredSelection) cardGenreViewer.getSelection();
			game.setGenre((Genre) selection.getFirstElement());

			string = cardPublisherText.getText().trim();
			game.setPublisher(string);

			string = cardMinimumPlayersText.getText().trim();
			int value;
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumPlayers(value);

			string = cardMaximumPlayersText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumPlayers(value);

			string = cardMinimumAgeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumAge(value);

			string = cardMaximumAgeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumAge(value);

			string = cardMinimumPlaytimeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumPlaytime(value);

			string = cardMaximumPlaytimeText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumPlaytime(value);
		}

		private void applyChangesToModel(VideoGame game) {
			String string = videoLabelText.getText().trim();
			game.setLabel(string);

			IStructuredSelection selection = (IStructuredSelection) videoGenreViewer.getSelection();
			game.setGenre((Genre) selection.getFirstElement());

			string = videoPublisherText.getText().trim();
			game.setPublisher(string);

			string = videoMinimumPlayersText.getText().trim();
			int value;
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMinimumPlayers(value);

			string = videoMaximumPlayersText.getText().trim();
			try {
				value = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				value = Integer.MIN_VALUE;
			}
			game.setMaximumPlayers(value);

			selection = (IStructuredSelection) videoRatingViewer.getSelection();
			game.setRating((GameRating) selection.getFirstElement());

			Set<com.braithwood.gl.ui.model.System> systems = new TreeSet<com.braithwood.gl.ui.model.System>();
			Object[] elements = videoSystemsViewer.getCheckedElements();
			for (Object next : elements) {
				GameSystem gs = (GameSystem) next;

				System system = new System();
				system.setSystem(gs);
				systems.add(system);
			}
			game.setSystems(systems);
		}
	}
}