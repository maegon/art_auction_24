    // Define global JavaScript variables with Thymeleaf expressions
    var memberNickname = '[[${member.nickname}]]';
    var memberId = '[[${member.id}]]';
    var memberMobileNumber = /*[[${member.phoneNumber}]]*/ 'default_mobile_number';
    var memberLandlineNumber = 'default_landline_number'; // Update as needed

    // Function to initialize ChannelIO
    (function() {
        var w = window;
        if (w.ChannelIO) {
            return w.console.error("ChannelIO script included twice.");
        }
        var ch = function() {
            ch.c(arguments);
        };
        ch.q = [];
        ch.c = function(args) {
            ch.q.push(args);
        };
        w.ChannelIO = ch;
        function l() {
            if (w.ChannelIOInitialized) {
                return;
            }
            w.ChannelIOInitialized = true;
            var s = document.createElement("script");
            s.type = "text/javascript";
            s.async = true;
            s.src = "https://cdn.channel.io/plugin/ch-plugin-web.js";
            var x = document.getElementsByTagName("script")[0];
            if (x.parentNode) {
                x.parentNode.insertBefore(s, x);
            }
        }
        if (document.readyState === "complete") {
            l();
        } else {
            w.addEventListener("DOMContentLoaded", l);
            w.addEventListener("load", l);
        }
    })();

    // Initialize ChannelIO with dynamic data
    ChannelIO('boot', {
        "pluginKey": "72ea21d5-9db0-4a66-ac19-1aa0eb72e588",
        "memberId": memberId, // fill user's member id
        "profile": { // fill user's profile
            "name": memberNickname, // fill user's name
            "mobileNumber": memberMobileNumber, // fill user's mobile number
            "landlineNumber": memberLandlineNumber, // fill user's landline number
            "CUSTOM_VALUE_1": "VALUE_1", // custom property
            "CUSTOM_VALUE_2": "VALUE_2" // custom property
        }
    });