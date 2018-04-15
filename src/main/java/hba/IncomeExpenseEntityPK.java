package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class IncomeExpenseEntityPK implements Serializable {
    private short idIncomeExpense;
    private short idResourseType;

    @Column(name = "idIncomeExpense", nullable = false)
    @Id
    public short getIdIncomeExpense() {
        return idIncomeExpense;
    }

    public void setIdIncomeExpense(short idIncomeExpense) {
        this.idIncomeExpense = idIncomeExpense;
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
        IncomeExpenseEntityPK that = (IncomeExpenseEntityPK) o;
        return idIncomeExpense == that.idIncomeExpense &&
                idResourseType == that.idResourseType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idIncomeExpense, idResourseType);
    }
}
