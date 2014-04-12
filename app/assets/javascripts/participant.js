test = function () {
alert("test");
var ws = new WebSocket("@routes.ParticipantController.ws()");
ws.onMessage = function (message) {
    alert(message);
};
}

