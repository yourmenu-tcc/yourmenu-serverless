package tech.miqueias.bento.io.pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import jakarta.persistence.*;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private StatusPedido status = StatusPedido.RASCUNHO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoEntrega tipoEntrega;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoProdutos = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal taxaEntrega = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco = BigDecimal.ZERO;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private PedidoCliente cliente;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private EnderecoPedido endereco;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    protected Pedido() { }

    public Pedido(Restaurante restaurante, PedidoCliente cliente, TipoEntrega tipoEntrega) {
        if (restaurante == null || cliente == null || tipoEntrega == null)
            throw new DomainException("Restaurante, cliente e tipo de entrega são obrigatórios.");
        this.restaurante = restaurante;
        this.cliente = cliente;
        cliente.vincularPedido(this);
        this.tipoEntrega = tipoEntrega;
        this.criadoEm = LocalDateTime.now(ZoneId.systemDefault());
    }
    public ItemPedido adicionarItem(Integer quantidade) {
        garantirEditavel();
        ItemPedido item = new ItemPedido(this, quantidade);
        itens.add(item);
        return item;
    }

    public void removerItem(ItemPedido item) {
        garantirEditavel();
        if (!itens.remove(item)) throw new DomainException("O item não pertence a este pedido.");
        recalcularTotais();
    }

    public void definirEndereco(EnderecoPedido endereco) {
        garantirEditavel();
        if (tipoEntrega != TipoEntrega.ENTREGA) throw new DomainException("Pedidos para retirada não possuem endereço de entrega.");
        if (endereco == null || !mesmoRestaurante(endereco.getZonaDeEntrega().getRestaurante(), restaurante)) throw new DomainException("A zona de entrega deve pertencer ao restaurante do pedido.");
        this.endereco = endereco; endereco.vincularPedido(this); recalcularTotais();
    }

    public void avancarPara(StatusPedido novoStatus) {
        if (novoStatus == null || novoStatus == status) throw new DomainException("Informe um novo status para o pedido.");
        if (novoStatus == StatusPedido.CANCELADO) { cancelar(); return; }
        if (!proximosStatus().contains(novoStatus)) throw new DomainException("Transição de status do pedido não permitida: " + status + " para " + novoStatus + ".");
        if (novoStatus == StatusPedido.RECEBIDO) validarParaRecebimento();
        status = novoStatus;
    }

    public void cancelar() {
        if (status == StatusPedido.ENTREGUE || status == StatusPedido.CANCELADO) throw new DomainException("Este pedido não pode mais ser cancelado.");
        status = StatusPedido.CANCELADO;
    }
    void garantirEditavel() {
        if (status != StatusPedido.RASCUNHO)
            throw new DomainException("Apenas pedidos em rascunho podem ser alterados.");
    }

    void recalcularTotais() {
        precoProdutos = itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        taxaEntrega = tipoEntrega == TipoEntrega.ENTREGA && endereco != null ? endereco.getZonaDeEntrega().getValor() : BigDecimal.ZERO;
        preco = precoProdutos.add(taxaEntrega).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarParaRecebimento() {
        if (itens.isEmpty()) throw new DomainException("Um pedido deve possuir pelo menos um item.");
        itens.forEach(ItemPedido::validarCompleto);
        if (tipoEntrega == TipoEntrega.ENTREGA && endereco == null) throw new DomainException("Pedidos para entrega exigem endereço.");
        recalcularTotais();
    }

    private EnumSet<StatusPedido> proximosStatus() {
        return switch (status) {
            case RASCUNHO -> EnumSet.of(StatusPedido.RECEBIDO);
            case RECEBIDO -> EnumSet.of(StatusPedido.EM_PREPARO);
            case EM_PREPARO -> EnumSet.of(StatusPedido.PRONTO);
            case PRONTO -> tipoEntrega == TipoEntrega.ENTREGA ? EnumSet.of(StatusPedido.SAIU_PARA_ENTREGA) : EnumSet.of(StatusPedido.ENTREGUE);
            case SAIU_PARA_ENTREGA -> EnumSet.of(StatusPedido.ENTREGUE);
            default -> EnumSet.noneOf(StatusPedido.class);
        };
    }

    public Long getId() {
        return id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public BigDecimal getPrecoProdutos() {
        return precoProdutos;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public PedidoCliente getCliente() {
        return cliente;
    }

    public EnderecoPedido getEndereco() {
        return endereco;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    private boolean mesmoRestaurante(Restaurante primeiro, Restaurante segundo) {
        return primeiro == segundo || (primeiro != null && primeiro.getId() != null && primeiro.getId().equals(segundo.getId()));
    }
}
