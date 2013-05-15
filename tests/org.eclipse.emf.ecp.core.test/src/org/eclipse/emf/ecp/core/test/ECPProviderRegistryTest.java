package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.junit.Test;

public class ECPProviderRegistryTest extends AbstractTest {

//	@Test
//	public void getProviderByItselfTest(){
//		ECPProvider ecpProvider=ECPProviderRegistry.INSTANCE.getProvider(getProvider());
//		assertEquals(ecpProvider,getProvider());
//	}
//	
	@Test
	public void removeAddProviderTest(){
		ECPProviderRegistry.INSTANCE.removeProvider(EMFStoreProvider.NAME);
		assertEquals(0,ECPProviderRegistry.INSTANCE.getProviders().size());
		ECPProviderRegistry.INSTANCE.addProvider(new EMFStoreProvider());
		assertEquals(1,ECPProviderRegistry.INSTANCE.getProviders().size());
	}
}
