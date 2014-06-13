/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web;

import com.anosym.webcmd.web.profile.Profile;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.push.PushContext;

/**
 *
 * @author mochieng
 */
@SessionScoped
@Named("sshTerminalController")
public class SSHTerminalController implements Serializable {

    /**
     * This will be based on current logged in user unique identifier.
     *
     * Must start with a trailing forward slash.
     */
    private String sessionChannel = "defaultChannel";
    private PushContext currentPushContext;
    private String command;
    private String currentUserId = "mochieng";
    private String pwd = "~";
    private String hostname = "localhost";
    private Profile profile;

    private static String getParameter(String paramId) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(paramId);
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    public void handleCommand() {
        try {
            String command = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ssh-terminal-command");
            String cmd[] = command.trim().split(" ");
            System.out.println("Command: " + Arrays.toString(cmd));
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            InputStream inn = p.getInputStream();
            int count = inn.available();
            byte[] buffer = new byte[count];
            //wait for the input to be available.
            inn.read(buffer);
//            String result = new String(buffer);
//            SSHTerminalResponse response = new SSHTerminalResponse(result, SSHTerminalResponse.TerminalResponseType.COMPLETE);
//            RequestContext rc = RequestContext.getCurrentInstance();
//            rc.addCallbackParam("response", response);
//            System.out.println("Response: " + result);
        } catch (Exception ex) {
//            Logger.getLogger(SSHTerminalController.class.getName()).log(Level.SEVERE, null, ex);
//            String result = ExceptionUtils.getFullStackTrace(ex);
//            SSHTerminalResponse response = new SSHTerminalResponse(result, SSHTerminalResponse.TerminalResponseType.COMPLETE);
//            RequestContext rc = RequestContext.getCurrentInstance();
//            rc.addCallbackParam("response", response);
        }

    }

    public void registerNewUser() {
        String username = getParameter("wc-sshregistration-username");
        String password = getParameter("wc-sshregistration-password");
        System.out.println("Username: " + username);
        System.out.println("password: " + password);
        RequestContext ctx = RequestContext.getCurrentInstance();
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            profile = new Profile(username, password);
            System.out.println("Profile Created:  " + profile);
            ctx.addCallbackParam("isSuccess", true);
        } else {
            ctx.addCallbackParam("error", "You must provide username and password");
            ctx.addCallbackParam("isSuccess", false);
        }
    }

    public void logout() {
        this.profile = null;
    }

    public boolean isLoggedIn() {
        return profile != null;
    }

    public void endSession() {

    }

    public String getPrompt() {
        return "<span class=\"wc-user\">mochieng</span>@<span class=\"wc-host\">localhost</span> ~$";
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

}
