package hba;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class FinancialOperateEntityPK implements Serializable {
    private Integer idFinOperate;
    private Integer idResourseType;

    @Column(name = "idFinOperate", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getIdFinOperate() {
        return idFinOperate;
    }

    public void setIdFinOperate(Integer idFinOperate) {
        this.idFinOperate = idFinOperate;
    }

    @Column(name = "idResourseType", nullable = false)
    @Id
    public Integer getIdResourseType() {
        return idResourseType;
    }

    public void setIdResourseType(Integer idResourseType) {
        this.idResourseType = idResourseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialOperateEntityPK that = (FinancialOperateEntityPK) o;
        return Objects.equals(idFinOperate, that.idFinOperate) &&
                Objects.equals(idResourseType, that.idResourseType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idFinOperate, idResourseType);
    }
}
