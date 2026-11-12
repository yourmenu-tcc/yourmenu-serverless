package tech.miqueias.bento.io.colaborador;

import jakarta.persistence.*;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.restaurante.StatusVinculoRestaurante;
import tech.miqueias.bento.io.usuario.Role;
import tech.miqueias.bento.io.usuario.Usuario;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(
    name = "usuario_restaurante",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_restaurante", columnNames = {"usuario_id", "restaurante_id"})
    }
)
public class UsuarioRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role papel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVinculoRestaurante status = StatusVinculoRestaurante.ATIVO;

    protected UsuarioRestaurante() {
    }

    public UsuarioRestaurante(Usuario usuario, Restaurante restaurante, Role papel, StatusVinculoRestaurante status) {
        if (usuario == null || restaurante == null || papel == null || papel == Role.ADMINISTRADOR) {
            throw new DomainException("O vínculo deve possuir usuário, restaurante e papel operacional válido.");
        }
        this.usuario = usuario;
        this.restaurante = restaurante;
        this.papel = papel;
        this.status = status != null ? status : StatusVinculoRestaurante.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Role getPapel() {
        return papel;
    }

    public StatusVinculoRestaurante getStatus() {
        return status;
    }

    public void atualizar(Role papel, StatusVinculoRestaurante status) {
        if (papel == null || papel == Role.ADMINISTRADOR || status == null) throw new DomainException("Papel e status do vínculo são obrigatórios.");
        this.papel = papel;
        this.status = status;
    }
}
