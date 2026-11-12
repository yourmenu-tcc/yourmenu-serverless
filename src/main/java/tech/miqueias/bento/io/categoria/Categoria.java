package tech.miqueias.bento.io.categoria;

import jakarta.persistence.*;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(
    name = "categoria",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "restaurante_id"}, name = "uk_nome_restaurante")
    }
)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    protected Categoria() {}

    public Categoria(Restaurante restaurante, String nome, String descricao) {
        this.restaurante = restaurante;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
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

    public void atualizar(String nome, String descricao) {
        if (nome == null || nome.isBlank()) throw new DomainException("O nome da categoria é obrigatório.");
        this.nome = nome;
        this.descricao = descricao;
    }
}
