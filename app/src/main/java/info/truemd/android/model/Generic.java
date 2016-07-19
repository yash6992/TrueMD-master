package info.truemd.android.model;

/**
 * Created by yashvardhansrivastava on 19/02/16.
 */
public class Generic {

            TherapeuticUsage[] therapeuticUsages;
            String tc;
    String uses;

            String aim;
            String  alcoholUse;
            String ci;
            String ci_fd;
            String ci_lb;
            String ci_lc;

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    String ci_pg;
            String di;
            String dosAndDont;
            String dosage;
            String faqs;
            String fd;
            String furtherInformation;
            String gId;
            String indications;
            String iwa;
            String lb;
            String lc;
            String mc;
            String moa;
            String name;
            String notSaleableReason;
            String pg;
            String prescriptionRequired;
            String saleable;
            String sc;
            String schedule;
            String se;
            String severId;
            String  source;
            String strength;
            Medline medline;


    public Medline getMedline() {
        return medline;
    }

    public void setMedline(Medline medline) {
        this.medline = medline;
    }

    public Generic() {
    }

    public Generic(TherapeuticUsage[] therapeuticUsages, String tc, String aim, String alcoholUse, String ci, String ci_fd, String ci_lb, String ci_lc, String ci_pg, String di, String dosAndDont, String dosage, String faqs, String fd, String furtherInformation, String gId, String indications, String iwa, String lb, String lc, String mc, String moa, String name, String notSaleableReason, String pg, String prescriptionRequired, String saleable, String sc, String schedule, String se, String severId, String source, String strength) {
        this.therapeuticUsages = therapeuticUsages;
        this.tc = tc;
        this.aim = aim;
        this.alcoholUse = alcoholUse;
        this.ci = ci;

        this.ci_fd = ci_fd;
        this.ci_lb = ci_lb;
        this.ci_lc = ci_lc;
        this.ci_pg = ci_pg;
        this.di = di;
        this.dosAndDont = dosAndDont;
        this.dosage = dosage;
        this.faqs = faqs;
        this.fd = fd;
        this.furtherInformation = furtherInformation;
        this.gId = gId;
        this.indications = indications;
        this.iwa = iwa;
        this.lb = lb;
        this.lc = lc;
        this.mc = mc;
        this.moa = moa;
        this.name = name;
        this.notSaleableReason = notSaleableReason;
        this.pg = pg;
        this.prescriptionRequired = prescriptionRequired;
        this.saleable = saleable;
        this.sc = sc;
        this.schedule = schedule;
        this.se = se;
        this.severId = severId;
        this.source = source;
        this.strength = strength;
    }

    public TherapeuticUsage[] getTherapeuticUsages() {
        return therapeuticUsages;
    }

    public void setTherapeuticUsages(TherapeuticUsage[] therapeuticUsages) {
        this.therapeuticUsages = therapeuticUsages;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getAlcoholUse() {
        return alcoholUse;
    }

    public void setAlcoholUse(String alcoholUse) {
        this.alcoholUse = alcoholUse;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getDosAndDont() {
        return dosAndDont;
    }

    public void setDosAndDont(String dosAndDont) {
        this.dosAndDont = dosAndDont;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFaqs() {
        return faqs;
    }

    public void setFaqs(String faqs) {
        this.faqs = faqs;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getFurtherInformation() {
        return furtherInformation;
    }

    public void setFurtherInformation(String furtherInformation) {
        this.furtherInformation = furtherInformation;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
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

    public String getMoa() {
        return moa;
    }

    public void setMoa(String moa) {
        this.moa = moa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotSaleableReason() {
        return notSaleableReason;
    }

    public void setNotSaleableReason(String notSaleableReason) {
        this.notSaleableReason = notSaleableReason;
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

    public String getSaleable() {
        return saleable;
    }

    public void setSaleable(String saleable) {
        this.saleable = saleable;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSe() {
        return se;
    }

    public void setSe(String se) {
        this.se = se;
    }

    public String getSeverId() {
        return severId;
    }

    public void setSeverId(String severId) {
        this.severId = severId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }


//
//    {
//        _id: {},
//        aim: null,
//                alcoholUse: null,
//            ci: null,
//            ci_fd: "NA",
//            ci_lb: "NA",
//            ci_lc: "L5",
//            ci_pg: "B",
//            di: null,
//            dosAndDont: "Do not take clopidogrel:<ul><li>if you are allergic (hypersensitive) to clopidogrel or any of the other ingredients of this medicine.</li><li>if you have a medical condition that is causing internal bleeding such as a stomach ulcer or bleeding within the brain.</li><li>if you suffer from severe liver disease.</li></ul>If you take more clopidogrel tablet than you should:<ul><li>Contact your doctor or the nearest emergency department because of the increased risk of bleeding.</li></ul>If you forget to take a dose of Clopidogrel tablets, but remember within 12 hours of your usual time, take your tablet immediately and then take your next tablet at the usual time.If you forget for more than 12 hours, simply take the next single dose at the usual time. Do not take a double dose to make up for a forgotten tablet.",
//            dosage: null,
//            faqs: "<b>Q. </b>Is clopidogrel/ Plavix/ Ceruvin/ Deplatt/ Clopilet/ Clopivas safe in pregnancy?No. It is not safe in pregnancy, please follow the advice of the doctor regarding its use.<br><b>Q. </b>What is Deplatt CV 20?Deplatt CV 20 is a combination of clopidogrel 75 mg, atorvastatin 10 mg and aspirin 75 mg.<br><b>Q.</b> Is clopidogrel a narcotic/ controlled substance?No. Clopidogrel is not narcotic/ controlled substance.",
//            fd: "NA",
//            furtherInformation: null,
//            gId: 209634,
//            indications: "Clopidogrel is used to prevent blood clots (thrombi) forming in hardened blood vessels (arteries), a process known as atherothrombosis, which can lead to atherothrombotic events such as stroke, heart attack, or death.",
//            iwa: false,
//            lb: "No Studies Available",
//            lc: "The drug is contraindicated in women who are breastfeeding an infant.",
//            mc: "M",
//            moa: "Clopidogrel belongs to a class of medicines called antiplatelet. It’s a “blood thinner”. It works by blocking certain blood cells called platelets and prevents them from forming harmful blood clots. This "anti-platelet" effect helps keep blood flowing smoothly in your body.",
//            name: "Clopidogrel",
//            notSaleableReason: null,
//            pg: "The drug can be taken during pregnancy if the benefit outweigh the risk. Patients should follow the advice of the doctor regarding its use.",
//            prescriptionRequired: false,
//            saleable: true,
//            sc: null,
//            schedule: "H",
//            se: "sensation of tingling and numbness, nose, Easy bruising, indigestion or heartburn, unusual bleeding, bleeding in the stomach or bowels, abdominal pain, abnormal blood counts, vomiting, urge to vomit, loose motions, blood in the urine, constipation, dizziness, rash, gas, stomach ulcer, headache, itching.",
//            severId: null,
//            source: null,
//            strength: "75 mg",
//            tc: "CARDIAC",
//            therapeuticUsages: [
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
//        webUrl: "/details/generics/209634/Clopidogrel"
//    }
//
//
//





}
