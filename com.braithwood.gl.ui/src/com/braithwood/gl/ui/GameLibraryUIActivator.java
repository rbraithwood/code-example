package com.braithwood.gl.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GameLibraryUIActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.braithwood.gl.ui"; //$NON-NLS-1$

	private static GameLibraryUIActivator plugin;

	public GameLibraryUIActivator() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);

		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;

		super.stop(context);
	}

	public static GameLibraryUIActivator getDefault() {
		return plugin;
	}

	public static synchronized Image getImage(String url) {
		Image img = getDefault().getImageRegistry().get(url);
		if (img != null)
			return img;

		try {
			img = ImageDescriptor.createFromURL(getDefault().getBundle().getEntry(url)).createImage(false);
			getDefault().getImageRegistry().put(url, img);
			return img;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public static synchronized ImageDescriptor getImageDescriptor(String url) {
		String descriptorKey = url + "-descriptor";
		ImageDescriptor descriptor = getDefault().getImageRegistry().getDescriptor(descriptorKey);
		if (descriptor != null) {
			return descriptor;
		}

		descriptor = ImageDescriptor.createFromURL(getDefault().getBundle().getEntry(url));
		getDefault().getImageRegistry().put(descriptorKey, descriptor);
		return descriptor;
	}
}