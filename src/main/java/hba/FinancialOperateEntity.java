package hba;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "FinancialOperate", schema = "main", catalog = "")
@IdClass(FinancialOperateEntityPK.class)
public class FinancialOperateEntity {
    private short idFinOperate;
    private LocalDate date;
    private short count;
    private String description;
    private double unitPrice;
    private Boolean operationType;
    private short idResourseType;
    private ResourceTypeEntity resourceTypeByIdResourseType;

    @Id
    @Column(name = "idFinOperate", nullable = false)
    public short getIdFinOperate() {
        return idFinOperate;
    }

    public void setIdFinOperate(short idFinOperate) {
        this.idFinOperate = idFinOperate;
    }

    @Basic
    @Column(name = "Date", nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Basic
    @Column(name = "Count", nullable = false)
    public short getCount() {
        return count;
    }

    public void setCount(short count) {
        this.count = count;
    }

    @Basic
    @Column(name = "Description", nullable = false, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "UnitPrice", nullable = false, precision = 0)
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Basic
    @Column(name = "OperationType", nullable = false)
    public Boolean getOperationType() {
        return operationType;
    }

    public void setOperationType(Boolean operationType) {
        this.operationType = operationType;
    }

    @Id
    @Column(name = "idResourseType", nullable = false, insertable = false, updatable = false)
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
        FinancialOperateEntity that = (FinancialOperateEntity) o;
        return idFinOperate == that.idFinOperate &&
                count == that.count &&
                Double.compare(that.unitPrice, unitPrice) == 0 &&
                idResourseType == that.idResourseType &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(operationType, that.operationType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idFinOperate, date, count, description, unitPrice, operationType, idResourseType);
    }

    @ManyToOne
    @JoinColumn(name = "idResourseType", referencedColumnName = "idResourseType", nullable = false, insertable = false, updatable = false)
    public ResourceTypeEntity getResourceTypeByIdResourseType() {
        return resourceTypeByIdResourseType;
    }

    public void setResourceTypeByIdResourseType(ResourceTypeEntity resourceTypeByIdResourseType) {
        this.resourceTypeByIdResourseType = resourceTypeByIdResourseType;
    }
}
