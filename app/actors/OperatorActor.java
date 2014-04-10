package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Akka;
import akka.actor.UntypedActor;
import play.mvc.WebSocket;
import actors.SystemActor.*;

/**
 * Created by zaagman on 09/04/14.
 */
public class OperatorActor extends UntypedActor {

    private ActorRef systemActor;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NewOperatorWs){
            systemActor = getSender();
            systemActor.tell(new SystemActor.NewOperatorActor(), this.getSelf());
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
