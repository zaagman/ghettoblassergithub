package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import models.Question;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

public class ParticipantActor extends UntypedActor {

    private WebSocket.Out<JsonNode> out;

    private Question currentQuestion;
    private Answer currentReaction;

    public ParticipantActor(WebSocket.Out<JsonNode> out, Integer id) {
        this.out = out;
        System.out.println("creating ParticipantActor...");
        LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(id), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof AskQuestion){
            AskQuestion askQuestion = (AskQuestion)message;
            currentQuestion = askQuestion.question;
            out.write(askQuestion.question.toJson());
        } else if (message instanceof Reaction){
            Reaction reaction = (Reaction) message;
            currentQuestion.addReaction(reaction.reaction);
            System.out.println("Reaction added...");
            LiveVoteActor.instance.tell(message, getSelf());
        } else if (message instanceof SetOut) {
            SetOut setOut = (SetOut) message;
            out = setOut.out;
        }

    }

}
