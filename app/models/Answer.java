package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * Created by zaagman on 03/04/14.
 */
@Entity
public class Answer extends Model {

    @Id
    public long id;
    public String answertext;
    public int note;

    public static Finder<Long,Answer> find = new Finder(
            Long.class, Answer.class
    );
}
