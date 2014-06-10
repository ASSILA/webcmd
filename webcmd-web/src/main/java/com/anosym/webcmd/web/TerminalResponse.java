/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web;

/**
 *
 * @author mochieng
 */
public class TerminalResponse {

    private String value;
    private boolean complete;

    public TerminalResponse(String value, boolean complete) {
        this.value = value;
        this.complete = complete;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
