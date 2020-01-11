package com.company.Sementic;

public class ClassTable {

    public String name;
    public String type;
    public String AM;
    public String constant;
    public String TM;

    public ClassTable() {
    }

    public ClassTable(String name, String type, String AM, String constant, String TM) {
        this.name = name;
        this.type = type;
        this.AM = AM;
        this.constant = constant;
        this.TM = TM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAM() {
        return AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getTM() {
        return TM;
    }

    public void setTM(String TM) {
        this.TM = TM;
    }
}
