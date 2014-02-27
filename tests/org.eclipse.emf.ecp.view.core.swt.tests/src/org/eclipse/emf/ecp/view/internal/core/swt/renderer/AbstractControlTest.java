package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.AbstractControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.mockito.Mockito;

public abstract class AbstractControlTest {
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$
	private AbstractControlSWTRenderer<VControl> renderer;

	private Resource createResource() {
		Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> extToFactoryMap = registry
				.getExtensionToFactoryMap();
		extToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				new ResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI,
				EcorePackage.eINSTANCE);

		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
				new ComposedAdapterFactory(
						ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
				new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(
				new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		Resource resource = resourceSet
				.createResource(URI.createURI("VIRTUAL"));
		return resource;

	}

	protected void mockControl(EObject eObject,
			EStructuralFeature eStructuralFeature) {
		VDomainModelReference domainModelReference = Mockito
				.mock(VDomainModelReference.class);
		Setting setting = mock(Setting.class);
		Resource resource = createResource();
		resource.getContents().add(eObject);

		when(setting.getEObject()).thenReturn(eObject);
		when(setting.getEStructuralFeature()).thenReturn(eStructuralFeature);
		when(domainModelReference.getIterator()).thenReturn(
				Collections.singleton(setting).iterator());
		Mockito.when(control.getDomainModelReference()).thenReturn(
				domainModelReference);
	}

	protected void setMockLabelAlignment(LabelAlignment labelAlignment) {
		Mockito.when(control.getLabelAlignment()).thenReturn(labelAlignment);
	}

	private ViewModelContext context;
	private VControl control;
	private Shell shell;

	protected void setup(AbstractControlSWTRenderer<VControl> renderer) {

		this.renderer = renderer;
		control = Mockito.mock(VControl.class);
		context = Mockito.mock(ViewModelContext.class);
		shell = new Shell();
	}

	
	protected void dispose() {
		shell.dispose();
	}

	@Test
	public void testGridDescriptionLabelAlignmentNone() {
		setMockLabelAlignment(LabelAlignment.NONE);
		renderer.init(control, context);
		GridDescription gridDescription = renderer.getGridDescription();
		assertEquals(2, gridDescription.getColumns());
		assertEquals(1, gridDescription.getRows());
	}

	@Test
	public void testGridDescriptionLabelAlignmentLeft() {
		setMockLabelAlignment(LabelAlignment.LEFT);
		renderer.init(control, context);
		GridDescription gridDescription = renderer.getGridDescription();
		assertEquals(3, gridDescription.getColumns());
		assertEquals(1, gridDescription.getRows());
	}

	@Test
	public void renderValidationIconLabelAlignmentNone()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		renderValidationIcon(new GridCell(0, 0, new GridCellDescription()));
	}

	@Test
	public void renderValidationIconLabelAlignmentLeft()
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		renderValidationIcon(new GridCell(0, 1, new GridCellDescription()));
	}

	private void renderValidationIcon(GridCell gridCell)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderer.init(control, context);
		Control render = renderer.render(gridCell, shell);
		assertTrue(Label.class.isInstance(render));
		assertEquals("", Label.class.cast(render).getText());
	}

	
	protected void renderLabel(String text) throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		mockControl();
		renderer.init(control, context);
		Control render = renderer.render(new GridCell(0, 0,
				new GridCellDescription()), shell);
		assertTrue(Label.class.isInstance(render));
		assertEquals(text, Label.class.cast(render).getText());
	}

	protected Control renderControl(GridCell gridCell)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		mockControl();
		renderer.init(control, context);
		Control render = renderer.render(gridCell, shell);
		return render;
	}

	protected abstract void mockControl();
}
