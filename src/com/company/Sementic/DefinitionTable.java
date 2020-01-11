package com.company.Sementic;

import java.util.ArrayList;
import java.util.List;

public class DefinitionTable {
    public String name;
    public String type;
    public String  category;
    public String parent;
    public List<ClassTable> classTableList=new ArrayList<ClassTable>();

    public DefinitionTable() {

    }

    public DefinitionTable(String name, String type, String category, String parent, List<ClassTable> classTableList) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.parent = parent;
        this.classTableList = classTableList;
    }

    public List<ClassTable> getClassTableList() {
        return classTableList;
    }

    public void setClassTableList(List<ClassTable> classTableList) {
        this.classTableList = classTableList;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
