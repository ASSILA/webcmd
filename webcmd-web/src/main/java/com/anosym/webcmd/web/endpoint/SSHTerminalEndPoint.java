/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.endpoint;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.websocket.OnOpen;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;

/**
 *
 * @author mochieng
 */
@PushEndpoint("/terminal/{userId}/{host}/pwd")
public class SSHTerminalEndPoint {

    private static final Logger LOG = Logger.getLogger(SSHTerminalEndPoint.class.getName());

    @PathParam("userId")
    private String userId;
    @PathParam("host")
    private String host;
    @PathParam("pwd")
    private String pwd;
    @Inject
    private ServletContext servletContext;

    @OnOpen
    public void onOpen(RemoteEndpoint remoteEndpoint, EventBus eventBus) {
        LOG.log(Level.INFO, "Opening endpoint: {0}", remoteEndpoint);
        eventBus.publish(userId, new SSHTerminalResponse(pwd, userId, host, "Connected to terminal", SSHTerminalResponse.TerminalResponseType.COMPLETE));
        //at this point, we create the ssh connection.
    }

    @OnClose
    public void onClose(RemoteEndpoint remoteEndpoint, EventBus eventBus) {
        //at this point, we close the ssh connection.
    }

    @OnMessage(encoders = {SSHTerminalResponseEncoder.class})
    public SSHTerminalResponse onCommand(String command) {
        return null;
    }
}
