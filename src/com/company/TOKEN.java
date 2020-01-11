package com.company;

public class TOKEN {
    String CP;
    String VP;
    Integer L_N0;
    Main main;


    public TOKEN() {
    }

    public TOKEN(String CP, String VP, Integer l_N0) {
        this.CP = CP;
        this.VP = VP;
        L_N0 = l_N0;
    }

    public String getCP() {

        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getVP() {
        return VP;
    }

    public void setVP(String VP) {
        this.VP = VP;
    }

    public Integer getL_N0() {
        return L_N0;
    }

    public void setL_N0(Integer l_N0) {
        this.L_N0 = l_N0;
    }

    public void printToken(String cp, String vp, Integer l_no){
        this.main=new Main();
        System.out.println("ClassPart: " + cp +" ,  "+"Value part: "+vp+"  ,"+"Line Number: "+l_no+" , ");
    }

    @Override
    public String toString() {
        return "TOKEN{" +
                "CP='" + CP + '\'' +
                ", VP='" + VP + '\'' +
                ", L_N0=" + L_N0 +
                '}';
    }
}
