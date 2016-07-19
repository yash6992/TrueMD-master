package info.truemd.android.model;

/**
 * Created by yashvardhansrivastava on 06/02/16.
 */


public class SuggestGetSet {

    String id;
    String name;
    String manufacturer;
    String mrp;
    String packSize;


    public SuggestGetSet(String id, String name, String manufacturer, String packSize, String mrp) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.mrp = mrp;
        this.packSize = packSize;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
