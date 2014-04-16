package controllers;

import actors.LiveVoteActor;
import actors.MidicontrollerActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import java.util.HashMap;
import java.util.Map;

public class MidicontrollerController extends Controller {

    private static Integer midicontrollerCounter = 0;

    private static Map<String, ActorRef> midicontrollerMap = new HashMap<String, ActorRef>();


    public static Result midicontroller() { return ok(views.html.midicontroller.render()); }

    public static WebSocket<JsonNode> ws(final String midicontrollerID) {

        return new WebSocket<JsonNode>() {
            public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {
                final Integer id = midicontrollerCounter++;
                final ActorRef midicontrollerActor;
                if (midicontrollerMap.containsKey(midicontrollerID)) {
                    midicontrollerActor = midicontrollerMap.get(midicontrollerID);
                    midicontrollerActor.tell(new LiveVoteActor.Reconnect(out), null);
                    System.out.println("Linked to existing actor...");
                } else {
                    midicontrollerActor = Akka.system().actorOf(Props.create(MidicontrollerActor.class, out));
                    midicontrollerMap.put("midicontroller" + id.toString(), midicontrollerActor);
                    sendID(out, id);
                    System.out.println("Linked to new actor...");
                }
                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("Midicontroller" + midicontrollerCounter + " received: " + jsonNode.toString());

                    }
                });

                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        LiveVoteActor.instance.tell(new LiveVoteActor.RemoveMidicontroller(), midicontrollerActor);
                    }
                });


            }

            private void sendID(Out<JsonNode> out, Integer id) {
                JsonNodeFactory factory = JsonNodeFactory.instance;
                ObjectNode jsonID = new ObjectNode(factory);
                jsonID.put("midicontrollerID", "midicontroller" + id);
                out.write(jsonID);
            }
        };
    }
}
