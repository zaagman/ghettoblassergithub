package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Akka;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

/**
 * Created by zaagman on 09/04/14.
 */
public class MidicontrollerActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    public MidicontrollerActor(WebSocket.Out<JsonNode> out, Integer id) {
        this.out = out;
        System.out.println("creating MidicontrollerActor...");
        LiveVoteActor.instance.tell(new AddMidicontroller(id), getSelf());
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
    }


    public static class SendNote {
        final int note;

        public SendNote(int note) {
            this.note = note;
        }
    }
}
