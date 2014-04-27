package models;

import actors.LiveVoteActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * Created by zaagman on 27/04/14.
 */
public class PersonalQuestionlist {

    public ArrayList<PersonalQuestion> questions = new ArrayList<PersonalQuestion>();

    public PersonalQuestionlist(Questionlist questionlist){
        for (Question question : questionlist.questions){
           this.questions.add(new PersonalQuestion(question));
        }
    }

    public PersonalQuestionlist (){

    }

    public JsonNode toJson () {

        JsonNodeFactory factory = JsonNodeFactory.instance;

        ArrayNode jsonPersonalQuestions = new ArrayNode(factory);
        for (PersonalQuestion personalQuestion : questions){
            jsonPersonalQuestions.add(personalQuestion.toJson());
        }

        ObjectNode jsonPersonalQuestionlist = new ObjectNode(factory);
        jsonPersonalQuestionlist.put("questions", jsonPersonalQuestions);
        return jsonPersonalQuestionlist;
    }

    public PersonalQuestionlist getActiveAndPost(){
        PersonalQuestionlist list = new PersonalQuestionlist();
        for(PersonalQuestion personalQuestion : questions){
            if (personalQuestion.question.getStatus().equals(Question.StatusEnum.ACTIVE) || personalQuestion.question.getStatus().equals(Question.StatusEnum.POST)){
                list.questions.add(personalQuestion);

            }
        }
        return list;
    }


    public PersonalQuestion getPersonalQuestion(Answer answer){
        PersonalQuestion personalQuestion = null;
        for (PersonalQuestion q : questions){
            for (Answer a : q.question.answers){
                if (a.equals(answer)){
                    personalQuestion = q;
                }
            }
        }
        return personalQuestion;
    }



//    public Answer getAnswer(Answer answerData) {
//        Answer answerRef = null;
//        for (PersonalQuestion personalQuestion : questions){
//            for (Answer answer : personalQuestion.question.answers){
//                if (answer.equals(answerData)){
//                    answerRef = answer;
//                }
//            }
//        }
//        return answerRef;
//    }
}
