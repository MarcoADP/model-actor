package actor.model.ator;

import actor.model.pedido.PedidoCriar;
import actor.model.pedido.PedidoMessage;
import actor.model.pedido.PedidoPronto;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;
import java.util.Map;

import static actor.model.util.TimeUtil.getHorario;

public class Pizzaiolo extends AbstractBehavior<PedidoMessage> {

    private final ActorRef<PedidoMessage> pizzaria;
    private static final Map<String, Integer> TEMPO_PREPARO = Map.of(
            "calabresa", 3,
            "marguerita", 2,
            "portuguesa", 4,
            "frango", 5,
            "quatro queijos", 6,
            "napolitana", 3
    );

    public static Behavior<PedidoMessage> create(ActorRef<PedidoMessage> pizzaria) {
        return Behaviors.setup(ctx ->
                Behaviors.withTimers(timers ->
                        new Pizzaiolo(ctx, pizzaria, timers)
                )
        );
    }

    private final TimerScheduler<PedidoMessage> timers;
    private PedidoCriar pedidoAtual;

    private Pizzaiolo(ActorContext<PedidoMessage> ctx, ActorRef<PedidoMessage> pizzaria, TimerScheduler<PedidoMessage> timers) {
        super(ctx);
        this.pizzaria = pizzaria;
        this.timers = timers;
    }

    @Override
    public Receive<PedidoMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(PedidoCriar.class, this::onFazerPedido)
                .onMessage(PedidoPronto.class, this::onPedidoPronto)
                .build();
    }

    private Behavior<PedidoMessage> onFazerPedido(PedidoCriar pedido) {
        this.pedidoAtual = pedido;
        getContext().getLog().info("{}: 👨‍🍳 Preparando pizza de {}", getHorario(), pedido.sabor());

        int segundos = TEMPO_PREPARO.getOrDefault(pedido.sabor().toLowerCase(), 4);
        timers.startSingleTimer(new PedidoPronto(pedido.sabor(), pedido.cliente()), Duration.ofSeconds(segundos));
        getContext().getLog().info("{}: ⏳ Tempo estimado para {}: {}s", getHorario(), pedido.sabor(), segundos);
        return this;
    }

    private Behavior<PedidoMessage> onPedidoPronto(PedidoPronto msg) {
        getContext().getLog().info("{}: ✅ Pizza de {} pronta! Informando pizzaria.", getHorario(), msg.sabor());
        pizzaria.tell(msg);
        return this;
    }
}
