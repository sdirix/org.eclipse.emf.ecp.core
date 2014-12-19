package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicEList;
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
import org.eclipse.emf.ecp.view.spi.model.DomainModelReferenceChangeListener;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridDescription;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class AbstractControl_PTest {
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$
	protected AbstractControlSWTRenderer<VControl> renderer;

	private Resource createResource() {
		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extToFactoryMap = registry
			.getExtensionToFactoryMap();
		extToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
			new ResourceFactoryImpl());
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI,
			EcorePackage.eINSTANCE);

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(
			new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		final Resource resource = resourceSet
			.createResource(URI.createURI("VIRTUAL"));
		return resource;

	}

	protected void mockControl(EObject eObject,
		final EStructuralFeature eStructuralFeature) {
		final VDomainModelReference domainModelReference = Mockito
			.mock(VDomainModelReference.class);
		final Setting setting = mock(Setting.class);
		final Resource resource = createResource();
		resource.getContents().add(eObject);

		when(setting.getEObject()).thenReturn(eObject);
		when(setting.getEStructuralFeature()).thenReturn(eStructuralFeature);
		when(domainModelReference.getIterator()).then(new Answer<Iterator<Setting>>() {
			@Override
			public Iterator<Setting> answer(InvocationOnMock invocation) throws Throwable {
				return Collections.singleton(setting).iterator();
			}
		});
		when(domainModelReference.getEStructuralFeatureIterator()).then(new Answer<Iterator<EStructuralFeature>>() {
			@Override
			public Iterator<EStructuralFeature> answer(
				InvocationOnMock invocation) throws Throwable {
				return Collections.singleton(eStructuralFeature).iterator();
			}
		});
		final BasicEList<DomainModelReferenceChangeListener> changeListener = new BasicEList<DomainModelReferenceChangeListener>();
		when(domainModelReference.getChangeListener()).thenReturn(changeListener);
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
		mockControl();
		renderer.init(control, context);
		final SWTGridDescription gridDescription = renderer.getGridDescription(GridDescriptionFactory.INSTANCE
			.createEmptyGridDescription());
		assertEquals(2, gridDescription.getColumns());
		assertEquals(1, gridDescription.getRows());
	}

	@Test
	public void testGridDescriptionLabelAlignmentLeft() {
		setMockLabelAlignment(LabelAlignment.LEFT);
		mockControl();
		renderer.init(control, context);
		final SWTGridDescription gridDescription = renderer.getGridDescription(GridDescriptionFactory.INSTANCE
			.createEmptyGridDescription());
		assertEquals(3, gridDescription.getColumns());
		assertEquals(1, gridDescription.getRows());
	}

	@Test
	public void renderValidationIconLabelAlignmentNone()
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		renderValidationIcon(new SWTGridCell(0, 0, renderer));
	}

	@Test
	public void renderValidationIconLabelAlignmentLeft()
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		renderValidationIcon(new SWTGridCell(0, 1, renderer));
	}

	private void renderValidationIcon(SWTGridCell gridCell)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		mockControl();
		renderer.init(control, context);
		final Control render = renderer.render(gridCell, shell);
		assertTrue(Label.class.isInstance(render));
		assertEquals("", Label.class.cast(render).getText());
	}

	protected void renderLabel(String text) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		mockControl();
		renderer.init(control, context);
		final Control render = renderer.render(new SWTGridCell(0, 0, renderer), shell);
		assertTrue(Label.class.isInstance(render));
		assertEquals(text, Label.class.cast(render).getText());
	}

	protected Control renderControl(SWTGridCell gridCell)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		mockControl();
		renderer.init(control, context);
		final Control render = renderer.render(gridCell, shell);
		return render;
	}

	protected abstract void mockControl();
}
