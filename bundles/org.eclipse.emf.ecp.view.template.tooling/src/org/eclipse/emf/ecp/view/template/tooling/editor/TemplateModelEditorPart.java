/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.template.tooling.editor;

import java.io.IOException;
import java.util.EventObject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.internal.ide.util.EcoreHelper;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.template.internal.tooling.Activator;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplate;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * EditorPart for the Template Model Editor.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class TemplateModelEditorPart extends EditorPart {

	private VTViewTemplate template;
	private BasicCommandStack basicCommandStack;
	private Composite parent;
	private Resource resource;

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			resource.save(null);
			basicCommandStack.saveIsDone();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (final IOException e) {
			Activator.log(e);
		}
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	private ResourceSet createResourceSet() {
		final ResourceSet resourceSet = new ResourceSetImpl();

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(new AdapterFactory[] {
				new CustomReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) }),
			basicCommandStack, resourceSet);
		resourceSet.eAdapters().add(
			new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		return resourceSet;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
		super.setPartName(input.getName());

		basicCommandStack = new BasicCommandStack();
		basicCommandStack.addCommandStackListener
			(new CommandStackListener()
			{
				@Override
				public void commandStackChanged(final EventObject event)
				{
					parent.getDisplay().asyncExec
						(new Runnable()
						{
							@Override
							public void run()
							{
								firePropertyChange(IEditorPart.PROP_DIRTY);
							}
						});
				}
			});

		final FileEditorInput fei = (FileEditorInput) getEditorInput();

		final ResourceSet resourceSet = createResourceSet();
		try {
			resource = resourceSet.createResource(URI.createURI(fei.getURI().toURL().toExternalForm()));
			resource.load(null);

			template = (VTViewTemplate) resource.getContents().get(0);

			for (final String ecorePath : template.getReferencedEcores()) {
				EcoreHelper.registerEcore(ecorePath);
			}

		} catch (final IOException e) {
			Activator.log(e);
		}

	}

	@Override
	public void dispose() {
		for (final String ecorePath : template.getReferencedEcores()) {
			EcoreHelper.unregisterEcore(ecorePath);
		}
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		return basicCommandStack.isSaveNeeded();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		try {
			ECPSWTViewRenderer.INSTANCE.render(parent, template);
		} catch (final ECPRendererException ex) {
			Activator.log(ex);
		}
	}

	@Override
	public void setFocus() {
		// do nothing
	}

}
