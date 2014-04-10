package actors;


import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import models.Answer;
import models.Question;
import models.Questionlist;

import java.util.*;

/**
 * Created by zaagman on 05/04/14.
 */
public class SystemActor extends UntypedActor {

    ArrayList<Questionlist> questionlists = new ArrayList<Questionlist>();

    Map<String, ActorRef> guestActors = new HashMap<String, ActorRef>();
    Map<String, ActorRef> midicontrollerActors = new HashMap<String, ActorRef>();
    Map<String, ActorRef> operatorActors = new HashMap<String, ActorRef>();
    Scheduler scheduler = new Scheduler(this.getSelf());


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Questionlist){
            Questionlist questionlist = (Questionlist)message;
            questionlists.add(questionlist);
            System.out.println("Adding questionlist...");
//            System.out.println(questionlist);
            self().tell(new Start(), self());
        }
        if (message instanceof Start) {
            if (!questionlists.isEmpty()){
                System.out.println("Scheduling questionlist...");
//                System.out.println(questionlists.get(0));
                scheduler.scheduleQuestionlist(questionlists.get(0));
            }
        }
        if (message instanceof AskQuestion) {
            System.out.println("Asking question...");
//            System.out.println(((AskQuestion) message).question);
//            Send question to operators
            for (ActorRef operator : operatorActors.values()){
                operator.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerActors.values()){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef guest : guestActors.values()){
                guest.tell(message, this.getSelf());
            }
        }

        if (message instanceof SendResult) {
            SendResult sendResult = (SendResult) message;
            System.out.println("Sending Result...");
//            If there were no reactions send the first answer as result
            if (sendResult.question.result == null){
                sendResult.question.addReaction(sendResult.question.answers.get(0));
            }
            System.out.println(sendResult.question);
//            Send question to operators
            for (ActorRef operator : operatorActors.values()){
                operator.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerActors.values()){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef guest : guestActors.values()){
                guest.tell(message, this.getSelf());
            }


        }

        if (message instanceof NewOperatorActor){
            operatorActors.put("", getSender());
        }

    }

    public static class Start {

        public Start()	{

        }
    }

    public static class AskQuestion  {
        final public Question question;

        public AskQuestion (Question question){
            this.question = question;
        }
    }

    public static class SendResult {
        public Question question;
        public SendResult(Question question) {
            this.question = question;
        }
    }

    public static class Reaction {
        public Answer reaction;

        public Reaction (Answer reaction){
            this.reaction = reaction;
        }
    }

    public static class NewOperatorActor {}

}
