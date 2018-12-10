package com.skqtec.entity;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITY", schema = "ketuanDB_test", catalog = "")
public class AuthorityEntity {
    private String authorityId;
    private String adminName;
    private String adminCount;
    private String adminPass;
    private String adminAuthor;
    private String adminUserName;
    private String adminUserPhone;

    @Id
    @Column(name = "authority_id")
    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    @Basic
    @Column(name = "admin_name")
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Basic
    @Column(name = "admin_count")
    public String getAdminCount() {
        return adminCount;
    }

    public void setAdminCount(String adminCount) {
        this.adminCount = adminCount;
    }

    @Basic
    @Column(name = "admin_pass")
    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    @Basic
    @Column(name = "admin_author")
    public String getAdminAuthor() {
        return adminAuthor;
    }

    public void setAdminAuthor(String adminAuthor) {
        this.adminAuthor = adminAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorityEntity that = (AuthorityEntity) o;

        if (authorityId != null ? !authorityId.equals(that.authorityId) : that.authorityId != null) return false;
        if (adminName != null ? !adminName.equals(that.adminName) : that.adminName != null) return false;
        if (adminCount != null ? !adminCount.equals(that.adminCount) : that.adminCount != null) return false;
        if (adminPass != null ? !adminPass.equals(that.adminPass) : that.adminPass != null) return false;
        if (adminAuthor != null ? !adminAuthor.equals(that.adminAuthor) : that.adminAuthor != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authorityId != null ? authorityId.hashCode() : 0;
        result = 31 * result + (adminName != null ? adminName.hashCode() : 0);
        result = 31 * result + (adminCount != null ? adminCount.hashCode() : 0);
        result = 31 * result + (adminPass != null ? adminPass.hashCode() : 0);
        result = 31 * result + (adminAuthor != null ? adminAuthor.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "admin_user_name")
    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    @Basic
    @Column(name = "admin_user_phone")
    public String getAdminUserPhone() {
        return adminUserPhone;
    }

    public void setAdminUserPhone(String adminUserPhone) {
        this.adminUserPhone = adminUserPhone;
    }
}
