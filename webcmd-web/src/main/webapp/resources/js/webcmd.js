/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function WEBCMD () {
    var terminal;
    var terminalId;
    var commandValueId;
    var sendCommand;
    var count;

    this.setEditable = function (editable) {
        if (editable === true) {
            $(terminal).attr('contenteditable', editable);
        } else {
            $(terminal).removeAttr('contenteditable');
        }
    };

    this.freezeEdit = function () {
        setEditable(false);
    };

    this.handleSendMessage = function (cmdWithParams) {
        $(this.commandValueId).val(cmdWithParams);
        this.sendCommand();
        this.freezeEdit();
    };

    this.addTrackingEvent = function (selector) {
        var _this = this;
        $(selector).keypress(function (e) {
            if (e.which === 13) {
                e.preventDefault();
                //get the current input
                var input = $(this).children('[data-active=\"true\"]:focus').text();
                //remove the prompt
                var ix = input.indexOf('$');
                input = input.substring(ix + 1).trim();
                if (input === 'undefined' || input.length === 0) {
                    _this.addPrompt();
                } else {
                    _this.handleSendMessage(input);
                }
            }
        });
        $(selector).unbind('keydown').bind('keydown', function (e) {
            var input = $(this).children('[data-active]:focus').text();
            if (e.which === 8) {
                //make sure we do not delet the command prompt.
                //remove the prompt
                var ix = input.trim().indexOf('$') + 1;
                if (input.length === ix) {
                    e.preventDefault();
                }
            }
            if (e.which === 46 && input.indexOf('\n') === 0) {
                //prevent deleting next line.
                e.preventDefault();
            }
        });
    };

    this.getPrompt = function () {
        if (hostname === 'undefined' || hostname.trim().length === 0) {
            hostname = "localhost";
        }
        return "<span>" + currentUser + "</span>@" + hostname + " " + pwd + "$&nbsp;";
    };

    this.addMessage = function (message, enableEdit, countId) {
        var div = "<div id=\"" + this.terminalId + "-wc-terminal-" + countId + "\" class=\"wc-line\" data-active=\"true\" tab-index=\"" + countId + "\">" + this.getPrompt() + message + "</div>";
        if (enableEdit === true && $(this.terminal).attr("contenteditable") === 'undefined') {
            this.setEditable(enableEdit);
        }
        return div;
    };

    this.addPrompt = function () {
        this.count = this.count + 1;
        var activeLines = $(this.terminal).find("[data-active=\"true\"]");
        activeLines.attr('data-active', 'false');
        $(this.terminal).append(this.addMessage("", false, this.count));
        var lineId = this.terminalId + "-wc-terminal-" + this.count;
        $('#' + lineId).focus();
    };

    this.init = function () {
        this.terminal = $('.wc-terminal');
        this.terminalId = $('.wc-terminal').id;
        this.commandValueId = $(this.terminal).data('commandValue');
        this.sendCommand = window[$(this.terminal).data('sendCommandName')];
        this.count = 0;
        this.trackEnterEvent();
        this.addPrompt();
    };

    this.trackEnterEvent = function () {
        this.addTrackingEvent('div.wc-terminal');
    };

}
;

function handleResponse (data) {
    WEBCMD.addMessage(data.value, data.complete);
}

$(document).ready(function () {
    var webcmd = new WEBCMD();
    console.log(webcmd);
    webcmd.init();
});