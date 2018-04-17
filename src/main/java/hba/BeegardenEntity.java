package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Beegarden", schema = "main", catalog = "")
public class BeegardenEntity {
    private Integer idBeegarden;
    private String name;
    private String address;
    private Integer idBeeman;
    private BeemanEntity beemanByIdBeeman;
    private Collection<BeehiveEntity> beehivesByIdBeegarden;

    @Id
    @Column(name = "idBeegarden", nullable = false)
    public Integer getIdBeegarden() {
        return idBeegarden;
    }

    public void setIdBeegarden(Integer idBeegarden) {
        this.idBeegarden = idBeegarden;
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
    @Column(name = "Address", nullable = false, length = -1)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "idBeeman", nullable = false, insertable = false, updatable = false)
    public Integer getIdBeeman() {
        return idBeeman;
    }

    public void setIdBeeman(Integer idBeeman) {
        this.idBeeman = idBeeman;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeegardenEntity that = (BeegardenEntity) o;
        return Objects.equals(idBeegarden, that.idBeegarden) &&
                Objects.equals(idBeeman, that.idBeeman) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBeegarden, name, address, idBeeman);
    }

    @ManyToOne
    @JoinColumn(name = "idBeeman", referencedColumnName = "idBeeman", nullable = false, insertable = false, updatable = false)
    public BeemanEntity getBeemanByIdBeeman() {
        return beemanByIdBeeman;
    }

    public void setBeemanByIdBeeman(BeemanEntity beemanByIdBeeman) {
        this.beemanByIdBeeman = beemanByIdBeeman;
    }

    @OneToMany(mappedBy = "beegardenByIdBeegarden")
    public Collection<BeehiveEntity> getBeehivesByIdBeegarden() {
        return beehivesByIdBeegarden;
    }

    public void setBeehivesByIdBeegarden(Collection<BeehiveEntity> beehivesByIdBeegarden) {
        this.beehivesByIdBeegarden = beehivesByIdBeegarden;
    }
}
