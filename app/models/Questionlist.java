package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;

//import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by zaagman on 03/04/14.
 */
//@Entity
public class Questionlist /*extends Model*/ {

//    @Id
//    @GeneratedValue
//    public long id;
    public String user;
//    @OneToMany (cascade = PERSIST)
    public ArrayList<Question> questions = new ArrayList<Question>();

//    public static Finder<Long,Questionlist> find = new Finder(
//            Long.class, Questionlist.class
//    );

    public String toString () {
        String result = new String();
        for (Question question : questions){
            result = result + "question: " + question;
        }
        return result;
    }

    public JsonNode toJson () {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonQuestionlist = new ArrayNode(factory);
        ObjectNode jsonUser = new ObjectNode(factory);
        jsonUser.put("user", user);
        jsonQuestionlist.add(jsonUser);
        for (Question question : questions){
            jsonQuestionlist.add (question.toJson());
        }
        return jsonQuestionlist;
    }

    public static Questionlist newQuestionlistFromJson(JsonNode data) {
        Questionlist questionlist = new Questionlist();
        if (data.isArray()){
            for(final JsonNode questionNode : data){
                Question question = new Question();
                question.questiontext = questionNode.get("questiontext").asText();
                question.time = questionNode.get("time").asInt();
                question.duration = questionNode.get("duration").asInt();
                if(questionNode.has("answers")){
                    for (final JsonNode answerNode : questionNode.get("answers")){
                        Answer answer = new Answer();
                        answer.answertext = answerNode.get("answertext").asText();
                        answer.note = answerNode.get("note").asInt();
                        question.answers.add(answer);
                    }
                }
                questionlist.questions.add(question);
            }
        }

        return questionlist;
    }
}
