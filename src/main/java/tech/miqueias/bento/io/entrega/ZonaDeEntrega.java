package tech.miqueias.bento.io.entrega;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(
        name = "zona_de_entrega",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_zona_restaurante",
                columnNames = {"localizacao", "restaurante_id"}
        )
)
public class ZonaDeEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false, length = 150)
    private String localizacao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    protected ZonaDeEntrega() {
    }

    public ZonaDeEntrega(Restaurante restaurante, String localizacao, BigDecimal valor) {
        if (restaurante == null || localizacao == null || localizacao.isBlank()) {
            throw new DomainException("Restaurante e localização da zona são obrigatórios.");
        }
        if (valor == null || valor.signum() < 0) {
            throw new DomainException("A taxa de entrega não pode ser negativa.");
        }

        this.restaurante = restaurante;
        this.localizacao = localizacao;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void alterarTaxa(BigDecimal valor) {
        if (valor == null || valor.signum() < 0) {
            throw new DomainException("A taxa de entrega não pode ser negativa.");
        }
        this.valor = valor;
    }

    public void atualizar(String localizacao, BigDecimal valor) {
        if (localizacao == null || localizacao.isBlank()) {
            throw new DomainException("A localização da zona é obrigatória.");
        }

        this.localizacao = localizacao;
        alterarTaxa(valor);
    }
}
