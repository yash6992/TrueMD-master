package info.truemd.android.model;

import java.util.ArrayList;

/**
 * Created by yashvardhansrivastava on 19/02/16.
 */
public class Medicine {

            String ci_fd;
    String schedule;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    String ci_lb;
            String ci_lc;
           String  ci_pg;
            String discountPerc;
            String favorite;
            String fd;
            String form;
           Generic[] generics;
       String  imgUrl;
              String   iwa;
            String lb;
            String lc;
            String mc;
            String metaInfo;
              String mf;
           String  mfId;
            String mrp;
           String  name;
           String  oPrice;
           String  pForm;
           String  pSize;
           String  pg;
             String  prescriptionRequired;
           String  productGroupBrandName;
            String productGroupId;
            String saltInfo;
            String sc;
            String severId;
            String su;
           String  truemdCode;
           String  truemdDrugCode;
            String truemdId;
           String  type;
    ArrayList<String> constituents;

    public ArrayList<String> getConstituents() {
        return constituents;
    }

    public void setConstituents(ArrayList<String> constituents) {
        this.constituents = constituents;
    }

    public Medicine() {
    }

    public Medicine(String ci_fd, String ci_lb, String ci_lc, String ci_pg, String discountPerc, String favorite, String fd, String form, Generic[] generics, String imgUrl, String iwa, String lb, String lc, String mc, String metaInfo, String mf, String mfId, String mrp, String name, String oPrice, String pForm, String pSize, String pg, String prescriptionRequired, String productGroupBrandName, String productGroupId, String saltInfo, String sc, String severId, String su, String truemdCode, String truemdDrugCode, String truemdId, String type, String uPrice, String uip, String webUrl) {

        this.ci_fd = ci_fd;
        this.ci_lb = ci_lb;
        this.ci_lc = ci_lc;
        this.ci_pg = ci_pg;
        this.discountPerc = discountPerc;
        this.favorite = favorite;
        this.fd = fd;
        this.form = form;
        this.generics = generics;
        this.imgUrl = imgUrl;
        this.iwa = iwa;
        this.lb = lb;
        this.lc = lc;
        this.mc = mc;
        this.metaInfo = metaInfo;
        this.mf = mf;
        this.mfId = mfId;
        this.mrp = mrp;
        this.name = name;
        this.oPrice = oPrice;
        this.pForm = pForm;
        this.pSize = pSize;
        this.pg = pg;
        this.prescriptionRequired = prescriptionRequired;
        this.productGroupBrandName = productGroupBrandName;
        this.productGroupId = productGroupId;
        this.saltInfo = saltInfo;
        this.sc = sc;
        this.severId = severId;
        this.su = su;
        this.truemdCode = truemdCode;
        this.truemdDrugCode = truemdDrugCode;
        this.truemdId = truemdId;
        this.type = type;
        this.uPrice = uPrice;
        this.uip = uip;
        this.webUrl = webUrl;
    }

    public String getCi_fd() {
        return ci_fd;
    }

    public void setCi_fd(String ci_fd) {
        this.ci_fd = ci_fd;
    }

    public String getCi_lb() {
        return ci_lb;
    }

    public void setCi_lb(String ci_lb) {
        this.ci_lb = ci_lb;
    }

    public String getCi_lc() {
        return ci_lc;
    }

    public void setCi_lc(String ci_lc) {
        this.ci_lc = ci_lc;
    }

    public String getCi_pg() {
        return ci_pg;
    }

    public void setCi_pg(String ci_pg) {
        this.ci_pg = ci_pg;
    }

    public String getDiscountPerc() {
        return discountPerc;
    }

    public void setDiscountPerc(String discountPerc) {
        this.discountPerc = discountPerc;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Generic[] getGenerics() {
        return generics;
    }

    public void setGenerics(Generic[] generics) {
        this.generics = generics;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIwa() {
        return iwa;
    }

    public void setIwa(String iwa) {
        this.iwa = iwa;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getMf() {
        return mf;
    }

    public void setMf(String mf) {
        this.mf = mf;
    }

    public String getMfId() {
        return mfId;
    }

    public void setMfId(String mfId) {
        this.mfId = mfId;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getoPrice() {
        return oPrice;
    }

    public void setoPrice(String oPrice) {
        this.oPrice = oPrice;
    }

    public String getpForm() {
        return pForm;
    }

    public void setpForm(String pForm) {
        this.pForm = pForm;
    }

    public String getpSize() {
        return pSize;
    }

    public void setpSize(String pSize) {
        this.pSize = pSize;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(String prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public String getProductGroupBrandName() {
        return productGroupBrandName;
    }

    public void setProductGroupBrandName(String productGroupBrandName) {
        this.productGroupBrandName = productGroupBrandName;
    }

    public String getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getSaltInfo() {
        return saltInfo;
    }

    public void setSaltInfo(String saltInfo) {
        this.saltInfo = saltInfo;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSeverId() {
        return severId;
    }

    public void setSeverId(String severId) {
        this.severId = severId;
    }

    public String getSu() {
        return su;
    }

    public void setSu(String su) {
        this.su = su;
    }

    public String getTruemdCode() {
        return truemdCode;
    }

    public void setTruemdCode(String truemdCode) {
        this.truemdCode = truemdCode;
    }

    public String getTruemdDrugCode() {
        return truemdDrugCode;
    }

    public void setTruemdDrugCode(String truemdDrugCode) {
        this.truemdDrugCode = truemdDrugCode;
    }

    public String getTruemdId() {
        return truemdId;
    }

    public void setTruemdId(String truemdId) {
        this.truemdId = truemdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getuPrice() {
        return uPrice;
    }

    public void setuPrice(String uPrice) {
        this.uPrice = uPrice;
    }

    public String getUip() {
        return uip;
    }

    public void setUip(String uip) {
        this.uip = uip;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    String  uPrice;
            String uip;
          String   webUrl;


}
