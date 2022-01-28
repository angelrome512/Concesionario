package com.concesionarioquery.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.concesionarioquery.myapp.domain.Coche} entity.
 */
public class CocheDTO implements Serializable {

    private Long id;

    private String marca;

    private String modelo;

    private String color;

    private String numeroSerie;

    private Long precio;

    private Boolean expuesto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Boolean getExpuesto() {
        return expuesto;
    }

    public void setExpuesto(Boolean expuesto) {
        this.expuesto = expuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CocheDTO)) {
            return false;
        }

        CocheDTO cocheDTO = (CocheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cocheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CocheDTO{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", color='" + getColor() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", precio=" + getPrecio() +
            ", expuesto='" + getExpuesto() + "'" +
            "}";
    }
}
