package com.smartdevapp.smartnote.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "shopping")
public class Shopping implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "item")
    String item = "";

    @ColumnInfo(name = "quantity")
    int quantity ;

    @ColumnInfo(name = "price")
    double price ;

    @ColumnInfo(name = "TotalPrice")
    double TotalPrice ;

    @ColumnInfo(name = "TotalItems")
    double TotalItems ;

    @ColumnInfo(name = "checked")
    boolean checked = false;

    @ColumnInfo(name = "boxed")
    boolean boxed = false;

    @ColumnInfo(name = "longClicked")
    boolean longClicked = false;



    public boolean isMyColor() {
        return myColor;
    }

    public void setMyColor(boolean myColor) {
        this.myColor = myColor;
    }

    boolean myColor = false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isBoxed() {
        return boxed;
    }

    public void setBoxed(boolean boxed) {
        this.boxed = boxed;
    }

    public boolean isLongClicked() {
        return longClicked;
    }

    public void setLongClicked(boolean longClicked) {
        this.longClicked = longClicked;
    }

    public double getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(double totalItems) {
        TotalItems = totalItems;
    }
}
