package hba;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class IncomeExpenseEntityPK implements Serializable {
    private Integer idIncomeExpense;
    private Integer idResourseType;

    @Column(name = "idIncomeExpense", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getIdIncomeExpense() {
        return idIncomeExpense;
    }

    public void setIdIncomeExpense(Integer idIncomeExpense) {
        this.idIncomeExpense = idIncomeExpense;
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
        IncomeExpenseEntityPK that = (IncomeExpenseEntityPK) o;
        return Objects.equals(idIncomeExpense, that.idIncomeExpense) &&
                Objects.equals(idResourseType, that.idResourseType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idIncomeExpense, idResourseType);
    }
}
