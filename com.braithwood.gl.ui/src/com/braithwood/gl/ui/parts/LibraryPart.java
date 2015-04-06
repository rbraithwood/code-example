package com.braithwood.gl.ui.parts;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.actions.AddGameAction;
import com.braithwood.gl.ui.actions.DeleteGameAction;
import com.braithwood.gl.ui.actions.HideBoardGamesAction;
import com.braithwood.gl.ui.actions.HideCardGamesAction;
import com.braithwood.gl.ui.actions.HideVideoGamesAction;
import com.braithwood.gl.ui.model.BoardGame;
import com.braithwood.gl.ui.model.CardGame;
import com.braithwood.gl.ui.model.GLEntity;
import com.braithwood.gl.ui.model.Game;
import com.braithwood.gl.ui.model.VideoGame;
import com.braithwood.gl.ui.services.GameLibraryServices;

public class LibraryPart extends GLSectionPart implements IMenuListener {

	private Color lightGray;
	private ApplyFilterJob applyFilterJob;
	private Text filterText;
	private TableViewer viewer;
	private GamesFilter gamesFilter;
	private HideBoardGamesFilter boardFilter;
	private HideCardGamesFilter cardFilter;
	private HideVideoGamesFilter videoFilter;
	private AddGameAction addAction;
	private DeleteGameAction deleteAction;
	private HideBoardGamesAction hideBoardAction;
	private HideCardGamesAction hideCardAction;
	private HideVideoGamesAction hideVideoAction;

	public LibraryPart(Composite parent, FormToolkit toolkit) {
		super(parent, toolkit, true);
	}

	protected void createSectionContents(Composite parent, FormToolkit toolkit) {
		RGB rgb = new RGB(247, 247, 255);
		lightGray = new Color(parent.getDisplay(), rgb);

		createStackComposite(parent, toolkit);
		createMainComposite(stackComposite, toolkit);

		GridLayout layout = (GridLayout) mainComposite.getLayout();
		layout.verticalSpacing = 2;

		applyFilterJob = new ApplyFilterJob();

		filterText = toolkit.createText(mainComposite, "");
		filterText.setMessage("Enter filter text");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		filterText.setLayoutData(gd);
		filterText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				applyFilterJob.setFilterText(filterText.getText().trim().toLowerCase());
				applyFilterJob.cancel();
				applyFilterJob.schedule(500);
			}
		});
		filterText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '\r') {
					applyFilterJob.setFilterText(filterText.getText().trim().toLowerCase());
					applyFilterJob.cancel();
					applyFilterJob.schedule();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing
			}
		});

		Table table = toolkit.createTable(mainComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 150;
		gd.heightHint = 300;
		table.setLayoutData(gd);
		table.setBackground(lightGray);

		viewer = new TableViewer(table);
		viewer.setUseHashlookup(true);
		viewer.setContentProvider(new GamesContentProvider());
		viewer.setLabelProvider(new GamesLabelProvider(table));
		gamesFilter = new GamesFilter();
		viewer.addFilter(gamesFilter);
		boardFilter = new HideBoardGamesFilter(viewer);
		viewer.addFilter(boardFilter);
		cardFilter = new HideCardGamesFilter(viewer);
		viewer.addFilter(cardFilter);
		videoFilter = new HideVideoGamesFilter(viewer);
		viewer.addFilter(videoFilter);

		addAction = new AddGameAction(viewer);
		deleteAction = new DeleteGameAction(viewer);
		deleteAction.setEnabled(false);
		viewer.addSelectionChangedListener(deleteAction);
		hideBoardAction = new HideBoardGamesAction(boardFilter);
		hideCardAction = new HideCardGamesAction(cardFilter);
		hideVideoAction = new HideVideoGamesAction(videoFilter);

		createWorkingComposite(stackComposite, toolkit);

		new RetrieveGamesJob().schedule();

		getSection().setClient(stackComposite);
	}

	@Override
	protected void initializeToolbar(ToolBarManager manager) {
		manager.add(addAction);
		manager.add(new Separator());
		manager.add(deleteAction);
		manager.add(new Separator());
		manager.add(hideBoardAction);
		manager.add(hideCardAction);
		manager.add(hideVideoAction);
	}

	@Override
	protected void initializeMenu() {
		Table table = viewer.getTable();
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator("additions"));
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(this);
		Menu menu = menuManager.createContextMenu(table);
		table.setMenu(menu);
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		manager.add(addAction);
		manager.add(new Separator());
		manager.add(deleteAction);
		manager.add(new Separator());
		manager.add(hideBoardAction);
		manager.add(hideCardAction);
		manager.add(hideVideoAction);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void runAddGame() {
		if (addAction != null) {
			addAction.run();
		}
	}

	public void runHideBoardGamesAction() {
		if (hideBoardAction != null) {
			hideBoardAction.run();
		}
	}

	public void runHideCardGamesAction() {
		if (hideCardAction != null) {
			hideCardAction.run();
		}
	}

	public void runHideVideoGamesAction() {
		if (hideVideoAction != null) {
			hideVideoAction.run();
		}
	}

	@Override
	protected String getSectionTitle() {
		return "Library";
	}

	@Override
	protected String getSectionInfoKey() {
		return "gl.library";
	}

	private void updateSection() {
		setInputOnUIThread(viewer, new Object());
	}

	private void filter(String filterText) {
		gamesFilter.setFilterText(filterText);
		viewer.refresh();
	}

	@Override
	public void dispose() {
		lightGray.dispose();
		lightGray = null;

		super.dispose();
	}

	public static class HideBoardGamesFilter extends ViewerFilter {

		private final TableViewer viewer;
		private boolean active;

		public HideBoardGamesFilter(TableViewer viewer) {
			super();

			this.viewer = viewer;
			active = false;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof BoardGame) {
				return !active;
			}
			return true;
		}

		public void setActive(boolean active) {
			this.active = active;
			viewer.refresh();
		}
	}

	public static class HideCardGamesFilter extends ViewerFilter {

		private final TableViewer viewer;
		private boolean active;

		public HideCardGamesFilter(TableViewer viewer) {
			super();

			this.viewer = viewer;
			active = false;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof CardGame) {
				return !active;
			}
			return true;
		}

		public void setActive(boolean active) {
			this.active = active;
			viewer.refresh();
		}
	}

	public static class HideVideoGamesFilter extends ViewerFilter {

		private final TableViewer viewer;
		private boolean active;

		public HideVideoGamesFilter(TableViewer viewer) {
			super();

			this.viewer = viewer;
			active = false;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof VideoGame) {
				return !active;
			}
			return true;
		}

		public void setActive(boolean active) {
			this.active = active;
			viewer.refresh();
		}
	}

	private final class GamesFilter extends ViewerFilter {

		private String filterText;

		public GamesFilter() {
			super();
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (filterText == null || filterText.length() == 0)
				return true;

			if (element instanceof Game) {
				Game game = (Game) element;

				String label = game.getLabel().toLowerCase();
				return label.indexOf(filterText) >= 0;
			}
			return true;
		}

		public void setFilterText(String filterText) {
			this.filterText = filterText.toLowerCase();
		}
	}

	private static class GamesLabelProvider extends LabelProvider {

		public GamesLabelProvider(Control control) {
			super();
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof BoardGame) {
				return GameLibraryUIActivator.getImage("icons/board-game16.png");
			}

			if (element instanceof CardGame) {
				return GameLibraryUIActivator.getImage("icons/card-game16.png");
			}

			if (element instanceof VideoGame) {
				return GameLibraryUIActivator.getImage("icons/video-game16.png");
			}

			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof GLEntity) {
				GLEntity entity = (GLEntity) element;
				return entity.getLabel();
			}

			return "";
		}
	}

	private static class GamesContentProvider implements IStructuredContentProvider {

		public GamesContentProvider() {
			super();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return GameLibraryServices.getInstance().getGames().toArray();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}
	}

	private final class ApplyFilterJob extends Job {

		private String filterText;

		public ApplyFilterJob() {
			super("apply games filter");
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					filter(filterText);
				}
			});

			return Status.OK_STATUS;
		}

		public void setFilterText(String filterText) {
			this.filterText = filterText;
		}
	}

	private final class RetrieveGamesJob extends Job {

		public RetrieveGamesJob() {
			super("retrieve games");
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			Display.getDefault().asyncExec(new ShowWorkingCompositeRunnable());

			try {
				updateSection();
			} finally {
				Display.getDefault().asyncExec(new ShowMainCompositeRunnable());
			}

			return Status.OK_STATUS;
		}
	}
}