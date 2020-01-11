package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ICG {
    public static int tIndex = 1;
    public static int lIndex = 1;
    public static String variableName="", functionName = "", functionReturnType="", functionArgs = "",callfunctionName="";
    public static Stack<String> varNameStack=new Stack<>();
    public static Stack<String> operatorStack=new Stack<>();
    public static String currentTemp="t";
    public static Stack<String> parameterStack = new Stack<>();
    public static String intermediateCode = "";
    public static String calledFunc="";
    public static String calledFuncParams="";


    public static String createTemp(){
        return "t"+tIndex++;
    }

    public static String createLabel(){
        return "L"+lIndex++;
    }

    public static void generateOutput(String IcgCode){

//        System.out.println(IcgCode + "\n");
        intermediateCode += "\n"+IcgCode + "\n";

    }
}
