/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author mochieng
 */
@SessionScoped
@Named
public class WebCommandLineController implements Serializable {

    public String handleCommand(String command, String[] params) {
        try {
            Process p= Runtime.getRuntime().exec(command);
            InputStream inn = p.getInputStream();
            int count = inn.available();
            byte[] buffer = new byte[count];
            inn.read(buffer);
            return new String(buffer);
        } catch (IOException ex) {
            Logger.getLogger(WebCommandLineController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
