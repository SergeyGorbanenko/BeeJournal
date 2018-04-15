package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class FinancialOperateEntityPK implements Serializable {
    private short idFinOperate;
    private short idResourseType;

    @Column(name = "idFinOperate", nullable = false)
    @Id
    public short getIdFinOperate() {
        return idFinOperate;
    }

    public void setIdFinOperate(short idFinOperate) {
        this.idFinOperate = idFinOperate;
    }

    @Column(name = "idResourseType", nullable = false)
    @Id
    public short getIdResourseType() {
        return idResourseType;
    }

    public void setIdResourseType(short idResourseType) {
        this.idResourseType = idResourseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialOperateEntityPK that = (FinancialOperateEntityPK) o;
        return idFinOperate == that.idFinOperate &&
                idResourseType == that.idResourseType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idFinOperate, idResourseType);
    }
}
