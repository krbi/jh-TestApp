package com.mycompany.mytestapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Temat.
 */
@Entity
@Table(name = "temat")
public class Temat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nazwa")
    private String nazwa;

    @ManyToOne
    @JsonIgnoreProperties("temats")
    private Program program;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Temat nazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Program getProgram() {
        return program;
    }

    public Temat program(Program program) {
        this.program = program;
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temat)) {
            return false;
        }
        return id != null && id.equals(((Temat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Temat{" +
            "id=" + getId() +
            ", nazwa='" + getNazwa() + "'" +
            "}";
    }
}
