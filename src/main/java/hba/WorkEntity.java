package hba;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Work", schema = "main", catalog = "")
@IdClass(WorkEntityPK.class)
public class WorkEntity {
    private Integer idWork;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String workStatus;
    private Integer idWorkKind;
    private Integer idBeehive;
    private Integer idBeegarden;
    private WorkKindEntity workKindByIdWorkKind;
    private BeehiveEntity beehive;

    @Id
    @Column(name = "idWork", nullable = false)
    public Integer getIdWork() {
        return idWork;
    }

    public void setIdWork(Integer idWork) {
        this.idWork = idWork;
    }

    @Basic
    @Column(name = "DateStart", nullable = false)
    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    @Basic
    @Column(name = "DateEnd", nullable = false)
    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Basic
    @Column(name = "WorkStatus", nullable = false, length = -1)
    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    @Id
    @Column(name = "idWorkKind", nullable = false, insertable = false, updatable = false)
    public Integer getIdWorkKind() {
        return idWorkKind;
    }

    public void setIdWorkKind(Integer idWorkKind) {
        this.idWorkKind = idWorkKind;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkEntity that = (WorkEntity) o;
        return Objects.equals(idWork, that.idWork) &&
                Objects.equals(idWorkKind, that.idWorkKind) &&
                Objects.equals(idBeehive, that.idBeehive) &&
                Objects.equals(idBeegarden, that.idBeegarden) &&
                Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(workStatus, that.workStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWork, dateStart, dateEnd, workStatus, idWorkKind, idBeehive, idBeegarden);
    }

    @ManyToOne
    @JoinColumn(name = "idWorkKind", referencedColumnName = "idWorkKind", nullable = false, insertable = false, updatable = false)
    public WorkKindEntity getWorkKindByIdWorkKind() {
        return workKindByIdWorkKind;
    }

    public void setWorkKindByIdWorkKind(WorkKindEntity workKindByIdWorkKind) {
        this.workKindByIdWorkKind = workKindByIdWorkKind;
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "idBeehive", referencedColumnName = "idBeehive"), @JoinColumn(name = "idBeegarden", referencedColumnName = "idBeegarden", nullable = true, insertable = true, updatable = true)})
    public BeehiveEntity getBeehive() {
        return beehive;
    }

    public void setBeehive(BeehiveEntity beehive) {
        this.beehive = beehive;
    }
}
