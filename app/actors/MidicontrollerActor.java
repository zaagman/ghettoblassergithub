package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

public class MidicontrollerActor extends UntypedActor {

    private WebSocket.Out<JsonNode> out;

    public MidicontrollerActor(WebSocket.Out<JsonNode> out) {
        this.out = out;
        System.out.println("creating MidicontrollerActor...");
        LiveVoteActor.instance.tell(new AddMidicontroller(), getSelf());
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SendResult){
            LiveVoteActor.SendResult sendResult = (SendResult)message;
            SendNote sendNote = new SendNote(sendResult.question.result.note);
            this.getSelf().tell(sendNote, getSelf());
        }

        if (message instanceof SendNote) {
            SendNote sendNote = (SendNote)message;
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode jsonSendNote = new ObjectNode(factory);
            jsonSendNote.put("sendNote", sendNote.note);
            out.write(jsonSendNote);
        }
        else if (message instanceof Reconnect) {
            Reconnect reconnect = (Reconnect) message;
            out = reconnect.out;
            LiveVoteActor.instance.tell(new AddMidicontroller(), getSelf());
        }
    }

    public static class SendNote {
        final int note;

        public SendNote(int note) {
            this.note = note;
        }
    }
}
