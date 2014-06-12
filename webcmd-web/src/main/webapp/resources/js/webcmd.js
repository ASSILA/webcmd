var WEBCMD = {
    initializeViewWidth: function () {
        var terminalWidth = $('.wc-ssh-terminal .wc-ssh-terminal-ctx').width();
        var promptWidth = $('.wc-ssh-terminal .wc-ssh-terminal-prompt').width();
        var viewWidth = terminalWidth - (promptWidth + 10); //remove padding
        $('.wc-ssh-terminal .wc-ssh-terminal-view').width(viewWidth);
    },
    showCurrentActiveInput: function () {
        $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-active="true"]').focus();
    },
    addCmdHandler: function () {
        $this = this;
        $(document).click(function () {
            //set the last input active
            $this.showCurrentActiveInput();
        });
        $(document).keydown(function (e) {
            //ctrl-c pressed when input is busy.
            //send ajax request to cancel the cmd.
            if (e.ctrlKey && e.which === 67 && $this.isInputDisabled()) {
                $this.addNewRowAndSetFocus();
            }
        });
    },
    addEnterKeyHandler: function () {
        var $this = this;
        $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-active="true"]').keydown(function (e) {
            if (e.which === 13) {
                e.preventDefault();
                var input = $(this).val();
                if (input && (input === 'clear' || input === 'clear()')) {
                    $this.clear();
                } else {
                    wcSSHSend();
                    $(this).attr('data-waiting', 'true');
                    $(this).attr('disabled', 'disabled');
                }
            }
        });

    },
    disableInputs: function () {
        $('.wc-ssh-terminal .wc-ssh-terminal-view input').attr('disabled', 'disabled');
        $('.wc-ssh-terminal .wc-ssh-terminal-view input').attr('data-active', 'false');
        $('.wc-ssh-terminal .wc-ssh-terminal-view input').removeAttr('name');
        $('.wc-ssh-terminal').data('ws-busy', 'true');
    },
    //This is simply a state marker.
    //input can only be enabled by having an active input field, through the addition of new row.
    enableInput: function () {
        $('.wc-ssh-terminal').data('ws-busy', 'false');
    },
    isInputDisabled: function () {
        var disabled = $('.wc-ssh-terminal').data('ws-busy');
        return disabled && disabled === 'true';
    },
    addNewRowAndSetFocus: function () {
        //disable all inputs
        this.disableInputs();
        var row = '<div class="wc-ssh-terminal-ctx">\n\
                        <div class="wc-ssh-terminal-prompt">\n\
                        <span class="wc-ssh-terminal-prompt-user">' + this.getUser() + '</span>@<span class="wc-ssh-terminal-prompt-host">' + this.getHost() + '</span>:<span class="wc-ssh-terminal-prompt-dir">&nbsp;' + this.getDir() + '</span>$\n\
                        </div>\n\
                        <div class="wc-ssh-terminal-view">\n\
                            <input name="ssh-terminal-command" type="text" data-active=\"true"/>\n\
                        </div>\n\
                        <div class="ws-async-terminal-clear"></div>\n\
                    </div>';
        $('.wc-ssh-terminal').append(row);
        this.initializeViewWidth();
        //make current input active
        this.showCurrentActiveInput();
        this.addEnterKeyHandler();
        this.enableInput();
    },
    addData: function (data) {
        var data = '<p>' + data + '</p>';
        $('.wc-ssh-terminal').append(data);
        this.addNewRowAndSetFocus();
    },
    addDataAndWaitForMoreResults: function (data) {
        var data = '<p>' + data + '</p>';
        $('.wc-ssh-terminal').append(data);
        this.disableInputs();
    },
    getUser: function () {
        return $('.wc-ssh-terminal').data('user');
    },
    getHost: function () {
        return $('.wc-ssh-terminal').data('host');
    },
    getDir: function () {
        return $('.wc-ssh-terminal').data('dir');
    },
    clear: function () {
        $('.wc-ssh-terminal').html('');
        this.addNewRowAndSetFocus();
    },
    handleResult: function (xhr, status, args) {
        console.log('xhr: '+xhr);
        console.log('status: '+status);
        console.log('args: '+args);
        if (args.response !== 'undefined') {
            this.addData(args.response.response);
            var currentInput = $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-waiting="true"]');
            currentInput.removeAttr('data-waiting');
            currentInput.removeAttr('disabled');
        }
    },
    init: function () {
        this.addNewRowAndSetFocus();
        this.addCmdHandler();
        this.showCurrentActiveInput();
    }
};
$(document).ready(function () {
    WEBCMD.init();
});