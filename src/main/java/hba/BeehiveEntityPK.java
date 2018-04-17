package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class BeehiveEntityPK implements Serializable {
    private Integer idBeehive;
    private Integer idBeegarden;

    @Column(name = "idBeehive", nullable = false)
    @Id
    public Integer getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(Integer idBeehive) {
        this.idBeehive = idBeehive;
    }

    @Column(name = "idBeegarden", nullable = false)
    @Id
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
        BeehiveEntityPK that = (BeehiveEntityPK) o;
        return Objects.equals(idBeehive, that.idBeehive) &&
                Objects.equals(idBeegarden, that.idBeegarden);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBeehive, idBeegarden);
    }
}
