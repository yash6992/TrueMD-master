
package com.truemdhq.projectx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "invoiceNo",
    "invoiceStatus",
    "invoiceDate",
    "invoiceDueDate",
    "subtotal",
    "discount",
    "vat",
    "tax1",
    "tax2",
    "shipping",
    "total",
    "paid",
    "balanceDue",
    "comment",
    "client",
    "items"
})
public class Invoice {

    @JsonProperty("invoiceNo")
    private String invoiceNo;
    @JsonProperty("invoiceStatus")
    private String invoiceStatus;
    @JsonProperty("invoiceDate")
    private String invoiceDate;
    @JsonProperty("invoiceDueDate")
    private String invoiceDueDate;
    @JsonProperty("subtotal")
    private double subtotal;
    @JsonProperty("discount")
    private double discount;
    @JsonProperty("vat")
    private double vat;
    @JsonProperty("tax1")
    private double tax1;
    @JsonProperty("tax2")
    private double tax2;
    @JsonProperty("shipping")
    private double shipping;
    @JsonProperty("total")
    private double total;
    @JsonProperty("paid")
    private double paid;
    @JsonProperty("balanceDue")
    private double balanceDue;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("client")
    private Client client;
    @JsonProperty("items")
    private List<Item> items = new ArrayList<Item>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Invoice() {
    }

    /**
     * 
     * @param total
     * @param invoiceDate
     * @param tax2
     * @param tax1
     * @param client
     * @param vat
     * @param invoiceDueDate
     * @param invoiceNo
     * @param subtotal
     * @param invoiceStatus
     * @param discount
     * @param shipping
     * @param paid
     * @param items
     * @param comment
     * @param balanceDue
     */
    public Invoice(String invoiceNo, String invoiceStatus, String invoiceDate, String invoiceDueDate, double subtotal, double discount, double vat, double tax1, double tax2, double shipping, double total, double paid, double balanceDue, String comment, Client client, List<Item> items) {
        this.invoiceNo = invoiceNo;
        this.invoiceStatus = invoiceStatus;
        this.invoiceDate = invoiceDate;
        this.invoiceDueDate = invoiceDueDate;
        this.subtotal = subtotal;
        this.discount = discount;
        this.vat = vat;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.shipping = shipping;
        this.total = total;
        this.paid = paid;
        this.balanceDue = balanceDue;
        this.comment = comment;
        this.client = client;
        this.items = items;
    }

    /**
     * 
     * @return
     *     The invoiceNo
     */
    @JsonProperty("invoiceNo")
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * 
     * @param invoiceNo
     *     The invoiceNo
     */
    @JsonProperty("invoiceNo")
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * 
     * @return
     *     The invoiceStatus
     */
    @JsonProperty("invoiceStatus")
    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * 
     * @param invoiceStatus
     *     The invoiceStatus
     */
    @JsonProperty("invoiceStatus")
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * 
     * @return
     *     The invoiceDate
     */
    @JsonProperty("invoiceDate")
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * 
     * @param invoiceDate
     *     The invoiceDate
     */
    @JsonProperty("invoiceDate")
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * 
     * @return
     *     The invoiceDueDate
     */
    @JsonProperty("invoiceDueDate")
    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }

    /**
     * 
     * @param invoiceDueDate
     *     The invoiceDueDate
     */
    @JsonProperty("invoiceDueDate")
    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    /**
     * 
     * @return
     *     The subtotal
     */
    @JsonProperty("subtotal")
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * 
     * @param subtotal
     *     The subtotal
     */
    @JsonProperty("subtotal")
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * 
     * @return
     *     The discount
     */
    @JsonProperty("discount")
    public double getDiscount() {
        return discount;
    }

    /**
     * 
     * @param discount
     *     The discount
     */
    @JsonProperty("discount")
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * 
     * @return
     *     The vat
     */
    @JsonProperty("vat")
    public double getVat() {
        return vat;
    }

    /**
     * 
     * @param vat
     *     The vat
     */
    @JsonProperty("vat")
    public void setVat(double vat) {
        this.vat = vat;
    }

    /**
     * 
     * @return
     *     The tax1
     */
    @JsonProperty("tax1")
    public double getTax1() {
        return tax1;
    }

    /**
     * 
     * @param tax1
     *     The tax1
     */
    @JsonProperty("tax1")
    public void setTax1(double tax1) {
        this.tax1 = tax1;
    }

    /**
     * 
     * @return
     *     The tax2
     */
    @JsonProperty("tax2")
    public double getTax2() {
        return tax2;
    }

    /**
     * 
     * @param tax2
     *     The tax2
     */
    @JsonProperty("tax2")
    public void setTax2(double tax2) {
        this.tax2 = tax2;
    }

    /**
     * 
     * @return
     *     The shipping
     */
    @JsonProperty("shipping")
    public double getShipping() {
        return shipping;
    }

    /**
     * 
     * @param shipping
     *     The shipping
     */
    @JsonProperty("shipping")
    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    /**
     * 
     * @return
     *     The total
     */
    @JsonProperty("total")
    public double getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    @JsonProperty("total")
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The paid
     */
    @JsonProperty("paid")
    public double getPaid() {
        return paid;
    }

    /**
     * 
     * @param paid
     *     The paid
     */
    @JsonProperty("paid")
    public void setPaid(double paid) {
        this.paid = paid;
    }

    /**
     * 
     * @return
     *     The balanceDue
     */
    @JsonProperty("balanceDue")
    public double getBalanceDue() {
        return balanceDue;
    }

    /**
     * 
     * @param balanceDue
     *     The balanceDue
     */
    @JsonProperty("balanceDue")
    public void setBalanceDue(double balanceDue) {
        this.balanceDue = balanceDue;
    }

    /**
     * 
     * @return
     *     The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * 
     * @param comment
     *     The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 
     * @return
     *     The client
     */
    @JsonProperty("client")
    public Client getClient() {
        return client;
    }

    /**
     * 
     * @param client
     *     The client
     */
    @JsonProperty("client")
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * 
     * @return
     *     The items
     */
    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
