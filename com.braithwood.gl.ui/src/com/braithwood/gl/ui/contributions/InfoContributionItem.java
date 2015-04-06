package com.braithwood.gl.ui.contributions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.progress.UIJob;

import com.braithwood.gl.ui.GameLibraryUIActivator;
import com.braithwood.gl.ui.services.InfopopServices;

public class InfoContributionItem extends ContributionItem {

	private static final int HOVER_DELAY = 250;

	private final ImageDescriptor imageDescriptor;
	private final Control control;
	private ToolItem toolItem;
	private Listener toolItemListener;
	private LocalResourceManager imageManager;

	private String infoMessage = null;
	private Map<String, Image> infoMessageImages = new HashMap<String, Image>();
	private Color yellow;
	private boolean infoHover;
	private HoverJob hoverJob;
	private HoverToolTip hoverToolTip;

	public InfoContributionItem(String infoKey, Control control) {
		super();

		this.control = control;
		this.yellow = new Color(control.getDisplay(), 250, 250, 210);

		this.imageDescriptor = GameLibraryUIActivator.getImageDescriptor("icons/info12.png");

		this.hoverToolTip = new HoverToolTip(control);

		InfopopEntry entry = InfopopServices.getInstance().getEntry(infoKey);
		if (entry != null) {
			buildInfoMessage(entry);
		}
	}

	private void buildInfoMessage(InfopopEntry entry) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<form><p><b>");
		buffer.append(entry.getTitle().trim());
		buffer.append("</b></p><p>");
		buffer.append(entry.getBody().trim());
		buffer.append("</p>");

		if (entry.getActions() != null) {
			StringBuffer actionBuffer = new StringBuffer();
			for (InfopopEntryAction action : entry.getActions()) {
				actionBuffer.append("<li style=\"image\" value=\"");
				actionBuffer.append(action.getId());
				actionBuffer.append("\">");
				actionBuffer.append(action.getDescription());
				actionBuffer.append("</li>");

				infoMessageImages.put(action.getId(), GameLibraryUIActivator.getImage(action.getImageLocation()));
			}

			if (actionBuffer.length() > 0) {
				buffer.append("<p><b>Actions</b></p>");
				buffer.append(actionBuffer);
			}
		}

		buffer.append("</form>");

		infoMessage = buffer.toString();
	}

	@Override
	public void fill(ToolBar parent, int index) {
		if (toolItem == null && parent != null) {
			if (index >= 0) {
				toolItem = new ToolItem(parent, SWT.PUSH, index);
			} else {
				toolItem = new ToolItem(parent, SWT.PUSH);
			}
			toolItem.setData(this);
			toolItem.addListener(SWT.Dispose, getToolItemListener());

			parent.addMouseMoveListener(new MouseMoveListener() {

				@Override
				public void mouseMove(MouseEvent e) {
					boolean nowIn = toolItem.getBounds().contains(e.x, e.y);
					if (nowIn != infoHover) {
						infoHover = nowIn;
						if (infoHover) {
							showInfo();
						} else {
							hideInfo();
						}
					}
				}
			});

			parent.addMouseTrackListener(new MouseTrackAdapter() {

				@Override
				public void mouseExit(MouseEvent e) {
					infoHover = false;
					hideInfo();
				}
			});

			update(null);
		}
	}

	private void showInfo() {
		synchronized (InfoContributionItem.this) {
			if (hoverJob == null) {
				hoverJob = new HoverJob();
				hoverJob.schedule(HOVER_DELAY);
			}
		}
	}

	private void hideInfo() {
		synchronized (InfoContributionItem.this) {
			if (hoverJob != null) {
				hoverJob.cancel();
				hoverJob = null;
			}
			hoverToolTip.hide();
		}
	}

	@Override
	public void update(String propertyName) {
		if (toolItem != null) {
			updateImages();

			if (!Platform.OS_LINUX.equals(Platform.getOS()))
				toolItem.setEnabled(false);
		}
	}

	private boolean updateImages() {
		ResourceManager parentResourceManager = JFaceResources.getResources();
		LocalResourceManager localManager = new LocalResourceManager(parentResourceManager);

		toolItem.setDisabledImage(imageDescriptor == null ? null : localManager.createImageWithDefault(imageDescriptor));
		toolItem.setImage(imageDescriptor == null ? null : localManager.createImageWithDefault(imageDescriptor));

		disposeOldImages();
		imageManager = localManager;

		return imageDescriptor != null;
	}

	private void disposeOldImages() {
		if (imageManager != null) {
			imageManager.dispose();
			imageManager = null;
		}
	}

	private void handleWidgetDispose(Event e) {
		// Check if our widget is the one being disposed.
		if (e.widget == toolItem) {
			dispose();
		}
	}

	public void dispose() {
		yellow.dispose();
		yellow = null;
	}

	private Listener getToolItemListener() {
		if (toolItemListener == null) {
			toolItemListener = new Listener() {
				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.Dispose:
						handleWidgetDispose(event);
						break;
					}
				}
			};
		}
		return toolItemListener;
	}

	private final class HoverJob extends UIJob {

		protected HoverJob() {
			super("hover");

			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			synchronized (InfoContributionItem.this) {
				hoverToolTip.show(new Point(2, 22));
				hoverJob = null;
			}
			return Status.OK_STATUS;
		}
	}

	private final class HoverToolTip extends ToolTip {

		public HoverToolTip(Control control) {
			super(control, NO_RECREATE, true);

			setHideDelay(0);
			setHideOnMouseDown(true);
		}

		@Override
		protected Composite createToolTipContentArea(Event event, Composite parent) {
			parent.setBackground(yellow);
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setBackground(yellow);

			FormText formText = new FormText(composite, SWT.NONE);
			formText.setText(infoMessage, true, false);
			for (String key : infoMessageImages.keySet()) {
				formText.setImage(key, infoMessageImages.get(key));
			}
			formText.setBackground(yellow);

			Rectangle bounds = InfoContributionItem.this.control.getBounds();
			GridData gd = new GridData();
			gd.widthHint = bounds.width - 20;
			formText.setLayoutData(gd);

			composite.setLayout(new GridLayout(1, false));
			return composite;
		}
	}
}