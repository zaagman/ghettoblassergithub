package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
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

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AddPerformer) {
            AddPerformer addPerformer = (AddPerformer)message;
            performerMap.put(addPerformer.id, getSender());
        } else if (message instanceof RemovePerformer) {
            RemovePerformer removePerformer = (RemovePerformer)message;
            performerMap.remove(removePerformer.id);
        } else if (message instanceof AddParticipant) {
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
}
