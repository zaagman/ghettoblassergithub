package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import models.Answer;
import models.Question;
import models.Questionlist;
import play.libs.Akka;

import java.util.HashMap;
import java.util.Map;

public class LiveVoteActor extends UntypedActor {

    public static ActorRef instance = Akka.system().actorOf(new Props(LiveVoteActor.class));

    private final Map<Integer, ActorRef> performerMap = new HashMap<Integer, ActorRef>();
    private final Map<Integer, ActorRef> participantMap = new HashMap<Integer, ActorRef>();
    private final Map<Integer, ActorRef> midicontrollerMap = new HashMap<Integer, ActorRef>();

    private Questionlist questionlist = null;

    Scheduler scheduler = new Scheduler(this.getSelf());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AddPerformer) {
            AddPerformer addPerformer = (AddPerformer)message;
            performerMap.put(addPerformer.id, getSender());
        } else if (message instanceof RemovePerformer) {
            RemovePerformer removePerformer = (RemovePerformer)message;
            performerMap.remove(removePerformer.id);
        } else if (message instanceof AddParticipant) {
            System.out.println("Added participant...");
            AddParticipant addParticipant = (AddParticipant)message;
            participantMap.put(addParticipant.id, getSender());
        } else if (message instanceof RemoveParticipant) {
            RemoveParticipant removeParticipant = (RemoveParticipant)message;
            participantMap.remove(removeParticipant.id);
        } else if (message instanceof AddMidicontroller) {
            AddMidicontroller addMidicontroller = (AddMidicontroller)message;
            midicontrollerMap.put(addMidicontroller.id, getSender());
        } else if (message instanceof RemoveMidicontroller) {
            RemoveMidicontroller removeMidicontroller = (RemoveMidicontroller)message;
            midicontrollerMap.remove(removeMidicontroller.id);
        }

        if (message instanceof Questionlist){
            questionlist = (Questionlist)message;
            System.out.println("Setting questionlist...");
//            System.out.println(questionlist);
            self().tell(new Start(), self());
        }
        if (message instanceof Start) {
            if (questionlist != null){
                System.out.println("Scheduling questionlist...");
//                System.out.println(questionlists.get(0));
                scheduler.scheduleQuestionlist(questionlist);
            }
        }
        if (message instanceof AskQuestion) {
            System.out.println("Asking question...");
//            System.out.println(((AskQuestion) message).question);
//            Send question to operators
            for (ActorRef performer : performerMap.values()){
                performer.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerMap.values()){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef participant : participantMap.values()){
                participant.tell(message, this.getSelf());
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
            for (ActorRef performer : performerMap.values()){
                performer.tell(message, this.getSelf());
            }
//            Send Question to midicontrollers
            for (ActorRef midicontroller : midicontrollerMap.values()){
                midicontroller.tell(message, this.getSelf());
            }
//            Send message to guests
            for (ActorRef participant : participantMap.values()){
                participant.tell(message, this.getSelf());
            }
        }
    }

    public static class AddPerformer {
        final Integer id;

        public AddPerformer(Integer id) {
            this.id = id;
        }
    }

    public static class RemovePerformer {
        final Integer id;

        public RemovePerformer(Integer id) {
            this.id = id;
        }
    }

    public static class AddParticipant {
        final Integer id;

        public AddParticipant(Integer id) {
            this.id = id;
        }
    }

    public static class RemoveParticipant {
        final Integer id;

        public RemoveParticipant(Integer id) {
            this.id = id;
        }
    }

    public static class AddMidicontroller {
        final Integer id;

        public AddMidicontroller(Integer id) {
            this.id = id;
        }
    }

    public static class RemoveMidicontroller {

        final Integer id;
        public RemoveMidicontroller(Integer id) {
            this.id = id;
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
