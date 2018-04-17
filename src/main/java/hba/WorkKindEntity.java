package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "WorkKind", schema = "main", catalog = "")
public class WorkKindEntity {
    private Integer idWorkKind;
    private String name;
    private String description;
    private Collection<WorkEntity> worksByIdWorkKind;

    @Id
    @Column(name = "idWorkKind", nullable = false)
    public Integer getIdWorkKind() {
        return idWorkKind;
    }

    public void setIdWorkKind(Integer idWorkKind) {
        this.idWorkKind = idWorkKind;
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
    @Column(name = "Description", nullable = false, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkKindEntity that = (WorkKindEntity) o;
        return Objects.equals(idWorkKind, that.idWorkKind) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWorkKind, name, description);
    }

    @OneToMany(mappedBy = "workKindByIdWorkKind")
    public Collection<WorkEntity> getWorksByIdWorkKind() {
        return worksByIdWorkKind;
    }

    public void setWorksByIdWorkKind(Collection<WorkEntity> worksByIdWorkKind) {
        this.worksByIdWorkKind = worksByIdWorkKind;
    }
}
