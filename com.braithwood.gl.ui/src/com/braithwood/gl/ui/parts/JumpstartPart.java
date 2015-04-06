package com.braithwood.gl.ui.parts;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.custom.CustomHyperlinksWithDetails;
import com.braithwood.gl.ui.custom.ICustomAction;

public class JumpstartPart extends GLSectionPart {

	private static final String ADD_TITLE = "Add a new game to the library";
	private static final String HIDE_TITLE = "Hide games from the library";

	private LibraryPart libraryPart;
	private Color lightGray;
	private FormText welcomeFormText;
	private CustomHyperlinksWithDetails add;
	private CustomHyperlinksWithDetails hide;

	public JumpstartPart(Composite parent, FormToolkit toolkit, LibraryPart libraryPart) {
		super(parent, toolkit, false);

		this.libraryPart = libraryPart;
	}

	@Override
	protected void createSectionContents(Composite parent, FormToolkit toolkit) {
		RGB rgb = new RGB(247, 247, 255);
		lightGray = new Color(parent.getDisplay(), rgb);

		createStackComposite(parent, toolkit);
		createMainComposite(stackComposite, toolkit);

		welcomeFormText = toolkit.createFormText(mainComposite, false);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 100;
		welcomeFormText.setLayoutData(gd);
		welcomeFormText.setText("Welcome back to the Game Library. What would you like to do today?", false, false);

		add = new CustomHyperlinksWithDetails(mainComposite, SWT.NONE);
		Image image = GameLibraryUIActivator.getImage("icons/add32.png");
		add.setImage(image);
		add.setTitle(ADD_TITLE);
		add.setActionSurrounds("Add", "");
		add.setActions(new ICustomAction[] { new AddGameAction() });
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.verticalIndent = 5;
		add.setLayoutData(gd);
		add.setBackground(lightGray);

		hide = new CustomHyperlinksWithDetails(mainComposite, SWT.NONE);
		image = GameLibraryUIActivator.getImage("icons/hide-board-games32.png");
		hide.setImage(image);
		hide.setTitle(HIDE_TITLE);
		hide.setActionSurrounds("Hide", "");
		hide.setActions(new ICustomAction[] { new HideBoardGamesAction(), new HideCardGamesAction(), new HideVideoGamesAction() });
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		hide.setLayoutData(gd);
		hide.setBackground(lightGray);

		stackLayout.topControl = mainComposite;
		getSection().setClient(stackComposite);
	}

	@Override
	protected void initializeToolbar(ToolBarManager manager) {
		// no toolbar
	}

	@Override
	protected String getSectionTitle() {
		return "Jumpstart";
	}

	@Override
	protected String getSectionInfoKey() {
		return "gl.jumpstart";
	}

	@Override
	public void dispose() {
		if (lightGray != null) {
			lightGray.dispose();
			lightGray = null;
		}

		super.dispose();
	}

	private class AddGameAction implements ICustomAction {

		public AddGameAction() {
			super();
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public String getText() {
			return "game to library";
		}

		@Override
		public void run(int x, int y) {
			libraryPart.runAddGame();
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

	private final class HideBoardGamesAction implements ICustomAction {

		public HideBoardGamesAction() {
			super();
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public String getText() {
			return "board games";
		}

		@Override
		public void run(int x, int y) {
			libraryPart.runHideBoardGamesAction();
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

	private final class HideCardGamesAction implements ICustomAction {

		public HideCardGamesAction() {
			super();
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public String getText() {
			return "card games";
		}

		@Override
		public void run(int x, int y) {
			libraryPart.runHideCardGamesAction();
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

	private final class HideVideoGamesAction implements ICustomAction {

		public HideVideoGamesAction() {
			super();
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public String getText() {
			return "video games";
		}

		@Override
		public void run(int x, int y) {
			libraryPart.runHideVideoGamesAction();
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}