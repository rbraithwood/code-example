package com.braithwood.gl.ui.custom;

import org.eclipse.swt.graphics.Image;

public interface ICustomAction {

	public Image getImage();

	public String getText();

	public void run(int x, int y);

	public boolean isEnabled();
}