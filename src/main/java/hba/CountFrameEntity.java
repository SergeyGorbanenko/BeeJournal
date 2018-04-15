package hba;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "CountFrame", schema = "main", catalog = "")
@IdClass(CountFrameEntityPK.class)
public class CountFrameEntity {
    private String countFrame;
    private LocalDate checkDate;
    private short idBeehive;
    private short idBeegarden;
    private BeehiveEntity beehive;

    @Basic
    @Column(name = "CountFrame", nullable = false, length = -1)
    public String getCountFrame() {
        return countFrame;
    }

    public void setCountFrame(String countFrame) {
        this.countFrame = countFrame;
    }

    @Basic
    @Column(name = "CheckDate", nullable = false)
    public LocalDate getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    @Id
    @Column(name = "idBeehive", nullable = false, insertable = false, updatable = false)
    public short getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(short idBeehive) {
        this.idBeehive = idBeehive;
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
        CountFrameEntity that = (CountFrameEntity) o;
        return idBeehive == that.idBeehive &&
                idBeegarden == that.idBeegarden &&
                Objects.equals(countFrame, that.countFrame) &&
                Objects.equals(checkDate, that.checkDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(countFrame, checkDate, idBeehive, idBeegarden);
    }

    @OneToOne
    @JoinColumns({@JoinColumn(name = "idBeehive", referencedColumnName = "idBeehive", nullable = false), @JoinColumn(name = "idBeegarden", referencedColumnName = "idBeegarden", nullable = false, insertable = false, updatable = false)})
    public BeehiveEntity getBeehive() {
        return beehive;
    }

    public void setBeehive(BeehiveEntity beehive) {
        this.beehive = beehive;
    }
}
