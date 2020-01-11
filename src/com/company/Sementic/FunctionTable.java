package com.company.Sementic;

public class FunctionTable {

    public String name;
    public String type;
    public int scope;

    public FunctionTable() {
    }

    public FunctionTable(String name, String type, int scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
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

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }
}
