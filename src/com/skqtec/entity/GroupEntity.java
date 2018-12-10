package com.skqtec.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "GROUP", schema = "ketuanDB_test", catalog = "")
public class GroupEntity {
    private String id;
    private String userId;
    private String productId;
    private int groupType;
    private int groupCount;
    private int offeredCount;
    private double groupPrice;
    private String offeredUserId;
    private int groupState;
    private Timestamp endTime;
    private String customerService;
    private String groupDiscription;
    private double productCost;
    private String deliverProvince;
    private String deliverCity;
    private String deliverAddress;
    private String groupFirstImg;
    private String groupSlideImg;
    private String groupName;
    private double returnCashRate;
    private double returnCashRateInviter;
    private String groupStyle;
    private double returnCashRateLinksender;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "USER_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "PRODUCT_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "group_type")
    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    @Basic
    @Column(name = "group_count")
    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    @Basic
    @Column(name = "offered_count")
    public int getOfferedCount() {
        return offeredCount;
    }

    public void setOfferedCount(int offeredCount) {
        this.offeredCount = offeredCount;
    }

    @Basic
    @Column(name = "group_price")
    public double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(double groupPrice) {
        this.groupPrice = groupPrice;
    }

    @Basic
    @Column(name = "offered_user_id")
    public String getOfferedUserId() {
        return offeredUserId;
    }

    public void setOfferedUserId(String offeredUserId) {
        this.offeredUserId = offeredUserId;
    }

    @Basic
    @Column(name = "group_state")
    public int getGroupState() {
        return groupState;
    }

    public void setGroupState(int groupState) {
        this.groupState = groupState;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "customer_service")
    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    @Basic
    @Column(name = "group_discription")
    public String getGroupDiscription() {
        return groupDiscription;
    }

    public void setGroupDiscription(String groupDiscription) {
        this.groupDiscription = groupDiscription;
    }

    @Basic
    @Column(name = "product_cost")
    public double getProductCost() {
        return productCost;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    @Basic
    @Column(name = "deliver_province")
    public String getDeliverProvince() {
        return deliverProvince;
    }

    public void setDeliverProvince(String deliverProvince) {
        this.deliverProvince = deliverProvince;
    }

    @Basic
    @Column(name = "deliver_city")
    public String getDeliverCity() {
        return deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    @Basic
    @Column(name = "deliver_address")
    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    @Basic
    @Column(name = "group_first_img")
    public String getGroupFirstImg() {
        return groupFirstImg;
    }

    public void setGroupFirstImg(String groupFirstImg) {
        this.groupFirstImg = groupFirstImg;
    }

    @Basic
    @Column(name = "group_slide_img")
    public String getGroupSlideImg() {
        return groupSlideImg;
    }

    public void setGroupSlideImg(String groupSlideImg) {
        this.groupSlideImg = groupSlideImg;
    }

    @Basic
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "return_cash_rate")
    public double getReturnCashRate() {
        return returnCashRate;
    }

    public void setReturnCashRate(double returnCashRate) {
        this.returnCashRate = returnCashRate;
    }

    @Basic
    @Column(name = "return_cash_rate_inviter")
    public double getReturnCashRateInviter() {
        return returnCashRateInviter;
    }

    public void setReturnCashRateInviter(double returnCashRateInviter) {
        this.returnCashRateInviter = returnCashRateInviter;
    }

    @Basic
    @Column(name = "group_style")
    public String getGroupStyle() {
        return groupStyle;
    }

    public void setGroupStyle(String groupStyle) {
        this.groupStyle = groupStyle;
    }

    @Basic
    @Column(name = "return_cash_rate_linksender")
    public double getReturnCashRateLinksender() {
        return returnCashRateLinksender;
    }

    public void setReturnCashRateLinksender(double returnCashRateLinksender) {
        this.returnCashRateLinksender = returnCashRateLinksender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupEntity that = (GroupEntity) o;

        if (groupType != that.groupType) return false;
        if (groupCount != that.groupCount) return false;
        if (offeredCount != that.offeredCount) return false;
        if (Double.compare(that.groupPrice, groupPrice) != 0) return false;
        if (groupState != that.groupState) return false;
        if (Double.compare(that.productCost, productCost) != 0) return false;
        if (Double.compare(that.returnCashRate, returnCashRate) != 0) return false;
        if (Double.compare(that.returnCashRateInviter, returnCashRateInviter) != 0) return false;
        if (Double.compare(that.returnCashRateLinksender, returnCashRateLinksender) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (offeredUserId != null ? !offeredUserId.equals(that.offeredUserId) : that.offeredUserId != null)
            return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (customerService != null ? !customerService.equals(that.customerService) : that.customerService != null)
            return false;
        if (groupDiscription != null ? !groupDiscription.equals(that.groupDiscription) : that.groupDiscription != null)
            return false;
        if (deliverProvince != null ? !deliverProvince.equals(that.deliverProvince) : that.deliverProvince != null)
            return false;
        if (deliverCity != null ? !deliverCity.equals(that.deliverCity) : that.deliverCity != null) return false;
        if (deliverAddress != null ? !deliverAddress.equals(that.deliverAddress) : that.deliverAddress != null)
            return false;
        if (groupFirstImg != null ? !groupFirstImg.equals(that.groupFirstImg) : that.groupFirstImg != null)
            return false;
        if (groupSlideImg != null ? !groupSlideImg.equals(that.groupSlideImg) : that.groupSlideImg != null)
            return false;
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
        if (groupStyle != null ? !groupStyle.equals(that.groupStyle) : that.groupStyle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + groupType;
        result = 31 * result + groupCount;
        result = 31 * result + offeredCount;
        temp = Double.doubleToLongBits(groupPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (offeredUserId != null ? offeredUserId.hashCode() : 0);
        result = 31 * result + groupState;
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (customerService != null ? customerService.hashCode() : 0);
        result = 31 * result + (groupDiscription != null ? groupDiscription.hashCode() : 0);
        temp = Double.doubleToLongBits(productCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (deliverProvince != null ? deliverProvince.hashCode() : 0);
        result = 31 * result + (deliverCity != null ? deliverCity.hashCode() : 0);
        result = 31 * result + (deliverAddress != null ? deliverAddress.hashCode() : 0);
        result = 31 * result + (groupFirstImg != null ? groupFirstImg.hashCode() : 0);
        result = 31 * result + (groupSlideImg != null ? groupSlideImg.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        temp = Double.doubleToLongBits(returnCashRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(returnCashRateInviter);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (groupStyle != null ? groupStyle.hashCode() : 0);
        temp = Double.doubleToLongBits(returnCashRateLinksender);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
