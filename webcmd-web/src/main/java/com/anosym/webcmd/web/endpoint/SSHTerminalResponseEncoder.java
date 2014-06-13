/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.endpoint;

import org.primefaces.json.JSONObject;
import org.primefaces.push.Encoder;

/**
 *
 * @author mochieng
 */
public class SSHTerminalResponseEncoder implements Encoder<SSHTerminalResponse, String> {

    @Override
    public String encode(SSHTerminalResponse terminalResponse) {
        return new JSONObject(terminalResponse).toString();
    }

}
