package actor.model.pedido;

import akka.actor.typed.ActorRef;

public record PedidoPronto(String sabor, ActorRef<PedidoMessage> cliente) implements PedidoMessage {
}
