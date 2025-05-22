package actor.model.ator;

import actor.model.pedido.PedidoCriar;
import actor.model.pedido.PedidoFinalizar;
import actor.model.pedido.PedidoMessage;
import actor.model.pedido.PedidoPronto;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static actor.model.util.TimeUtil.getHorario;

public class Pizzaria extends AbstractBehavior<PedidoMessage> {

    private final Queue<PedidoCriar> fila = new LinkedList<>();
    private final List<ActorRef<PedidoMessage>> pizzaiolos;

    public static Behavior<PedidoMessage> create(int numPizzaiolos) {
        return Behaviors.setup(ctx -> new Pizzaria(ctx, numPizzaiolos));
    }

    private Pizzaria(ActorContext<PedidoMessage> context, int numPizzaiolos) {
        super(context);

        pizzaiolos = new java.util.ArrayList<>();
        for (int i = 0; i < numPizzaiolos; i++) {
            var pizzaiolo = context.spawn(Pizzaiolo.create(context.getSelf()), "pizzaiolo" + i);
            pizzaiolos.add(pizzaiolo);
        }
    }

    @Override
    public Receive<PedidoMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(PedidoCriar.class, this::onFazerPedido)
                .onMessage(PedidoPronto.class, this::onPedidoPronto)
                .build();
    }

    private Behavior<PedidoMessage> onFazerPedido(PedidoCriar pedido) {
        getContext().getLog().info("{}: 📦 Pedido recebido: {}", getHorario(), pedido.sabor());
        fila.offer(pedido);
        processarFila();
        return this;
    }

    private Behavior<PedidoMessage> onPedidoPronto(PedidoPronto msg) {
        msg.cliente().tell(new PedidoFinalizar("🍕 Sua pizza de " + msg.sabor() + " está pronta!"));
        processarFila();
        return this;
    }

    private void processarFila() {
        while (!fila.isEmpty()) {
            var pedido = fila.poll();
            var pizzaiolo = pizzaiolos.get((int)(Math.random() * pizzaiolos.size()));
            pizzaiolo.tell(pedido);
        }
    }
}
