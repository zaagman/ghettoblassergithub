package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import play.libs.Akka;

/**
 * Created by zaagman on 05/04/14.
 */
public class GuestActor extends UntypedActor {

    ActorRef guestActor = Akka.system().actorOf(new Props(GuestActor.class));


    @Override
    public void onReceive(Object message) throws Exception {

    }
}
