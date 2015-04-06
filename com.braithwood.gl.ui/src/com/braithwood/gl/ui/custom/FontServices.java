package com.braithwood.gl.ui.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;

public class FontServices {

	public static Font growFont(Control control, double size, boolean makeBold) {
		Font font = control.getFont();
		FontData fd[] = font.getFontData();
		return new Font(font.getDevice(), fd[0].getName(), (int) (fd[0].getHeight() * size), makeBold ? fd[0].getStyle() | SWT.BOLD : fd[0].getStyle());
	}
}