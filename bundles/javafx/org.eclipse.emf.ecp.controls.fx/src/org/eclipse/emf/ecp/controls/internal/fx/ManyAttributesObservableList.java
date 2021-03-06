package org.eclipse.emf.ecp.controls.internal.fx;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ManyAttributesObservableList<T> implements ObservableList<T> {

	private static final String CHANGES_THROUGH_MODEL = "An AdapterFactoryObservableList cannot be manipulated directly. Changes must be made via the model.";

	private final ObservableList<T> elements = FXCollections.observableArrayList();

	@SuppressWarnings("unchecked")
	public ManyAttributesObservableList(final EObject eObject,
		final EStructuralFeature feature) {
		super();
		// elements.addAll((Collection<? extends T>) eObject.eGet(feature));

		final AdapterImpl adapter = new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getFeature() == feature) {
					elements.setAll((Collection<? extends T>) eObject
						.eGet(feature));
				}
			}

		};

		eObject.eAdapters().add(adapter);

	}

	@Override
	public boolean add(Object e) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public T get(int index) {
		return elements.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public T set(int index, Object element) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <A> A[] toArray(A[] a) {
		return elements.toArray(a);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		elements.addListener(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		elements.removeListener(listener);
	}

	@Override
	public boolean addAll(Object... arg0) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public void addListener(ListChangeListener<? super T> listener) {
		elements.addListener(listener);
	}

	@Override
	public void remove(int arg0, int arg1) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean removeAll(Object... arg0) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public void removeListener(ListChangeListener<? super T> listener) {
		elements.removeListener(listener);
	}

	@Override
	public boolean retainAll(Object... arg0) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean setAll(Object... arg0) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}

	@Override
	public boolean setAll(Collection<? extends T> arg0) {
		throw new UnsupportedOperationException(CHANGES_THROUGH_MODEL);
	}
}
