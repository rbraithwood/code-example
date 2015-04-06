package com.braithwood.gl.ui;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the actions added to a workbench window. Each
 * window will be populated with new actions.
 */
public class GLActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction helpAction;

	public GLActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		helpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		// need to have something in the toolbar to make the UI look right
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		toolbar.add(new GroupMarker("MainContrib"));
		toolbar.add(helpAction);
		coolBar.add(new ToolBarContributionItem(toolbar, "com.braithwood.gl.toolbars.main"));
	}

	public void fillTrayItem(MenuManager trayMenu) {
		trayMenu.add(exitAction);
	}

	@Override
	protected void fillMenuBar(final IMenuManager menuBar) {
		menuBar.add(createFileMenu());
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createHelpMenu());
	}

	private IContributionItem createFileMenu() {
		MenuManager menu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		menu.add(new Separator(IWorkbenchActionConstants.FILE_START));
		menu.add(new Separator(IWorkbenchActionConstants.GROUP_FILE));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IWorkbenchActionConstants.FILE_END));
		menu.add(exitAction);
		return menu;
	}

	private MenuManager createHelpMenu() {
		MenuManager menu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		menu.add(new Separator(IWorkbenchActionConstants.HELP_START));
		menu.add(new Separator(IWorkbenchActionConstants.GROUP_HELP));
		menu.appendToGroup(IWorkbenchActionConstants.GROUP_HELP, helpAction);
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IWorkbenchActionConstants.HELP_END));
		menu.add(aboutAction);
		return menu;
	}
}