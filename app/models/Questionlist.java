package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;

import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by zaagman on 03/04/14.
 */
@Entity
public class Questionlist extends Model {

    @Id
    @GeneratedValue
    public long id;
    public String user;
    @OneToMany (cascade = PERSIST)
    public ArrayList<Question> questions = new ArrayList<Question>();

    public String toString () {
        String result = new String();
        for (Question question : questions){
            result = result + "question: " + question;
        }
        return result;
    }

    public static Finder<Long,Questionlist> find = new Finder(
            Long.class, Questionlist.class
    );
}
