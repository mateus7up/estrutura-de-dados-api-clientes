package com.api.spring.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cidade")
@Getter
@Setter
public class Cidade {

    @Id
    private Long id;
    private String nome;
    @JoinColumn(name = "uf")
    @ManyToOne
    private Estado estado;
    private Integer ibge;

    @Override
    public String toString() {
        return "Cidade{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cidade)) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(id, cidade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
