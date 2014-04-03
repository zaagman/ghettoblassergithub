var midi=null;
var inputs=null;
var outputs=null;
var input=null;
var output=null;
var logger=null;

function log(text){
    if (!logger) {
        logger = document.getElementById("log");
    	logger.innerHTML = text;
    }
    else {
        logger.innerHTML = text + "\n" + logger.innerHTML;
    }
}

function runOutputTest() {
    if (!midi)
        runTest();

    log("Starting Output test");

    sendMiddleC(midi, output);
}

function setOutput( outputIndex ){
    log("output selected: " + outputIndex);
    output = outputIndex;
}

function sendMiddleC( midiAccess, indexOfPort ) {
    var noteOnMessage = [0x90, 60, 0x7f];    // note on, middle C, full velocity
    var output = outputs[indexOfPort];
    output.send( noteOnMessage );  //omitting the timestamp means send immediately.
    output.send( [0x80, 60, 0x40], window.performance.now() + 1000.0 ); // Inlined array creation- note off, middle C,
                                                                      // release velocity = 64, timestamp = now + 1000ms.
}

function runTest() {
	log("Starting up MIDI...");
	navigator.requestMIDIAccess().then( success, failure );
}

function handleMIDIMessage( ev ) {
	// testing - just reflect.
	log("Message: " + ev.data.length + " bytes, timestamp: " + ev.timestamp);
	if (ev.data.length == 3) 
		log(" 0x" + ev.data[0].toString(16) + " 0x" + ev.data[1].toString(16) + " 0x" + ev.data[2].toString(16));

	if (output)
		output.send( ev.data );
}

function success( midiAccess ) {
	log("MIDI ready!");
	midi = midiAccess;

	loadInputs();

	loadOutputs();

}

function loadInputs(){
    inputs = midi.inputs();
    log(inputs.length+" inputs:");
    for (var i=0;i<inputs.length;i++)
        log(i + ": " + inputs[i].name);

    if (inputs.length>0) {
        input = inputs[0];
         //		input.onmidimessage = handleMIDIMessage;
        input.addEventListener("midimessage", handleMIDIMessage);
        log("Hooked up first input.");
    }
}

function loadOutputs() {
    outputs = midi.outputs();
    var sel = document.getElementById("selectOutput");
    log(" outputs:");
    for (var i=0;i<outputs.length;i++){
        log(i + ": " + outputs[i].name);
        var opt = document.createElement('option');
        opt.innerHTML = outputs[i].name;
        opt.value = i;
        sel.appendChild(opt);
    }

    if (outputs.length) {
        output = outputs[0];
        output.send( [0xb0, 0x00, 0x7f] );	// If the first device is a Novation Launchpad, this will light it up!
    }

}

function failure( error ) {
	alert( "MIDI failed to start. Did you forget to install the Jazz plugin?");
}

function sendNote( note, velocity, duration ){
    output.send( [0x90, note, velocity] );
    output.send( [0x80, note, 0x40], window.performance.now() + duration );
}


