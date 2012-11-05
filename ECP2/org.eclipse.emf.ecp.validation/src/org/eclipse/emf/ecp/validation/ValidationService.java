package org.eclipse.emf.ecp.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.core.ECPProject;

public class ValidationService {

	private Map<Object, Integer> severityMapping=new HashMap<Object, Integer>();
	
	public static ValidationService INSTANCE;
	
	public ValidationService() {
		INSTANCE=this;
	}
	
	public Map<Object,Integer> getViolationMapping(){
		return severityMapping;
	}
	
	public Set<Object> getViolations(){
		return severityMapping.keySet();
	}
	
	private void validate(ECPProject project, Object element){
		Diagnostic highestSeverity = null;
		if (element instanceof ECPProject) {			
			// iterate through all root element and validate them
			for (EObject object : project.getElements()) {
				Diagnostic diagnostic = Diagnostician.INSTANCE.validate(object);
				if (highestSeverity == null || highestSeverity.getSeverity() < diagnostic.getSeverity()) {
					highestSeverity = diagnostic;
				}
				severityMapping.put(object, diagnostic.getSeverity());
				addChildDiagnostics(diagnostic);
			}
		}
		else if(element instanceof EObject){
			highestSeverity=Diagnostician.INSTANCE.validate((EObject)element);
			severityMapping.put(element, highestSeverity.getSeverity());
			EObject parent=((EObject)element).eContainer();
			while(parent!=null){
				severityMapping.put(parent, highestSeverity.getSeverity());
				parent=parent.eContainer();
			}
		}
		severityMapping.put(project, highestSeverity.getSeverity());
		
		
	}

	private void addChildDiagnostics(Diagnostic diagnostic) {
		for(Diagnostic childDiag:diagnostic.getChildren()){
			
		}
	}

}
