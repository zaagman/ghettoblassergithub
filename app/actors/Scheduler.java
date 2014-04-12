package actors;

import models.Question;
import models.Questionlist;
import play.libs.Akka;
import akka.actor.ActorRef;
import actors.SystemActor.*;
import scala.concurrent.duration.Duration;

/**
 * Created by zaagman on 05/04/14.
 */
public class Scheduler {


    private ActorRef systemActor;

    public Scheduler(ActorRef actorRef){
        this.systemActor = actorRef;
    }
    public boolean scheduleQuestionlist (Questionlist questionlist){

        for (Question question : questionlist.questions) {
            Akka.system().scheduler().scheduleOnce(
                    Duration.create(question.time, "seconds"),
                    systemActor,
                    new AskQuestion(question),
                    Akka.system().dispatcher(),
                    null);
            Akka.system().scheduler().scheduleOnce(
                    Duration.create(question.time + question.duration, "seconds"),
                    systemActor,
                    new SendResult(question),
                    Akka.system().dispatcher(),
                    null);
        }


        return true;
    }
}
