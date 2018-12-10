package com.skqtec.entity;

import javax.persistence.*;

@Entity
@Table(name = "LABEL", schema = "ketuanDB_test", catalog = "")
public class LabelEntity {
    private int id;
    private String name;
    private Integer hotDegree;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "hot_degree")
    public Integer getHotDegree() {
        return hotDegree;
    }

    public void setHotDegree(Integer hotDegree) {
        this.hotDegree = hotDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelEntity that = (LabelEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (hotDegree != null ? !hotDegree.equals(that.hotDegree) : that.hotDegree != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (hotDegree != null ? hotDegree.hashCode() : 0);
        return result;
    }
}
