package tech.miqueias.bento.io.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;
import tech.miqueias.bento.io.auth.dto.LoginRequest;
import tech.miqueias.bento.io.auth.dto.LoginResponse;
import tech.miqueias.bento.io.shared.exception.DomainException;
import tech.miqueias.bento.io.usuario.Usuario;
import tech.miqueias.bento.io.usuario.UsuarioRepository;
import tech.miqueias.bento.io.usuario.dto.UsuarioRequestDTO;
import tech.miqueias.bento.io.usuario.dto.UsuarioResponseDTO;

import java.time.Duration;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    @Inject
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existePorEmail(dto.email())) {
            throw new DomainException("Já existe um usuário com o email: " + dto.email());
        }

        Usuario usuario = new Usuario(dto.nome(), dto.email(), dto.senha(), dto.roles());
        usuario.setSenha(hashSenha(dto.senha()));
        usuarioRepository.salvar(usuario);

        return toResponseDTO(usuario);
    }

    public LoginResponse login(LoginRequest dto) {
        Usuario usuario = usuarioRepository.buscarPorEmail(dto.email())
                .orElseThrow(() -> new DomainException("Credenciais inválidas"));

        if (!verificarSenha(dto.senha(), usuario.getSenha())) {
            throw new DomainException("Credenciais inválidas");
        }

        long duracaoSegundos = 3600; // 1 hora
        String token = gerarTokenJwt(usuario, duracaoSegundos);

        return new LoginResponse(token, duracaoSegundos);
    }

    // ========== Métodos auxiliares ==========

    /**
     * 🔸 Geração do token JWT com SmallRye JWT Build:
     *    - Jwt.issuer("comandas-api") → define o emissor
     *    - .upn(email) → UserPrincipalName, identifica o usuário
     *    - .groups(roles) → claims de grupo/role, usados pelo @RolesAllowed
     *    - .expiresIn(Duration) → tempo de expiração
     */
    private String gerarTokenJwt(Usuario usuario, long duracaoSegundos) {
        return Jwt.issuer("comandas-api")
                .upn(usuario.getEmail())
            .groups(
                usuario.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet())
            )
                .claim("nome", usuario.getNome())
                .claim("userId", usuario.getId())
                .expiresIn(Duration.ofSeconds(duracaoSegundos))
                .sign();
    }

    /**
     * Hash da senha utilizando BCrypt via Elytron Security.
     * Quarkus (Elytron):
     * Usado o org.wildfly.security para BCrypt programático.
     */
    private String hashSenha(String senhaPura) {
        try {
            return org.wildfly.security.password.util.ModularCrypt.encodeAsString(
                    org.wildfly.security.password.PasswordFactory
                            .getInstance(BCryptPassword.ALGORITHM_BCRYPT)
                            .generatePassword(
                                    new org.wildfly.security.password.spec.EncryptablePasswordSpec(
                                            senhaPura.toCharArray(),
                                            new org.wildfly.security.password.spec.IteratedSaltedPasswordAlgorithmSpec(
                                                    10,
                                                    new java.security.SecureRandom().generateSeed(16)
                                            )
                                    )
                            )
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    private boolean verificarSenha(String senhaPura, String senhaHash) {
        try {
            Password password = ModularCrypt.decode(senhaHash);
            PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            return factory.verify(factory.translate(password), senhaPura.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar senha", e);
        }
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getRoles());
    }
}
