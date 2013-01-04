package org.eclipse.emf.ecp.example.internal.leagueview;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.emfstore.bowling.League;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class LeagueViewPart extends ViewPart {

	public static final String ID="org.eclipse.emf.ecp.example.leagueView";
	private League league;
	private ComposedAdapterFactory composedAdapterFactory;
	
	public LeagueViewPart() {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	}

	@Override
	public void createPartControl(Composite parent) {
		ListViewer tv=new ListViewer(parent);
		
		tv.setContentProvider(new AdapterFactoryContentProvider(composedAdapterFactory));
		tv.setLabelProvider(new AdapterFactoryLabelProvider(composedAdapterFactory));
		tv.setInput(league.getPlayers());
		GridDataFactory.fillDefaults().grab(true, true).span(SWT.FILL, SWT.FILL).applyTo(tv.getControl());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getSelectionService()
			.getSelection();
		league=(League)selection.getFirstElement();
		StringBuilder sb=new StringBuilder();
		sb.append("My View");
		sb.append("/");
		sb.append(league.getName());
		sb.append("/");
		sb.append(league.getPlayers().size());
		sb.append("-Players");
		setPartName(sb.toString());
		
	}

	@Override
	public void dispose() {
		super.dispose();
		composedAdapterFactory.dispose();
	}

}
