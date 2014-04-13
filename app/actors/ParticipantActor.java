package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Answer;
import models.Question;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

public class ParticipantActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    private Question currentQuestion;
    private Answer currentReaction;

    public ParticipantActor(WebSocket.Out<JsonNode> out, Integer id) {
        this.out = out;
        System.out.println("creating actor...");
        LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(id), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof AskQuestion){
            AskQuestion askQuestion = (AskQuestion)message;
            currentQuestion = askQuestion.question;
            out.write(askQuestion.question.toJson());
        }
        if (message instanceof ReceiveReaction){
            ReceiveReaction receiveReaction = (ReceiveReaction) message;
            currentQuestion.addReaction(receiveReaction.reaction);
            System.out.println("Reaction added");
        }

    }

    public static class ReceiveReaction {
        final Answer reaction;
        public ReceiveReaction(Answer reaction) {
            this.reaction = reaction;
        }
    }
}
