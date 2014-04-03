var midi = null;
var inputs = null;
var outputs = null;
var input = null;
var output = null;

function setOutput(outputIndex) {
    toConsole("output selected: " + outputIndex);
    toConsole(outputs[outputIndex].name);
    output = outputs[outputIndex];
}

function runInputTest() {
    toConsole("dummy inputtest");
}

function runOutputTests() {
    if (midi != null){
        toConsole("output test running...");
        testNote(50);
    }
}

function bootMidi() {
    toConsole("Starting up MIDI...");
    navigator.requestMIDIAccess().then(success, failure);
}

function success(midiAccess) {
    toConsole("MIDI ready!");
    midi = midiAccess;

    loadInputs();
    loadOutputs();

}

function loadInputs() {
    inputs = midi.inputs();
    toConsole(inputs.length + " inputs:");
    for (var i = 0; i < inputs.length; i++)
        toConsole(i + ": " + inputs[i].name);

    if (inputs.length > 0) {
        input = inputs[0];
        input.addEventListener("midimessage", handleMIDIMessage);
        toConsole("Hooked up first input.");
    }
}

function loadOutputs() {
    outputs = midi.outputs();
    toConsole(" outputs:");
    for (var i = 0; i < outputs.length; i++) {
        toConsole(i + ": " + outputs[i].name);
    }

        if (document.getElementById("selectOutput") != null) {
            var sel = document.getElementById("selectOutput");
            for (var i = 0; i < outputs.length; i++) {
                var opt = document.createElement('option');
                opt.innerHTML = outputs[i].name;
                opt.value = i;
                sel.appendChild(opt);
            }
        }

    if (outputs.length) {
        output = outputs[0];
        output.send([0xb0, 0x00, 0x7f]); // If the first device is a Novation Launchpad, this will light it up!
    }

}

function failure(error) {
    alert("MIDI failed to start. Did you forget to install the Jazz plugin?");
}

function handleMIDIMessage(ev) {
    // testing - just reflect.
//    toConsole("Message: " + ev.data.length + " bytes, timestamp: " + ev.timestamp);
//    if (ev.data.length == 3) toConsole(" 0x" + ev.data[0].toString(16) + " 0x" + ev.data[1].toString(16) + " 0x" + ev.data[2].toString(16));

    //if (output) output.send(ev.data);
}

function sendNote(note, velocity, duration) {
    output.send([0x90, note, velocity]);
    output.send([0x80, note, 0x40], window.performance.now() + duration);
}

function testNote(note) {
    sendNote(note, 100, 100);
}

function toConsole(text) {
    console.log(text + "\n");
}