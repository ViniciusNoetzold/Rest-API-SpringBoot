package br.edu.atitus.api_example.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

@Entity
@Table(name = "tb_animal")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String especie;
    private String descricao;

    // Campo de cor para o mapa (Ex: #FF0000)
    private String color;

    // Mantemos o campo no banco por compatibilidade, mas não será usado
    // obrigatoriamente
    private String fotoPath;

    private boolean isAdotado = false;

    private double latitude;
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adotante_id")
    @JsonIgnore
    private UserEntity adotante;

    public AnimalEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public boolean isAdotado() {
        return isAdotado;
    }

    public void setAdotado(boolean adotado) {
        isAdotado = adotado;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public UserEntity getAdotante() {
        return adotante;
    }

    public void setAdotante(UserEntity adotante) {
        this.adotante = adotante;
    }
}