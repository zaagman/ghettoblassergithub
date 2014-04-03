package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by zaagman on 31/03/14.
 */
public class MidiController extends Controller {

    public static Result midi() {
        return ok(views.html.midi.render("Hello from Controller"));
    }
}
