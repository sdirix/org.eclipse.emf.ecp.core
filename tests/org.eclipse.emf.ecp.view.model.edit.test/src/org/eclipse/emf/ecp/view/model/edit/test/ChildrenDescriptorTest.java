package org.eclipse.emf.ecp.view.model.edit.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.provider.CategoryItemProvider;
import org.eclipse.emf.ecp.view.model.provider.ViewItemProviderAdapterFactory;
import org.junit.Test;

public class ChildrenDescriptorTest {

	@Test
	public void testCategoryChildDescriptors() {
		CategoryItemProvider categoryItemProvider = new CategoryItemProvider(new ViewItemProviderAdapterFactory());
		Category category = ViewFactory.eINSTANCE.createCategory();
		Collection<?> newChildDescriptors = categoryItemProvider.getNewChildDescriptors(category, null, null);
		int size = newChildDescriptors.size();
		assertEquals(10, size);
	}

}
