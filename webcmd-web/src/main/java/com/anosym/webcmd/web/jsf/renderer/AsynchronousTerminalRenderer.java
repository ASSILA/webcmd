/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.webcmd.web.jsf.renderer;

import com.anosym.webcmd.web.TerminalResponse;
import com.anosym.webcmd.web.TerminalResponse.TerminalResponseListener;
import com.anosym.webcmd.web.jsf.component.AsynchronousTerminal;
import java.io.IOException;
import java.util.Arrays;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.primefaces.component.terminal.Terminal;
import org.primefaces.component.terminal.TerminalRenderer;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 *
 * @author mochieng
 */
@FacesRenderer(componentFamily = Terminal.COMPONENT_FAMILY, rendererType = AsynchronousTerminal.RENDERER_TYPE)
public class AsynchronousTerminalRenderer extends TerminalRenderer {

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    protected void handleCommand(FacesContext context, Terminal terminal) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        try {
            String clientId = terminal.getClientId(context);
            String value = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");
            String tokens[] = value.split(" ");
            String command = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            AsynchronousTerminal asyncTerminal = (AsynchronousTerminal) terminal;
            MethodExpression commandHandler = asyncTerminal.getCommandHandler();
            TerminalResponse result = (TerminalResponse) commandHandler.invoke(context.getELContext(), new Object[]{command, args});
            if (result.getResponseType() == TerminalResponse.TerminalResponseType.COMPLETE) {
                writer.write(result.getResponse());
            } else {
                //register listener
                result.setResponseListener(new TerminalResponseListenerImpl(asyncTerminal.getChannelId()));
            }
        } catch (Exception ex) {
            writer.write(ExceptionUtils.getStackTrace(ex).replaceAll("\n", "<br/>").replaceAll("Caused by:", "<br/>Caused by:"));
        }
    }

    private static final class TerminalResponseListenerImpl implements TerminalResponseListener {

        private final PushContext currentPushContext;
        private final String channelId;

        public TerminalResponseListenerImpl(String channelId) {
            this.channelId = channelId;
            this.currentPushContext = PushContextFactory.getDefault().getPushContext();
        }

        @Override
        public void onResponse(String data) {
            updateCmd(data);
        }

        @Override
        public void onResponseComplete() {
            //this is the way the javascript handler detects that the response is complete and hence unfreezes the command line.
            updateCmd("complete:true");
        }

        private synchronized void updateCmd(Object message) {
            currentPushContext.push(channelId, message);
        }
    }
}
