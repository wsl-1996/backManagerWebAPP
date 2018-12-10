package com.skqtec.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ORDER", schema = "ketuanDB_test", catalog = "")
public class OrderEntity {
    private String id;
    private String productId;
    private String groupId;
    private String userId;
    private int state;
    private String sendName;
    private String sendAddress;
    private String sendZip;
    private String sendTel;
    private Timestamp orderTime;
    private String paymethod;
    private String meno;
    private double totalPrice;
    private String trackCode;
    private String trackId;
    private Timestamp payTime;
    private Timestamp deliverTime;
    private Timestamp receiptTime;
    private double productPrice;
    private double carriagePrice;
    private String descript;
    private int sums;
    private String typeSpecification;
    private String outRefundNo;
    private double deduction;
    private double actualPayment;
    private String linkSender;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "GROUP_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
    @Column(name = "state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "send_name")
    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    @Basic
    @Column(name = "send_address")
    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    @Basic
    @Column(name = "send_zip")
    public String getSendZip() {
        return sendZip;
    }

    public void setSendZip(String sendZip) {
        this.sendZip = sendZip;
    }

    @Basic
    @Column(name = "send_tel")
    public String getSendTel() {
        return sendTel;
    }

    public void setSendTel(String sendTel) {
        this.sendTel = sendTel;
    }

    @Basic
    @Column(name = "order_time")
    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    @Basic
    @Column(name = "paymethod")
    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    @Basic
    @Column(name = "meno")
    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    @Basic
    @Column(name = "total_price")
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Basic
    @Column(name = "track_code")
    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    @Basic
    @Column(name = "track_id")
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    @Basic
    @Column(name = "pay_time")
    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Column(name = "deliver_time")
    public Timestamp getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Timestamp deliverTime) {
        this.deliverTime = deliverTime;
    }

    @Basic
    @Column(name = "receipt_time")
    public Timestamp getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Timestamp receiptTime) {
        this.receiptTime = receiptTime;
    }

    @Basic
    @Column(name = "product_price")
    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Basic
    @Column(name = "carriage_price")
    public double getCarriagePrice() {
        return carriagePrice;
    }

    public void setCarriagePrice(double carriagePrice) {
        this.carriagePrice = carriagePrice;
    }

    @Basic
    @Column(name = "descript")
    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    @Basic
    @Column(name = "sums")
    public int getSums() {
        return sums;
    }

    public void setSums(int sums) {
        this.sums = sums;
    }

    @Basic
    @Column(name = "type_specification")
    public String getTypeSpecification() {
        return typeSpecification;
    }

    public void setTypeSpecification(String typeSpecification) {
        this.typeSpecification = typeSpecification;
    }

    @Basic
    @Column(name = "out_refund_no")
    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    @Basic
    @Column(name = "deduction")
    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    @Basic
    @Column(name = "actual_payment")
    public double getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(double actualPayment) {
        this.actualPayment = actualPayment;
    }

    @Basic
    @Column(name = "link_sender")
    public String getLinkSender() {
        return linkSender;
    }

    public void setLinkSender(String linkSender) {
        this.linkSender = linkSender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        if (state != that.state) return false;
        if (Double.compare(that.totalPrice, totalPrice) != 0) return false;
        if (Double.compare(that.productPrice, productPrice) != 0) return false;
        if (Double.compare(that.carriagePrice, carriagePrice) != 0) return false;
        if (sums != that.sums) return false;
        if (Double.compare(that.deduction, deduction) != 0) return false;
        if (Double.compare(that.actualPayment, actualPayment) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (sendName != null ? !sendName.equals(that.sendName) : that.sendName != null) return false;
        if (sendAddress != null ? !sendAddress.equals(that.sendAddress) : that.sendAddress != null) return false;
        if (sendZip != null ? !sendZip.equals(that.sendZip) : that.sendZip != null) return false;
        if (sendTel != null ? !sendTel.equals(that.sendTel) : that.sendTel != null) return false;
        if (orderTime != null ? !orderTime.equals(that.orderTime) : that.orderTime != null) return false;
        if (paymethod != null ? !paymethod.equals(that.paymethod) : that.paymethod != null) return false;
        if (meno != null ? !meno.equals(that.meno) : that.meno != null) return false;
        if (trackCode != null ? !trackCode.equals(that.trackCode) : that.trackCode != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;
        if (payTime != null ? !payTime.equals(that.payTime) : that.payTime != null) return false;
        if (deliverTime != null ? !deliverTime.equals(that.deliverTime) : that.deliverTime != null) return false;
        if (receiptTime != null ? !receiptTime.equals(that.receiptTime) : that.receiptTime != null) return false;
        if (descript != null ? !descript.equals(that.descript) : that.descript != null) return false;
        if (typeSpecification != null ? !typeSpecification.equals(that.typeSpecification) : that.typeSpecification != null)
            return false;
        if (outRefundNo != null ? !outRefundNo.equals(that.outRefundNo) : that.outRefundNo != null) return false;
        if (linkSender != null ? !linkSender.equals(that.linkSender) : that.linkSender != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (sendName != null ? sendName.hashCode() : 0);
        result = 31 * result + (sendAddress != null ? sendAddress.hashCode() : 0);
        result = 31 * result + (sendZip != null ? sendZip.hashCode() : 0);
        result = 31 * result + (sendTel != null ? sendTel.hashCode() : 0);
        result = 31 * result + (orderTime != null ? orderTime.hashCode() : 0);
        result = 31 * result + (paymethod != null ? paymethod.hashCode() : 0);
        result = 31 * result + (meno != null ? meno.hashCode() : 0);
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (trackCode != null ? trackCode.hashCode() : 0);
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (payTime != null ? payTime.hashCode() : 0);
        result = 31 * result + (deliverTime != null ? deliverTime.hashCode() : 0);
        result = 31 * result + (receiptTime != null ? receiptTime.hashCode() : 0);
        temp = Double.doubleToLongBits(productPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(carriagePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (descript != null ? descript.hashCode() : 0);
        result = 31 * result + sums;
        result = 31 * result + (typeSpecification != null ? typeSpecification.hashCode() : 0);
        result = 31 * result + (outRefundNo != null ? outRefundNo.hashCode() : 0);
        temp = Double.doubleToLongBits(deduction);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(actualPayment);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (linkSender != null ? linkSender.hashCode() : 0);
        return result;
    }
}
