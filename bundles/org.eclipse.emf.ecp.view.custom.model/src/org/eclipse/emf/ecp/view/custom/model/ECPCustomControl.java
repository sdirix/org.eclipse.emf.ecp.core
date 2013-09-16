/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * Edagr Mueller - initial API and implementation
 * Johannes Faltermeier - ECPCustomControl API changes
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * {@link ECPControl} is the interface describing a custom control.
 * 
 * @author eneufeld
 * @author emueller
 * @author jfaltermeier
 * 
 */
public interface ECPCustomControl extends ECPControl {

	/**
	 * {@link ECPCustomControlChangeListener} will get informed the value of a {@link ECPCustomControlFeature} changes.
	 * 
	 * @author eneufeld
	 * 
	 */
	public interface ECPCustomControlChangeListener {

		/**
		 * When a change on the registered {@link ECPCustomControlFeature} is noticed listeners will be notified.
		 */
		void notifyChanged();
	}

	/**
	 * <br>
	 * This class represents a setting between an {@link EObject} and a {@link EStructuralFeature}. The EObject is
	 * specified and resolved by following a path on the root object. </br>
	 * An {@link ECPCustomControlFeature} offers the possibility to change the referenced value (if editable) and to
	 * read it. Furthermore change listener can be registered and databinding can be performed.
	 * 
	 * @author eneufeld
	 * @author jfaltermeier
	 * 
	 */
	public final class ECPCustomControlFeature {

		private boolean initialised;
		private final List<EReference> eReferencePath = new ArrayList<EReference>();
		private final EStructuralFeature targetFeature;
		private EObject relevantEObject;
		private final List<Adapter> adapter;
		private boolean adapterAttached;
		private final boolean isEditable;
		private DataBindingContext databindingContext;
		private EditingDomain editingDomain;

		/**
		 * Default constructor.
		 * 
		 * @param eReferencePath the path of the root object to the relevant object
		 * @param targetFeature the {@link EStructuralFeature} with which the value can be retrieved on the relevant
		 *            object
		 * @param isEditable <code>true</code> if the value of this feature can be edited, <code>false</code> otherwise
		 */
		public ECPCustomControlFeature(List<EReference> eReferencePath, EStructuralFeature targetFeature,
			boolean isEditable) {
			if (eReferencePath != null) {
				this.eReferencePath.addAll(eReferencePath);
			}
			if (targetFeature == null) {
				throw new IllegalArgumentException("Feature must not be null.");
			}
			this.targetFeature = targetFeature;
			this.isEditable = isEditable;
			initialised = false;
			adapter = new ArrayList<Adapter>();
		}

		/**
		 * <br>
		 * Initializes the {@link ECPCustomControlFeature}. Without calling this method some operations will fail. </br>
		 * All parameters can be resolved from an {@link org.eclipse.emf.ecp.edit.ECPControlContext ECPControlContext}.
		 * 
		 * @param rootObject the root object on which the reference path can be followed
		 * @param databindingContext the context for databinding
		 * @param editingDomain the editing domain
		 */
		// API of eGet is not type-safe
		@SuppressWarnings("unchecked")
		public void init(EObject rootObject, DataBindingContext databindingContext, EditingDomain editingDomain) {
			if (rootObject == null) {
				throw new IllegalArgumentException("Root Object must not be null.");
			}

			this.editingDomain = editingDomain;

			// find path to relevant EObject
			if (relevantEObject == null) {
				relevantEObject = rootObject;
				for (final EReference eReference : geteReferencePath()) {
					final Object refObject = relevantEObject.eGet(eReference);
					if (EObject.class.isInstance(refObject)) {
						relevantEObject = (EObject) refObject;
					}
					else if (EList.class.isInstance(refObject)) {
						relevantEObject = ((EList<EObject>) refObject).get(0);
					}
				}
			}
			if (!adapterAttached && adapter != null) {
				for (final Adapter a : adapter) {
					if (a != null) {
						relevantEObject.eAdapters().add(a);
						adapterAttached = true;
					}
				}
			}
			this.databindingContext = databindingContext;
			initialised = true;
		}

		/**
		 * Returns the reference path.
		 * 
		 * @return the reference path
		 */
		public List<EReference> geteReferencePath() {
			return eReferencePath;
		}

		/**
		 * Returns the target feature.
		 * 
		 * @return the target feature
		 */
		public EStructuralFeature getTargetFeature() {
			return targetFeature;
		}

		/**
		 * Whether the associated attribute/reference can be modified.
		 * 
		 * @return <code>true</code> if editable, <code>false</code> otherwise
		 */
		public boolean isEditable() {
			return isEditable;
		}

		/**
		 * Returns the {@link EObject} that was resolved by following the reference path on the root object.
		 * 
		 * @return the referenced object
		 */
		public EObject getRelevantEObject() {
			if (!initialised) {
				throw new IllegalStateException("The ECPCustomControl was not initialised.");
			}
			return relevantEObject;
		}

		/**
		 * Registers a change listener on the referenced object. {@link ECPCustomControlChangeListener#notifyChanged()}
		 * will be called when a change on the referenced object is noticed.
		 * 
		 * @param changeListener the change listener to register
		 */
		public void registerChangeListener(final ECPCustomControlChangeListener changeListener) {
			final Adapter newAdapter = new AdapterImpl() {

				@Override
				public void notifyChanged(Notification msg) {
					if (msg.isTouch()) {
						return;
					}
					if (msg.getFeature().equals(getTargetFeature())) {
						super.notifyChanged(msg);
						changeListener.notifyChanged();
					}
				}

			};
			if (relevantEObject != null) {
				relevantEObject.eAdapters().add(newAdapter);
				adapterAttached = true;
				adapter.add(newAdapter);
			}
		}

		/**
		 * Sets the value of the feature to the new value.
		 * 
		 * @param newValue the value to be set
		 */
		public void setValue(Object newValue) {
			if (!isEditable) {
				throw new UnsupportedOperationException(
					"Set value is not supported on ECPCustomControlFeatures that are not editable.");
			}

			relevantEObject.eSet(targetFeature, newValue);
		}

		/**
		 * Returns the current set value of the feature.
		 * 
		 * @return the value
		 */
		public Object getValue() {
			return relevantEObject.eGet(targetFeature);
		}

		/**
		 * Method for enabling databinding on the reference/attribute of the referenced object.
		 * 
		 * @param targetValue the target observerable
		 * @param targetToModel update strategy target to model
		 * @param modelToTarget update strategy model to target
		 * @return the resulting binding
		 */
		public Binding bindTargetToModel(IObservableValue targetValue, UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget) {
			if (!initialised) {
				throw new IllegalStateException("The ECPCustomControl was not initialised.");
			}

			if (!isEditable()) {
				throw new IllegalArgumentException("Feature is not registered as editable");
			}

			return databindingContext.bindValue(targetValue, getModelValue(), targetToModel, modelToTarget);
		}

		private IObservableValue getModelValue() {
			return EMFEditObservables.observeValue(editingDomain, getRelevantEObject(), getTargetFeature());
		}

		/**
		 * Disposes all listener.
		 */
		public void dispose() {
			if (relevantEObject != null) {
				for (final Adapter a : adapter) {
					relevantEObject.eAdapters().remove(a);
				}
			}
			relevantEObject = null;
			databindingContext = null;
			editingDomain = null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (targetFeature == null ? 0 : targetFeature.hashCode());
			result = prime * result + (eReferencePath == null ? 0 : eReferencePath.size());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ECPCustomControlFeature other = (ECPCustomControlFeature) obj;

			if (targetFeature == null && other.targetFeature != null) {
				return false;
			}
			if (targetFeature != null && !targetFeature.equals(other.targetFeature)) {
				return false;
			}

			if (!isEReferencePathEqual(other)) {
				return false;
			}

			if (isEditable != other.isEditable) {
				return false;
			}

			return true;
		}

		/**
		 * @param other
		 */
		private boolean isEReferencePathEqual(final ECPCustomControlFeature other) {
			if (eReferencePath == null && other.eReferencePath != null) {
				return false;
			}

			if (eReferencePath != null && other.eReferencePath == null) {
				return false;
			}

			if (eReferencePath != null && other.eReferencePath != null
				&& eReferencePath.size() != other.eReferencePath.size()) {
				return false;
			}
			for (final EReference eReference : eReferencePath) {
				if (!other.eReferencePath.contains(eReference)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Returns all registered {@link ECPCustomControlFeature}s of this {@link ECPCustomControl}.
	 * 
	 * @return all features
	 */
	Set<ECPCustomControlFeature> getECPCustomControlFeatures();

}
