package tech.miqueias.bento.io.restaurante;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.auth.AuthService;
import tech.miqueias.bento.io.restaurante.dto.RestauranteRequestDto;
import tech.miqueias.bento.io.restaurante.dto.RestauranteResponseDto;
import tech.miqueias.bento.io.usuario.Usuario;
import tech.miqueias.bento.io.usuario.UsuarioRepository;
import tech.miqueias.bento.io.usuario.dto.UsuarioRequestDTO;
import tech.miqueias.bento.io.colaborador.UsuarioRestaurante;
import tech.miqueias.bento.io.colaborador.UsuarioRestauranteRepository;
import tech.miqueias.bento.io.usuario.Role;

import java.util.List;
import java.util.Optional;
import java.text.Normalizer;

@ApplicationScoped
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioRestauranteRepository usuarioRestauranteRepository;
    private final AuthService authService;

    @Inject
    public RestauranteService(RestauranteRepository restauranteRepository,
                              UsuarioRepository usuarioRepository,
                              UsuarioRestauranteRepository usuarioRestauranteRepository,
                              AuthService authService) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioRestauranteRepository = usuarioRestauranteRepository;
        this.authService = authService;
    }

    public List<RestauranteResponseDto> buscarTodos(Integer page, Integer size) {
        return restauranteRepository.listarTodos(page, size).stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<RestauranteResponseDto> buscarPorId(Long id) {
        return restauranteRepository.buscarPorId(id).map(this::toDTO);
    }

    @Transactional
    public RestauranteResponseDto criar(RestauranteRequestDto dto) {
        Usuario gerente = null;
        if (dto.gerenteId() != null) {
            gerente = usuarioRepository.buscarPorId(dto.gerenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Gerente informado não encontrado."));
        } else if (dto.gerenteEntidade() != null) {
            UsuarioRequestDTO uReq = dto.gerenteEntidade();
            var created = authService.criarUsuario(uReq);
            gerente = usuarioRepository.buscarPorId(created.id())
                    .orElseThrow(() -> new IllegalStateException("Usuário recém-criado não encontrado."));
        }

        Restaurante restaurante = new Restaurante(
                dto.nome(),
                dto.descricao(),
                dto.endereco(),
                dto.horaAbertura(),
                dto.horaFechamento(),
                dto.tempoDeEntrega(),
                dto.logoUrl(),
                dto.bannerUrl(),
                gerarAcessoUrl(dto.nome())
        );

        if (dto.aberto() != null) {
            restaurante.setAberto(dto.aberto());
        }

        restauranteRepository.salvar(restaurante);

        if (gerente != null) {
            UsuarioRestaurante vinculo = new UsuarioRestaurante(gerente, restaurante, Role.GERENTE, StatusVinculoRestaurante.ATIVO);
            usuarioRestauranteRepository.persist(vinculo);
        }

        return toDTO(restaurante);
    }

    @Transactional
    public RestauranteResponseDto atualizar(Long id, RestauranteRequestDto dto) {
        Restaurante restaurante = restauranteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado."));

        if (dto.nome() != null) restaurante.setNome(dto.nome());
        if (dto.descricao() != null) restaurante.setDescricao(dto.descricao());
        if (dto.endereco() != null) restaurante.setEndereco(dto.endereco());
        if (dto.horaAbertura() != null) restaurante.setHoraAbertura(dto.horaAbertura());
        if (dto.horaFechamento() != null) restaurante.setHoraFechamento(dto.horaFechamento());
        if (dto.tempoDeEntrega() != null) restaurante.setTempoDeEntrega(dto.tempoDeEntrega());
        if (dto.logoUrl() != null) restaurante.setLogoUrl(dto.logoUrl());
        if (dto.bannerUrl() != null) restaurante.setBannerUrl(dto.bannerUrl());
        if (dto.aberto() != null) restaurante.setAberto(dto.aberto());

        return toDTO(restaurante);
    }

    @Transactional
    public void deletar(Long id) {
        restauranteRepository.deletarPorId(id);
    }

    private RestauranteResponseDto toDTO(Restaurante restaurante) {
        return new RestauranteResponseDto(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getDescricao(),
                restaurante.isAberto(),
                restaurante.getEndereco(),
                restaurante.getHoraAbertura(),
                restaurante.getHoraFechamento(),
                restaurante.getTempoDeEntrega(),
                restaurante.getLogoUrl(),
                restaurante.getBannerUrl(),
                restaurante.getAcessoUrl()
        );
    }

    private String gerarAcessoUrl(String nome) {
        String base = Normalizer.normalize(nome == null ? "restaurante" : nome, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) base = "restaurante";
        String candidato = base;
        int sufixo = 2;
        while (restauranteRepository.existePorAcessoUrl(candidato)) {
            candidato = base + "-" + sufixo++;
        }
        return candidato;
    }
}
