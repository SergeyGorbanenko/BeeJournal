package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class WorkEntityPK implements Serializable {
    private short idWork;
    private short idWorkKind;

    @Column(name = "idWork", nullable = false)
    @Id
    public short getIdWork() {
        return idWork;
    }

    public void setIdWork(short idWork) {
        this.idWork = idWork;
    }

    @Column(name = "idWorkKind", nullable = false)
    @Id
    public short getIdWorkKind() {
        return idWorkKind;
    }

    public void setIdWorkKind(short idWorkKind) {
        this.idWorkKind = idWorkKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkEntityPK that = (WorkEntityPK) o;
        return idWork == that.idWork &&
                idWorkKind == that.idWorkKind;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWork, idWorkKind);
    }
}
