package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zaagman on 03/04/14.
 */
@Entity
public class Questionlist extends Model {

    @Id
    public long id;
    public String user;
    public List<Question> questions;

    public String toString () {
        String result = new String();
        for (Question question : questions){
            result = "question: " + question.questiontext + " time: " + question.time + " duration: " + question.duration + "\n";
            for (Answer answer : question.answers){
                result = result + "answer: " + answer.answertext + " note: " + answer.note + "\n";
            }
        }
        return result;
    }
}
