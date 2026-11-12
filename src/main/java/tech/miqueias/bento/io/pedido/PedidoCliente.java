package tech.miqueias.bento.io.pedido;

import jakarta.persistence.*;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(name = "pedido_cliente")
public class PedidoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Column(nullable = false, length = 100)
    private String primeiroNome;

    @Column(nullable = false, length = 100)
    private String ultimoNome;

    @Column(nullable = false, length = 30)
    private String telefone;

    protected PedidoCliente() { }
    public PedidoCliente(String primeiroNome, String ultimoNome, String telefone) {
        if (primeiroNome == null || primeiroNome.isBlank() || telefone == null || telefone.isBlank())
            throw new DomainException("Nome e telefone do cliente são obrigatórios.");
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome == null ? "" : ultimoNome;
        this.telefone = telefone;
    }

    void vincularPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public String getTelefone() {
        return telefone;
    }
}
