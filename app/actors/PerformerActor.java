package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

/**
 * Created by zaagman on 09/04/14.
 */
public class PerformerActor extends UntypedActor {



    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NewOperatorWs){
            LiveVoteActor.instance.tell(new AddPerformer(0), this.getSelf());
        }

        if (message instanceof SendResult){

        }
        if (message instanceof AskQuestion){

        }
    }

    public static class NewOperatorWs {
        public WebSocket.In<String> in;
        public WebSocket.Out<String> out;

        public NewOperatorWs(WebSocket.In<String> in, WebSocket.Out<String> out){
            this.in = in;
            this.out = out;
        }
    }
}
