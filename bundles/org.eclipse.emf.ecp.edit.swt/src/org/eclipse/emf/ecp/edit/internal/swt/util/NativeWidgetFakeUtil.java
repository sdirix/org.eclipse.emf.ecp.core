/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2006, 2014 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 * Florian Potschka <signalrauschen@gmail.com> - Bug 260061
 * Alexander Ljungberg <siker@norwinter.com> - Bug 260061
 * Jeanderson Candido <http://jeandersonbc.github.io> - Bug 414565
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Util class for faking native widgets.
 *
 * @author jfaltermeier
 *
 */
public final class NativeWidgetFakeUtil {

	private static final String WS_CARBON = "carbon";//$NON-NLS-1$
	private static final String WS_COCOA = "cocoa";//$NON-NLS-1$

	private static ImageData checked;
	private static ImageData unchecked;

	private NativeWidgetFakeUtil() {
		// util
	}

	/**
	 * <p>
	 * Returns an image of a native checkbox.
	 * </p>
	 * <p>
	 * This is based on org.eclipse.jface.snippets.viewers.Snippet061FakedNativeCellEditor
	 * </p>
	 *
	 * @param control the control
	 * @param type <code>true</code> for checked state, <code>false</code> otherwise
	 * @return the image
	 */
	public static Image createCheckBoxImage(Control control, boolean type) {
		if (type && checked != null) {
			return new Image(control.getDisplay(), checked);
		}
		if (!type && unchecked != null) {
			return new Image(control.getDisplay(), unchecked);
		}

		// Hopefully no platform uses exactly this color because we'll make
		// it transparent in the image.
		final Color greenScreen = new Color(control.getDisplay(), 222, 223, 224);

		final Shell shell = new Shell(control.getShell(), SWT.NO_TRIM);

		// otherwise we have a default gray color
		shell.setBackground(greenScreen);

		if (isMac()) {
			final Button button2 = new Button(shell, SWT.CHECK);
			final Point bsize = button2.computeSize(SWT.DEFAULT, SWT.DEFAULT);

			// otherwise an image is stretched by width
			bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
			bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
			button2.setSize(bsize);
			button2.setLocation(100, 100);
		}

		final Button button = new Button(shell, SWT.CHECK);
		button.setBackground(greenScreen);
		button.setSelection(type);

		// otherwise an image is located in a corner
		button.setLocation(1, 1);
		final Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		// otherwise an image is stretched by width
		bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
		bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
		button.setSize(bsize);

		shell.setSize(bsize);
		shell.setVisible(false);

		shell.open();

		final GC gc = new GC(shell);
		final Image image = new Image(control.getDisplay(), bsize.x, bsize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		shell.close();

		final ImageData imageData = image.getImageData();
		imageData.transparentPixel = imageData.palette.getPixel(greenScreen
			.getRGB());

		if (type) {
			checked = imageData;
		} else {
			unchecked = imageData;
		}

		final Image img = new Image(control.getDisplay(), imageData);
		image.dispose();

		return img;
	}

	private static boolean isMac() {
		final String ws = SWT.getPlatform();
		return WS_CARBON.equals(ws) || WS_COCOA.equals(ws);
	}

}
