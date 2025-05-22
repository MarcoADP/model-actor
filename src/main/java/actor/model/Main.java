package actor.model;

import actor.model.ator.Cliente;
import actor.model.ator.Pizzaria;
import actor.model.pedido.PedidoMessage;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ActorSystem<PedidoMessage> sistema = ActorSystem.create(
                Behaviors.setup(context -> {

                    ActorRef<PedidoMessage> pizzaria = context.spawn(Pizzaria.create(2), "pizzaria");

                    List<String> sabores = List.of("calabresa", "marguerita", "portuguesa", "frango", "quatro queijos", "napolitana");

                    for (int i = 0; i < sabores.size(); i++) {
                        context.spawn(Cliente.create(pizzaria, sabores.get(i)), "cliente" + i);
                    }

                    return Behaviors.empty();
                }),
                "SistemaPizzaria"
        );
    }
}