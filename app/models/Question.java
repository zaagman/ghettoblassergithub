package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zaagman on 03/04/14.
 */
@Entity
public class Question extends Model {

    @Id
    long id;
    public String questiontext;
    public int time;
    public int duration;
    public List<Answer> answers;

    public static Finder<Long,Question> find = new Finder(
            Long.class, Question.class
    );


}
