package com.company;

import com.company.Sementic.ClassTable;
import com.company.Sementic.DefinitionTable;
import com.company.Sementic.FunctionTable;
//import com.sun.org.apache.bcel.internal.generic.GOTO;
//import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.instrument.ClassDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SementicAnalyzer {

    //DEFINITIONTABLE VARIABLES
    public static String className = "";
    public static String classCat = "General";
    public static String classType = "class";
    public static String classParent = "Object";

    //CLASS DEFINITION TABLE VARIABLES

    public static String memberName = "";
    public static String memberAM = "private";
    public static String memberCONSTANT = "No";
    public static String memberType = "";
    public static String memberTM = "General";
    public static String memberReturnType = "";


    //FUNCTION TABLE VARIABLES

    public static String FT_name = "";
    public static String FT_type = "";
    public static int FT_scope = 0;
    public static Boolean inheritanceCheck = true;

    //SEMENTIC VARIABLES

    public static Stack<Integer> scopeStack = new Stack<>();
    public static List<String> errors = new ArrayList<>();
    public static List<DefinitionTable> definitionTableList = new ArrayList<>();
    public static List<FunctionTable> functionTableList = new ArrayList<>();
    public static Integer scope = 0;

    //ASSIGNMENT STATEMENT
    public static String globalTypeParent = "";
    public static String globalChild = "";
    public static String globalParams = "";
    public static Boolean paramsCheck = false;


    //COMPATIBILITY

    public static Stack<String> typeStack = new Stack<>();
    public static Stack<String> operatorStack = new Stack<>();
    public static Boolean conditionFlag=false;
    //OBJECT PARAMS N TYPE
    public static String objName = "";
    public static String objType = "";


    public static void resetObjAttr() {
        objName = "";
        objType = "";
    }

    public SementicAnalyzer() {

    }

    public static void createScope() {
        scopeStack.push(++scope);
    }

    public static void destroyScope() {
        scopeStack.pop();
    }

    public static int currentScope() {
        return (scopeStack.isEmpty()) ? 0 : scopeStack.peek();
    }

    //DEFINITIONTABLE FUNCTIONS

    public static void resetDefinitionTable() {
        classParent = "Object";
        classCat = "General";
        classType = "class";
    }

    public static Boolean lookupInDefinitionTable(String name) {


        for (DefinitionTable d : definitionTableList)
            if (d.name.equals(name)) {
                if (inheritanceCheck) {
                    if (d.category.equals("final")) {
                        finalClassError();
                        return false;
                    }
                    return true;
                } else {
                    return true;
                }
//                        else if(!inheritanceCheck) {
//                            classNotFoundError(name);
//                            return true;
//                        }

            }
        if (!inheritanceCheck) {
            classNotFoundError(name);
        }

        return false;
    }

    public static Boolean insertInDefinitionTable() {
        if (!lookupInDefinitionTable(className)) {
            definitionTableList.add(new DefinitionTable(className, classType, classCat, classParent, new ArrayList<ClassTable>()));
            return true;
        }

        return false;
    }


    //CLASS_DEFINITIONTABLE FUNCTIONS

    public static void resetClassMembers() {

        memberAM = "private";
        memberCONSTANT = "No";
        memberType = "";
        memberTM = "General";
        memberReturnType = "";
    }

    public static Boolean insertToClassTable() {


        if (!lookupInClassTable()) {
            for (DefinitionTable d : definitionTableList) {
                if (d.name.equals(className)) {
                    d.classTableList.add(new ClassTable(memberName, memberType, memberAM, memberCONSTANT, memberTM));
                    return true;
                }
            }
        }

        return false;
    }

    public static Boolean lookupInClassTable() {

        Boolean classCheck = false;

        int typeIndex = memberType.indexOf("-");
        String newType = "";
        if (typeIndex != -1 && typeIndex != 0)
            newType = memberType.substring(0, typeIndex);

        if (typeIndex == -1 && !memberType.equals("void") && !memberType.equals("int") && !memberType.equals("float") && !memberType.equals("string") && !memberType.equals("char")) {
            for (DefinitionTable dt : definitionTableList)
                if (dt.name.equals(memberType)) {
                    classCheck = true;

                }
            if (!classCheck)
                errors.add("ERROR:" + memberType + " is not a known class");
        }


        for (DefinitionTable d : definitionTableList) {
            if (d.name.equals(className)) {
                for (ClassTable c : d.classTableList) {
                    if (c.name.equals(memberName)) {
                        if (typeIndex != -1 && typeIndex != 0 && c.type.indexOf("-") != 0 && c.type.indexOf("-") != -1 && c.type.substring(0, c.type.indexOf("-")).equals(newType)) {
                            return true;
                        } else if (typeIndex == -1 && c.type.indexOf("-") == -1)
                            return true;
                    }

                }
            }

        }
        return false;
    }

    //FUNCTION TABLE FUNCTIONS

    public static void resetFunctionTableVariables() {
        FT_scope = currentScope();
        FT_type = "";
    }

    public static String lookUpInFunctionTable() {

        for (FunctionTable ft : functionTableList) {
            if (ft.name.equals(FT_name) && ft.scope == currentScope())
                return ft.type;
        }
        return null;
    }

    public static Boolean insertIntoFunctionTable() {
        if (lookUpInFunctionTable() == null) {
            functionTableList.add(new FunctionTable(FT_name, FT_type, FT_scope));
            return true;
        }
        return false;
    }


    //GLOBAL LOOKUP

    public static String globalLookup(String str, String classPart) {


        Object[] scopeStackArray = scopeStack.toArray();

        //SEARACH IN FUNCTION_TABLE
        for (int i = scopeStackArray.length - 1; i >= 0; i--) {
            for (FunctionTable ft : functionTableList) {
                if (ft.scope == (int) scopeStackArray[i]) {
                    if (ft.name.equals(str))
                        return ft.type;
                }
            }
        }

        //SEARCH IN CLASS_DEFINITION _TABLE

        String parent = "Object";
        for (DefinitionTable dt : definitionTableList) {
            if (dt.name.equals(className)) {
                parent = dt.parent;
                for (ClassTable ct : dt.classTableList) {
                    if (ct.name.equals(str))
                        return ct.type;
                }
            }
        }

        //SEARCH IN PARENT CLASS
        if (!parent.equals("Object")) {
            for (DefinitionTable dt : definitionTableList) {
                if (dt.name.equals(parent)) {

                    for (ClassTable ct : dt.classTableList) {
                        if (ct.name.equals(str) && !ct.AM.equals("private"))
                            return ct.type;
                    }
                }
            }
        }

        if (classPart != null) {
            if (classPart.equals("INTEGER_CONSTANT"))
                return "int";
            else if (classPart.equals("FLOAT_CONSTANT"))
                return "float";
            else if (classPart.equals("BOOLEAN_CONSTANT"))
                return "boolean";
            else if (classPart.equals("CHAR_CONSTANT"))
                return "char";
            else if (classPart.equals("STRING_CONSTANT"))
                return "string";
        }
        //
        return null;


    }


    //FUNCTION LOOKUP

//    public static String lookupForFunction() {
//        Boolean flag = false;
//
//        String constructorType = FT_type.substring(0, FT_type.indexOf("-"));
//        for (DefinitionTable d : definitionTableList) {
//            if (d.name.equals(FT_name) && !d.category.equals("abstract")) {
//                for (ClassTable c : d.classTableList) {
//
//                }
//            }
//        }
//        if (!flag && constructorType.equals("void"))
//            return constructorType;
//
//        errors.add("ERROR:Constructor with following parameters not found");
//        return null;
//
//
//    }


    //DOT OPERATOR TYPE RETURN

    public static String dotOperatorType(Boolean isFunc) {

        if (globalTypeParent != null && globalChild != null && !globalTypeParent.equals("float") && !globalTypeParent.equals("string") && !globalTypeParent.equals("int")) {
            //SEARCH IN CLASS_DEFINITION _TABLE
            String parent = "Object";
            if (!isFunc) {


                for (DefinitionTable dt : definitionTableList) {
                    if (dt.name.equals(globalTypeParent)) {
                        parent = dt.parent;
                        for (ClassTable ct : dt.classTableList) {
                            if (ct.name.equals(globalChild))
                                if (ct.type.indexOf("-") == -1)
                                    return ct.type;
//                                else
//                                    return ct.type.substring(ct.type.indexOf(">") + 1);
                        }
                    }
                }

                //SEARCH IN PARENT CLASS
                if (!parent.equals("Object")) {
                    for (DefinitionTable dt : definitionTableList) {
                        if (dt.name.equals(parent)) {

                            for (ClassTable ct : dt.classTableList) {
                                if (ct.name.equals(globalChild) && !ct.AM.equals("private"))
                                    if (ct.type.indexOf("-") == -1)
                                        return ct.type;
//                                    else
//                                        return ct.type.substring(ct.type.indexOf(">") + 1);
                            }
                        }
                    }
                }
            } else {


                parent = "Object";

                for (DefinitionTable dt : definitionTableList) {

                    if (globalTypeParent.equals(dt.name)) {
                        parent = dt.parent;
                        for (ClassTable ct : dt.classTableList) {
                            if (ct.name.equals(globalChild) && ct.type.indexOf("-") != -1 && !ct.TM.equals("abstract") && ct.type.substring(0, ct.type.indexOf("-")).equals(globalParams)) {
                                if (ct.type.indexOf("-") == -1)
                                    return ct.type;
                                else
                                    return ct.type.substring(ct.type.indexOf(">") + 1);
                            }

                        }
                    }
                }

                //SEARCH IN PARENT CLASS
                if (!parent.equals("Object")) {
                    for (DefinitionTable dt : definitionTableList) {
                        if (dt.name.equals(parent)) {

                            for (ClassTable ct : dt.classTableList) {
                                if (ct.name.equals(globalChild) && !ct.AM.equals("private") && !ct.TM.equals("abstract") && ct.type.substring(0, ct.type.indexOf("-")).equals(globalParams))
                                    if (ct.type.indexOf("-") == -1)
                                        return ct.type;
                                    else
                                        return ct.type.substring(ct.type.indexOf(">") + 1);
                            }
                        }
                    }
                }
            }
        }
        //
        return null;


    }

    public static String THIS_LOOKUP(String str)
    {

        for (DefinitionTable dt : definitionTableList) {
            if (dt.name.equals(className)) {
                for (ClassTable ct : dt.classTableList) {
                    if (ct.name.equals(str))
                        return ct.type;
                }
            }
        }
        return null;
    }

    public static String BASE_LOOKUP(String str)
    {
        String parent = "Object";
        for (DefinitionTable dt : definitionTableList) {
            if (dt.name.equals(className)) {
                parent = dt.parent;

            }
        }
        if(!parent.equals("Object"))
        {
            for (DefinitionTable dt : definitionTableList) {
                if (dt.name.equals(parent)) {
                   return dt.type;
                }
            }
        }
        return null;
    }

//********ERRORS*************

    public static void redeclarationError(int lineNo) {
        errors.add("ERROR: Class already declared at Line no: " + lineNo);
    }

    public static void memberRedeclarationError(int lineNo) {
        errors.add("ERROR: Member redeclaration error at Line no: " + lineNo);
    }

    public static void inheritError(int lineNo) {
        errors.add("ERROR: The inherited class not found at Line no: " + lineNo);
    }

    public static void finalClassError() {
        errors.add("ERROR: Final class cannot be inherited");
    }

    public static void classNotAbstractError(int lineNo) {
        for (DefinitionTable d : definitionTableList) {

            if (d.name.equals(className)) {
                if (!d.category.equals("abstract"))
                    errors.add("ERROR: Abstract functions cannot be initialized as the class is not abstract at line no : " + lineNo);
            }
        }
    }

    public static void constructorError(int lineNo) {
        errors.add("ERROR: Constructor with same signature already present at line no: " + lineNo);
    }

    public static void classNotFoundError(String className) {
        errors.add("ERROR: Class with name " + className + " not found");
    }

    public static void returnStatementError(int lineNo) {

        Boolean stackNotEmpty = !typeStack.isEmpty();
        if (SementicAnalyzer.memberName.equals("constructor"))
            errors.add("ERROR:Constructor cannot have a return type at line no " + lineNo);
        else {
            String parent = "Object";
            for (DefinitionTable dt : definitionTableList) {
                if (dt.name.equals(className)) {
                    parent = dt.parent;
                    for (ClassTable ct : dt.classTableList) {
                        if (ct.name.equals(memberName)) {
                            if (ct.type.indexOf("-") != -1 && ct.type.substring(ct.type.indexOf(">") + 1).equals("void")) {
                                errors.add("ERROR:Void functions cannot have return type at line no" + lineNo);

                            } else if (ct.type.indexOf("-") != -1 && !ct.type.substring(ct.type.indexOf(">") + 1).equals("void")) {

                                if (stackNotEmpty) {
                                    if (ct.type.substring(ct.type.indexOf(">") + 1).equals("int") && !typeStack.peek().equals("int"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no:" + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("float") && !typeStack.peek().equals("float"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no: " + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("string") && !typeStack.peek().equals("string"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no: " + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("boolean") && !typeStack.peek().equals("boolean"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("char") && !typeStack.peek().equals("char"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                    else if (stackNotEmpty && !ct.type.substring(ct.type.indexOf(">") + 1).equals(typeStack.peek()))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                } else if (!stackNotEmpty && globalTypeParent != null) {
                                    if (ct.type.substring(ct.type.indexOf(">") + 1).equals("int") && !globalTypeParent.equals("int"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no:" + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("float") && !globalTypeParent.equals("float"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no: " + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("string") && !globalTypeParent.equals("string"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no: " + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("boolean") && !globalTypeParent.equals("boolean"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                    else if (ct.type.substring(ct.type.indexOf(">") + 1).equals("char") && !globalTypeParent.equals("char"))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                    else if (stackNotEmpty && !ct.type.substring(ct.type.indexOf(">") + 1).equals(globalTypeParent))
                                        errors.add("ERROR:Function type and return type doesnot match at line no :" + lineNo);
                                }
                            }
                            return;
                        }
                    }
                }
            }

            //SEARCH IN PARENT CLASS
            if (!parent.equals("Object")) {
                for (DefinitionTable dt : definitionTableList) {
                    if (dt.name.equals(parent)) {

                        for (ClassTable ct : dt.classTableList) {
                            if (ct.name.equals(memberName)) {
                                if (ct.type.indexOf("-") != -1 && ct.type.substring(ct.type.indexOf(">") + 1).equals("void")) {
                                    errors.add("ERROR:Void functions cannot have return type at line no " + lineNo);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }


    }

    public static void objectTypeCheckError(int lineNo) {
        Boolean constCheck = false;
        String type = "";
        for (DefinitionTable dt : definitionTableList) {
            if (dt.name.equals(objName)) {

                for (ClassTable ct : dt.classTableList) {

                    if (ct.name.equals("constructor")) {
                        if (ct.type.substring(0, ct.type.indexOf("-")).equals(objType)) {
                            return;
                        }
                        constCheck = true;
                    }
                }
            }
        }

        if (!constCheck && !objType.equals("void"))
            errors.add("ERROR:Constructor with the following parameters not found in class " + objName + " at line no: " + lineNo);
        else if (constCheck)
            errors.add("ERROR:Constructor with the following parameters not found in class " + objName + " at line no: " + lineNo);

    }


    public static void checkCompatibleTypes(int lineNo) {
        if (typeStack.size() > 1 && operatorStack.size() > 0) {
            String tempType = Compatibility(typeStack.pop(), typeStack.pop(), operatorStack.pop());
            if (tempType != null) {
                if (!tempType.equals("")) {
                    typeStack.push(tempType);
                }
            } else {
                errors.add("ERROR:Types are not compatible at line no " + lineNo);
            }
        }
        if(ICG.varNameStack.size()>1 && ICG.operatorStack.size() > 0)
        {
            String op=ICG.operatorStack.pop();
            String temp=ICG.createTemp();
            String v1=ICG.varNameStack.pop();
            String v2=ICG.varNameStack.pop();
            if(!isAssignmentOperator(op))
                ICG.generateOutput(temp+" = "+v2+" "+op+" "+v1);
            else
                ICG.generateOutput(v2+op+v1);
            ICG.currentTemp=temp;
            ICG.varNameStack.push(temp);

        }


    }

    public static String Compatibility(String operator)
    {
        if (typeStack.size()!=0 && typeStack.peek().equals("boolean")) {
           if(operator.equals("!"))
           {
               return "boolean";
           }
        }
        return null;
    }

    public static String Compatibility(String Left, String Right, String Operator) {

        if (Left.equals("int")) {
            if (isArithmeticOperator(Operator)) {
                if (Right.equals("int"))
                    return "int";
                else if (Right.equals("float"))
                    return "float";
            } else if (isRelationalOperator(Operator)) {
                if (Right.equals("int") || Right.equals("float"))
                    return "boolean";
            } else if (isAssignmentOperator(Operator)) {
                if (Right.equals("int"))
                    return "";
            }

        } else if (Left.equals("float")) {
            if (isArithmeticOperator(Operator)) {
                if (Right.equals("float") || Right.equals("int"))
                    return "float";
            } else if (isRelationalOperator(Operator)) {
                if (Right.equals("int") || Right.equals("float"))
                    return "boolean";
            } else if (isAssignmentOperator(Operator)) {
                if (Right.equals("float"))
                    return "";
            }
        } else if (Left.equals("char")) {
            if (Operator.equals("==")) {
                if (Right.equals("char"))
                    return "boolean";
            } else if (Operator.equals("=")) {
                if (Right.equals("char"))
                    return "";
            }
        } else if (Left.equals("string")) {
            if (isRelationalOperator(Operator)) {
                if (Right.equals("string"))
                    return "boolean";
            } else if (Operator.equals("+")) {
                if (Right.equals("char") || Right.equals("string"))
                    return "string";
            } else if (isAssignmentOperator(Operator)) {
                if (Right.equals("string"))
                    return "";
            }
        } else if (Left.equals("boolean")) {
            if (Operator.equals("&&") || Operator.equals("||") || Operator.equals("==")) {
                if (Right.equals("boolean"))
                    return "boolean";
            } else if (Operator.equals("=")) {
                if (Right.equals("boolean"))
                    return "";
            }
        }

        if (isAssignmentOperator(Operator)) {
            String parent = "";
            for (DefinitionTable dt : definitionTableList) {
                if (dt.name.equals(Left)) {
                    parent = dt.parent;
                    if (dt.category.equals("abstract")) {
                        errors.add("ERROR: " + dt.name + " is a abstract class");
                        return "";
                    }
                } else if (dt.name.equals(Right)) {
                    if (!dt.category.equals("abstract") && Right.equals(parent))
                        return "";
                }
            }

            int absFuncParent = 0, absFuncChild = 0;

            if (!parent.isEmpty() && !parent.equals("Object")) {
                for (DefinitionTable dt : definitionTableList) {
                    if (dt.name.equals(parent) || dt.category.equals("abstract")) {
                        for (ClassTable ct : dt.classTableList) {
                            if (ct.TM.equals("abstract")) {
                                absFuncParent++;
                                for (DefinitionTable dt2 : definitionTableList) {
                                    if (dt2.name.equals(Left)) {
                                        for (ClassTable leftObj : dt2.classTableList) {
                                            if (ct.type.indexOf("-") != -1 && leftObj.type.indexOf("-") != -1 && ct.name.equals(leftObj.name) && ct.type.substring(0, ct.type.indexOf("-")).equals(leftObj.type.substring(0, leftObj.type.indexOf("-")))) {
                                                absFuncChild++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    if (absFuncChild != absFuncParent) {
                        errors.add("ERROR:Child class has not implemented the Parent class " + parent + " abstract method");
                        return "";
                    } else if (absFuncChild == absFuncParent && (Right.equals(parent) || Right.equals(Left)))
                        return "";
//        else if(Left.equals(Right)){ //Non premitive assignmen
                }


            }
            if (absFuncChild == absFuncParent && Left.equals(Right))
                return "";
        }
//            return "";
//        }


        return null; // Report Error
    }


    public static boolean isArithmeticOperator(String Operator) {
        if (Operator.equals("-") || Operator.equals("/") || Operator.equals("+") || Operator.equals("*"))
            return true;

        return false;
    }

    public static boolean isAssignmentOperator(String Operator) {
        if (Operator.equals("="))
            return true;
        return false;
    }


    public static boolean isRelationalOperator(String Operator) {
        if (Operator.matches("==|<|>|>=|<=|!="))
            return true;

        return false;
    }
}

