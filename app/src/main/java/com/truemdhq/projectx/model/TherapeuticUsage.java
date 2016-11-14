package com.truemdhq.projectx.model;

/**
 * Created by yashvardhansrivastava on 20/02/16.
 */
public class TherapeuticUsage {

    String description,name;

    public TherapeuticUsage(){

    }

    public TherapeuticUsage(String description, String name){
        this.setDescription(description);
        this.setName(name);

    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//          therapeuticUsages: [
//        {
//            gId: "1",
//                    therapeuticClass: {
//            gId: "1",
//                    name: "Cardiovascular Disorders",
//                    description: null
//        },
//            name: "Cardiovascular Disorders",
//                    description: null
//        },
//        {
//            gId: "8",
//                    therapeuticClass: {
//            gId: "8",
//                    name: "Hematologic Disorders",
//                    description: null
//        },
//            name: "Hematologic Disorders",
//                    description: null
//        }
//        ],