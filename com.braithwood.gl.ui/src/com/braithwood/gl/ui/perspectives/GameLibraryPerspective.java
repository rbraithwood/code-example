package com.braithwood.gl.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.braithwood.gl.ui.views.GameLibraryView;

public class GameLibraryPerspective implements IPerspectiveFactory {

	public static final String ID = "com.braithwood.gl.ui.perspective";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(GameLibraryView.ID, false, IPageLayout.TOP, IPageLayout.RATIO_MAX, layout.getEditorArea());
	}
}
