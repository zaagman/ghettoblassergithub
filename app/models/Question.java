package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import play.db.ebean.Model;

//import javax.persistence.*;
import java.util.ArrayList;

//import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by zaagman on 03/04/14.
 */
//@Entity
public class Question /*extends Model*/ {

//    @Id
//    @GeneratedValue
//    long id;
    public String questiontext;
    public int time;
    public int duration;
    public int end = 20;
    public boolean allowMultipleReactions;

//    @OneToMany (cascade = PERSIST)
    public ArrayList<Answer> answers = new ArrayList<Answer>();

//    Result is null until the question is answered
    public Answer result;

    public enum StatusEnum {
        PRE, ACTIVE, POST, ENDED;
    }

    public StatusEnum status = StatusEnum.PRE;


//    public static Finder<Long,Question> find = new Finder(
//            Long.class, Question.class
//    );

    public boolean hasResult (){
        return result != null;
    }

    public String toString () {
        String string = new String();
        string = "question: " + questiontext + " time: " + time + " duration: " + duration + " end in: " + end + "\n" ;
        string = string + "Answers: \n";
        for (Answer answer : answers){
            string = string + answer;
        }

        if (this.hasResult()){
            string = string + "Result: \n";
            string = string + result + "\n";
        }
        string = string + "status: " + status.toString() + "\n";
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

        ObjectNode jsonQuestion = new ObjectNode(factory);


        jsonQuestion.put("questiontext", questiontext);
        jsonQuestion.put("time", time);
        jsonQuestion.put("duration", duration);
        jsonQuestion.put("end", end);
        jsonQuestion.put("allowMultipleReactions", allowMultipleReactions);
        jsonQuestion.put("status", status.toString());
        ArrayNode jsonAnswers = new ArrayNode(factory);
        for (Answer answer : answers){
            jsonAnswers.add(answer.toJson());
        }

        jsonQuestion.put("answers", jsonAnswers);

        if (this.hasResult()){
            jsonQuestion.put("result", result.toJson());
        }
        return jsonQuestion;
    }

    public void setStatus (StatusEnum status){
        this.status = status;
    }
}
