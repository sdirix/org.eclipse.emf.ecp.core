package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(DatabindingClassRunner.class)
public class ViewRendererTest {

	private SWTViewRenderer viewRenderer;
	private VView view;
	private ViewModelContext context;
	private Shell shell;
	private SWTRendererFactory factory;

	@Before
	public void setUp() {
		factory = mock(SWTRendererFactory.class);
		
		viewRenderer = new SWTViewRenderer(factory);
		view = Mockito.mock(VView.class);

		context = Mockito.mock(ViewModelContext.class);
		shell = new Shell();

	}

	@After
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void testGridDescription() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		viewRenderer.init(view, context);
		GridDescription gridDescription = viewRenderer.getGridDescription();
		assertEquals(1, gridDescription.getColumns());
		assertEquals(1, gridDescription.getRows());
	}

	@Test
	public void testEmptyView() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		when(view.getChildren())
				.thenReturn(new BasicEList<VContainedElement>());
		viewRenderer.init(view, context);
		Control render = viewRenderer.render(new GridCell(0, 0,
				new GridCellDescription()), shell);
		assertTrue(Composite.class.isInstance(render));
		assertEquals(0, Composite.class.cast(render).getChildren().length);
	}

	@Test
	public void testMultipleSimpleCompositeView() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		BasicEList<VContainedElement> basicEList = new BasicEList<VContainedElement>();
		VContainedElement control1 = mock(VContainedElement.class);
		VContainedElement control2 = mock(VContainedElement.class);
		basicEList.add(control1);
		basicEList.add(control2);
		when(view.getChildren()).thenReturn(basicEList);
		
		AbstractSWTRenderer<VElement> mockRenderer1=createCompositeMockRenderer(control1,1);
		AbstractSWTRenderer<VElement> mockRenderer2=createCompositeMockRenderer(control2,1);
		
		when(factory.getRenderer(control1, context)).thenReturn(mockRenderer1);
		when(factory.getRenderer(control2, context)).thenReturn(mockRenderer2);
		
		viewRenderer.init(view, context);
		Control render = viewRenderer.render(new GridCell(0, 0,
				new GridCellDescription()), shell);
		assertTrue(Composite.class.isInstance(render));
		assertEquals(2, Composite.class.cast(render).getChildren().length);
		assertTrue(GridData.class.isInstance(Composite.class.cast(render).getChildren()[0].getLayoutData()));
		assertTrue(GridData.class.isInstance(Composite.class.cast(render).getChildren()[1].getLayoutData()));
		
		assertEquals(1,GridData.class.cast(Composite.class.cast(render).getChildren()[0].getLayoutData()).horizontalSpan);
		assertEquals(1,GridData.class.cast(Composite.class.cast(render).getChildren()[1].getLayoutData()).horizontalSpan);
	}
	
	@Test
	public void testMultipleComplexGridDescriptionView() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		BasicEList<VContainedElement> basicEList = new BasicEList<VContainedElement>();
		VContainedElement control1 = mock(VContainedElement.class);
		VContainedElement control2 = mock(VContainedElement.class);
		VContainedElement control3 = mock(VContainedElement.class);
		basicEList.add(control1);
		basicEList.add(control2);
		basicEList.add(control3);
		when(view.getChildren()).thenReturn(basicEList);
		
		AbstractSWTRenderer<VElement> mockRenderer1=createCompositeMockRenderer(control1,1);
		AbstractSWTRenderer<VElement> mockRenderer2=createCompositeMockRenderer(control2,3);
		AbstractSWTRenderer<VElement> mockRenderer3=createCompositeMockRenderer(control3,2);
		
		when(factory.getRenderer(control1, context)).thenReturn(mockRenderer1);
		when(factory.getRenderer(control2, context)).thenReturn(mockRenderer2);
		when(factory.getRenderer(control3, context)).thenReturn(mockRenderer3);
		
		viewRenderer.init(view, context);
		Control render = viewRenderer.render(new GridCell(0, 0,
				new GridCellDescription()), shell);
		assertTrue(Composite.class.isInstance(render));
		assertEquals(6, Composite.class.cast(render).getChildren().length);
		for(int i=0;i<6;i++)
		assertTrue(GridData.class.isInstance(Composite.class.cast(render).getChildren()[i].getLayoutData()));
		
		assertEquals(3,GridData.class.cast(Composite.class.cast(render).getChildren()[0].getLayoutData()).horizontalSpan);
		assertEquals(2,GridData.class.cast(Composite.class.cast(render).getChildren()[5].getLayoutData()).horizontalSpan);
	}

	private AbstractSWTRenderer<VElement> createCompositeMockRenderer(
			VContainedElement control1, int numColumns) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		AbstractSWTRenderer<VElement> mockRenderer=mock(AbstractSWTRenderer.class);
		
		GridDescription gd=GridDescriptionFactory.INSTANCE.createSimpleGrid(1, numColumns);
		when(mockRenderer.getGridDescription()).thenReturn(gd);
		when(mockRenderer.render(any(GridCell.class), any(Composite.class))).thenAnswer(new Answer<Control>() {

			public Control answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return new Composite((Composite) args[1], SWT.NONE);
			}

			
		});
		mockRenderer.init(control1, context);
		return mockRenderer;
	}

}
