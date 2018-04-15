package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Beehive", schema = "main", catalog = "")
@IdClass(BeehiveEntityPK.class)
public class BeehiveEntity {
    private short idBeehive;
    private String hiveNumber;
    private String hiveType;
    private String description;
    private short idBeegarden;
    private BeegardenEntity beegardenByIdBeegarden;
    private CountFrameEntity countFrame;
    private Collection<IncomeExpenseEntity> incomeExpenses;
    private Collection<WorkEntity> works;

    @Id
    @Column(name = "idBeehive", nullable = false)
    public short getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(short idBeehive) {
        this.idBeehive = idBeehive;
    }

    @Basic
    @Column(name = "HiveNumber", nullable = false, length = -1)
    public String getHiveNumber() {
        return hiveNumber;
    }

    public void setHiveNumber(String hiveNumber) {
        this.hiveNumber = hiveNumber;
    }

    @Basic
    @Column(name = "HiveType", nullable = false, length = -1)
    public String getHiveType() {
        return hiveType;
    }

    public void setHiveType(String hiveType) {
        this.hiveType = hiveType;
    }

    @Basic
    @Column(name = "Description", nullable = false, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @Column(name = "idBeegarden", nullable = false, insertable = false, updatable = false)
    public short getIdBeegarden() {
        return idBeegarden;
    }

    public void setIdBeegarden(short idBeegarden) {
        this.idBeegarden = idBeegarden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeehiveEntity that = (BeehiveEntity) o;
        return idBeehive == that.idBeehive &&
                idBeegarden == that.idBeegarden &&
                Objects.equals(hiveNumber, that.hiveNumber) &&
                Objects.equals(hiveType, that.hiveType) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBeehive, hiveNumber, hiveType, description, idBeegarden);
    }

    @ManyToOne
    @JoinColumn(name = "idBeegarden", referencedColumnName = "idBeegarden", nullable = false, insertable = false, updatable = false)
    public BeegardenEntity getBeegardenByIdBeegarden() {
        return beegardenByIdBeegarden;
    }

    public void setBeegardenByIdBeegarden(BeegardenEntity beegardenByIdBeegarden) {
        this.beegardenByIdBeegarden = beegardenByIdBeegarden;
    }

    @OneToOne(mappedBy = "beehive")
    public CountFrameEntity getCountFrame() {
        return countFrame;
    }

    public void setCountFrame(CountFrameEntity countFrame) {
        this.countFrame = countFrame;
    }

    @OneToMany(mappedBy = "beehive")
    public Collection<IncomeExpenseEntity> getIncomeExpenses() {
        return incomeExpenses;
    }

    public void setIncomeExpenses(Collection<IncomeExpenseEntity> incomeExpenses) {
        this.incomeExpenses = incomeExpenses;
    }

    @OneToMany(mappedBy = "beehive")
    public Collection<WorkEntity> getWorks() {
        return works;
    }

    public void setWorks(Collection<WorkEntity> works) {
        this.works = works;
    }
}
