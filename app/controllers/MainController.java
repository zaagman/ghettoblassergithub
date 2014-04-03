package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.api.libs.json.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;


public class MainController extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render("Hello from Java"));
    }

    public static Result questions() {
        return ok(views.html.questions.render());
    }

    public static Result addQuestionlist() {
        JsonNode data = request().body().asJson();
        System.out.println(data.toString());
        return ok(views.html.midi.render(""));
    }

}
