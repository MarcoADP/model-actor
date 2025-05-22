package actor.model.pedido;

import akka.actor.typed.ActorRef;

public record PedidoCriar(String sabor, ActorRef<PedidoMessage> cliente) implements PedidoMessage {

}
