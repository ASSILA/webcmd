/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function resetCommandPrompt () {
    var prompt = $('div[prompt]').attr('prompt');
    $('span.ui-terminal-prompt').html(prompt);
}

function overrideUITerminalInput () {
    $('input.ui-terminal-input').on('keydown.terminal', function (e) {
        if (e.which === 13) {
            var form = this.form;
            var input = $(this).val();
            if(input.trim() === 'clear()'){
                $('.ui-terminal-content').html('');
                return true;
            }
            e.preventDefault();
            var sendCommand = $(form).attr('sendCommand');
            window[sendCommand]();
        }
    });
}

function handleAsynchronousMessages (messages) {

}

$(document).ready(function () {
    resetCommandPrompt();
    overrideUITerminalInput();
});