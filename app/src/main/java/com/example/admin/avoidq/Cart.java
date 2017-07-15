package com.example.admin.avoidq;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 26-10-2016.
 */
public class Cart implements Parcelable {
    private  String itemname,mfd_date,exp_date;
    String id;
    int quantity;
    double price,discount,weight,tax;
   // private  int quantity;
   String barcode;
    public  Cart(){

    }
    protected Cart(Parcel in) {
        itemname = in.readString();
        mfd_date = in.readString();
        exp_date = in.readString();
        id = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        discount = in.readDouble();
        weight = in.readDouble();
        tax = in.readDouble();
        barcode = in.readString();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public static Creator<Cart> getCREATOR() {
        return CREATOR;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getMfd_date() {
        return mfd_date;
    }

    public void setMfd_date(String mfd_date) {
        this.mfd_date = mfd_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemname);
        dest.writeString(mfd_date);
        dest.writeString(exp_date);
        dest.writeString(id);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeDouble(discount);
        dest.writeDouble(weight);
        dest.writeDouble(tax);
        dest.writeString(barcode);
    }
}
