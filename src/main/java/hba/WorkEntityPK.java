package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class WorkEntityPK implements Serializable {
    private Integer idWork;
    private Integer idWorkKind;

    @Column(name = "idWork", nullable = false)
    @Id
    public Integer getIdWork() {
        return idWork;
    }

    public void setIdWork(Integer idWork) {
        this.idWork = idWork;
    }

    @Column(name = "idWorkKind", nullable = false)
    @Id
    public Integer getIdWorkKind() {
        return idWorkKind;
    }

    public void setIdWorkKind(Integer idWorkKind) {
        this.idWorkKind = idWorkKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkEntityPK that = (WorkEntityPK) o;
        return Objects.equals(idWork, that.idWork) &&
                Objects.equals(idWorkKind, that.idWorkKind);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWork, idWorkKind);
    }
}
