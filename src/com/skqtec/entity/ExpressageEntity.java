package com.skqtec.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EXPRESSAGE", schema = "ketuanDB_test", catalog = "")
public class ExpressageEntity {
    private String id;
    private int isNew;
    private String expressageName;
    private String productId;
    private String expressCode;
    private String priceStand;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "is_new")
    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    @Basic
    @Column(name = "expressage_name")
    public String getExpressageName() {
        return expressageName;
    }

    public void setExpressageName(String expressageName) {
        this.expressageName = expressageName;
    }

    @Basic
    @Column(name = "product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "express_code")
    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    @Basic
    @Column(name = "price_stand")
    public String getPriceStand() {
        return priceStand;
    }

    public void setPriceStand(String priceStand) {
        this.priceStand = priceStand;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressageEntity that = (ExpressageEntity) o;

        if (isNew != that.isNew) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (expressageName != null ? !expressageName.equals(that.expressageName) : that.expressageName != null)
            return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (expressCode != null ? !expressCode.equals(that.expressCode) : that.expressCode != null) return false;
        if (priceStand != null ? !priceStand.equals(that.priceStand) : that.priceStand != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + isNew;
        result = 31 * result + (expressageName != null ? expressageName.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (expressCode != null ? expressCode.hashCode() : 0);
        result = 31 * result + (priceStand != null ? priceStand.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
