package controllers;

import actors.PerformerActor;
import actors.SystemActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import play.libs.Akka;
import com.fasterxml.jackson.databind.JsonNode;
import models.Questionlist;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainController extends Controller {


    static ActorRef systemActor = Akka.system().actorOf(new Props(SystemActor.class));
//    ArrayList<ActorRef> operatorActors = new ArrayList<ActorRef>();
//    ArrayList<ActorRef> guestActors = new ArrayList<ActorRef>();


    public static Result index() {
        return ok(views.html.index.render("Hello from Java"));
    }

    public static Result questions() {
        return ok(views.html.questions.render());
    }

    public static Result addQuestionlist() {
        JsonNode data = request().body().asJson();
        Questionlist questionlist = Questionlist.newQuestionlistFromJson(data);


        systemActor.tell(questionlist, null);

        System.out.println(questionlist.toJson().toString());

        return ok(views.html.questions.render());
    }

    public static WebSocket<String> operatorWs() {
        return new WebSocket<String>() {
            ActorRef operatorActor = Akka.system().actorOf(new Props(PerformerActor.class));
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                final Cancellable cancellable = Akka.system().scheduler().schedule(Duration.create(1, SECONDS),
                        Duration.create(1, SECONDS),
                        operatorActor,
                        new PerformerActor.NewOperatorWs(in, out),
                        Akka.system().dispatcher(),
                        systemActor
                );



                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        cancellable.cancel();
                    }
                });
            }
        };
    }

}
