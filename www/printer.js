var exec = require('cordova/exec');

var printer = {
    platforms: ['android'],

    isSupported: function() {
        if (window.device) {
            var platform = window.device.platform;
            if ((platform !== undefined) && (platform !== null)) {
                return (this.platforms.indexOf(platform.toLowerCase()) >= 0);
            }
        }
        return false;
    },
    printText: function(text, charset, onSuccess, onError) {
        alert(2)
        exec(onSuccess, onError, 'M3Printer', 'printText', [text, charset]);
    },
    printBase64: function(text, charset, onSuccess, onError) {
        alert(3)
        exec(onSuccess, onError, 'M3Printer', 'printBase64', [text, charset]);
    }
};
module.exports = printer;