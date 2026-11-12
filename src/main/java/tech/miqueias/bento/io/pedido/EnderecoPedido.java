package tech.miqueias.bento.io.pedido;

import jakarta.persistence.*;
import tech.miqueias.bento.io.entrega.ZonaDeEntrega;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(name = "endereco_pedido")
public class EnderecoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zona_de_entrega_id", nullable = false)
    private ZonaDeEntrega zonaDeEntrega;

    @Column(nullable = false, length = 10)
    private String cep;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 150)
    private String rua;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false, length = 100)
    private String bairro;

    private String complemento;
    private String referencia;

    protected EnderecoPedido() { }

    public EnderecoPedido(ZonaDeEntrega zonaDeEntrega, String cep, String cidade, String rua, String numero, String bairro, String complemento, String referencia) {
        if (zonaDeEntrega == null || emBranco(cep) || emBranco(cidade) || emBranco(rua) || emBranco(numero) || emBranco(bairro))
            throw new DomainException("Os campos obrigatórios do endereço devem ser informados.");

        this.zonaDeEntrega = zonaDeEntrega;
        this.cep = cep;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.complemento = complemento;
        this.referencia = referencia;
    }

    void vincularPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    private boolean emBranco(String valor) {
        return valor == null || valor.isBlank();
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public ZonaDeEntrega getZonaDeEntrega() {
        return zonaDeEntrega;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getReferencia() {
        return referencia;
    }
}
