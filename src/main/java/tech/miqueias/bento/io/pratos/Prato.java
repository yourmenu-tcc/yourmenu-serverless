package tech.miqueias.bento.io.pratos;

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
import tech.miqueias.bento.io.categoria.Categoria;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"nome", "restaurante_id"}, name = "uk_prato_restaurante")
        })
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column
    private String imagemUrl;

    protected Prato() {}

    public Prato(
            String nome,
            String descricao,
            Categoria categoria,
            Restaurante restaurante,
            BigDecimal preco) {
        if (categoria == null
                || restaurante == null
                || !mesmoRestaurante(categoria.getRestaurante(), restaurante)) {
            throw new DomainException("A categoria do prato deve pertencer ao mesmo restaurante.");
        }
        if (preco == null || preco.signum() <= 0) {
            throw new DomainException("O preço do prato deve ser maior que zero.");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.restaurante = restaurante;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void atualizar(
            String nome, String descricao, Categoria categoria, BigDecimal preco, String imagemUrl) {
        if (nome == null || nome.isBlank() || descricao == null || descricao.isBlank()) {
            throw new DomainException("Nome e descrição do prato são obrigatórios.");
        }
        if (categoria == null || !mesmoRestaurante(categoria.getRestaurante(), restaurante)) {
            throw new DomainException("A categoria do prato deve pertencer ao mesmo restaurante.");
        }
        if (preco == null || preco.signum() <= 0) {
            throw new DomainException("O preço do prato deve ser maior que zero.");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.imagemUrl = imagemUrl;
    }

    private boolean mesmoRestaurante(Restaurante primeiro, Restaurante segundo) {
        return primeiro == segundo
                || (primeiro != null
                        && primeiro.getId() != null
                        && primeiro.getId().equals(segundo.getId()));
    }
}
