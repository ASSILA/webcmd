/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.jsf.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

/**
 *
 * @author mochieng
 */
@FacesComponent(createTag = true, namespace = "http://webcmd.anosym.com/jsf", tagName = "webcmd")
public class JWebCommandLineUIInput extends UIInput {

    public static final String WEBCMD_COMPONENT_FAMILY = "text-cmd";

    @Override
    public String getFamily() {
        return WEBCMD_COMPONENT_FAMILY;
    }

}
