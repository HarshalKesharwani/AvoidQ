package com.example.admin.avoidq;

/**
 * Created by Admin on 21-03-2017.
 */
public class OuterBill {
    String bill_id,date;
            double total_price,total_weight; int paid_flag;

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPaid_flag() {
        return paid_flag;
    }

    public void setPaid_flag(int paid_flag) {
        this.paid_flag = paid_flag;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(double total_weight) {
        this.total_weight = total_weight;
    }
}
