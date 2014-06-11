/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.push.PushContext;

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
    private String sessionChannel = "defaultChannel";
    private PushContext currentPushContext;
    private String command;
    private String currentUserId = "mochieng";
    private String pwd = "~";
    private String hostname = "localhost";

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    public TerminalResponse handleCommand(String command, String[] params) {
        try {
            String cmd[] = new String[params.length + 1];
            cmd[0] = command;
            System.arraycopy(params, 0, cmd, 1, params.length);
            System.out.println("Command: " + Arrays.toString(cmd));
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            InputStream inn = p.getInputStream();
            int count = inn.available();
            byte[] buffer = new byte[count];
            //wait for the input to be available.
            inn.read(buffer);
            String result = new String(buffer);
            result = result.replaceAll("\n", "<br/>");
            return new TerminalResponse(result, TerminalResponse.TerminalResponseType.COMPLETE);
        } catch (Exception ex) {
            Logger.getLogger(WebCommandLineController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TerminalResponse("", TerminalResponse.TerminalResponseType.COMPLETE);
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
