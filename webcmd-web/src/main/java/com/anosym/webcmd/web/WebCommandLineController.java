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
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 *
 * @author mochieng
 */
@SessionScoped
@Named
public class WebCommandLineController implements Serializable {

    /**
     * This will be based on current logged in user unique identifier.
     *
     * Must start with a trailing forward slash.
     */
    private String sessionChannel;
    private PushContext currentPushContext;
    private String command;
    private String currentUserId = "mochieng";
    private String pwd = "~";
    private String hostname = "localhost";

    public void handleCommand(String command, String[] params) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream inn = p.getInputStream();
            int count = inn.available();
            byte[] buffer = new byte[count];
            inn.read(buffer);
        } catch (IOException ex) {
            Logger.getLogger(WebCommandLineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getPwd() {
        return pwd;
    }

    public String getHostname() {
        return hostname;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getSessionChannel() {
        return sessionChannel;
    }

    private synchronized void updateCmd(Object message) {
        if (currentPushContext == null) {
            currentPushContext = PushContextFactory.getDefault().getPushContext();
        }
        currentPushContext.push(sessionChannel, message);
    }
}
