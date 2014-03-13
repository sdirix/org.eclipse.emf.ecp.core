package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestFactory;
import org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(DatabindingClassRunner.class)
public class DateTimeControlRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		SWTRendererFactory factory = mock(SWTRendererFactory.class);
		setup(new DateTimeControlSWTRenderer(factory));
	}

	@After
	public void testTearDown() {
		dispose();
	}

	@Test
	public void renderControlLabelAlignmentNone()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		Control render = renderControl(new GridCell(0, 1,renderer));
		assertControl(render);
	}

	@Test
	public void renderControlLabelAlignmentLeft()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		Control render = renderControl(new GridCell(0, 2,renderer));

		assertControl(render);
	}

	private void assertControl(Control render) {
		assertTrue(Composite.class.isInstance(render));
		assertEquals(2, Composite.class.cast(render).getChildren().length);
		assertTrue(DateTime.class.isInstance(Composite.class.cast(render).getChildren()[0]));
		assertTrue(DateTime.class.isInstance(Composite.class.cast(render).getChildren()[1]));
		
		
	}

	@Override
	protected void mockControl() {
		SimpleTestObject eObject=TestFactory.eINSTANCE.createSimpleTestObject();
		super.mockControl(eObject, TestPackage.eINSTANCE.getSimpleTestObject_Date());
	}

}
