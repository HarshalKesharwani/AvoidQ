package com.example.admin.avoidq;

/**
 * Created by Admin on 28-10-2016.
 */
public class Bill {
    private int _id;
    private String _productname;
    private double _price;
    private int quantity;
    private double discount;
    private double weight;
    private double tax;

    String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    protected Bill() {
    }

    public Bill(int _id, String _productname, double _price) {
        this._id = _id;
        this._price = _price;
        this._productname = _productname;
    }

    public Bill(double _price, String _productname) {
        this._price = _price;
        this._productname = _productname;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double get_price() {
        return _price;
    }

    public void set_price(double _price) {
        this._price = _price;
    }

    public String get_productname() {
        return _productname;
    }

    public void set_productname(String _productname) {
        this._productname = _productname;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
}

