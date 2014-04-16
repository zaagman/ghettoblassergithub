package models;

//import javax.persistence.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import play.db.ebean.*;

/**
 * Created by zaagman on 03/04/14.
 */
//@Entity
public class Answer /*extends Model*/ {

//    @Id
//    @GeneratedValue
//    public long id;
    private static int idCounter;
    public Integer id;
    public String answertext;
    public int note;
    private int reactionCounter = 0;

//    public static Finder<Long,Answer> find = new Finder(
//            Long.class, Answer.class
//    );

    public Answer () {
        this.id = idCounter++;
    }

    public String toString () {
        return "Answer "+ id + ": " + answertext +" reactions: " + reactionCounter  + " note: " + note + "\n";
    }

    public JsonNode toJson() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode jsonAnswer = new ObjectNode(factory);

        jsonAnswer.put("answertext", answertext);
        jsonAnswer.put("note", note);
        jsonAnswer.put("reactions", reactionCounter);
        jsonAnswer.put("id", id);


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
            if (this.id.equals(answer.id)){
                return true;
            }
        }
        return false;

    }
}
