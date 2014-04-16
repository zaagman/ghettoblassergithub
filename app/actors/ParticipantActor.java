package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import models.Questionlist;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

import java.util.HashSet;
import java.util.Set;

public class ParticipantActor extends UntypedActor {

    private WebSocket.Out<JsonNode> out;

    public ParticipantActor(WebSocket.Out<JsonNode> out) {
        this.out = out;
        System.out.println("creating ParticipantActor...");
        LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(), getSelf());
    }

    private Questionlist ownQuestionlist;
    private Set<Reaction> ownReactions = new HashSet<Reaction>();


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Reaction){
            Reaction reaction = (Reaction) message;
            Answer answer = ownQuestionlist.getAnswerRef(reaction.answer);
            if (answer != null) {
                answer.addReaction();
                ownReactions.add(reaction);
                System.out.println("Reaction added...");
            }

            LiveVoteActor.instance.tell(message, getSelf());
        } else if (message instanceof Reconnect) {
            Reconnect reconnect = (Reconnect) message;
            out = reconnect.out;
            LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(), getSelf());
            getSelf().tell(new SendActiveAndPost(ownQuestionlist), getSelf());
        } else if (message instanceof SendActiveAndPost){
            SendActiveAndPost sendActiveAndPost = (SendActiveAndPost)message;
            ownQuestionlist = sendActiveAndPost.questionlist;
            out.write(sendActiveAndPost.questionlist.toJson());
        }


    }

}
