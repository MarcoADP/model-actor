package actor.model.ator;

import actor.model.pedido.PedidoCriar;
import actor.model.pedido.PedidoFinalizar;
import actor.model.pedido.PedidoMessage;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Cliente extends AbstractBehavior<PedidoMessage> {

    private final ActorRef<PedidoMessage> pizzaria;

    public static Behavior<PedidoMessage> create(ActorRef<PedidoMessage> pizzaria) {
        return Behaviors.setup(context -> new Cliente(context, pizzaria));
    }

    private Cliente(ActorContext<PedidoMessage> context, ActorRef<PedidoMessage> pizzaria) {
        super(context);
        this.pizzaria = pizzaria;

        // Faz o pedido ao iniciar
        pizzaria.tell(new PedidoCriar("calabresa", context.getSelf()));
    }

    @Override
    public Receive<PedidoMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(PedidoFinalizar.class, this::onPedidoConcluido)
                .build();
    }

    private Behavior<PedidoMessage> onPedidoConcluido(PedidoFinalizar msg) {
        getContext().getLog().info("👨‍🍳 Cliente recebeu: {}", msg.mensagem());
        return this;
    }
}

