package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "ResourceType", schema = "main", catalog = "")
public class ResourceTypeEntity {
    private short idResourseType;
    private String name;
    private String category;
    private String description;
    private String measure;
    private Collection<FinancialOperateEntity> financialOperatesByIdResourseType;
    private Collection<IncomeExpenseEntity> incomeExpensesByIdResourseType;

    @Id
    @Column(name = "idResourseType", nullable = false)
    public short getIdResourseType() {
        return idResourseType;
    }

    public void setIdResourseType(short idResourseType) {
        this.idResourseType = idResourseType;
    }

    @Basic
    @Column(name = "Name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Category", nullable = false, length = -1)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
    @Column(name = "Measure", nullable = false, length = -1)
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceTypeEntity that = (ResourceTypeEntity) o;
        return idResourseType == that.idResourseType &&
                Objects.equals(name, that.name) &&
                Objects.equals(category, that.category) &&
                Objects.equals(description, that.description) &&
                Objects.equals(measure, that.measure);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idResourseType, name, category, description, measure);
    }

    @OneToMany(mappedBy = "resourceTypeByIdResourseType")
    public Collection<FinancialOperateEntity> getFinancialOperatesByIdResourseType() {
        return financialOperatesByIdResourseType;
    }

    public void setFinancialOperatesByIdResourseType(Collection<FinancialOperateEntity> financialOperatesByIdResourseType) {
        this.financialOperatesByIdResourseType = financialOperatesByIdResourseType;
    }

    @OneToMany(mappedBy = "resourceTypeByIdResourseType")
    public Collection<IncomeExpenseEntity> getIncomeExpensesByIdResourseType() {
        return incomeExpensesByIdResourseType;
    }

    public void setIncomeExpensesByIdResourseType(Collection<IncomeExpenseEntity> incomeExpensesByIdResourseType) {
        this.incomeExpensesByIdResourseType = incomeExpensesByIdResourseType;
    }
}
