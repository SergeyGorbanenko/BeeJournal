package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Beehive", schema = "main", catalog = "")
@IdClass(BeehiveEntityPK.class)
public class BeehiveEntity {
    private Integer idBeehive;
    private String hiveNumber;
    private String hiveType;
    private String description;
    private Integer idBeegarden;
    private BeegardenEntity beegardenByIdBeegarden;
    private Collection<IncomeExpenseEntity> incomeExpenses;
    private Collection<WorkEntity> works;
    private Collection<CountFrameEntity> countFrames;

    @Id
    @Column(name = "idBeehive", nullable = false, insertable = false, updatable = false)
    public Integer getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(Integer idBeehive) {
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
    public Integer getIdBeegarden() {
        return idBeegarden;
    }

    public void setIdBeegarden(Integer idBeegarden) {
        this.idBeegarden = idBeegarden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeehiveEntity that = (BeehiveEntity) o;
        return Objects.equals(idBeehive, that.idBeehive) &&
                Objects.equals(idBeegarden, that.idBeegarden) &&
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

/*    @OneToOne(mappedBy = "beehive")
    public CountFrameEntity getCountFrame() {
        return countFrame;
    }

    public void setCountFrame(CountFrameEntity countFrame) {
        this.countFrame = countFrame;
    }*/

    public void setBeegardenByIdBeegarden(BeegardenEntity beegardenByIdBeegarden) {
        this.beegardenByIdBeegarden = beegardenByIdBeegarden;
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

    @OneToMany(mappedBy = "beehive")
    public Collection<CountFrameEntity> getCountFrames() {
        return countFrames;
    }

    public void setCountFrames(Collection<CountFrameEntity> countFrames) {
        this.countFrames = countFrames;
    }
}
