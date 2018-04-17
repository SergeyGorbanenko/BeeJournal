package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Beeman", schema = "main", catalog = "")
public class BeemanEntity {
    private Integer idBeeman;
    private String fio;
    private Collection<BeegardenEntity> beegardensByIdBeeman;

    @Id
    @Column(name = "idBeeman", nullable = false)
    public Integer getIdBeeman() {
        return idBeeman;
    }

    public void setIdBeeman(Integer idBeeman) {
        this.idBeeman = idBeeman;
    }

    @Basic
    @Column(name = "FIO", nullable = false, length = -1)
    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeemanEntity that = (BeemanEntity) o;
        return Objects.equals(idBeeman, that.idBeeman) &&
                Objects.equals(fio, that.fio);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBeeman, fio);
    }

    @OneToMany(mappedBy = "beemanByIdBeeman")
    public Collection<BeegardenEntity> getBeegardensByIdBeeman() {
        return beegardensByIdBeeman;
    }

    public void setBeegardensByIdBeeman(Collection<BeegardenEntity> beegardensByIdBeeman) {
        this.beegardensByIdBeeman = beegardensByIdBeeman;
    }
}
