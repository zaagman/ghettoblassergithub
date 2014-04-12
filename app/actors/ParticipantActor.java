package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.WebSocket;

public class ParticipantActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    public ParticipantActor(WebSocket.Out<JsonNode> out, Integer id) {
        this.out = out;

        ObjectNode stockUpdateMessage = Json.newObject();
        stockUpdateMessage.put("type", "stockupdate");
        out.write(stockUpdateMessage);

//            getSender().tell(new ParticipantActor.StockUpdate(), getSelf());

        System.out.println("creating actor...");
        LiveVoteActor.instance.tell(new LiveVoteActor.AddParticipant(id), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof StockUpdate){
            ObjectNode stockUpdateMessage = Json.newObject();
            stockUpdateMessage.put("type", "stockupdate");
            out.write(stockUpdateMessage);

        }

    }

    public static class StockUpdate {

    }
}
