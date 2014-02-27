package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.SWTNumberControlRenderer;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.SWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class NumberControlRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		SWTRendererFactory factory = mock(SWTRendererFactory.class);
		setup(new SWTNumberControlRenderer(factory));
	}

	@After
	public void testTearDown() {
		dispose();
	}

	@Test
	public void renderControlLabelAlignmentNone()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		Control render = renderControl(new GridCell(0, 1,
				new GridCellDescription()));
		assertControl(render);
	}

	@Test
	public void renderControlLabelAlignmentLeft()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		Control render = renderControl(new GridCell(0, 2,
				new GridCellDescription()));

		assertControl(render);
	}
	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption{
		renderLabel("Lower Bound");
	}
	

	private void assertControl(Control render) {
		assertTrue(Text.class.isInstance(render));
		assertEquals(SWT.RIGHT, Text.class.cast(render).getStyle()
				& SWT.RIGHT);
		assertEquals(SWT.RIGHT, Text.class.cast(render).getStyle()
				& SWT.RIGHT);
		
		assertEquals("org_eclipse_emf_ecp_control_numerical", Text.class.cast(render).getData(CUSTOM_VARIANT));
	}

	@Override
	protected void mockControl() {
		EStructuralFeature eObject = EcoreFactory.eINSTANCE.createEAttribute();
		EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getETypedElement_LowerBound();
		super.mockControl(eObject, eStructuralFeature);
	}

}
