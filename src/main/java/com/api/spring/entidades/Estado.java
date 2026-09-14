package com.api.spring.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "estado")
@Getter
@Setter
public class Estado {

    @Id
    private Long id;
    private String nome;
    private String uf;
    private Integer ibge;

    @Override
    public String toString() {
        return "Estado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", uf='" + uf + '\'' +
                ", ibge=" + ibge +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Estado)) return false;
        Estado estado = (Estado) o;
        return Objects.equals(id, estado.id) && Objects.equals(nome, estado.nome) && Objects.equals(uf, estado.uf) && Objects.equals(ibge, estado.ibge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, uf, ibge);
    }
}
