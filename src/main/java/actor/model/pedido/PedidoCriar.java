package actor.model.pedido;

import akka.actor.typed.ActorRef;

public class PedidoCriar implements PedidoMessage {

    public final String sabor;
    public final ActorRef<PedidoMessage> cliente;

    public PedidoCriar(String sabor, ActorRef<PedidoMessage> cliente) {
        this.sabor = sabor;
        this.cliente = cliente;
    }

}
