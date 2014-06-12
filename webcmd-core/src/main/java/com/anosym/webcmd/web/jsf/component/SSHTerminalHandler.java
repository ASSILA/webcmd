/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.jsf.component;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

/**
 *
 * @author mochieng
 */
public class SSHTerminalHandler extends ComponentHandler {

//    private static final MethodRule COMMAND_HANDLER
//            = new MethodRule("commandHandler", TerminalResponse.class, new Class[]{String.class, String[].class});

    public SSHTerminalHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
//        metaRuleset.addRule(COMMAND_HANDLER);
        return metaRuleset;
    }
}
