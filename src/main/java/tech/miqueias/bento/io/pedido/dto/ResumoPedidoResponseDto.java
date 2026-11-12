package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RegisterForReflection
public class ResumoPedidoResponseDto {
    private final Long totalPedidos;
    private final Long totalPedidosAbertos;
    private final Long totalPedidosCancelados;
    private final Long totalPedidosConcluidos;
    private final Map<String, Long> totalPedidosPorStatus;

    public ResumoPedidoResponseDto(Long totalPedidos, Long totalPedidosAbertos, 
                                    Long totalPedidosCancelados, Long totalPedidosConcluidos, 
                                    Map<String, Long> totalPedidosPorStatus) {
        this.totalPedidos = totalPedidos;
        this.totalPedidosAbertos = totalPedidosAbertos;
        this.totalPedidosCancelados = totalPedidosCancelados;
        this.totalPedidosConcluidos = totalPedidosConcluidos;
        this.totalPedidosPorStatus = totalPedidosPorStatus;
    }

    public Long getTotalPedidos() {
        return totalPedidos;
    }

    public Long getTotalPedidosAbertos() {
        return totalPedidosAbertos;
    }

    public Long getTotalPedidosCancelados() {
        return totalPedidosCancelados;
    }

    public Long getTotalPedidosConcluidos() {
        return totalPedidosConcluidos;
    }

    public Map<String, Long> getTotalPedidosPorStatus() {
        return totalPedidosPorStatus;
    }
}