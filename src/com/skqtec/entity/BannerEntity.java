package com.skqtec.entity;

import javax.persistence.*;

@Entity
@Table(name = "BANNER", schema = "ketuanDB_test", catalog = "")
public class BannerEntity {
    private int id;
    private String imgUrl;
    private String groupId;
    private int canTab;
    private int state;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Basic
    @Column(name = "group_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "can_tab")
    public int getCanTab() {
        return canTab;
    }

    public void setCanTab(int canTab) {
        this.canTab = canTab;
    }

    @Basic
    @Column(name = "state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BannerEntity that = (BannerEntity) o;

        if (id != that.id) return false;
        if (canTab != that.canTab) return false;
        if (state != that.state) return false;
        if (imgUrl != null ? !imgUrl.equals(that.imgUrl) : that.imgUrl != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + canTab;
        result = 31 * result + state;
        return result;
    }
}
