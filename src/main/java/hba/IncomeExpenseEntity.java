package hba;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "IncomeExpense", schema = "main", catalog = "")
@IdClass(IncomeExpenseEntityPK.class)
public class IncomeExpenseEntity {
    private short idIncomeExpense;
    private LocalDate date;
    private short count;
    private String description;
    private Boolean operationType;
    private short idBeehive;
    private short idBeegarden;
    private short idResourseType;
    private BeehiveEntity beehive;
    private ResourceTypeEntity resourceTypeByIdResourseType;

    @Id
    @Column(name = "idIncomeExpense", nullable = false)
    public short getIdIncomeExpense() {
        return idIncomeExpense;
    }

    public void setIdIncomeExpense(short idIncomeExpense) {
        this.idIncomeExpense = idIncomeExpense;
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
    @Column(name = "OperationType", nullable = false)
    public Boolean getOperationType() {
        return operationType;
    }

    public void setOperationType(Boolean operationType) {
        this.operationType = operationType;
    }

    @Basic
    @Column(name = "idBeehive", nullable = true, insertable = false, updatable = false)
    public short getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(short idBeehive) {
        this.idBeehive = idBeehive;
    }

    @Basic
    @Column(name = "idBeegarden", nullable = false, insertable = false, updatable = false)
    public short getIdBeegarden() {
        return idBeegarden;
    }

    public void setIdBeegarden(short idBeegarden) {
        this.idBeegarden = idBeegarden;
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
        IncomeExpenseEntity that = (IncomeExpenseEntity) o;
        return idIncomeExpense == that.idIncomeExpense &&
                count == that.count &&
                idBeegarden == that.idBeegarden &&
                idResourseType == that.idResourseType &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(idBeehive, that.idBeehive);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idIncomeExpense, date, count, description, operationType, idBeehive, idBeegarden, idResourseType);
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "idBeehive", referencedColumnName = "idBeehive"), @JoinColumn(name = "idBeegarden", referencedColumnName = "idBeegarden", nullable = true, insertable = true, updatable = true)})
    public BeehiveEntity getBeehive() {
        return beehive;
    }

    public void setBeehive(BeehiveEntity beehive) {
        this.beehive = beehive;
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
