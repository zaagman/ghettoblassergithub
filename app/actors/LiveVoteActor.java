package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import models.Question;
import models.Questionlist;
import play.Logger;
import play.libs.Akka;
import play.mvc.WebSocket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiveVoteActor extends UntypedActor {

    public static ActorRef instance = Akka.system().actorOf(new Props(LiveVoteActor.class));

    private final Set<ActorRef> performerSet = new HashSet<>();
    private final Set<ActorRef> participantSet = new HashSet<>();
    private final Set<ActorRef> midicontrollerSet = new HashSet<>();

    private Questionlist questionlist = null;

    private Question currentQuestion;

    Scheduler scheduler = new Scheduler(this.getSelf());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AddPerformer) {
            performerSet.add(getSender());
            Logger.info("Add performer - (performers: " + performerSet.size() + ")");
        } else if (message instanceof RemovePerformer) {
            performerSet.remove(getSender());
            Logger.info("Remove performer - (performers: " + performerSet.size() + ")");
        } else if (message instanceof AddMidicontroller) {
            midicontrollerSet.add(getSender());
            Logger.info("Add midicontroller - (midicontrollers: " + midicontrollerSet.size() + ")");
        } else if (message instanceof RemoveMidicontroller) {
            midicontrollerSet.remove(getSender());
            Logger.info("Remove midicontroller - (midicontrollers: " + midicontrollerSet.size() + ")");
        } else if (message instanceof AddParticipant) {
            participantSet.add(getSender());
            Logger.info("Add participant - (participants: " + participantSet.size() + ")");
        } else if (message instanceof RemoveParticipant) {
            participantSet.remove(getSender());
            Logger.info("Remove participant - (participants: " + participantSet.size() + ")");
        }

        else if (message instanceof Questionlist){
            questionlist = (Questionlist)message;
            System.out.println("Setting questionlist...");
        }
        else if (message instanceof Start) {
            if (questionlist != null){
                System.out.println("Scheduling questionlist...");
                scheduler.scheduleQuestionlist(questionlist);
            }
        }
        else if (message instanceof AskQuestion) {
            System.out.println("Asking question...");
            AskQuestion askQuestion = (AskQuestion)message;
            currentQuestion = askQuestion.question;
            for (ActorRef performer : performerSet){
                performer.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerSet){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef participant : participantSet){
                participant.tell(message, this.getSelf());
            }
        }

        else if (message instanceof SendResult) {
            SendResult sendResult = (SendResult) message;
            System.out.println("Sending Result...");
//            If there were no reactions send the first answer as result
            if (sendResult.question.result == null){
                sendResult.question.addReaction(sendResult.question.answers.get(0));
            }
//            Send question to operators
            for (ActorRef performer : performerSet){
                performer.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerSet){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef participant : participantSet){
                participant.tell(message, this.getSelf());
            }
        }
        else if (message instanceof Reaction){
            for(ActorRef performer : performerSet){
                performer.tell(new Standing(currentQuestion), getSelf());

            }
        }
    }


    public static class AddPerformer {}
    public static class RemovePerformer {}
    public static class AddParticipant {}
    public static class RemoveParticipant {}
    public static class AddMidicontroller {}
    public static class RemoveMidicontroller {}

    public static class Start {}

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
        final Answer reaction;
        public Reaction(Answer reaction) {
            this.reaction = reaction;
        }
    }

    public static class Standing {
        final Question standing;
        public Standing(Question standing) {
            this.standing = standing;
        }
    }

    public static class SetOut {
        final WebSocket.Out<JsonNode> out;

        public SetOut(WebSocket.Out<JsonNode> out) {
            this.out = out;
        }
    }
}
