/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.jsf.component;

import javax.faces.component.FacesComponent;
import org.primefaces.component.terminal.Terminal;

/**
 *
 * @author mochieng
 */
@FacesComponent(
        namespace = "http://webcmd.anosym.com/jsf",
        value = "com.anosym.webcmd.web.jsf.component.AsynchronousTerminal")
public class AsynchronousTerminal extends Terminal {

    protected static enum AsynchronousPropertyKeys {

        channelId,
        cmdHandler;
        private String name_;

        private AsynchronousPropertyKeys() {
        }

        private AsynchronousPropertyKeys(String name_) {
            this.name_ = name_;
        }

        @Override
        public String toString() {
            return name_;
        }

    }
    public static final String RENDERER_TYPE = "com.anosym.webcmd.web.jsf.renderer.AsynchronousTerminalRenderer";

    public AsynchronousTerminal() {
        setRendererType(RENDERER_TYPE);
    }

    public void setChannelId(String channelId) {
        getStateHelper().put(AsynchronousPropertyKeys.channelId, channelId);
    }

    public String getChannelId() {
        return (String) getStateHelper().eval(AsynchronousPropertyKeys.channelId, "_channel");
    }

}
