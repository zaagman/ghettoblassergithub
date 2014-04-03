package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Result;

public class MainController extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render("Hello from Java"));
    }

    public static Result questions() {
        return ok(views.html.questions.render());
    }

    public static Result addQuestionlist(Object questionlist) {
         System.out.println(questionlist.getClass());
         return ok(views.html.questions.render());
    }

}
