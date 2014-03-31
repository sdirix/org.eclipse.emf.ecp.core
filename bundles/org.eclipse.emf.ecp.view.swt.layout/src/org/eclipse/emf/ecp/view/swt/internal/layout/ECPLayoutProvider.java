package org.eclipse.emf.ecp.view.swt.internal.layout;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.layout.AbstractLayoutProvider;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class ECPLayoutProvider extends AbstractLayoutProvider {

	public Layout getColumnLayout(int numColumns, boolean equalWidth) {
		return GridLayoutFactory.fillDefaults().numColumns(numColumns)
			.equalWidth(equalWidth).create();
	}

	public Object getLayoutData(GridCell gridCell,
		GridDescription controlGridDescription,
		GridDescription currentRowGridDescription, GridDescription fullGridDescription, VElement vElement,
		Control control) {
		if (VControl.class.isInstance(vElement)) {
			// last column of control
			if (gridCell.getColumn() + 1 == controlGridDescription.getColumns()) {
				return getControlGridData(1 + fullGridDescription.getColumns()
					- currentRowGridDescription.getColumns(), VControl.class.cast(vElement), control);
			} else if (controlGridDescription.getColumns() == 3 && gridCell.getColumn() == 0) {
				return getLabelGridData();
			} else if (controlGridDescription.getColumns() == 3 && gridCell.getColumn() == 1) {
				return getValidationGridData();
			}
		}
		// we have some kind of container -> render with necessary span

		return getSpanningGridData(1 + fullGridDescription.getColumns()
			- currentRowGridDescription.getColumns(), 1);

	}

	private GridData getLabelGridData() {
		return GridDataFactory.fillDefaults().grab(false, false)
			.align(SWT.BEGINNING, SWT.CENTER).create();
	}

	private GridData getValidationGridData() {
		return GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER)
			.hint(16, 17).grab(false, false).create();
	}

	private GridData getControlGridData(int xSpan, VControl vControl, Control control) {
		GridDataFactory gdf =
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(true, false).span(xSpan, 1);

		if (Text.class.isInstance(control) && vControl.getDomainModelReference() != null) {
			final Setting setting = vControl.getDomainModelReference().getIterator().next();

			if (isMultiLine(setting)) {
				gdf = gdf.hint(50, 200); // set x hint to enable wrapping
			}
		}

		return gdf.create();
	}

	private GridData getSpanningGridData(int xSpan, int ySpan) {
		return GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
			.grab(true, true).span(xSpan, ySpan).create();
	}

	public Object getSpanningLayoutData(int spanX, int spanY) {
		return getSpanningGridData(spanX, spanY);
	}

}
