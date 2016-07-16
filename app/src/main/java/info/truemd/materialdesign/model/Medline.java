package info.truemd.materialdesign.model;

/**
 * Created by yashvardhansrivastava on 02/04/16.
 */
public class Medline {

    String why;
    String precaution;
    String storage;
    String diet;


    String how;

    public Medline(String why, String precaution, String storage, String diet, String how) {
        this.why = why;
        this.precaution = precaution;
        this.storage = storage;
        this.diet = diet;
        this.how = how;
    }

    public String getWhy() {

        return why;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }


    public void setWhy(String why) {
        this.why = why;
    }

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
}
