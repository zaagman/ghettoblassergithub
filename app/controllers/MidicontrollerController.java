package controllers;

import actors.LiveVoteActor;
import actors.MidicontrollerActor;
import actors.ParticipantActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.participant;

public class MidicontrollerController extends Controller {

    private static Integer midicontrollerCounter = 0;


    public static Result midicontroller() { return ok(views.html.midicontroller.render()); }

    public static WebSocket<JsonNode> ws() {
        return new WebSocket<JsonNode>() {
            public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {
                final Integer id = midicontrollerCounter++;
                final ActorRef midicontrollerActor = Akka.system().actorOf(Props.create(MidicontrollerActor.class, out, id));

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("Midicontroller recieved: " + jsonNode.toString());

                    }
                });

                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        LiveVoteActor.instance.tell(new LiveVoteActor.RemoveMidicontroller(id), midicontrollerActor);
                    }
                });
            }
        };
    }
}
