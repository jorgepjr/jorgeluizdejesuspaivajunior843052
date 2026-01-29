package com.musiccatalog.model;

import jakarta.persistence.*;

@Entity
public class Album {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, length = 150)
   private String nome;

   protected Album(){}

    public Album(String nome) {
        this.nome = nome;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}
}
