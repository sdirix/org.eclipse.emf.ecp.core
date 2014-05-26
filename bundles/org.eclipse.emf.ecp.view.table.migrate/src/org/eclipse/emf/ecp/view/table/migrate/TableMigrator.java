package org.eclipse.emf.ecp.view.table.migrate;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecp.view.internal.provider.Migrator;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VReadOnlyColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;

public class TableMigrator implements Migrator{

	
	@Override
	public void migrate(EObject eObject,FeatureMap anyAttribute,FeatureMap mixed){
		if (!VTableControl.class.isInstance(eObject))
			return;
		for(int i=0;i<mixed.size();i++){
			EStructuralFeature feature=mixed.getEStructuralFeature(i);
			AnyType object = (AnyType) mixed.getValue(i);
			if("columns".equals(feature.getName())){
				migrateColumns(VTableControl.class.cast(eObject),object.getAnyAttribute(),object.getMixed());
			}
		}
	}

	private static void migrateColumns(VTableControl cast,
			FeatureMap anyAttribute, FeatureMap mixed) {
		boolean readOnly=false;
		for(int i=0;i<anyAttribute.size();i++){
			if("readOnly".equals(anyAttribute.getEStructuralFeature(i).getName())){
				readOnly = Boolean.parseBoolean((String) anyAttribute.getValue(i));
			}
		}
		for(int i=0;i<mixed.size();i++){
			if("attribute".equals(mixed.getEStructuralFeature(i).getName())){
				AnyType object = (AnyType) mixed.getValue(i);
				URI uri = EcoreUtil.getURI(object);
				EObject eObject = cast.eResource().getResourceSet().getEObject(uri, true);
				VFeaturePathDomainModelReference dmr=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
				dmr.setDomainModelEFeature((EStructuralFeature) eObject);
				VTableDomainModelReference tdmr=getTableDomainModelReference(cast);
				if(tdmr==null)
					continue;
				tdmr.getColumnDomainModelReferences().add(dmr);
				if(readOnly){
					VReadOnlyColumnConfiguration readOnlyColumnConfiguration=getReadOnlyColumnConfiguration(cast);
					readOnlyColumnConfiguration.getColumnDomainReferences().add(dmr);
				}
			}
		}
		
	}
	
	private static VTableDomainModelReference getTableDomainModelReference(VTableControl tableControl){
		VDomainModelReference dmr=tableControl.getDomainModelReference();
		if(VTableDomainModelReference.class.isInstance(dmr))
			return (VTableDomainModelReference) dmr;
		else{
			for(EObject eObject:dmr.eContents()){
				if(VTableDomainModelReference.class.isInstance(eObject))
					return (VTableDomainModelReference) eObject;
			}
		}
		return null;
	}

	private static VReadOnlyColumnConfiguration getReadOnlyColumnConfiguration(
			VTableControl cast) {
		for(VTableColumnConfiguration cc:cast.getColumnConfigurations()){
			if(VReadOnlyColumnConfiguration.class.isInstance(cc)){
				return (VReadOnlyColumnConfiguration) cc;
			}
		}
		VReadOnlyColumnConfiguration config=VTableFactory.eINSTANCE.createReadOnlyColumnConfiguration();
		cast.getColumnConfigurations().add(config);
		return config;
	}
}
