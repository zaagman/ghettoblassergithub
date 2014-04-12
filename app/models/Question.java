package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.ebean.Model;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by zaagman on 03/04/14.
 */
//@Entity
public class Question extends Model {

    @Id
    @GeneratedValue
    long id;
    public String questiontext;
    public int time;
    public int duration;
    @OneToMany (cascade = PERSIST)
    public ArrayList<Answer> answers = new ArrayList<Answer>();

//    Result is null until the question is answered
    public Answer result;

    public static Finder<Long,Question> find = new Finder(
            Long.class, Question.class
    );

    public boolean hasResult (){
        return result != null;
    }

    public String toString () {
        String string = new String();
        string = "question: " + questiontext + " time: " + time + " duration: " + duration + "\n";
        string = string + "Answers: \n";
        for (Answer answer : answers){
            string = string + answer;
        }
        if (this.hasResult()){
            string = string + "Result: \n";
            string = string + result + "\n";
        }

        return string;
    }

//    Returns true if reaction was in answerlist. Returns false if reaction was not found in actionlist
    public boolean addReaction (Answer reaction){
        Answer currentAnswer = null;
//        Get the reference to the right answer in answerlist and add a reaction
        for (Answer answer : answers){
            if (reaction.equals(answer)){
                currentAnswer = answer;
            }
        }

//      Check if reaction is in answers at all
        if (currentAnswer != null) {
            currentAnswer.addReaction();
//            if there is a previous result, compare its number of reactions to the currentAnswer given and set the highest one as result
            if (this.hasResult()){
                if (currentAnswer.numberOfReactions() > result.numberOfReactions()){
                    result = currentAnswer;
                }
            } else {
                result = currentAnswer;
            }
            return true;
        }
        else {
            return false;
        }

    }



    public JsonNode toJson () {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonQuestion = new ArrayNode(factory);

        ObjectNode jsonQuestiontext = new ObjectNode(factory);
        ObjectNode jsonTime = new ObjectNode(factory);
        ObjectNode jsonDuration = new ObjectNode(factory);

        jsonQuestiontext.put("questiontext", questiontext);
        jsonTime.put("time", time);
        jsonDuration.put("duration", duration);
        ArrayNode jsonAnswers = new ArrayNode(factory);
        for (Answer answer : answers){
            jsonAnswers.add(answer.asJson());
        }

        jsonQuestion.add(jsonQuestiontext);
        jsonQuestion.add(jsonTime);
        jsonQuestion.add(jsonDuration);
        jsonQuestion.add(jsonAnswers);

        if (this.hasResult()){
            ObjectNode jsonResult = new ObjectNode(factory);
            jsonResult.put("result", result.asJson());
            jsonQuestion.add(jsonResult);
        }

        return jsonQuestion;
    }
}
