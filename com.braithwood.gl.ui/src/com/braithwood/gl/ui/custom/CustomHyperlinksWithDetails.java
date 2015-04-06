package com.braithwood.gl.ui.custom;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class CustomHyperlinksWithDetails extends CustomPaintedCanvas {

	private Image image;
	private String title = "";
	private String noActionsText = "";
	private ICustomAction actions[] = new ICustomAction[0];
	private String actionPrefix = "";
	private Font boldFont;
	private Color blue;
	private Color black;
	private Color gray;
	private Rectangle[] actionsPlacement;
	private ICustomAction actionHovering;
	private String andXMorePattern;
	private boolean singleLinkRow;
	private Cursor hoverCursor;

	public CustomHyperlinksWithDetails(Composite parent, int style) {
		super(parent, style);

		FontData[] fd = getFont().getFontData();
		this.boldFont = new Font(getDisplay(), fd[0].getName(), fd[0].getHeight() + 1, SWT.BOLD);

		this.blue = new Color(getDisplay(), 60, 90, 220);
		this.black = new Color(getDisplay(), 0, 0, 0);
		this.gray = new Color(getDisplay(), 120, 120, 120);

		hoverCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);

		CustomMouseListener mouseListener = new CustomMouseListener();
		addMouseListener(mouseListener);
		addMouseMoveListener(mouseListener);
		addMouseTrackListener(mouseListener);
	}

	@Override
	public void dispose() {
		super.dispose();

		boldFont.dispose();
		hoverCursor.dispose();

		blue.dispose();
		black.dispose();
		gray.dispose();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNoActionsText() {
		return noActionsText;
	}

	public void setNoActionsText(String noActionsText) {
		this.noActionsText = noActionsText;
	}

	public ICustomAction[] getActions() {
		return actions;
	}

	public void setActions(ICustomAction[] actions) {
		this.actions = actions;
	}

	public String getActionPrefix() {
		return actionPrefix;
	}

	public String getAndXMorePattern() {
		return andXMorePattern;
	}

	public void setActionSurrounds(String actionPrefix, String andXMorePattern) {
		this.actionPrefix = actionPrefix;
		this.andXMorePattern = andXMorePattern;
	}

	@Override
	protected void doOnPaint(PaintEvent e, Rectangle clientArea) {
		e.gc.setAdvanced(true);
		e.gc.setAlpha(isEnabled() ? 255 : 120);
		e.gc.setFont(getFont());
		e.gc.setTextAntialias(SWT.ON);
		e.gc.setAntialias(SWT.ON);

		e.gc.setFont(getFont());
		int linkTextHeight = e.gc.textExtent("AqBw").y;

		List<ICustomAction> actionsLine1 = new ArrayList<ICustomAction>();
		List<ICustomAction> actionsLine2 = new ArrayList<ICustomAction>();
		int linksOffsetX = 0;
		if (actionPrefix != null && actionPrefix.length() > 0) {
			linksOffsetX += e.gc.textExtent(actionPrefix + " ").x;
		}
		int availableSize = clientArea.width - (image != null ? image.getBounds().width + 5 : 0);
		boolean firstRow = true;
		List<ICustomAction> filling = actionsLine1;
		for (int i = 0; i < actions.length; i++) {
			String text = actions[i].getText();
			Point size = e.gc.textExtent(text);
			if (linksOffsetX + size.x > availableSize) {
				if (firstRow) {
					linksOffsetX = 0;
					filling = actionsLine2;
				} else {
					break;
				}
			}
			filling.add(actions[i]);
			linksOffsetX += size.x;
		}

		e.gc.setFont(boldFont);
		int titleTextHeight = e.gc.textExtent(title).y;

		int offsetX = 0;
		int offsetY = 0;
		if (image != null) {
			Rectangle imgBounds = image.getBounds();
			e.gc.drawImage(image, clientArea.x, clientArea.y + ((clientArea.height - imgBounds.height) / 2));
			offsetX += imgBounds.width + 5;
			offsetY += (clientArea.height - (titleTextHeight + 1 + linkTextHeight + (!actionsLine2.isEmpty() ? linkTextHeight : 0))) / 2;
		}

		e.gc.setForeground(black);
		e.gc.setFont(boldFont);
		e.gc.drawText(title, offsetX, offsetY, true);
		offsetY += titleTextHeight + 1;
		e.gc.setFont(getFont());

		if (actions == null || actions.length == 0) {
			e.gc.drawText(noActionsText, offsetX, offsetY, true);
			actionsPlacement = null;
		} else {

			linksOffsetX = offsetX;

			if (actionPrefix != null && actionPrefix.length() > 0) {
				e.gc.setForeground(gray);
				e.gc.drawText(actionPrefix, linksOffsetX, offsetY, true);
				linksOffsetX += e.gc.textExtent(actionPrefix + " ").x;
			}
			actionsPlacement = new Rectangle[actionsLine1.size() + actionsLine2.size()];

			for (int i = 0; i < actionsPlacement.length; i++) {

				if (actionHovering == actions[i])
					e.gc.setForeground(gray);
				else
					e.gc.setForeground(blue);

				if (i == actionsLine1.size()) {
					linksOffsetX = offsetX;
					offsetY += linkTextHeight;
				}

				String text = actions[i].getText();
				Point linkSize = e.gc.textExtent(text);
				e.gc.drawText(text, linksOffsetX, offsetY, true);
				e.gc.drawLine(linksOffsetX, offsetY + linkSize.y - 1, linksOffsetX + linkSize.x, offsetY + linkSize.y - 1);
				actionsPlacement[i] = new Rectangle(linksOffsetX, offsetY, linkSize.x, linkSize.y);

				linksOffsetX += linkSize.x;
				if (i + 1 < actions.length) {
					e.gc.setForeground(gray);
					String more = ",";
					if (i + 1 == actionsPlacement.length)
						more = MessageFormat.format(andXMorePattern, actions.length - actionsPlacement.length);
					e.gc.drawText(more, linksOffsetX, offsetY, true);
					linksOffsetX += e.gc.textExtent(more + " ").x;
				}
			}
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return computeSize(wHint, hHint);
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		GC gc = new GC(this);
		try {
			gc.setAdvanced(true);
			gc.setTextAntialias(SWT.ON);
			gc.setAntialias(SWT.ON);
			gc.setFont(boldFont);
			Rectangle imageSize;
			if (image != null)
				imageSize = image.getBounds();
			else
				imageSize = new Rectangle(0, 0, 0, 0);

			Point textExtents = gc.textExtent(title);
			gc.setFont(getFont());
			int linkHeight = gc.textExtent("AqBw").y;

			return new Point(imageSize.width + textExtents.x + 5, Math.max(imageSize.height, textExtents.y + linkHeight + (singleLinkRow ? 0 : linkHeight) + 1));
		} finally {
			gc.dispose();
		}
	}

	public void setSingleLinkRow(boolean b) {
		this.singleLinkRow = b;
	}

	public Object getActionAt(int x, int y) {
		Point p = new Point(x, y);
		for (int i = 0; i < actionsPlacement.length; i++) {
			if (actionsPlacement[i] != null && actionsPlacement[i].contains(p)) {
				return actions[i];
			}
		}
		return null;
	}

	public Object getAction(int index) {
		return actions[index];
	}

	public Rectangle getBounds(Object item) {
		int index = getActionIndex(item);
		if (index >= 0)
			return actionsPlacement[index];
		return new Rectangle(0, 0, 0, 0);
	}

	public int getActionIndex(Object item) {
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] == item)
				return i;
		}
		return -1;
	}

	public int getActionCount() {
		return actions.length;
	}

	public String getActionText(Object item) {
		int index = getActionIndex(item);
		if (index >= 0)
			return actions[index].getText();
		return null;
	}

	public class CustomMouseListener implements MouseListener, MouseMoveListener, MouseTrackListener {

		private boolean clicking = false;

		public void mouseDoubleClick(MouseEvent e) {
			// nothing to do
		}

		public void mouseDown(MouseEvent e) {
			if (actionsPlacement == null)
				return;

			if (actionHovering != null)
				clicking = true;
		}

		public void mouseUp(MouseEvent e) {
			if (actionsPlacement == null)
				return;

			if (clicking) {
				if (actionHovering != null) {
					ICustomAction actionToRun = actionHovering;
					clicking = false;
					actionHovering = null;
					setToolTipText(null);
					redraw();

					int x = -1;
					int y = -1;
					for (int i = 0; i < actionsPlacement.length; i++) {
						if (actionHovering == actions[i]) {
							Rectangle rect = actionsPlacement[i];
							Point topLeft = toDisplay(rect.x, rect.y);
							x = topLeft.x + (rect.height / 2);
							y = topLeft.y + (rect.height / 2);
							break;
						}
					}

					actionToRun.run(x, y);
				} else {
					clicking = false;
				}
			}
		}

		public void mouseEnter(MouseEvent e) {
			// nothing to do
		}

		public void mouseExit(MouseEvent e) {
			if (actionsPlacement == null)
				return;

			if (clicking || actionHovering != null) {
				actionHovering = null;
				setToolTipText(null);
				redraw();
			}
		}

		public void mouseMove(MouseEvent e) {
			if (actionsPlacement == null)
				return;

			ICustomAction wasHovering = actionHovering;
			ICustomAction nowHovering = null;
			Point p = new Point(e.x, e.y);
			for (int i = 0; i < actionsPlacement.length; i++) {
				if (actionsPlacement[i].contains(p)) {
					nowHovering = actions[i];
					break;
				}
			}
			if (wasHovering != nowHovering) {
				actionHovering = nowHovering;
				if (nowHovering == null) {
					CustomHyperlinksWithDetails.this.setCursor(null);
				} else {
					CustomHyperlinksWithDetails.this.setCursor(hoverCursor);
				}
				redraw();
			}
		}

		public void mouseHover(MouseEvent e) {
			// nothing to do
		}
	}
}