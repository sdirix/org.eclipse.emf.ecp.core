/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.core.util.observer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a universal observer bus. This class follows the publish/subscribe pattern, it is a central dispatcher for
 * observers and makes use of generics in order to allow type safety. It can be used as singleton or be injected through
 * DI.
 * Observers have to implement the {@link IECPObserver} interface, which is only used as a marker. Future use of
 * Annotations is possible.
 * by using {@link #notify(Class)} (e.g. <code>bus.notify(MyObserver.class).myObserverMethod()</code>) all registered
 * Observers are notified.
 * This is implemented by using the java {@link Proxy} class. By calling {@link #notify(Class)} a proxy is returned,
 * which then calls all registered observers.
 * The proxy can also be casted into {@link ECPObserverCall}, which allows to access all results by the different
 * observers.
 * 
 * 
 * Example code:
 * 
 * <pre>
 * // A is IObserver
 * A a = new A() {
 * 
 * 	public void foo() {
 * 		System.out.println(&quot;A says: go!&quot;);
 * 	}
 * };
 * 
 * // B extends A and is IObserver
 * B b = new B() {
 * 
 * 	public void say(String ja) {
 * 		System.out.println(&quot;B says: &quot; + ja);
 * 	}
 * 
 * 	public void foo() {
 * 		System.out.println(&quot;B says: h??&quot;);
 * 	}
 * };
 * 
 * // B is registered first
 * ObserverBus.register(b);
 * ObserverBus.register(a);
 * 
 * ObserverBus.notify(A.class).foo();
 * 
 * ObserverBus.notify(B.class).say(&quot;w00t&quot;);
 * 
 * // Output:
 * 
 * // B says: h??
 * // A says: go!
 * //
 * // B says: w00t
 * 
 * </pre>
 * 
 * @author wesendon
 */
public class ECPObserverBus {

	/**
	 * Initializes the singleton instance statically.
	 */
	private static class SingletonHolder {
		public static final ECPObserverBus INSTANCE = new ECPObserverBus();
	}

	/**
	 * Static ObserverBus singleton. Use of singleton is optional, for that reason the constructor is public.
	 * 
	 * @return Static instance of the observerbus
	 */
	public static ECPObserverBus getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private HashMap<Class<? extends IECPObserver>, List<IECPObserver>> observerMap;

	/**
	 * Default constructor.
	 */
	public ECPObserverBus() {
		observerMap = new HashMap<Class<? extends IECPObserver>, List<IECPObserver>>();
	}

	/**
	 * This method allows you to notify all observers.
	 * 
	 * @param <T> class of observer
	 * @param clazz class of observer
	 * @return call object
	 */
	public <T extends IECPObserver> T notify(Class<T> clazz) {
		return notify(clazz, false);
	}

	/**
	 * This method allows you to notify all observers.
	 * 
	 * @param <T> class of observer
	 * @param clazz class of observer
	 * @param prioritized sort observer after {@link ECPPrioritizedIObserver}
	 * 
	 * @return call object
	 */
	public <T extends IECPObserver> T notify(Class<T> clazz, boolean prioritized) {
		if (clazz == null) {
			return null;
		}
		return createProxy(clazz, false);
	}

	/**
	 * Registers an observer for all observer interfaces implemented by the object or its super classes.
	 * 
	 * @param observer observer object
	 */
	public void register(IECPObserver observer) {
		register(observer, getObserverInterfaces(observer));
	}

	/**
	 * Registers an observer for the specified observer interfaces.
	 * 
	 * @param observer observer object
	 * @param classes set of classes
	 */
	public void register(IECPObserver observer, Class<? extends IECPObserver>... classes) {
		for (Class<? extends IECPObserver> iface : classes) {
			if (iface.isInstance(observer)) {
				addObserver(observer, iface);
			}
		}
	}

	/**
	 * Unregisters an observer for all observer interfaces implemented by the object or its super classes.
	 * 
	 * @param observer observer object
	 */
	public void unregister(IECPObserver observer) {
		unregister(observer, getObserverInterfaces(observer));
	}

	/**
	 * Unregisters an observer for the specified observer interfaces.
	 * 
	 * @param observer observer object
	 * @param classes set of classes
	 */
	public void unregister(IECPObserver observer, Class<? extends IECPObserver>... classes) {
		for (Class<? extends IECPObserver> iface : classes) {
			if (iface.isInstance(observer)) {
				removeObserver(observer, iface);
			}
		}
	}

	private void addObserver(IECPObserver observer, Class<? extends IECPObserver> iface) {
		List<IECPObserver> observers = initObserverList(iface);
		observers.add(observer);
	}

	private void removeObserver(IECPObserver observer, Class<? extends IECPObserver> iface) {
		List<IECPObserver> observers = initObserverList(iface);
		observers.remove(observer);
	}

	private List<IECPObserver> initObserverList(Class<? extends IECPObserver> iface) {
		List<IECPObserver> list = observerMap.get(iface);
		if (list == null) {
			list = new ArrayList<IECPObserver>();
			observerMap.put(iface, list);
		}
		return list;
	}

	private List<IECPObserver> getObserverByClass(Class<? extends IECPObserver> clazz) {
		List<IECPObserver> list = observerMap.get(clazz);
		if (list == null) {
			list = Collections.emptyList();
		}
		return new ArrayList<IECPObserver>(list);
	}

	private boolean isPrioritizedObserver(Class<?> clazz, Method method) {
		// Only prioritize if requested class extends PrioritizedIObserver and method is part of this class and not part
		// of some super class
		if (!clazz.equals(method.getDeclaringClass())) {
			return false;
		}
		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			if (ECPPrioritizedIObserver.class.equals(interfaceClass)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T extends IECPObserver> T createProxy(Class<T> clazz, boolean prioritized) {
		ProxyHandler handler = new ProxyHandler((Class<IECPObserver>) clazz, prioritized);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz, ECPObserverCall.class }, handler);

	}

	/**
	 * Proxyobserver which notifies all observers.
	 * 
	 * @author wesendon
	 */
	private final class ProxyHandler implements InvocationHandler, ECPObserverCall {

		private Class<IECPObserver> clazz;
		private List<ECPObserverCall.Result> lastResults;
		private boolean prioritized;

		public ProxyHandler(Class<IECPObserver> clazz, boolean prioritized) {
			this.clazz = clazz;
			this.prioritized = prioritized;
			lastResults = new ArrayList<ECPObserverCall.Result>();
		}

		// BEGIN SUPRESS CATCH EXCEPTION
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// END SUPRESS CATCH EXCEPTION
			// fork for calls to ObserverCall.class
			if (ECPObserverCall.class.equals(method.getDeclaringClass())) {
				return accessObserverCall(method, args);
			}

			List<IECPObserver> observers = getObserverByClass(clazz);

			if (prioritized && isPrioritizedObserver(clazz, method)) {
				sortObservers(observers);
			}

			// return default value if no observers are registered
			if (observers.size() == 0) {
				lastResults = new ArrayList<ECPObserverCall.Result>();
				return Result.getDefaultValue(method);
			}

			lastResults = notifiyObservers(observers, method, args);
			return lastResults.get(0).getResultOrDefaultValue();
		}

		private Object accessObserverCall(Method method, Object[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
			return method.invoke(this, args);

		}

		private List<ECPObserverCall.Result> notifiyObservers(List<IECPObserver> observers, Method method, Object[] args) {
			List<ECPObserverCall.Result> results = new ArrayList<ECPObserverCall.Result>(observers.size());
			for (IECPObserver observer : observers) {
				try {
					results.add(new Result(observer, method, method.invoke(observer, args)));
					// BEGIN SUPRESS CATCH EXCEPTION
				} catch (Throwable e) {
					// END SUPRESS CATCH EXCEPTION
					results.add(new Result(observer, e, method));
				}
			}
			return results;
		}

		public List<Result> getObserverCallResults() {
			return lastResults;
		}

		// END SUPRESS CATCH EXCEPTION
	}

	/**
	 * Sorts Observers. Make sure they are {@link ECPPrioritizedIObserver}!!
	 * 
	 * @param observers list of observers
	 */
	private void sortObservers(List<IECPObserver> observers) {
		Collections.sort(observers, new Comparator<IECPObserver>() {
			public int compare(IECPObserver o1, IECPObserver o2) {
				int prio1 = ((ECPPrioritizedIObserver) o1).getPriority();
				int prio2 = ((ECPPrioritizedIObserver) o2).getPriority();
				if (prio1 == prio2) {
					return 0;
				}
				return prio1 > prio2 ? 1 : -1;
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IECPObserver>[] getObserverInterfaces(IECPObserver observer) {
		HashSet<Class<? extends IECPObserver>> observerInterfacsFound = new HashSet<Class<? extends IECPObserver>>();
		getClasses(observer.getClass(), observerInterfacsFound);
		return observerInterfacsFound.toArray(new Class[observerInterfacsFound.size()]);
	}

	@SuppressWarnings("unchecked")
	private boolean getClasses(Class<?> clazz, HashSet<Class<? extends IECPObserver>> result) {
		for (Class<?> iface : getAllInterfaces(clazz, new HashSet<Class<?>>())) {
			if (iface.equals(IECPObserver.class) && clazz.isInterface()) {
				result.add((Class<? extends IECPObserver>) clazz);
				return true;
			} else {
				if (getClasses(iface, result) && clazz.isInterface()) {
					result.add((Class<? extends IECPObserver>) clazz);
				}
			}
		}
		return false;
	}

	private Set<Class<?>> getAllInterfaces(final Class<?> clazz, final Set<Class<?>> interfacesFound) {

		for (Class<?> iface : clazz.getInterfaces()) {
			interfacesFound.add(iface);
		}

		if (clazz.getSuperclass() == null) {
			return interfacesFound;
		}

		return getAllInterfaces(clazz.getSuperclass(), interfacesFound);
	}

}
