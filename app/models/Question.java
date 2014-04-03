package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;

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

    public static Finder<Long,Question> find = new Finder(
            Long.class, Question.class
    );

    public String toString () {
        String result = new String();
        result = "question: " + questiontext + " time: " + time + " duration: " + duration + "\n";
        for (Answer answer : answers){
            result = result + answer;
        }

        return result;
    }


}
