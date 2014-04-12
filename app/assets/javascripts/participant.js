test = function () {
//    var vs = new WebSocket("@routes.ParticipantController.ws().webSocketURL(request)");
    var ws = new WebSocket(wsRoute);
    ws.onmessage = function (message) {
        console.log(message + "\n");
    };
}

