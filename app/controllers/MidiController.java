package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class MidiController extends Controller {

    public static Result midi() {
        return ok(views.html.midi.render("Hello from Controller"));
    }
}
