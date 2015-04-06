package com.braithwood.gl.ui.custom;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ILayoutExtension;

public class PrettySection extends Canvas {

	private Image image = null;
	private String text = "Unnamed";
	private boolean noMargin = false;
	private Color white;
	private Color light;
	private Color black;
	private Color gray;
	private boolean hasFocus = false;
	private ToolBarManager toolbarManager;
	private Control control;
	private Font headerFont;
	private boolean updateToolbarBackground = false;
	private FocusListener focusListener;
	private Set<Control> hooked = new HashSet<Control>();

	public PrettySection(Composite parent, FormToolkit toolkit) {
		this(parent, toolkit, false);
	}

	public PrettySection(Composite parent, FormToolkit toolkit, boolean noMargin) {
		super(parent, SWT.DOUBLE_BUFFERED);

		this.noMargin = noMargin;
		headerFont = FontServices.growFont(this, 1.1, true);

		toolbarManager = new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT) {
			@Override
			public void update(boolean force) {
				super.update(force);
				PrettySection.this.redraw();
			}
		};
		ToolBar bar = toolbarManager.createControl(this);
		bar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		toolkit.adapt(this);

		FormColors colors = toolkit.getColors();

		black = new Color(this.getDisplay(), 0, 0, 0);
		white = new Color(this.getDisplay(), 255, 255, 255);
		gray = new Color(this.getDisplay(), 180, 180, 180);
		light = colors.getColor(IFormColors.H_GRADIENT_START);

		this.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				onPaint(e);
			}
		});

		super.setLayout(new PrettyLayout(bar));

		focusListener = new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (hasFocus)
					return;
				hasFocus = true;
				updateToolbarBackground = true;
				redraw();
			}

			public void focusLost(FocusEvent e) {
				if (!hasFocus)
					return;
				hasFocus = false;
				updateToolbarBackground = true;
				redraw();
			}
		};
		super.addFocusListener(focusListener);

		super.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				updateToolbarBackground = true;
				redraw();
			}

			@Override
			public void controlMoved(ControlEvent e) {
				updateToolbarBackground = true;
				redraw();
			}
		});
	}

	protected void onPaint(PaintEvent e) {
		// Linux wants to make the toolbar about 26 pixels tall.
		// PrettySection must compensate for this by drawing the
		// header 10 pixels taller
		boolean runningLinux = Platform.OS_LINUX.equals(Platform.getOS());

		Rectangle bounds = getClientArea();

		Image buffer = new Image(getDisplay(), bounds.width, bounds.height);
		buffer.setBackground(getBackground());
		GC gc = new GC(buffer);
		enableAntiAlias(gc, SWT.ON);

		gc.setBackground(white);
		gc.fillRectangle(bounds);

		gc.setForeground(white);
		Color lightInUse = light;

		gc.setBackground(lightInUse);
		if (runningLinux)
			gc.fillGradientRectangle(bounds.x, bounds.y, bounds.width, 30, true);
		else
			gc.fillGradientRectangle(bounds.x, bounds.y, bounds.width, 20, true);

		gc.setForeground(gray);
		if (runningLinux)
			gc.drawLine(bounds.x, bounds.y + 29, bounds.x + bounds.width, bounds.y + 29);
		else
			gc.drawLine(bounds.x, bounds.y + 19, bounds.x + bounds.width, bounds.y + 19);

		if (image != null) {
			if (runningLinux)
				gc.drawImage(image, 2, 12);
			else
				gc.drawImage(image, 2, 2);
		}

		gc.setForeground(black);
		gc.setFont(headerFont);
		FontMetrics fm = gc.getFontMetrics();
		if (runningLinux)
			gc.drawString(text, bounds.x + 2, bounds.y + 24 - fm.getHeight(), true);
		else
			gc.drawString(text, bounds.x + 2, bounds.y + 16 - fm.getHeight(), true);

		ToolBar toolbar = toolbarManager.getControl();
		if (toolbar.isVisible()) {
			Rectangle barRect = toolbar.getBounds();
			Rectangle imageSize = null;
			Image existingImg = toolbar.getBackgroundImage();
			if (existingImg != null)
				imageSize = existingImg.getBounds();
			if (updateToolbarBackground || (barRect.width > 0 && barRect.height > 0 && (existingImg == null || (imageSize.width != barRect.width || imageSize.height != barRect.height)))) {
				Image buffer2 = new Image(getDisplay(), barRect.width, barRect.height);
				buffer2.setBackground(getBackground());
				GC gc2 = new GC(buffer2);
				gc2.drawImage(buffer, barRect.x, barRect.y, barRect.width, barRect.height, 0, 0, barRect.width, barRect.height);
				gc2.dispose();
				toolbar.setBackgroundImage(buffer2);
				if (existingImg != null)
					existingImg.dispose();
				updateToolbarBackground = false;
			}
		}

		gc.dispose();
		e.gc.drawImage(buffer, 0, 0);
		buffer.dispose();
	}

	@Override
	public void dispose() {
		if (!headerFont.isDisposed()) {
			headerFont.dispose();
		}

		super.dispose();
	}

	private void enableAntiAlias(GC gc, int style) {
		if (!gc.getAdvanced()) {
			gc.setAdvanced(true);
			if (!gc.getAdvanced())
				return;
		}
		gc.setAntialias(style);
		gc.setTextAntialias(style);
	}

	public ToolBarManager getToolBarManager() {
		return toolbarManager;
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}

	public Image getImage() {
		return this.image;
	}

	public void setText(String text) {
		this.text = text;
		redraw();
	}

	public String getText() {
		return text;
	}

	public void setClient(Control client) {
		if (client.getLayoutData() == null || !(client.getLayoutData() instanceof GridData)) {
			GridData gd = new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1);
			gd.verticalIndent = 1;
			client.setLayoutData(gd);
		} else {
			GridData gd = (GridData) client.getLayoutData();
			gd.verticalIndent = 1;
		}
		control = client;
		childrenUpdated();
	}

	@Override
	public void setLayout(Layout layout) {
		// layouts can not be set on the pretty section
		throw new IllegalStateException("Not available on PrettySection");
	}

	public Control getClient() {
		return control;
	}

	public void childrenUpdated() {
		hookFocusListener(control);
	}

	private void hookFocusListener(Control toHook) {
		if (toHook instanceof PrettySection && toHook != this)
			return; // don't hook child sections

		if (!hooked.contains(toHook)) {
			toHook.addFocusListener(focusListener);
			hooked.add(toHook);
		}
		if (toHook instanceof Composite) {
			for (Control next : ((Composite) toHook).getChildren())
				hookFocusListener(next);
		}
	}

	@Override
	public void layout(boolean changed, boolean all) {
		if (control != null)
			childrenUpdated();
		super.layout(changed, all);
	}

	private final class PrettyLayout extends Layout implements ILayoutExtension {

		private final ToolBar bar;

		PrettyLayout(ToolBar bar) {
			super();

			this.bar = bar;
		}

		@Override
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			Point size;
			int xExtra;
			if (noMargin) {
				xExtra = 4;
			} else {
				xExtra = 12;
			}
			int cwHint = wHint;
			if (cwHint != SWT.DEFAULT) {
				cwHint -= xExtra;
			}

			int yExtra = 30;
			if (control != null) {
				Point p = control.computeSize(cwHint, SWT.DEFAULT, true);
				size = new Point(Math.max(150, p.x + xExtra), Math.max(80, p.y + yExtra));
			} else {
				size = new Point(150, yExtra);
			}

			size.y = Math.max(size.y, 80);
			return size;
		}

		@Override
		protected void layout(Composite composite, boolean flushCache) {
			// Linux wants to make the toolbar about 26 pixels tall.
			// PrettySection must compensate for this by drawing the
			// header 10 pixels taller
			boolean runningLinux = Platform.OS_LINUX.equals(Platform.getOS());

			Rectangle bounds = composite.getClientArea();
			Point barSize = bar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			if (toolbarManager.isEmpty())
				bar.setVisible(false);
			else {
				int x = bounds.x + bounds.width - barSize.x - 2;

				int y;
				if (runningLinux)
					y = Math.max(0, bounds.y + 29 - barSize.y);
				else
					y = Math.max(0, bounds.y + 19 - barSize.y);

				if (Platform.OS_WIN32.equals(Platform.getOS()))
					y -= 2;
				if (runningLinux)
					y -= 2;

				y = Math.max(0, y);
				bar.setVisible(true);
				bar.setBounds(x, y, barSize.x, barSize.y);
			}

			int x;
			int areaWidth;
			if (noMargin) {
				x = bounds.x + 2;
				areaWidth = bounds.width - 4;
			} else {
				x = bounds.x + 6;
				areaWidth = bounds.width - 12;
			}

			int y;
			if (runningLinux)
				y = bounds.y + 36;
			else
				y = bounds.y + 26;
			int yOffset = 0;

			if (control != null) {
				if (runningLinux)
					control.setBounds(x, y + yOffset, areaWidth, bounds.height - 36 - yOffset);
				else
					control.setBounds(x, y + yOffset, areaWidth, bounds.height - 26 - yOffset);
			}
		}

		public int computeMaximumWidth(Composite parent, boolean changed) {
			return computeSize(parent, SWT.DEFAULT, SWT.DEFAULT, changed).x;
		}

		public int computeMinimumWidth(Composite parent, boolean changed) {
			return computeSize(parent, 0, SWT.DEFAULT, changed).x;
		}
	}
}