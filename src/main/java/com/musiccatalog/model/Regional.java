package com.musiccatalog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "regional")
public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "sinc_id")
    private String sincId;

    @Column(name = "ultima_sinc")
    private LocalDateTime ultimaSinc = LocalDateTime.now();

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    public LocalDateTime getCriadoEm() {return criadoEm;}

    public void setCriadoEm(LocalDateTime criadoEm) {this.criadoEm = criadoEm;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public boolean getAtivo() {return ativo;}

    public void setAtivo(boolean ativo) {this.ativo = ativo;}

    public String getSincId() {return sincId;}

    public void setSincId(String sincId) {this.sincId = sincId;}

    public LocalDateTime getUltimaSinc() {return ultimaSinc;}

    public void setUltimaSinc(LocalDateTime ultimaSinc) {this.ultimaSinc = ultimaSinc;}

    public LocalDateTime getAtualizadoEm() {return atualizadoEm;}

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {this.atualizadoEm = atualizadoEm;}

    public boolean isAtivo() {
        return true;
    }
}
