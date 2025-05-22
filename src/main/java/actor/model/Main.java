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

                    // Cria pizzaria
                    ActorRef<PedidoMessage> pizzaria = context.spawn(Pizzaria.create(), "pizzaria");

                    // Lista de sabores para simular múltiplos pedidos/clientes
                    List<String> sabores = List.of("calabresa", "marguerita", "portuguesa", "frango com catupiry", "quatro queijos");

                    // Cria um cliente para cada pedido
                    for (int i = 0; i < sabores.size(); i++) {
                        String nomeCliente = "cliente" + (i + 1);
                        context.spawn(Cliente.create(pizzaria, sabores.get(i)), nomeCliente);
                    }

                    return Behaviors.empty();
                }),
                "SistemaPizzaria"
        );
    }
}