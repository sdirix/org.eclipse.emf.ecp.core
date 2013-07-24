package org.eclipse.emf.ecp.view.custom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControl;

public interface ECPCustomControl extends ECPControl{

	public interface ECPCustomControlChangeListener{
		void notifyChanged();
	}
	
	public final class ECPCustomControlFeature{
		
		private final List<EReference> eReferencePath=new ArrayList<EReference>();
		private final EStructuralFeature targetFeature;
		private EObject relevantEObject;
		private Adapter adapter;
		private boolean adapterAttached=false;

		public ECPCustomControlFeature(List<EReference> eReferencePath,EStructuralFeature targetFeature){
			if(eReferencePath!=null){
				this.eReferencePath.addAll(eReferencePath);
			}
			if(targetFeature==null)
				throw new IllegalArgumentException("Feature must not be null.");
			this.targetFeature=targetFeature;
		}

		public List<EReference> geteReferencePath() {
			return eReferencePath;
		}

		public EStructuralFeature getTargetFeature() {
			return targetFeature;
		}
		//API of eGet is not type-safe
		@SuppressWarnings("unchecked")
		public EObject getRelevantEObject(final EObject rootObject){
			if(rootObject==null){
				throw new IllegalArgumentException("Root Object must not be null.");
			}
			//find path to relevant EObject
			if(relevantEObject==null){
				relevantEObject=rootObject;
				for(EReference eReference:geteReferencePath()){
					Object refObject=relevantEObject.eGet(eReference);
					if(EObject.class.isInstance(refObject)){
						relevantEObject=(EObject) refObject;
					}
					else if(EList.class.isInstance(refObject)){
						relevantEObject= ((EList<EObject>)refObject).get(0);
					}
				}
			}
			if(!adapterAttached && adapter!=null){
				relevantEObject.eAdapters().add(adapter);
				adapterAttached=true;
			}
			return relevantEObject;
		}
		
		public void registerChangeListener(final ECPCustomControlChangeListener changeListener){
			adapter=new AdapterImpl(){

				@Override
				public void notifyChanged(Notification msg) {
					if(msg.isTouch()){
						return;
					}
					if(msg.getFeature().equals(getTargetFeature())){
						super.notifyChanged(msg);
						changeListener.notifyChanged();
					}
				}
				
			};
			if(relevantEObject!=null){
				relevantEObject.eAdapters().add(adapter);
				adapterAttached=true;
			}
		}
		
		public void dispose(){
			if(relevantEObject!=null){
				relevantEObject.eAdapters().remove(adapter);
			}
			relevantEObject=null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((targetFeature == null) ? 0 : targetFeature.hashCode());
			result = prime * result + ((eReferencePath == null) ? 0 : eReferencePath.size());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ECPCustomControlFeature other = (ECPCustomControlFeature) obj;
			
			if (targetFeature == null) {
				if (other.targetFeature != null)
					return false;
			} else if (!targetFeature.equals(other.targetFeature))
				return false;
			if (eReferencePath == null) {
				if (other.eReferencePath != null)
					return false;
			} else if (eReferencePath.size()!=other.eReferencePath.size()){
				return false;
			}
			else{
				for(EReference eReference:eReferencePath){
					if(!other.eReferencePath.contains(eReference)){
						return false;
					}
				}
			}
			
			return true;
		}

		
	}
	
	Set<ECPCustomControlFeature> getEditableFeatures();
	Set<ECPCustomControlFeature> getReferencedFeatures();
	
}
