package controllers;

import actors.LiveVoteActor;
import actors.PerformerActor;
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


        LiveVoteActor.instance.tell(questionlist, null);

        System.out.println(questionlist.toJson().toString());

        return ok(views.html.questions.render());
    }

}
