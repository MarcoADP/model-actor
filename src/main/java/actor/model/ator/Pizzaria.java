package actor.model.ator;

import actor.model.pedido.PedidoCriar;
import actor.model.pedido.PedidoFinalizar;
import actor.model.pedido.PedidoMessage;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Pizzaria extends AbstractBehavior<PedidoMessage> {

    public static Behavior<PedidoMessage> create() {
        return Behaviors.setup(Pizzaria::new);
    }

    public Pizzaria(ActorContext<PedidoMessage> context) {
        super(context);
    }

    @Override
    public Receive<PedidoMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(PedidoCriar.class, this::onFazerPedido)
                .build();
    }

    private Behavior<PedidoMessage> onFazerPedido(PedidoCriar pedido) {
        getContext().getLog().info("🍕 Recebido pedido: {}", pedido.sabor);
        pedido.cliente.tell(new PedidoFinalizar("Pizza de " + pedido.sabor + " pronta!"));
        return this;
    }
}
