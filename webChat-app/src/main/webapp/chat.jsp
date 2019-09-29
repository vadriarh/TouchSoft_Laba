<%--
  Created by IntelliJ IDEA.
  User: oit-el2
  Date: 27.09.2019
  Time: 8:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        input#chat {
        width: 410px
    }

    #console-container {
        width: 400px;
    }

    #console {
        border: 1px solid #CCCCCC;
        border-right-color: #999999;
        border-bottom-color: #999999;
        height: 170px;
        overflow-y: scroll;
        padding: 5px;
        width: 100%;
    }

    #console p {
        padding: 0;
        margin: 0;
    }
    </style>
    <script type="application/javascript">

    var Chat = {};

    Chat.socket = null;

    Chat.connect = (function(host) {
        Chat.socket = new WebSocket(host);

        Chat.socket.onopen = function () {
            Console.log('Info: WebSocket connection opened.');
            document.getElementById('chat').onkeydown = function(event) {
                if (event.keyCode == 13) {
                    Chat.sendMessage();
                }
            };
        };

        Chat.socket.onclose = function () {
            document.getElementById('chat').onkeydown = null;
            Console.log('Info: WebSocket closed.');
        };

        Chat.socket.onmessage = function (message) {
            var messageObj=JSON.parse(message.data);
            Console.log(messageObj.text);
        };
    });

    Chat.initialize = function() {
        if (window.location.protocol == 'http:') {
            Chat.connect('ws://' + window.location.host + '/webChat_app_war_exploded/chat');
        } else {
            Chat.connect('wss://' + window.location.host + '/webChat_app_war_exploded/chat');
        }
    };

    Chat.sendMessage = (function() {
        var message = document.getElementById('chat').value;
        if (message != '') {
            Chat.socket.send(JSON.stringify({text:message}));
            document.getElementById('chat').value = '';
            if(message=='/exit'){
                Console.log('Connection closed. Goodbye.');
                Console.log('To reconnect, refresh the page.');
                document.getElementById('chat').onkeydown = null;
            }
        }
    });

    var Console = {};

    Console.log = (function(message) {
        var console = document.getElementById('console');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.innerHTML = message;
        console.appendChild(p);
        while (console.childNodes.length > 25) {
            console.removeChild(console.firstChild);
        }
        console.scrollTop = console.scrollHeight;
    });

    Chat.initialize();

    // document.addEventListener("DOMContentLoaded", function() {
    //     // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
    //     var noscripts = document.getElementsByClassName("noscript");
    //     for (var i = 0; i < noscripts.length; i++) {
    //         noscripts[i].parentNode.removeChild(noscripts[i]);
    //     }
    // }, false);
    </script>
</head>
<body>
<div>
    <p>
        <input type="text" placeholder="type and press enter to chat" id="chat" />
    </p>
    <div id="console-container">
        <div id="console"/>
    </div>
</div>
</body>
</html>
