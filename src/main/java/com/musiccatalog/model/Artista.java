package com.musiccatalog.model;

import com.musiccatalog.enums.TipoArtista;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoArtista tipo;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArtistaAlbum> albuns = new HashSet<>();


    protected Artista(){}

    public Artista(String nome, TipoArtista tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public Long getId() {return id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public TipoArtista getTipo() {return tipo;}

    public void setTipo(TipoArtista tipo) {this.tipo = tipo;}
}
