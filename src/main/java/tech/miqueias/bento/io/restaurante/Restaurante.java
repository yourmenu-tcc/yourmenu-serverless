package tech.miqueias.bento.io.restaurante;

import jakarta.persistence.*;
import tech.miqueias.bento.io.usuario.Usuario;

import java.time.LocalTime;

@Entity
@Table(name = "restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome")
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private boolean aberto = false;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private LocalTime horaAbertura;

    @Column(nullable = false)
    private LocalTime horaFechamento;

    @Column(nullable = false)
    private Integer tempoDeEntrega;

    @Column(nullable = false)
    private String logoUrl;

    @Column(nullable = false)
    private String bannerUrl;

    @Column(nullable = false, unique = true)
    private String acessoUrl;

    protected Restaurante() {
    }

    public Restaurante(String nome, String descricao, String endereco, LocalTime horaAbertura, LocalTime horaFechamento, Integer tempoDeEntrega, String logoUrl, String bannerUrl, String acessoUrl) {
        this.nome = nome;
        this.descricao = descricao;
        this.endereco = endereco;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.tempoDeEntrega = tempoDeEntrega;
        this.logoUrl = logoUrl;
        this.bannerUrl = bannerUrl;
        this.acessoUrl = acessoUrl;
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

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Integer getTempoDeEntrega() {
        return tempoDeEntrega;
    }

    public void setTempoDeEntrega(Integer tempoDeEntrega) {
        this.tempoDeEntrega = tempoDeEntrega;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getAcessoUrl() {
        return acessoUrl;
    }

    public void setAcessoUrl(String acessoUrl) {
        this.acessoUrl = acessoUrl;
    }
}
