package controllers;

import actors.LiveVoteActor;
import actors.ParticipantActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Answer;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;
import views.html.participant;

import java.util.HashMap;
import java.util.Map;

public class ParticipantController extends Controller {

    private static Integer participantCounter = 0;
    private static Map<String, ActorRef> participantIDMap = new HashMap<String, ActorRef>();


    public static Result participant() {
        return ok(participant.render());
    }

    public static WebSocket<JsonNode> ws(final String participantID) {
        System.out.println("Received ID: " + participantID);
        ActorRef participantActor = null;
        if (participantIDMap.containsKey(participantID)){
            participantActor = participantIDMap.get(participantID);
        }

        return new WebSocket<JsonNode>() {
            public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                final Integer id = participantCounter++;
                if (participantIDMap.containsKey(participantID)){
                    final ActorRef participantActor = participantIDMap.get(participantID);
                }   else {
                    final ActorRef participantActor = Akka.system().actorOf(Props.create(ParticipantActor.class, out, id));
                }

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("Participant" + participantCounter + " received: " + jsonNode.toString());
                        if (jsonNode.has("answertext")){
                            Answer answer = new Answer();
                            answer.answertext = jsonNode.get("answertext").asText();
                            answer.note = jsonNode.get("note").asInt();
                            participantActor.tell(new LiveVoteActor.Reaction(answer), null);


                        }
                    }
                });

                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        LiveVoteActor.instance.tell(new LiveVoteActor.RemoveParticipant(id), participantActor);
                    }
                });
                sendID(out, participantActor);
            }

            private void sendID(Out<JsonNode> out, ActorRef participantActor) {
                JsonNodeFactory factory = JsonNodeFactory.instance;
                ObjectNode jsonID = new ObjectNode(factory);
                jsonID.put("participantID", "participant" + participantCounter);
                out.write(jsonID);
                participantIDMap.put("participant" + participantCounter, participantActor);
                System.out.println("ID " + "participant" + participantCounter + " sent..." );
            }
        };
    }
}
