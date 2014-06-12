/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.jsf.component;

import static com.anosym.webcmd.web.jsf.component.SSHTerminal.COMPONENT_FAMILY;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

/**
 *
 * @author mochieng
 */
@FacesComponent(
        namespace = "http://webcmd.anosym.com/jsf",
        value = COMPONENT_FAMILY)
public class SSHTerminal extends UIInput {

    protected static enum SSHPropertyKeys {

        channelId,
        cmdHandler;
        private String name_;

        private SSHPropertyKeys() {
        }

        private SSHPropertyKeys(String name_) {
            this.name_ = name_;
        }

        @Override
        public String toString() {
            return name_;
        }

    }
    public static final String RENDERER_TYPE = "com.anosym.webcmd.web.jsf.renderer.AsynchronousTerminalRenderer";
    public static final String COMPONENT_TYPE = "com.anosym.webcmd.web.jsf.component.SSHTerminal";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";

    public SSHTerminal() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setChannelId(String channelId) {
        getStateHelper().put(SSHPropertyKeys.channelId, channelId);
    }

    public String getChannelId() {
        return (String) getStateHelper().eval(SSHPropertyKeys.channelId, "_channel");
    }

}
