package controllers;

import actors.LiveVoteActor;
import actors.PerformerActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

public class PerformerController extends Controller {

    private static Integer performerCounter = 0;


    public static Result performer() {
        return ok(views.html.performer.render());
    }

    public static WebSocket<JsonNode> ws() {
        return new WebSocket<JsonNode>() {
            public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {
                final Integer id = performerCounter++;
                final ActorRef performerActor = Akka.system().actorOf(Props.create(PerformerActor.class, out, id));

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("Performer" + performerCounter + " received: " + jsonNode.toString());

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
        };
    }
}
