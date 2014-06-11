/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web;

import java.io.Serializable;

/**
 *
 * @author mochieng
 */
public class TerminalResponse implements Serializable{

    public static enum TerminalResponseType {

        //The response we have received is asynchronous, and hence we should wait for results.
        ASYNCHRONOUS,
        //the response was a single invocation.
        COMPLETE;
    }

    //Used for asynchronous invocations.
    public static interface TerminalResponseListener {

        void onResponse(String data);

        void onResponseComplete();
    }
    private final String response;
    private final TerminalResponseType responseType;
    //registered listener to this reponse if you require more data.
    private TerminalResponseListener responseListener;

    public TerminalResponse(String response, TerminalResponseType responseType) {
        this.response = response;
        this.responseType = responseType;
    }

    public String getResponse() {
        return response;
    }

    public TerminalResponseType getResponseType() {
        return responseType;
    }

    public TerminalResponseListener getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(TerminalResponseListener responseListener) {
        this.responseListener = responseListener;
    }

}
