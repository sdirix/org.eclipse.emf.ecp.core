package org.elipse.emf.ecp.view.model.test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.model.SettingPath;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Fan;
import org.junit.Before;
import org.junit.Test;

public class TestDomainModelReference {

	@Before
	public void setUp() throws Exception {
		
		
	}

	@Test
	public void testFeaturePathDomainModelReferencePathSimpleIterator() {
		Fan createFan = BowlingFactory.eINSTANCE.createFan();
		createFan.setFavouriteMerchandise(BowlingFactory.eINSTANCE.createMerchandise());
		
		
		VFeaturePathDomainModelReference domainModelReference=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		domainModelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		
		domainModelReference.init(createFan );
		
		int pathCount=0;
		Iterator<SettingPath> fullPathIterator = domainModelReference.getFullPathIterator();
		while(fullPathIterator.hasNext()){
			pathCount++;
			SettingPath next = fullPathIterator.next();
			Iterator<Setting> pathIterator = next.getPath();
			int pathSegments=0;
			while(pathIterator.hasNext()){
				pathSegments++;
				Setting setting = pathIterator.next();
				if(pathSegments==1){
					assertEquals(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise(), setting.getEStructuralFeature());
					assertEquals(createFan, setting.getEObject());
				}
				if(pathSegments==2){
					assertEquals(BowlingPackage.eINSTANCE.getMerchandise_Name(), setting.getEStructuralFeature());
				}
			}
			assertEquals(2, pathSegments);
		}
		assertEquals(1, pathCount);
	}
	
	@Test
	public void testFeaturePathDomainModelReferenceSimpleIterator() {
		Fan createFan = BowlingFactory.eINSTANCE.createFan();
		createFan.setFavouriteMerchandise(BowlingFactory.eINSTANCE.createMerchandise());
		
		
		VFeaturePathDomainModelReference domainModelReference=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(BowlingPackage.eINSTANCE.getMerchandise_Name());
		domainModelReference.getDomainModelEReferencePath().add(BowlingPackage.eINSTANCE.getFan_FavouriteMerchandise());
		
		domainModelReference.init(createFan );
		
		
			Iterator<Setting> iterator = domainModelReference.getIterator();
			int pathSegments=0;
			while(iterator.hasNext()){
				pathSegments++;
				Setting setting = iterator.next();
				if(pathSegments==1){
					assertEquals(BowlingPackage.eINSTANCE.getMerchandise_Name(), setting.getEStructuralFeature());
				}
			}
			assertEquals(1, pathSegments);
	}
	
	

}
