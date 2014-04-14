package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

/**
 * Created by zaagman on 09/04/14.
 */
public class PerformerActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    public PerformerActor(WebSocket.Out<JsonNode> out, Integer id) {
        this.out = out;
        System.out.println("creating PerformerActor...");
        LiveVoteActor.instance.tell(new AddPerformer(id), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof AskQuestion){
            AskQuestion askQuestion = (AskQuestion)message;
            Standing standing = new Standing(askQuestion.question);
            getSelf().tell(standing, getSelf());
        }
        if (message instanceof Standing){
            Standing reaction = (Standing) message;
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode jsonStanding = new ObjectNode(factory);
            jsonStanding.put("standing", reaction.standing.toJson());
            out.write(jsonStanding);
            System.out.println("Standing sent...");
        }
        if (message instanceof Start){
            LiveVoteActor.instance.tell(message, getSelf());
        }


    }
}
