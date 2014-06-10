/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.jsf.renderer;

import static com.anosym.webcmd.jsf.component.JWebCommandLineUIInput.WEBCMD_COMPONENT_FAMILY;
import static com.anosym.webcmd.jsf.renderer.JWebCommandLineRenderer.WEBCMD_RENDERER_TYPE;

import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 *
 * @author mochieng
 */
@FacesRenderer(componentFamily = WEBCMD_COMPONENT_FAMILY, rendererType = WEBCMD_RENDERER_TYPE)
public class JWebCommandLineRenderer extends Renderer {

    public static final String WEBCMD_RENDERER_TYPE = "text-cmd-type";
}
