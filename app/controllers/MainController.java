package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Answer;
import models.Question;
import models.Questionlist;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;


public class MainController extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render("Hello from Java"));
    }

    public static Result questions() {
        return ok(views.html.questions.render());
    }

    public static Result addQuestionlist() {
        JsonNode data = request().body().asJson();
        Questionlist questionlist = createQuestionlist(data);
        System.out.println(questionlist.toString());


        return ok(views.html.questions.render());
    }

    private static Questionlist createQuestionlist(JsonNode data) {
        Questionlist questionlist = new Questionlist();
        if (data.isArray()){

            for(final JsonNode questionNode : data){
                Question question = new Question();
                question.questiontext = questionNode.get("questiontext").asText();
                question.time = questionNode.get("time").asInt();
                question.duration = questionNode.get("duration").asInt();
                if(questionNode.get("answers").isArray()){
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
