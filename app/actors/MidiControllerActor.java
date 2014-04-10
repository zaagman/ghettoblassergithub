package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import play.libs.Akka;

/**
 * Created by zaagman on 09/04/14.
 */
public class MidicontrollerActor extends UntypedActor {


    ActorRef midicontrollerActor = Akka.system().actorOf(new Props(MidicontrollerActor.class));

    @Override
    public void onReceive(Object message) throws Exception {

    }
}
