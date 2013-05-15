package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.junit.Test;

public class ECPProviderRegistryTest extends AbstractTest {

//	@Test
//	public void getProviderByItselfTest(){
//		ECPProvider ecpProvider=ECPUtil.getECPProviderRegistry().getProvider(getProvider());
//		assertEquals(ecpProvider,getProvider());
//	}
//	
	@Test
	public void removeAddProviderTest(){
		ECPUtil.getECPProviderRegistry().removeProvider(EMFStoreProvider.NAME);
		assertEquals(0,ECPUtil.getECPProviderRegistry().getProviders().size());
		ECPUtil.getECPProviderRegistry().addProvider(new EMFStoreProvider());
		assertEquals(1,ECPUtil.getECPProviderRegistry().getProviders().size());
	}
}
