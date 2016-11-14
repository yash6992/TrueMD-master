package com.truemdhq.projectx.model;

import java.util.Date;

/**
 * Created by yashvardhansrivastava on 09/11/16.
 */
public class InvoiceChild {

    String noOfItems, invoiceDate, vendorPhone, invoiceNo, comment, vendorEmail;

    public String getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(String noOfItems) {
        this.noOfItems = noOfItems;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public InvoiceChild(){}

    public InvoiceChild(Invoice invoice)
    {

        this.setInvoiceNo(invoice.getInvoiceNo());
        this.setComment(invoice.getComment());
        this.setInvoiceDate(invoice.getInvoiceDate());
        this.setNoOfItems(""+invoice.getItems().size());
        this.setVendorEmail(invoice.getClient().getVendorEmail());
        this.setVendorPhone(invoice.getClient().getVendorPhoneNo());
    }

    public InvoiceChild(String noOfItems, String invoiceDate, String vendorPhone, String invoiceNo, String comment, String vendorEmail) {
        this.noOfItems = noOfItems;
        this.invoiceDate = invoiceDate;
        this.vendorPhone = vendorPhone;
        this.invoiceNo = invoiceNo;
        this.comment = comment;
        this.vendorEmail = vendorEmail;
    }
}