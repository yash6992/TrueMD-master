
package com.truemdhq.projectx.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
    "vendorLogoURL",
    "vendorName",
    "vendorShippingAddress",
    "vendorBillingAddress",
    "vendorEmail",
    "vendorPhoneNo",
    "vendorTIN"
})
public class Client {

    @JsonProperty("vendorLogoURL")
    private String vendorLogoURL;
    @JsonProperty("vendorName")
    private String vendorName;
    @JsonProperty("vendorShippingAddress")
    private String vendorShippingAddress;
    @JsonProperty("vendorBillingAddress")
    private String vendorBillingAddress;
    @JsonProperty("vendorEmail")
    private String vendorEmail;
    @JsonProperty("vendorPhoneNo")
    private String vendorPhoneNo;
    @JsonProperty("vendorTIN")
    private String vendorTIN;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Client() {
    }

    /**
     * 
     * @param vendorEmail
     * @param vendorTIN
     * @param vendorName
     * @param vendorPhoneNo
     * @param vendorLogoURL
     * @param vendorShippingAddress
     * @param vendorBillingAddress
     */
    public Client(String vendorLogoURL, String vendorName, String vendorShippingAddress, String vendorBillingAddress, String vendorEmail, String vendorPhoneNo, String vendorTIN) {
        this.vendorLogoURL = vendorLogoURL;
        this.vendorName = vendorName;
        this.vendorShippingAddress = vendorShippingAddress;
        this.vendorBillingAddress = vendorBillingAddress;
        this.vendorEmail = vendorEmail;
        this.vendorPhoneNo = vendorPhoneNo;
        this.vendorTIN = vendorTIN;
    }

    /**
     * 
     * @return
     *     The vendorLogoURL
     */
    @JsonProperty("vendorLogoURL")
    public String getVendorLogoURL() {
        return vendorLogoURL;
    }

    /**
     * 
     * @param vendorLogoURL
     *     The vendorLogoURL
     */
    @JsonProperty("vendorLogoURL")
    public void setVendorLogoURL(String vendorLogoURL) {
        this.vendorLogoURL = vendorLogoURL;
    }

    /**
     * 
     * @return
     *     The vendorName
     */
    @JsonProperty("vendorName")
    public String getVendorName() {
        return vendorName;
    }

    /**
     * 
     * @param vendorName
     *     The vendorName
     */
    @JsonProperty("vendorName")
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * 
     * @return
     *     The vendorShippingAddress
     */
    @JsonProperty("vendorShippingAddress")
    public String getVendorShippingAddress() {
        return vendorShippingAddress;
    }

    /**
     * 
     * @param vendorShippingAddress
     *     The vendorShippingAddress
     */
    @JsonProperty("vendorShippingAddress")
    public void setVendorShippingAddress(String vendorShippingAddress) {
        this.vendorShippingAddress = vendorShippingAddress;
    }

    /**
     * 
     * @return
     *     The vendorBillingAddress
     */
    @JsonProperty("vendorBillingAddress")
    public String getVendorBillingAddress() {
        return vendorBillingAddress;
    }

    /**
     * 
     * @param vendorBillingAddress
     *     The vendorBillingAddress
     */
    @JsonProperty("vendorBillingAddress")
    public void setVendorBillingAddress(String vendorBillingAddress) {
        this.vendorBillingAddress = vendorBillingAddress;
    }

    /**
     * 
     * @return
     *     The vendorEmail
     */
    @JsonProperty("vendorEmail")
    public String getVendorEmail() {
        return vendorEmail;
    }

    /**
     * 
     * @param vendorEmail
     *     The vendorEmail
     */
    @JsonProperty("vendorEmail")
    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    /**
     * 
     * @return
     *     The vendorPhoneNo
     */
    @JsonProperty("vendorPhoneNo")
    public String getVendorPhoneNo() {
        return vendorPhoneNo;
    }

    /**
     * 
     * @param vendorPhoneNo
     *     The vendorPhoneNo
     */
    @JsonProperty("vendorPhoneNo")
    public void setVendorPhoneNo(String vendorPhoneNo) {
        this.vendorPhoneNo = vendorPhoneNo;
    }

    /**
     * 
     * @return
     *     The vendorTIN
     */
    @JsonProperty("vendorTIN")
    public String getVendorTIN() {
        return vendorTIN;
    }

    /**
     * 
     * @param vendorTIN
     *     The vendorTIN
     */
    @JsonProperty("vendorTIN")
    public void setVendorTIN(String vendorTIN) {
        this.vendorTIN = vendorTIN;
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
