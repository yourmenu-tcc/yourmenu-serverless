package tech.miqueias.bento.io.usuario;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senha, Set<Role> roles) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public void adicionarRole(Role role) {
        this.roles.add(role);
    }

    public boolean possuiRole(Role role) {
        return this.roles.contains(role);
    }

    public void setSenha(String senhaHash) {
        this.senha = senhaHash;
    }

    public void atualizar(String nome, String email, Set<Role> roles) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank() || roles == null || roles.isEmpty()) throw new DomainException("Nome, email e ao menos um papel são obrigatórios.");
        this.nome = nome;
        this.email = email;
        this.roles = new HashSet<>(roles);
    }
}
