package tech.miqueias.bento.io.shared.tenant_context;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class TenantResponseFilter implements ContainerResponseFilter {
    @Inject TenantContext tenantContext;

    @Override
    public void filter(
            ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        tenantContext.clear();
    }
}
