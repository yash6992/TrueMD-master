
package com.truemdhq.projectx.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;
//import javax.annotation.Generated;
//import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import com.fasterxml.jackson.annotation.JsonAnySetter;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "itemCode",
    "itemQuantity",
    "itemRate",
    "itemAmount",
    "itemName",
    "itemBarcode",
    "vatRate",
    "tax1Rate",
    "tax2Rate",
    "vat",
    "tax1",
    "tax2",
    "product"
})
public class Item {

    @JsonProperty("itemCode")
    private String itemCode;
    @JsonProperty("itemQuantity")
    private double itemQuantity;
    @JsonProperty("itemRate")
    private double itemRate;
    @JsonProperty("itemAmount")
    private double itemAmount;
    @JsonProperty("itemName")
    private String itemName;
    @JsonProperty("itemBarcode")
    private String itemBarcode;
    @JsonProperty("vatRate")
    private double vatRate;
    @JsonProperty("tax1Rate")
    private double tax1Rate;
    @JsonProperty("tax2Rate")
    private double tax2Rate;
    @JsonProperty("vat")
    private boolean vat;
    @JsonProperty("tax1")
    private boolean tax1;
    @JsonProperty("tax2")
    private boolean tax2;
    @JsonProperty("product")
    private boolean product;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Item() {
    }

    /**
     * 
     * @param product
     * @param tax2
     * @param itemName
     * @param tax1
     * @param itemQuantity
     * @param tax1Rate
     * @param itemAmount
     * @param vat
     * @param tax2Rate
     * @param itemBarcode
     * @param vatRate
     * @param itemRate
     * @param itemCode
     */
    public Item(String itemCode, double itemQuantity, double itemRate, double itemAmount, String itemName, String itemBarcode, double vatRate, double tax1Rate, double tax2Rate, boolean vat, boolean tax1, boolean tax2, boolean product) {

        this.itemCode = itemCode;
        this.itemQuantity = itemQuantity;
        this.itemRate = itemRate;
        this.itemAmount = itemAmount;
        this.itemName = itemName;
        this.itemBarcode = itemBarcode;
        this.vatRate = vatRate;
        this.tax1Rate = tax1Rate;
        this.tax2Rate = tax2Rate;
        this.vat = vat;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.product = product;

    }

    /**
     * 
     * @return
     *     The itemCode
     */
    @JsonProperty("itemCode")
    public String getItemCode() {
        return itemCode;
    }

    /**
     * 
     * @param itemCode
     *     The itemCode
     */
    @JsonProperty("itemCode")
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * 
     * @return
     *     The itemQuantity
     */
    @JsonProperty("itemQuantity")
    public double getItemQuantity() {
        return itemQuantity;
    }

    /**
     * 
     * @param itemQuantity
     *     The itemQuantity
     */
    @JsonProperty("itemQuantity")
    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * 
     * @return
     *     The itemRate
     */
    @JsonProperty("itemRate")
    public double getItemRate() {
        return itemRate;
    }

    /**
     * 
     * @param itemRate
     *     The itemRate
     */
    @JsonProperty("itemRate")
    public void setItemRate(double itemRate) {
        this.itemRate = itemRate;
    }

    /**
     * 
     * @return
     *     The itemAmount
     */
    @JsonProperty("itemAmount")
    public double getItemAmount() {
        return itemAmount;
    }

    /**
     * 
     * @param itemAmount
     *     The itemAmount
     */
    @JsonProperty("itemAmount")
    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    /**
     * 
     * @return
     *     The itemName
     */
    @JsonProperty("itemName")
    public String getItemName() {
        return itemName;
    }

    /**
     * 
     * @param itemName
     *     The itemName
     */
    @JsonProperty("itemName")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * 
     * @return
     *     The itemBarcode
     */
    @JsonProperty("itemBarcode")
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * 
     * @param itemBarcode
     *     The itemBarcode
     */
    @JsonProperty("itemBarcode")
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
     * 
     * @return
     *     The vatRate
     */
    @JsonProperty("vatRate")
    public double getVatRate() {
        return vatRate;
    }

    /**
     * 
     * @param vatRate
     *     The vatRate
     */
    @JsonProperty("vatRate")
    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    /**
     * 
     * @return
     *     The tax1Rate
     */
    @JsonProperty("tax1Rate")
    public double getTax1Rate() {
        return tax1Rate;
    }

    /**
     * 
     * @param tax1Rate
     *     The tax1Rate
     */
    @JsonProperty("tax1Rate")
    public void setTax1Rate(double tax1Rate) {
        this.tax1Rate = tax1Rate;
    }

    /**
     * 
     * @return
     *     The tax2Rate
     */
    @JsonProperty("tax2Rate")
    public double getTax2Rate() {
        return tax2Rate;
    }

    /**
     * 
     * @param tax2Rate
     *     The tax2Rate
     */
    @JsonProperty("tax2Rate")
    public void setTax2Rate(double tax2Rate) {
        this.tax2Rate = tax2Rate;
    }

    /**
     * 
     * @return
     *     The vat
     */
    @JsonProperty("vat")
    public boolean isVat() {
        return vat;
    }

    /**
     * 
     * @param vat
     *     The vat
     */
    @JsonProperty("vat")
    public void setVat(boolean vat) {
        this.vat = vat;
    }

    /**
     * 
     * @return
     *     The tax1
     */
    @JsonProperty("tax1")
    public boolean isTax1() {
        return tax1;
    }

    /**
     * 
     * @param tax1
     *     The tax1
     */
    @JsonProperty("tax1")
    public void setTax1(boolean tax1) {
        this.tax1 = tax1;
    }

    /**
     * 
     * @return
     *     The tax2
     */
    @JsonProperty("tax2")
    public boolean isTax2() {
        return tax2;
    }

    /**
     * 
     * @param tax2
     *     The tax2
     */
    @JsonProperty("tax2")
    public void setTax2(boolean tax2) {
        this.tax2 = tax2;
    }

    /**
     * 
     * @return
     *     The product
     */
    @JsonProperty("product")
    public boolean isProduct() {
        return product;
    }

    /**
     * 
     * @param product
     *     The product
     */
    @JsonProperty("product")
    public void setProduct(boolean product) {
        this.product = product;
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
