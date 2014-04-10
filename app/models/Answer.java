package models;

import javax.persistence.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.ebean.*;

/**
 * Created by zaagman on 03/04/14.
 */
@Entity
public class Answer extends Model {

    @Id
    @GeneratedValue
    public long id;
    public String answertext;
    public int note;
    private int reactionCounter = 0;

    public static Finder<Long,Answer> find = new Finder(
            Long.class, Answer.class
    );

    public String toString () {

        if (reactionCounter > 0)
        {
            return reactionCounter + " reactions: " + answertext + " note: " + note + "\n";
        }
        else
        {
            return "answer: " + answertext + " note: " + note + "\n";
        }
    }

    public JsonNode asJson() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonAnswer = new ArrayNode(factory);

        ObjectNode jsonAnswertext = new ObjectNode(factory);
        ObjectNode jsonNote = new ObjectNode(factory);


        jsonAnswertext.put("answertext", answertext);
        jsonNote.put("note", note);

        jsonAnswer.add(jsonAnswertext);
        jsonAnswer.add(jsonNote);

        if (reactionCounter > 0){
            ObjectNode jsonReactionConter = new ObjectNode(factory);
            jsonReactionConter.put("reactionCounter", reactionCounter);
            jsonAnswer.add(jsonReactionConter);
        }

        return jsonAnswer;

    }

    public void addReaction () {
        reactionCounter += 1;
    }

    public int numberOfReactions(){
        return reactionCounter;
    }

    public boolean equals(Object obj){
        Answer answer = null;
        if (obj instanceof Answer){
            answer = (Answer)obj;
        }
        if (answer != null) {
            if (this.answertext.equals(answer.answertext)){
                return true;
            }
        }
        return false;

    }
}
