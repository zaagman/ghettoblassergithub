package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.Questionlist;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

public class PerformerActor extends UntypedActor {

    private WebSocket.Out<JsonNode> out;

    public PerformerActor(WebSocket.Out<JsonNode> out) {
        this.out = out;
        System.out.println("creating PerformerActor...");
        LiveVoteActor.instance.tell(new AddPerformer(), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {


        if (message instanceof Start){
            LiveVoteActor.instance.tell(message, getSelf());
        }
        else if (message instanceof Reconnect) {
            Reconnect reconnect = (Reconnect) message;
            out = reconnect.out;
            LiveVoteActor.instance.tell(new AddPerformer(), getSelf());
        }
        else if (message instanceof QuestionlistUpdated){
            if (LiveVoteActor.questionlist != null) {
                out.write(Questionlist.getActiveAndPost(LiveVoteActor.questionlist).toJson());
            }
        }
    }
}
