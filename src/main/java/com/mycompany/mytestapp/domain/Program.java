package com.mycompany.mytestapp.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "opis")
    private String opis;

    @OneToMany(mappedBy = "program")
    private Set<Temat> temats = new HashSet<>();

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

    public Program nazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public Program opis(String opis) {
        this.opis = opis;
        return this;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Set<Temat> getTemats() {
        return temats;
    }

    public Program temats(Set<Temat> temats) {
        this.temats = temats;
        return this;
    }

    public Program addTemat(Temat temat) {
        this.temats.add(temat);
        temat.setProgram(this);
        return this;
    }

    public Program removeTemat(Temat temat) {
        this.temats.remove(temat);
        temat.setProgram(null);
        return this;
    }

    public void setTemats(Set<Temat> temats) {
        this.temats = temats;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Program)) {
            return false;
        }
        return id != null && id.equals(((Program) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", nazwa='" + getNazwa() + "'" +
            ", opis='" + getOpis() + "'" +
            "}";
    }
}
