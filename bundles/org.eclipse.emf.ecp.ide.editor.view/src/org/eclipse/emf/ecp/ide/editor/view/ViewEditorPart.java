package org.eclipse.emf.ecp.ide.editor.view;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class ViewEditorPart extends EditorPart {

	private Resource resource;
	private BasicCommandStack basicCommandStack;

	public ViewEditorPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			resource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
		basicCommandStack = new BasicCommandStack();
	}

	private ResourceSet createResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();

		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			basicCommandStack, resourceSet);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		return resourceSet;
	}

	@Override
	public boolean isDirty() {
		return basicCommandStack.isSaveNeeded();
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			FileEditorInput fei = (FileEditorInput) getEditorInput();

			ResourceSet resourceSet = createResourceSet();

			resource = resourceSet.getResource(URI.createURI(fei.getURI().toURL().toExternalForm()),true);
			resource.load(null);
			VView view = (VView) resource.getContents().get(0);

			ECPSWTView render = ECPSWTViewRenderer.INSTANCE.render(parent, view);
		} catch (ECPRendererException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
