package controllers;

import actors.LiveVoteActor;
import actors.ParticipantActor;
import actors.PerformerActor;
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

public class PerformerController extends Controller {

    private static Integer performerCounter = 0;
    private static Map<String, ActorRef> performerMap = new HashMap<String, ActorRef>();


    public static Result performer() {
        return ok(views.html.performer.render());
    }

    public static WebSocket<JsonNode> ws(final String performerID) {
        System.out.println("received ID: " + performerID);
        return new WebSocket<JsonNode>() {
            public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {
                final Integer id = performerCounter++;
                final ActorRef performerActor;
                if (performerMap.containsKey(performerID)) {
                    performerActor = performerMap.get(performerID);
                    performerActor.tell(new LiveVoteActor.SetOut(out), null);
                    System.out.println("Linked to existing actor...");
                } else {

                    performerActor = Akka.system().actorOf(Props.create(PerformerActor.class, out, id));
                    performerMap.put("performer" + id.toString(), performerActor);
                    sendID(out, id);
                    System.out.println("Linked to new actor...");
                }

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("performer" + id + " received: " + jsonNode.toString());

                        if (jsonNode.has("start")){
                            performerActor.tell(new LiveVoteActor.Start(), null);
                        }
                    }
                });

                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        LiveVoteActor.instance.tell(new LiveVoteActor.RemovePerformer(id), performerActor);
                    }
                });
            }
            private void sendID(Out<JsonNode> out, Integer id) {
                JsonNodeFactory factory = JsonNodeFactory.instance;
                ObjectNode jsonID = new ObjectNode(factory);
                jsonID.put("performerID", "performer" + id);
                out.write(jsonID);
            }
        };
    }
}
