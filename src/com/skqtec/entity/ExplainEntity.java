package com.skqtec.entity;

import javax.persistence.*;

@Entity
@Table(name = "EXPLAIN", schema = "ketuanDB_test", catalog = "")
public class ExplainEntity {
    private int id;
    private String explainKey;
    private String explainInfo;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "explain_key")
    public String getExplainKey() {
        return explainKey;
    }

    public void setExplainKey(String explainKey) {
        this.explainKey = explainKey;
    }

    @Basic
    @Column(name = "explain_info")
    public String getExplainInfo() {
        return explainInfo;
    }

    public void setExplainInfo(String explainInfo) {
        this.explainInfo = explainInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplainEntity that = (ExplainEntity) o;

        if (id != that.id) return false;
        if (explainKey != null ? !explainKey.equals(that.explainKey) : that.explainKey != null) return false;
        if (explainInfo != null ? !explainInfo.equals(that.explainInfo) : that.explainInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (explainKey != null ? explainKey.hashCode() : 0);
        result = 31 * result + (explainInfo != null ? explainInfo.hashCode() : 0);
        return result;
    }
}
