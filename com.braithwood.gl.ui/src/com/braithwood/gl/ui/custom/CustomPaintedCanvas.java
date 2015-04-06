package com.braithwood.gl.ui.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public abstract class CustomPaintedCanvas extends CustomCanvas {

	public CustomPaintedCanvas(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);

		this.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				onPaint(e);
			}
		});
	}

	private final void onPaint(PaintEvent e) {
		if (isDisposed())
			return;

		doOnPaint(e, getClientArea());
	}

	protected abstract void doOnPaint(PaintEvent e, Rectangle clientArea);
}