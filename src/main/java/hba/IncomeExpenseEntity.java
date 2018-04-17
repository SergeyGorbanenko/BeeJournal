package hba;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "IncomeExpense", schema = "main", catalog = "")
@IdClass(IncomeExpenseEntityPK.class)
public class IncomeExpenseEntity {
    private Integer idIncomeExpense;
    private LocalDate date;
    private Integer count;
    private String description;
    private Boolean operationType;
    private Integer idBeehive;
    private Integer idBeegarden;
    private Integer idResourseType;
    private BeehiveEntity beehive;
    private ResourceTypeEntity resourceTypeByIdResourseType;

    @Id
    @Column(name = "idIncomeExpense", nullable = false)
    public Integer getIdIncomeExpense() {
        return idIncomeExpense;
    }

    public void setIdIncomeExpense(Integer idIncomeExpense) {
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
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
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
    public Integer getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(Integer idBeehive) {
        this.idBeehive = idBeehive;
    }

    @Basic
    @Column(name = "idBeegarden", nullable = false, insertable = false, updatable = false)
    public Integer getIdBeegarden() {
        return idBeegarden;
    }

    public void setIdBeegarden(Integer idBeegarden) {
        this.idBeegarden = idBeegarden;
    }

    @Id
    @Column(name = "idResourseType", nullable = false, insertable = false, updatable = false)
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
        IncomeExpenseEntity that = (IncomeExpenseEntity) o;
        return Objects.equals(idIncomeExpense, that.idIncomeExpense) &&
                Objects.equals(count, that.count) &&
                Objects.equals(idBeegarden, that.idBeegarden) &&
                Objects.equals(idResourseType, that.idResourseType) &&
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
