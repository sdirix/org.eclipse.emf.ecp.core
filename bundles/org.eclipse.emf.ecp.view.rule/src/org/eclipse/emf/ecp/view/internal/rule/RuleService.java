/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.rule;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;

/**
 * Rule service that, once instantiated, maintains and synchronizes
 * the state of a rule with its {@link Renderable}.
 * 
 * @author emueller
 */
public class RuleService extends AbstractViewService {

	private ViewModelContext context;
	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private final RuleRegistry<EnableRule> enableRuleRegistry;
	private final RuleRegistry<ShowRule> showRuleRegistry;
	private boolean isUnset;

	/**
	 * Instantiates the rule service.
	 */
	public RuleService() {
		enableRuleRegistry = new RuleRegistry<EnableRule>();
		showRuleRegistry = new RuleRegistry<ShowRule>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;
		final Renderable view = context.getViewModel();
		domainChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				if (isAttributeNotification(notification)) {
					if (isUnset) {
						return;
					}
					final EAttribute attribute = (EAttribute) notification.getStructuralFeature();
					evalShow(attribute);
					evalEnable(attribute);
				} else if (isMultiRefNotification(notification)) {
					final EReference reference = (EReference) notification.getStructuralFeature();
					final EClass eReferenceType = reference.getEReferenceType();
					for (final EAttribute attribute : eReferenceType.getEAllAttributes()) {
						evalShow(attribute);
						evalEnable(attribute);
					}
				}
			}

			public void notifyAdd(Notifier notifier) {
			}

			public void notifyRemove(Notifier notifier) {
			}
		};
		context.registerDomainChangeListener(domainChangeListener);
		viewChangeListener = new ModelChangeListener() {
			public void notifyChange(ModelChangeNotification notification) {
				// do nothing for now, dynamic registration of rules isn't possible yet
			}

			public void notifyAdd(Notifier notifier) {
				// TODO Auto-generated method stub
			}

			public void notifyRemove(Notifier notifier) {

				if (Renderable.class.isInstance(notifier)) {
					final Renderable renderable = Renderable.class.cast(notifier);
					showRuleRegistry.removeRenderable(renderable);
					enableRuleRegistry.removeRenderable(renderable);
				} else if (Condition.class.isInstance(notifier)) {
					final Condition condition = Condition.class.cast(notifier);
					resetToVisible(showRuleRegistry.removeCondition(condition));
					resetToEnabled(enableRuleRegistry.removeCondition(condition));
				} else if (ShowRule.class.isInstance(notifier)) {
					final ShowRule showRule = ShowRule.class.cast(notifier);
					resetToVisible(showRuleRegistry.removeRule(showRule));
				} else if (EnableRule.class.isInstance(notifier)) {
					final EnableRule enableRule = EnableRule.class.cast(notifier);
					resetToEnabled(enableRuleRegistry.removeRule(enableRule));
				}
			}
		};
		context.registerViewChangeListener(viewChangeListener);

		if (view == null) {
			throw new IllegalStateException("View model must not be null");
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null");
		}

		init(enableRuleRegistry, EnableRule.class, view, domainModel);
		init(showRuleRegistry, ShowRule.class, view, domainModel);

		evalEnable();
		evalShow();
	}

	private static void resetToVisible(Renderable renderable) {
		if (renderable != null) {
			renderable.setVisible(true);
		}
	}

	private static void resetToEnabled(Renderable renderable) {
		if (renderable != null) {
			renderable.setEnabled(true);
		}
	}

	private static Rule getRule(Renderable renderable) {
		for (final Attachment attachment : renderable.getAttachments()) {
			if (Rule.class.isInstance(attachment)) {
				final Rule rule = (Rule) attachment;
				return rule;
			}
		}

		return null;
	}

	private static void updateStateMap(Map<Renderable, Boolean> stateMap, Renderable renderable,
		boolean isOpposite, boolean evalResult) {

		if (!stateMap.containsKey(renderable)) {
			stateMap.put(renderable, isOpposite ? !evalResult : evalResult);
		} else {
			final Boolean currentState = stateMap.get(renderable).booleanValue();
			if (currentState) {
				stateMap.put(renderable, isOpposite ? !evalResult : evalResult);
			}
		}

		for (final EObject childContent : renderable.eContents()) {
			if (childContent instanceof Renderable) {
				updateStateMap(stateMap, (Renderable) childContent, isOpposite, evalResult);
			}
		}
	}

	private static <T extends Rule> boolean hasRule(Class<T> ruleType, EObject eObject) {

		if (!Renderable.class.isInstance(eObject)) {
			return false;
		}

		final Renderable renderable = (Renderable) eObject;
		final Rule rule = getRule(renderable);

		if (ruleType.isInstance(rule)) {
			return true;
		}

		return false;
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EStructuralFeature attribute, boolean dry, Object newValue) {

		final Map<Renderable, Boolean> map = new LinkedHashMap<Renderable, Boolean>();

		for (final Map.Entry<T, Renderable> ruleAndRenderable : registry.getAffectedRenderables(
			attribute).entrySet()) {

			final Rule rule = ruleAndRenderable.getKey();
			final Renderable renderable = ruleAndRenderable.getValue();
			// whether the value changed at all, if newValue has been provided
			boolean hasChanged = false;

			if (!ruleType.isInstance(rule)) {
				continue;
			}

			if (dry) {

				final TreeIterator<EObject> eAllContents = rule.eAllContents();
				while (eAllContents.hasNext()) {
					final EObject next = eAllContents.next();
					if (!LeafCondition.class.isInstance(next)) {
						continue;
					}

					final LeafCondition leafCondition = (LeafCondition) next;

					final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();
					// FIXME: duplicate code
					while (settingIterator.hasNext()) {
						final Setting setting = settingIterator.next();
						final EObject parent = setting.getEObject();
						final EStructuralFeature feature = setting.getEStructuralFeature();
						final EClass attributeClass = feature.getEContainingClass();
						if (!attributeClass.isInstance(parent)) {
							continue;
						}
						final Object actualValue = parent.eGet(feature);
						if (!feature.isMany()) {
							if (newValue == null) {
								hasChanged |= actualValue == null;
							} else {
								hasChanged |= !newValue.equals(actualValue);
							}
						}
						else {
							// EMF API
							@SuppressWarnings("unchecked")
							final List<Object> objects = (List<Object>) actualValue;
							hasChanged |= !objects.contains(newValue);
						}
					}
				}
			}

			if (hasChanged) {
				final boolean result = ConditionEvaluator.evaluate(newValue, rule.getCondition());
				updateStateMap(map, renderable, isDisableRule(rule) || isHideRule(rule), result);
			} else if (!dry) {
				final boolean result = ConditionEvaluator.evaluate(rule.getCondition());
				updateStateMap(map, renderable, isDisableRule(rule) || isHideRule(rule), result);
			}
		}

		return map;
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EStructuralFeature attribute, Object newValue) {
		return evalAffectedRenderables(registry, ruleType, attribute, true, newValue);
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EStructuralFeature attribute) {
		return evalAffectedRenderables(registry, ruleType, attribute, false, null);
	}

	private static boolean isDisableRule(Rule rule) {
		if (isEnableRule(rule)) {
			final EnableRule enableRule = (EnableRule) rule;
			return enableRule.isDisable();
		}

		return false;
	}

	private static boolean isHideRule(Rule rule) {
		if (isShowRule(rule)) {
			final ShowRule showRule = (ShowRule) rule;
			return showRule.isHide();
		}

		return false;
	}

	private static boolean isShowRule(Rule rule) {
		return rule instanceof ShowRule;
	}

	private <T extends Rule> void evalShow(EStructuralFeature attribute) {

		final Map<Renderable, Boolean> visibleMap = evalAffectedRenderables(showRuleRegistry, ShowRule.class,
			attribute);
		for (final Map.Entry<Renderable, Boolean> e : visibleMap.entrySet()) {
			final Boolean isVisible = e.getValue();
			final Renderable renderable = e.getKey();
			final boolean isCurrentlyVisible = renderable.isVisible();
			renderable.setVisible(isVisible);
			if (isCurrentlyVisible && !isVisible) {
				unset(renderable);
			}
		}
	}

	private <T extends Rule> void evalEnable(EStructuralFeature attribute) {

		final Map<Renderable, Boolean> enabledMap = evalAffectedRenderables(enableRuleRegistry, EnableRule.class,
			attribute);

		for (final Map.Entry<Renderable, Boolean> e : enabledMap.entrySet()) {
			e.getKey().setEnabled(e.getValue());
		}
	}

	private <T extends Rule> void evalShow() {
		for (final EStructuralFeature feature : showRuleRegistry.getAttributes()) {
			evalShow(feature);
		}
	}

	private <T extends Rule> void evalEnable() {
		for (final EStructuralFeature feature : enableRuleRegistry.getAttributes()) {
			evalEnable(feature);
		}
	}

	private <T extends Rule> void init(RuleRegistry<T> registry, Class<T> ruleType, EObject viewModel,
		EObject domainObject) {
		final TreeIterator<EObject> iterator = viewModel.eAllContents();

		register(registry, ruleType, domainObject, viewModel);

		while (iterator.hasNext()) {
			final EObject content = iterator.next();
			register(registry, ruleType, domainObject, content);
		}
	}

	private <T extends Rule> void register(RuleRegistry<T> registry, Class<T> ruleType, EObject domainObject,
		final EObject viewModel) {
		if (hasRule(ruleType, viewModel)) {
			final Renderable renderable = (Renderable) viewModel;
			@SuppressWarnings("unchecked")
			final T rule = (T) getRule(renderable);
			registry.register(renderable, rule, rule.getCondition(), domainObject);
		}
	}

	/**
	 * Returns all {@link Renderable}s, that would we disabled if {@code newValue} would be set for the given
	 * {@code setting}.
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value of the setting
	 * @return the disabled {@link Renderable}s and their new state if newValue would be set
	 */
	public Map<Renderable, Boolean> getDisabledRenderables(Setting setting, Object newValue) {

		final EStructuralFeature feature = setting.getEStructuralFeature();

		if (feature instanceof EAttribute) {

			final EAttribute attribute = (EAttribute) feature;

			return evalAffectedRenderables(enableRuleRegistry,
				EnableRule.class, attribute, newValue);
		}

		return Collections.emptyMap();
	}

	/**
	 * Returns all {@link Renderable}s, that would we hidden if {@code newValue} would be set for the given
	 * {@code setting}.
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value of the setting
	 * @return the hidden {@link Renderable}s and their new state if newValue would be set
	 */
	public Map<Renderable, Boolean> getHiddenRenderables(Setting setting, Object newValue) {
		final EStructuralFeature feature = setting.getEStructuralFeature();

		if (feature instanceof EAttribute) {

			final EAttribute attribute = (EAttribute) feature;

			return evalAffectedRenderables(showRuleRegistry,
				ShowRule.class, attribute, newValue);
		}

		return Collections.emptyMap();
	}

	private void unset(Renderable renderable) {

		if (renderable instanceof AbstractControl) {
			final AbstractControl control = (AbstractControl) renderable;
			for (final VDomainModelReference domainModelReference : control.getDomainModelReferences()) {
				final Iterator<Setting> settings = domainModelReference.getIterator();
				while (settings.hasNext()) {
					final Setting setting = settings.next();
					final EObject parent = setting.getEObject();
					final EStructuralFeature targetFeature = setting.getEStructuralFeature();
					if (targetFeature == null) {
						continue;
					}
					final Class<?> containerClass = targetFeature.getContainerClass();

					isUnset = true;
					if (containerClass.isInstance(parent)) {
						parent.eUnset(targetFeature);
					}
					isUnset = false;
				}
			}
		}

	}

	/**
	 * Dispose.
	 */
	@Override
	public void dispose() {
		// dispose stuff
		context.unregisterDomainChangeListener(domainChangeListener);
		context.unregisterViewChangeListener(viewChangeListener);
	}

	private static boolean isEnableRule(Rule rule) {
		return EnableRule.class.isInstance(rule);
	}

	private static boolean isAttributeNotification(ModelChangeNotification notification) {
		if (notification.getStructuralFeature() instanceof EAttribute) {
			return true;
		}

		return false;
	}

	private boolean isMultiRefNotification(ModelChangeNotification notification) {
		if (EReference.class.isInstance(notification.getStructuralFeature())) {
			final EReference reference = (EReference) notification.getStructuralFeature();
			return reference.getUpperBound() < 0 || reference.getUpperBound() > 1;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#getPriority()
	 */
	@Override
	public int getPriority() {
		return 1;
	}
}
