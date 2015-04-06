package com.braithwood.gl.ui.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class CustomCanvas extends Canvas {

	public CustomCanvas(Composite parent, int style) {
		super(parent, style);

		setFont(parent.getShell().getFont());

		if ((style & SWT.NO_FOCUS) == 0)
			addListener(SWT.Traverse, new TraverseListener());
	}

	private final class TraverseListener implements Listener {
		public void handleEvent(Event e) {
			switch (e.detail) {
			/* Do tab group traversal */
			case SWT.TRAVERSE_ESCAPE:
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_PAGE_NEXT:
			case SWT.TRAVERSE_PAGE_PREVIOUS:
				e.doit = true;
				break;
			}
		}
	}
}