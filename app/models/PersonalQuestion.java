package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * Created by zaagman on 27/04/14.
 */
public class PersonalQuestion {
    final public Question question;
    private Answer reaction = null;
    private boolean reacted = false;
    public ArrayList<Answer> reactions = new ArrayList<Answer>();


    public PersonalQuestion(Question question) {
        this.question = question;
        System.out.println("question made");
    }

    public Answer getReaction (){
        return reaction;
    }

    public boolean react(Answer reaction){
        if (this.reacted == false) {
            this.reacted = true;
            this.reaction = reaction;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasReacted () {
        return reacted;
    }

    public JsonNode toJson (){
        if (this.question.toJson() instanceof ObjectNode){
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode jsonQuestion = (ObjectNode)this.question.toJson();
            jsonQuestion.put("reacted", hasReacted());
            if(hasReacted()) {
                jsonQuestion.put("reaction", reaction.toJson());
            }
            return jsonQuestion;
        }
        else return null;
    }



}
