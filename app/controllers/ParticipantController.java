package controllers;

import actors.LiveVoteActor;
import actors.ParticipantActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;
import views.html.participant;

public class ParticipantController extends Controller {

    private static Integer participantCounter = 0;


    public static Result participant() {
        return ok(participant.render());
    }

    public static WebSocket<JsonNode> ws() {
        return new WebSocket<JsonNode>() {
            public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                final Integer id = participantCounter++;
                final ActorRef participantActor = Akka.system().actorOf(Props.create(ParticipantActor.class, out, id));

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                    }
                });

                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        LiveVoteActor.instance.tell(new LiveVoteActor.RemoveParticipant(id), participantActor);
                    }
                });
            }
        };
    }
}
