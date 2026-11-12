package tech.miqueias.bento.io.shared.tenant_context;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.miqueias.bento.io.shared.exception.DomainException;
import tech.miqueias.bento.io.shared.exception.AcessoNegadoException;
import tech.miqueias.bento.io.usuario.Role;

@ApplicationScoped
public class AcessoRestauranteService {
    private final TenantContext tenantContext;

    @Inject
    public AcessoRestauranteService(TenantContext tenantContext) {
        this.tenantContext = tenantContext;
    }

    public void exigirAcesso(Long restauranteId, Role... papeisPermitidos) {
        if (tenantContext.getUsuario().isPresent()
                && tenantContext.getUsuario().get().possuiRole(Role.ADMINISTRADOR)) {
            return;
        }
        Long tenantId = tenantContext
                        .getRestauranteId()
                        .orElseThrow(() -> new AcessoNegadoException("Informe um restaurante válido para esta operação."));
        Role papel = tenantContext
                        .getPapel()
                        .orElseThrow(() -> new AcessoNegadoException("Usuário sem vínculo ativo com o restaurante."));
        
        if (!tenantId.equals(restauranteId)) {
            throw new AcessoNegadoException("Não é permitido acessar dados de outro restaurante.");
        }

        for (Role permitido : papeisPermitidos) {
            if (papel == permitido) {
                return;
            }
        }
        
        throw new AcessoNegadoException("Usuário sem permissão para esta operação.");
    }
}
