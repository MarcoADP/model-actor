package actor.model.pedido;

public class PedidoFinalizar implements PedidoMessage {

    public final String mensagem;

    public PedidoFinalizar(String mensagem) {
        this.mensagem = mensagem;
    }

}
