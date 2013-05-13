/********************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 ********************************************************************************/
package org.eclipse.emf.ecp.internal.core.util;

import org.eclipse.net4j.util.io.IOUtil;

import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.observer.ECPObserver;
import org.eclipse.emf.ecp.internal.core.Activator;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore.StorableElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class PropertiesStore<ELEMENT extends StorableElement, OBSERVER extends ECPObserver> extends
	ElementRegistry<ELEMENT, OBSERVER> {
	private File folder;

	public PropertiesStore() {
	}

	public final File getFolder() {
		return folder;
	}

	public final void setFolder(File folder) {
		checkInactive();
		this.folder = folder;
	}

	@Override
	protected void doActivate() throws Exception {
		super.doActivate();
		if (folder == null) {
			throw new IllegalStateException("Folder is null");
		}

		if (folder.exists()) {
			if (!folder.isDirectory()) {
				throw new IllegalStateException("Not a folder: " + folder);
			}
		} else {
			folder.mkdirs();
		}

		load();
	}

	protected void load() {
		// TODO Trace properly
		System.out.println("Loading " + getClass().getSimpleName() + " from " + folder.getAbsolutePath());

		Set<ELEMENT> elements = new HashSet<ELEMENT>();
		for (File file : folder.listFiles()) {
			try {
				if (file.isFile()) {
					InputStream stream = null;

					try {
						stream = new FileInputStream(file);
						ObjectInputStream in = new ObjectInputStream(stream);
						ELEMENT element = loadElement(in);
						// ELEMENT existingElement = getElement(element.getName());
						// if (existingElement != null)
						// {
						// if (element instanceof ECPDisposable)
						// {
						// ECPDisposable disposable = (ECPDisposable)element;
						// disposable.dispose();
						// }
						// }
						// else
						{
							elements.add(element);
						}
					} finally {
						if (stream != null) {
							stream.close();
						}
					}
				}
			} catch (IOException ex) {
				Activator.log(ex);
			}
		}

		doChangeElements(null, elements);
	}

	protected abstract ELEMENT loadElement(ObjectInput in) throws IOException;

	@Override
	protected void elementsChanged(Collection<ELEMENT> oldElements, Collection<ELEMENT> newElements) {
		if (isDisposingElement()) {
			return;
		}

		if (isActive()) {
			for (ELEMENT element : InternalUtil.getRemovedElements(oldElements, newElements)) {
				try {
					File file = getFile(element);
					file.delete();
				} catch (Exception ex) {
					Activator.log(ex);
				}
			}

			for (ELEMENT element : InternalUtil.getAddedElements(oldElements, newElements)) {
				if (element.isStorable()) {
					storeElement(element);
				}
			}
		}
	}

	protected void storeElement(ELEMENT element) {
		File file = getFile(element);
		File temp = new File(file.getParentFile(), file.getName() + ".tmp");
		if (temp.isFile()) {
			temp.delete();
		}

		OutputStream stream = null;

		try {
			try {
				stream = new FileOutputStream(temp);
				ObjectOutputStream out = new ObjectOutputStream(stream);
				element.write(out);
				out.flush();
			} finally {
				IOUtil.close(stream);
			}

			file.delete();
			temp.renameTo(file);
		} catch (Exception ex) {
			temp.delete();
			Activator.log(ex);
		}
	}

	protected File getFile(ELEMENT element) {
		return new File(folder, element.getName());
	}

	/**
	 * @author Eike Stepper
	 */
	public interface StorableElement extends ECPElement {
		boolean isStorable();

		void write(ObjectOutput out) throws IOException;
	}
}
