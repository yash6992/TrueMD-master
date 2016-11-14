package com.truemdhq.projectx.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yashvardhansrivastava on 09/11/16.
 */


public class InvoiceParent implements ParentListItem {


    String vendorName, balanceDue, invoiceStatus, invoiceDueDate;
    Invoice invoiceActual;

    public Invoice getInvoiceActual() {
        return invoiceActual;
    }

    public void setInvoiceActual(Invoice invoiceActual) {
        this.invoiceActual = invoiceActual;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    /* Create an instance variable for your list of children */
    private List<Object> mChildrenList;

    /**
     * Your constructor and any other accessor
     *  methods should go here.
     */

    public InvoiceParent(){}

    public InvoiceParent(Invoice invoice)
    {
        this.setVendorName(invoice.getClient().getVendorName());
        this.setBalanceDue(""+invoice.getBalanceDue());
        this.setInvoiceStatus(invoice.getInvoiceStatus());
        this.setInvoiceDueDate(invoice.getInvoiceDueDate());
        this.setInvoiceActual(invoice);

    }





    public void setChildItemList(List<Object> list) {
        mChildrenList = list;
    }

    @Override
    public List<?> getChildItemList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }



}
