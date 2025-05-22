package actor.model;

import actor.model.ator.Cliente;
import actor.model.ator.Pizzaria;
import actor.model.pedido.PedidoMessage;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;

public class Main {
    public static void main(String[] args) {
        ActorSystem<PedidoMessage> sistema = ActorSystem.create(
                Behaviors.setup(context -> {
                    ActorRef<PedidoMessage> pizzaria = context.spawn(Pizzaria.create(), "pizzaria");
                    context.spawn(Cliente.create(pizzaria), "cliente");
                    return Behaviors.empty();
                }),
                "SistemaDePedidos"
        );
    }
}