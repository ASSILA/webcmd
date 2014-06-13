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
                $this.addData('...... command cancelled.');
            }
        });
    },
    addEnterKeyHandler: function () {
        var $this = this;
        $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-active="true"]').keydown(function (e) {
            if (e.which === 13) {
                e.preventDefault();

                //must be checked first since the input for the current input box will be empty.
                var maskedPassword = $(this).data('maskedpassword') === true;
                var registration = $(this).data('registration') === true;
                //if on masked password field, we have entered the password
                if (maskedPassword && registration) {
                    sshRegister();
                    return;
                }

                var input = $(this).val();
                var connected = $('.wc-ssh-terminal').data('connected') === 'true';

                if (input === 'undefined' || input.trim().length === 0) {
                    $this.addNewRowAndSetFocus();
                } else if (input && (input === 'clear' || input === 'clear()')) {
                    $this.clear();
                } else if (input === 'exit' || input === 'logout') {
                    sshLogout();
                } else if (!connected) {
                    var loggedIn = $('.wc-ssh-terminal').data('loggedIn') === 'true';
                    if (!loggedIn && input.trim().indexOf('register') === 0) {
                        //get the username
                        var array = input.trim().split(" ");
                        $.each(array, function (ix, cmd) {
                            cmd = cmd.trim();
                            if (cmd.indexOf('-u') === 0) {
                                //ensure it is registration
                                var username = cmd.substring(3);
                                $('input[name="wc-sshregistration-username"]').val(username);
                            }
                            if (cmd.indexOf('-p') === 0) {
                                var dataOptions = [
                                    {"name": "maskedpassword", "value": true},
                                    {"name": "registration", "value": true}
                                ];
                                $this.addData('Please enter password....', dataOptions);
                            }
                        });
                    } else if (input !== 'ssh') {
                        //the only command we expect at this point.
                        $this.addData('Invalid command, please connect to your ssh terminal before proceeding.<br/>To connect, type ssh -u={userid} -p={sshKeyId}');
                    }
                } else {
                    wcSSHSend();
                    $this.disableInputs();
                    $(this).attr('data-waiting', 'true');
                    $(this).attr('disabled', 'disabled');
                }
            } else if ($(this).data('maskedpassword') === true && e.which === 8) {
                var keyCode = e.which;
                var $password = $('input[name="wc-sshregistration-password"]');
                var password = $password.val() || '';
                console.log("password: " + password);
                if (keyCode === 8) {
                    if (password.length > 0) {
                        //delete
                        password = password.substring(0, password.length - 1);
                        $password.val(password);
                    } else {
                        var audioUrl = $('.wc-ssh-terminal').data('clickmp3');
                        console.log("Audio URL: " + audioUrl);
                        var audio = new Audio(audioUrl);
                        audio.play();
                    }
                }
            }
        });

        $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-active="true"]').keypress(function (e) {
            if ($(this).data('maskedpassword') === true) {
                e.preventDefault();
                var keyCode = e.which;
                $(this).val("");
                var $password = $('input[name="wc-sshregistration-password"]');
                var password = $password.val() || '';
                var char = String.fromCharCode(keyCode);
                if (char) {
                    $password.val(password + char);
                }
            }
        });

    },
    disableInputs: function () {
        var $input = $('.wc-ssh-terminal .wc-ssh-terminal-view input');
        $input.attr('disabled', 'disabled');
        $input.attr('data-active', 'false');
        $input.removeAttr('name');
        $('.wc-ssh-terminal').data('ws-busy', 'true');
        $input.unbind('keypress');
        $input.unbind('keydown');
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
    //dataOptions must be an array.
    addNewRowAndSetFocus: function (dataOptions) {
        //disable all inputs
        var input = '<input name="ssh-terminal-command" type="text" data-active=\"true" autocomplete="on" ';
        console.log('DataOptions: ' + dataOptions);
        if (dataOptions) {
            $.each(dataOptions, function (ix, data) {
                input = input + ' data-' + data.name + '="' + data.value + '" ';
            });
        }
        input = input + '/>';
        console.log('Input: ' + input);
        this.disableInputs();
        var row = '<div class="wc-ssh-terminal-ctx">\n\
                        <div class="wc-ssh-terminal-prompt">\n\
                        <span class="wc-ssh-terminal-prompt-user">' + this.getUser() + '</span>@<span class="wc-ssh-terminal-prompt-host">' + this.getHost() + '</span>:<span class="wc-ssh-terminal-prompt-dir">&nbsp;' + this.getDir() + '</span>$\n\
                        </div>\n\
                        <div class="wc-ssh-terminal-view"> ' + input + '</div>\n\
                        <div class="ws-async-terminal-clear"></div>\n\
                    </div>';
        $('.wc-ssh-terminal').append(row);
        this.initializeViewWidth();
        //make current input active
        this.showCurrentActiveInput();
        this.addEnterKeyHandler();
        this.enableInput();
    },
    addData: function (data, dataOptions) {
        $('.wc-ssh-terminal').append('<p class="wc-ssh-terminal-data">' + data + '</p>');
        this.addNewRowAndSetFocus(dataOptions);
    },
    addDataAndWaitForMoreResults: function (data) {
        var data = '<p class="wc-ssh-terminal-data">' + data + '</p>';
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
        this.init();
    },
    handleResult: function (xhr, status, args) {
        console.log('xhr: ' + xhr);
        console.log('status: ' + status);
        console.log('args: ' + args.response);
        if (args.response !== 'undefined') {
            var result = args.response.response;
            result = result.replace(/\n/g, "<br/>").replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
            this.addData(result);
            var currentInput = $('.wc-ssh-terminal .wc-ssh-terminal-view input[data-waiting="true"]');
            currentInput.removeAttr('data-waiting');
            currentInput.removeAttr('disabled');
        }
    },
    onRegistration: function (xhr, status, args) {
        console.log('onRegistration:status=' + status);
        if (status === 'success') {
            if (args.isSuccess) {
                $('.wc-ssh-terminal').data('loggedIn', 'true');
                PF('sshTerminal').select(1);
            } else {
                this.addData('Registration failed: ' + args.error);
            }
        }
    },
    init: function () {
        this.addData("<div id=\"sshTerminalWelcome\" class=\"wc-ssh-terminal-welcome\">\n" +
            "            <p>Welcome to WEBCMD: SSH Online terminal</p>\n" +
            "            <ul type=\"disc\">\n" +
            "                <li>To log into your SSH Terminal, type: login -u={username} -p={passoword}</li>\n" +
            "                <li>To connect to your SSH Terminal, type: ssh  -key={sshKeyId}</li>\n" +
            "                <li>To login and connect to your SSH Terminal, type: ssh -u={username} -p={passoword} -key={sshKeyId}</li>\n" +
            "                <li>To register to WEBCMD, type register -u={username} -p={password}</li>\n" +
            "            </ul>\n" +
            "        </div>");
        $('#sshTerminalWelcome').show();
        this.addCmdHandler();
        this.showCurrentActiveInput();
    }
};
$(document).ready(function () {
    WEBCMD.init();
});