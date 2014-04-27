package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.PersonalQuestion;
import models.PersonalQuestionlist;
import models.Questionlist;
import play.mvc.WebSocket;
import actors.LiveVoteActor.*;

public class ParticipantActor extends UntypedActor {

    private WebSocket.Out<JsonNode> out;

    public ParticipantActor(WebSocket.Out<JsonNode> out) {
        this.out = out;
        System.out.println("creating ParticipantActor...");
        LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(), getSelf());
    }

    private PersonalQuestionlist personalQuestionlist;


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Reaction){
            Reaction reaction = (Reaction)message;
            PersonalQuestion q = personalQuestionlist.getPersonalQuestion(reaction.answer);
            if (q.question.allowMultipleReactions){
                q.reactions.add(reaction.answer);
                LiveVoteActor.instance.tell(message, getSelf());
            }
            else {
                if (q.hasReacted() == false) {
                    q.react(reaction.answer);
                    LiveVoteActor.instance.tell(message, getSelf());
                }
            }


        }
        else if (message instanceof Reconnect) {
            Reconnect reconnect = (Reconnect) message;
            out = reconnect.out;
            LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(), getSelf());

        }
        else if (message instanceof QuestionlistUpdated){
            if (personalQuestionlist != null) {
                out.write(personalQuestionlist.getActiveAndPost().toJson());
            }
        }
        else if (message instanceof QuestionlistInitiated) {
            this.personalQuestionlist = new PersonalQuestionlist(LiveVoteActor.questionlist);
            getSelf().tell(new QuestionlistUpdated(), getSender());
        }
    }
}
