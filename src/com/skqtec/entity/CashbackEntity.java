package com.skqtec.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "CASHBACK", schema = "ketuanDB_test", catalog = "")
public class CashbackEntity {
    private String id;
    private double sumMoney;
    private Timestamp cashbackDate;
    private int paybacked;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sum_money")
    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    @Basic
    @Column(name = "cashback_date")
    public Timestamp getCashbackDate() {
        return cashbackDate;
    }

    public void setCashbackDate(Timestamp cashbackDate) {
        this.cashbackDate = cashbackDate;
    }

    @Basic
    @Column(name = "paybacked")
    public int getPaybacked() {
        return paybacked;
    }

    public void setPaybacked(int paybacked) {
        this.paybacked = paybacked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CashbackEntity that = (CashbackEntity) o;

        if (Double.compare(that.sumMoney, sumMoney) != 0) return false;
        if (paybacked != that.paybacked) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (cashbackDate != null ? !cashbackDate.equals(that.cashbackDate) : that.cashbackDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        temp = Double.doubleToLongBits(sumMoney);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (cashbackDate != null ? cashbackDate.hashCode() : 0);
        result = 31 * result + paybacked;
        return result;
    }
}
