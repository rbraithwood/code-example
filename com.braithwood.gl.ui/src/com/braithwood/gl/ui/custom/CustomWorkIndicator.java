package com.braithwood.gl.ui.custom;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class CustomWorkIndicator extends CustomPaintedCanvas {

	private static final Timer timer = new Timer("animate", true);
	private static boolean useAdvanced = true;

	private final int width;
	private final int height;
	private final int lineWeight;

	private boolean showing = false;
	private int arcDegree = 0;
	private AnimateTask animateTask;
	private Image image;
	private long lastRun = -1;

	public CustomWorkIndicator(Composite parent, int width, int height, int lineWeight) {
		super(parent, SWT.DOUBLE_BUFFERED | SWT.WRAP);

		// light blue
		setForeground(new Color(parent.getDisplay(), 210, 230, 255));

		// white
		setBackground(new Color(parent.getDisplay(), 255, 255, 255));

		this.width = width;
		this.height = height;
		this.lineWeight = lineWeight;
	}

	public void setWorking(boolean working) {
		if (working == showing)
			return;

		if (working) {
			arcDegree = 270;
			showing = working;
			animateTask = new AnimateTask();
			timer.scheduleAtFixedRate(animateTask, 33, 33);
		} else {
			animateTask.cancel();
			showing = false;
			lastRun = -1;
		}
		redraw();
	}

	protected void doOnPaint(PaintEvent e, Rectangle bounds) {
		if (!showing) {
			if (image != null) {
				e.gc.drawImage(image, 0, 0);
			}
			return;
		}

		if (useAdvanced) {
			try {
				e.gc.setAdvanced(true);
			} catch (Exception notavailable) {
				useAdvanced = false;
			}
		}

		e.gc.setForeground(getForeground());
		if (useAdvanced) {
			e.gc.setAlpha(220);
			e.gc.setAntialias(SWT.ON);
			e.gc.setLineAttributes(new LineAttributes(lineWeight, SWT.CAP_ROUND, SWT.JOIN_ROUND));
		} else {
			e.gc.setLineCap(SWT.CAP_ROUND);
			e.gc.setLineJoin(SWT.JOIN_ROUND);
			e.gc.setLineWidth(lineWeight);
		}

		int arcWidth = width - 1 - (lineWeight * 2);
		int arcHeight = height - 1 - (lineWeight * 2);
		if (useAdvanced) {
			int increment = 10;
			int multiplier = 3;
			if (width > 30) {
				increment = 4;
				multiplier = 1;
			}
			int localDegree = arcDegree;
			int ticks = 250 / increment;
			for (int i = 0; i < ticks; i++) {
				e.gc.setAlpha((ticks * multiplier) - ((ticks - i) * multiplier));
				e.gc.drawArc(lineWeight, lineWeight, arcWidth, arcHeight, localDegree + (i * increment), increment);
			}
		} else {
			e.gc.drawArc(lineWeight, lineWeight, arcWidth, arcHeight, arcDegree, 250);
		}
	}

	@Override
	public void dispose() {
		if (showing)
			animateTask.cancel();
		super.dispose();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(width, height);
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		return new Point(width, height);
	}

	public void setImage(Image image) {
		this.image = image;
		if (!showing)
			redraw();
	}

	private final class AnimateTask extends TimerTask {

		@Override
		public void run() {
			arcDegree += 10;
			if (arcDegree >= 360)
				arcDegree = 0;
			try {
				if (isDisposed() || getDisplay().isDisposed()) {
					animateTask.cancel();
				} else {
					long now = System.currentTimeMillis();
					if (lastRun > 0 && Math.abs(lastRun - now) > 30000) {
						animateTask.cancel();
						if (showing)
							timer.scheduleAtFixedRate(animateTask, 33, 33);
					} else {
						getDisplay().syncExec(new AnimateRedraw());
					}
					if (showing)
						lastRun = now;
				}
			} catch (Throwable t) {
				// ignore errors
			}
		}
	}

	private final class AnimateRedraw implements Runnable {
		public void run() {
			if (!isDisposed())
				redraw();
		}
	}
}