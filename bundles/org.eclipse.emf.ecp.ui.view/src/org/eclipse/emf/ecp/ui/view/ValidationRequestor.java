package org.eclipse.emf.ecp.ui.view;


public interface ValidationRequestor {

    void setValidationRequestProvider(ValidationResultProvider resultProvider);
    
    void requestValidation();
}
