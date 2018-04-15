package hba;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CountFrameEntityPK implements Serializable {
    private short idBeehive;
    private short idBeegarden;

    @Column(name = "idBeehive", nullable = false)
    @Id
    public short getIdBeehive() {
        return idBeehive;
    }

    public void setIdBeehive(short idBeehive) {
        this.idBeehive = idBeehive;
    }

    @Column(name = "idBeegarden", nullable = false)
    @Id
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
        CountFrameEntityPK that = (CountFrameEntityPK) o;
        return idBeehive == that.idBeehive &&
                idBeegarden == that.idBeegarden;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBeehive, idBeegarden);
    }
}
