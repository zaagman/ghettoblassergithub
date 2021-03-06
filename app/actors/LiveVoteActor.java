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

import java.util.HashSet;
import java.util.Set;

public class LiveVoteActor extends UntypedActor {

    public static ActorRef instance = Akka.system().actorOf(new Props(LiveVoteActor.class));

    private final Set<ActorRef> performerSet = new HashSet<>();
    private final Set<ActorRef> participantSet = new HashSet<>();
    private final Set<ActorRef> midicontrollerSet = new HashSet<>();

    public static Questionlist questionlist = null;


    Scheduler scheduler = new Scheduler(this.getSelf());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AddPerformer) {
            performerSet.add(getSender());
            if (questionlist != null) {
                QuestionlistUpdated questionlistUpdated = new QuestionlistUpdated();
                getSender().tell(questionlistUpdated, getSelf());
            }
            Logger.info("Add performer - (performers: " + performerSet.size() + ")");
        } else if (message instanceof RemovePerformer) {
            performerSet.remove(getSender());
            Logger.info("Remove performer - (performers: " + performerSet.size() + ")");
        } else if (message instanceof AddMidicontroller) {
            midicontrollerSet.add(getSender());
            if (questionlist != null) {
                QuestionlistUpdated questionlistUpdated = new QuestionlistUpdated();
                getSender().tell(questionlistUpdated, getSelf());
            }
            Logger.info("Add midicontroller - (midicontrollers: " + midicontrollerSet.size() + ")");
        } else if (message instanceof RemoveMidicontroller) {
            midicontrollerSet.remove(getSender());
            Logger.info("Remove midicontroller - (midicontrollers: " + midicontrollerSet.size() + ")");
        } else if (message instanceof AddParticipant) {
            participantSet.add(getSender());
            if (questionlist != null) {
                getSender().tell(new QuestionlistUpdated(), getSelf());
            }
            Logger.info("Add participant - (participants: " + participantSet.size() + ")");
        } else if (message instanceof RemoveParticipant) {
            participantSet.remove(getSender());
            Logger.info("Remove participant - (participants: " + participantSet.size() + ")");
        }

        else if (message instanceof Questionlist){
            questionlist = (Questionlist)message;
            tellAll(new QuestionlistInitiated());
            tellAll(new QuestionlistUpdated());
            System.out.println("Setting questionlist...");
        }
        else if (message instanceof Start) {
            if (questionlist != null){
                getSelf().tell(questionlist, getSelf());
                System.out.println("Scheduling questionlist...");
                scheduler.scheduleQuestionlist(questionlist);
            }
        }
        else if (message instanceof AskQuestion) {
            System.out.println("Asking question...");
            AskQuestion askQuestion = (AskQuestion)message;
            askQuestion.question.setStatus(Question.StatusEnum.ACTIVE);

            tellAll(new QuestionlistUpdated());
        }

        else if (message instanceof SendResult) {
            SendResult sendResult = (SendResult) message;
            System.out.println("Sending Result...");
            sendResult.question.setStatus(Question.StatusEnum.POST);
            QuestionlistUpdated questionlistUpdated = new QuestionlistUpdated();
            tellMidicontrollers(new MidicontrollerActor.SendNote(sendResult.question.result.note));
//            If there were no reactions send the first answer as result
            if (sendResult.question.result == null){
                sendResult.question.addReaction(sendResult.question.answers.get(0));
            }
//            Send question to operators
            tellAll(questionlistUpdated);
        }
        else if (message instanceof SendEnd) {
            SendEnd sendEnd = (SendEnd) message;
            System.out.println("Sending End...");
            sendEnd.question.setStatus(Question.StatusEnum.ENDED);
            QuestionlistUpdated questionlistUpdated = new QuestionlistUpdated();
//            If there were no reactions send the first answer as result
            if (sendEnd.question.result == null){
                sendEnd.question.addReaction(sendEnd.question.answers.get(0));
            }
            tellAll(questionlistUpdated);
        }

        else if (message instanceof Reaction){
            Reaction reaction = (Reaction)message;
            Answer answer = questionlist.getAnswerRef(reaction.answer);
            if (answer != null) {
                answer.addReaction();
            }

            QuestionlistUpdated questionlistUpdated = new QuestionlistUpdated();
            if (questionlist.getQuestionRef(reaction.answer).allowMultipleReactions){
                tellAll(questionlistUpdated);
                tellMidicontrollers(new MidicontrollerActor.SendNote(answer.note));
            }
            else {
                tellPerformers(questionlistUpdated);
                getSender().tell(questionlistUpdated, getSelf());
            }
        }
    }

    public void tellAll(Object o){

        tellMidicontrollers(o);
        tellParticipants(o);
        tellPerformers(o);
    }
    public void tellMidicontrollers (Object o){
        for (ActorRef midicontroller : midicontrollerSet){
            midicontroller.tell(o, this.getSelf());
        }
    }
    public void tellParticipants (Object o){
        for (ActorRef participant : participantSet){
            participant.tell(o, this.getSelf());
        }

    }
    public void tellPerformers (Object o){
        for (ActorRef performer : performerSet){
            performer.tell(o, this.getSelf());
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

    public static class SendEnd {
        public Question question;
        public SendEnd(Question question) {
            this.question = question;
        }
    }

    public static class Reaction {
        final Answer answer;
        public Reaction(Answer answer) {
            this.answer = answer;
        }
    }

    public static class Reconnect {
        final WebSocket.Out<JsonNode> out;

        public Reconnect(WebSocket.Out<JsonNode> out) {
            this.out = out;
        }
    }

    public static class QuestionlistUpdated {

        public QuestionlistUpdated() {

        }


    }

    public static class QuestionlistInitiated {

    }
}
