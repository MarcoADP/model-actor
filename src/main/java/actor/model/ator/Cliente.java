package actor.model.ator;

import actor.model.pedido.PedidoCriar;
import actor.model.pedido.PedidoFinalizar;
import actor.model.pedido.PedidoMessage;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import static actor.model.util.TimeUtil.getHorario;

public class Cliente extends AbstractBehavior<PedidoMessage> {

    private final ActorRef<PedidoMessage> pizzaria;
    private final String sabor;

    public static Behavior<PedidoMessage> create(ActorRef<PedidoMessage> pizzaria, String sabor) {
        return Behaviors.setup(context -> new Cliente(context, pizzaria, sabor));
    }

    private Cliente(ActorContext<PedidoMessage> context, ActorRef<PedidoMessage> pizzaria, String sabor) {
        super(context);
        this.pizzaria = pizzaria;
        this.sabor = sabor;

        // Faz o pedido ao iniciar
        pizzaria.tell(new PedidoCriar(sabor, context.getSelf()));
    }

    @Override
    public Receive<PedidoMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(PedidoFinalizar.class, this::onPedidoConcluido)
                .build();
    }

    private Behavior<PedidoMessage> onPedidoConcluido(PedidoFinalizar msg) {
        getContext().getLog().info("{}: 👨‍🍳 Cliente recebeu: {}", getHorario(), msg.mensagem());
        return this;
    }
}

