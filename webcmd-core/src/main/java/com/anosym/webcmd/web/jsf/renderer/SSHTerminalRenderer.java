/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.jsf.renderer;

import static com.anosym.webcmd.web.jsf.component.SSHTerminal.COMPONENT_FAMILY;

import static com.anosym.webcmd.web.jsf.component.SSHTerminal.RENDERER_TYPE;

import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 *
 * @author mochieng
 */
@FacesRenderer(componentFamily = COMPONENT_FAMILY, rendererType = RENDERER_TYPE)
public class SSHTerminalRenderer extends Renderer {

}
