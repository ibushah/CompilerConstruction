package com.company;

import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.io.IOException;
import java.util.*;

public class Main {
    public static Integer Topindex = 0;
    public static Integer count = 0;
    public static Integer count_line = 1;
    public static String[][] operators;
    public static List<String> stringList = new ArrayList<String>();
    public static List<Integer> integerList = new ArrayList<>();
    public static List<TOKEN> tokenList = new ArrayList<>();


//    char a='\n';


    public static String BreakWord(String line, Integer index) {
        String temp = "";
        Integer i = index;
        while (line.length() > 0 && i < line.length()) {

            if (line.toCharArray()[i] == ' ' || line.toCharArray()[i] == '\n') {

                if (line.toCharArray()[i] == '\n') {

                    i++;
                    Topindex = i;
                    count = count - 1;
                    count_line = count_line + 1;
                    if (i >= line.length()) {
                        count_line = count_line - 1;
                        return temp;
                    }
                }

                if (temp != "" && temp != "\t") {
                    Topindex = i;
                    return temp;
                }
            }


            //Multi line commment
            if (line.toCharArray()[i] == '/') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                } else if (!Character.isLetterOrDigit(line.charAt(i + 1)) && line.charAt(i + 1) != '/') {
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                }

                if (i < line.length() && line.charAt(i) == '*') {
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                    while (i < line.length()) {

                        if (i + 1 < line.length()) {
                            if (line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                                temp += line.toCharArray()[i];
                                i++;
                                count = count - 1;
                                Topindex = i;

                                temp += line.toCharArray()[i];
                                i++;
                                count = count - 1;
                                Topindex = i;
                                break;
                            }
                        }

                        temp += line.toCharArray()[i];
                        i++;
                        count = count - 1;
                        Topindex = i;
                    }
//                    if(line.charAt(i)=='*' && line.charAt(i+1)=='/'){
//                        temp += line.toCharArray()[i];
//                        i++;
//                        count = count - 1;
//                        Topindex = i;
//
//                        temp += line.toCharArray()[i];
//                        i++;
//                        count = count - 1;
//                        Topindex = i;
//                    }

                    return temp;

                }

            }

            //handling floats
            if (line.toCharArray()[i] == '.') {
                if (isStringOnlyAlphabet(temp) && !temp.matches("[0-9]+")) {
                    Topindex = i;
                    return temp;
                } else if ((line.toCharArray()[i] == '.') && Character.isLetter(line.toCharArray()[i + 1])) {
                    temp += line.toCharArray()[i];
                    i++;
                    Topindex = i;
                    count = count - 1;
                    return temp;
                } else if (temp.matches("[0-9]+") && (line.toCharArray()[i] == '.')) {
                    temp += line.toCharArray()[i];
                    i++;
                    Topindex = i;
                    count = count - 1;
                    while (isNumber(line.toCharArray(), i) || Character.isLetter(line.toCharArray()[i]) && count != 0) {
                        temp += line.toCharArray()[i];
                        i++;
                        Topindex = i;
                        count = count - 1;
                    }

                    return temp;
                } else if (line.toCharArray()[i] == '.') {
                    temp += line.toCharArray()[i];
                    i++;
                    Topindex = i;
                    count = count - 1;
                    while (isNumber(line.toCharArray(), i) || Character.isLetter(line.toCharArray()[i]) && count != 0) {
                        temp += line.toCharArray()[i];
                        i++;
                        Topindex = i;
                        count = count - 1;
                    }
                    return temp;
                }
            }

            if (line.toCharArray()[i] == '{') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }

                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == '}') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == '(') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == ')') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }

            if (line.toCharArray()[i] == ';') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == ',') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == ':') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == '?') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }

            if (line.toCharArray()[i] == '[') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }
            if (line.toCharArray()[i] == '\\') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);
            }

            if (line.toCharArray()[i] == ']') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                return returnWhatStoredInTemp(temp, line.toCharArray(), i);

            }


            if (line.toCharArray()[i] == '\'') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }

                if (line.charAt(i + 1) == '\\') {
                    for (int j = 0; j < 4; j++) {

                        if (i < line.toCharArray().length) {
                            temp += line.toCharArray()[i];
                            i++;
                            Topindex = i;
                            count++;
                        }
                    }

                } else if (line.charAt(i) != '\\') {
                    for (int j = 0; j < 3; j++) {
                        if (i < line.toCharArray().length) {
                            temp += line.toCharArray()[i];
                            i++;
                            Topindex = i;
                            count++;
                        }
                    }
                } else if (line.toCharArray()[i] == '\'') {
                    temp += line.toCharArray()[i];
                    i++;
                    Topindex = i;
                    count++;
                }

                return temp;

            }
            if (line.toCharArray()[i] == '+') {
                if (tokenList.size() - 1 < 0) {
                    return temp;
                }
                String checkVp = tokenList.get(tokenList.size() - 1).getVP();
                if (!checkVp.contains("=")) {
                    if (isStringOnlyAlphabet(temp)) {
                        Topindex = i;
                        return temp;
                    }
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                    return temp;
                }
                else {
                    if (isStringOnlyAlphabet(temp)) {
                        Topindex = i;
                        return temp;
                    }
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;


                    if (line.toCharArray()[i] == '+') {
                        return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                    }
                    if (line.toCharArray()[i] == '=') {
                        return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                    }
                    if (integerList.isEmpty()) {
                        while (Character.isDigit(line.toCharArray()[i])) {
                            temp += line.toCharArray()[i];
                            i++;
                            Topindex = i;
                            count--;
                        }
                        return temp;
                    } else if (integerList.size() > 0) {
                        return temp;
                    }
                }

            }
            if (line.toCharArray()[i] == '-') {
                if (tokenList.size() - 1 < 0) {
                    return temp;
                }
                String checkVp = tokenList.get(tokenList.size() - 1).getVP();
                if (!checkVp.contains("=")) {
                    if (isStringOnlyAlphabet(temp)) {
                        Topindex = i;
                        return temp;
                    }
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                    return temp;
                } else {


                    if (line.toCharArray()[i] == '-') {
                        return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                    }

                    if (line.toCharArray()[i] == '=') {
                        return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                    }
                    if (integerList.isEmpty()) {
                        while (Character.isDigit(line.toCharArray()[i])) {
                            temp += line.toCharArray()[i];
                            i++;
                            Topindex = i;
                            count--;
                        }
                        return temp;
                    } else if (integerList.size() > 0) {
                        return temp;
                    }


                }


            }


            if (line.toCharArray()[i] == '*') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }
            if (line.toCharArray()[i] == '/') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    if (line.toCharArray()[i] == '/') {
                        while (line.toCharArray()[i] != '\n') {
                            temp += line.toCharArray()[i];
                            i++;
                            count = count - 1;
                            Topindex = i;
                        }
                    }
                    return temp;
                }
            }


            if (line.toCharArray()[i] == '=') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }
            if (line.toCharArray()[i] == '<') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                }
                if (line.toCharArray()[i] == '<') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }
            if (line.toCharArray()[i] == '>') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                }
                if (line.toCharArray()[i] == '>') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }

            }
            if (line.toCharArray()[i] == '!') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }

            if (line.toCharArray()[i] == '&') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }

                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '&') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }


            if (line.toCharArray()[i] == '|') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }

                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '|') {

                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }
            if (line.toCharArray()[i] == '%') {
                if (isStringOnlyAlphabet(temp)) {
                    Topindex = i;
                    return temp;
                }

                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                Topindex = i;
                if (line.toCharArray()[i] == '=') {
                    return returnWhatStoredInTemp(temp, line.toCharArray(), i);
                } else {
                    return temp;
                }
            }
            if (line.toCharArray()[i] == '"') {
                temp += line.toCharArray()[i];
                i++;
                Topindex = i;
                count = count - 1;
                while (i < line.length() && count != 0 && line.toCharArray()[i] != '"' && line.charAt(i) != '\n') {
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                    if (i < line.length() && line.charAt(i) == '\\' && line.charAt(i + 1) == '"') {
                        temp += line.toCharArray()[i + 1];
                        i = i + 2;
                        count = count - 1;
                        Topindex = i;
                    }
//
                }
                if (i < line.length() && line.charAt(i) != '\n') {
                    temp += line.toCharArray()[i];
                    i++;
                    count = count - 1;
                    Topindex = i;
                    return temp;
                } else if (line.charAt(i) == '\n') {
                    return temp;
                }

            }


            if (line.toCharArray()[i] == '\\') {
                temp += line.toCharArray()[i];
                i++;
                count = count - 1;
                while (count != 0) {

                    if (line.toCharArray()[i] == '\\') {
                        temp += line.toCharArray()[i];
                        i++;
                        count = count - 1;
                        Topindex = i;
                        break;
                    } else {
                        temp += line.toCharArray()[i];
                        i++;
                        count = count - 1;
                        Topindex = i;
                    }
                }
                return temp;
            } else {
                while (line.toCharArray()[i] == ' ' || line.toCharArray()[i] == '\t') {
                    i++;
                    Topindex = i;
                    count = count - 1;

//                    count_line;
                    if (i >= line.length()) {

                        return temp;
                    }
                }

                while (line.toCharArray()[i] == '\n') {
                    i++;
                    Topindex = i;
                    count = count - 1;
                    count_line = count_line + 1;
                    if (i >= line.length()) {

                        return temp;
                    }
                }
                if (line.toCharArray()[i] != '(' && line.toCharArray()[i] != ')' && line.toCharArray()[i] != '{' &&
                        line.toCharArray()[i] != '}' && line.toCharArray()[i] != '[' && line.toCharArray()[i] != ']'
                        && line.toCharArray()[i] != '=' && line.toCharArray()[i] != '!' && line.toCharArray()[i] != '+'
                        && line.toCharArray()[i] != '-' && line.toCharArray()[i] != '*' && line.toCharArray()[i] != '/'
                        && line.toCharArray()[i] != '<' && line.toCharArray()[i] != '>' && line.toCharArray()[i] != '%' &&
                        line.toCharArray()[i] != ';' && line.toCharArray()[i] != '.' && line.toCharArray()[i] != '\"' && line.toCharArray()[i] != '\'' && line.toCharArray()[i] != ' ') {

                    temp += line.toCharArray()[i];
                    i++;
                    Topindex = i;
                    count = count - 1;
                }


            }
        }


        return temp;

    }

    public static boolean isStringOnlyAlphabet(String str) {

        return ((str != null) && (!str.equals("")) && (str.matches("^[a-z_A-Z_0-9]*$")));
//                && (str.matches("^[_a-zA-Z][_a-zA-Z0-9]")));
    }

    public static boolean isNumber(char str[], Integer i) {

        if (str[i] == '0' || str[i] == '1' || str[i] == '2' || str[i] == '3' || str[i] == '4' || str[i] == '5' || str[i] == '6' || str[i] == '7' || str[i] == '8' || str[i] == '9') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isIntegerConstant(String str) {
        return str.matches("^[+|-]?[0-9]{1,7}$");
    }

    public static boolean isCharConstant(String str) {

        return str.matches("^\'['\n'|'\t'|'\b'|'\r'|'\0'|'@'|'-'|'#'|'$'|'+'|'_'|'-'|'*'|'&'|'^'|'%'|'!'|' '|'|'|'\\'|'\"'|[a-zA-z0-9]{0,}]*\'$"
        );
    }


    public static boolean isFloatConstant(String str) {
        return str.matches("[+-]?([0-9]*[.])?[0-9]+");
    }

    public static String checkOperators(String str) {
        Integer op = 0;
        String ClassPart = "";
        while (op < operators.length) {
            if (operators[op][0].equals(str)) {
                ClassPart += operators[op][1];
                break;
            }
            op++;
        }
        if (ClassPart != "") {
            return ClassPart;
        } else {
            return ClassPart = "Invalid lexeme";
        }

    }

    static {
        operators = new String[][]{
                {"+", "PM"},
                {"-", "PM"},
                {"/", "MDM"},
                {"*", "MDM"},
                {"%", "MDM"},
                {"<", "ROP"},
                {">", "ROP"},
                {"<=", "ROP"},
                {">=", "ROP"},
                {"!=", "ROP"},
                {"==", "ROP"},
                {"&&", "OA"},
                {"||", "OA"},
                {"!", "NOT"},
                {"=", "ASSIGNMENT"},
                {"++", "INC_DEC"},
                {"--", "INC_DEC"},
                {"+=", "COMPOUND"},
                {"-=", "COMPOUND"},
                {"*=", "COMPOUND"},
                {"/=", "COMPOUND"},
                {"%=", "COMPOUND"},
                {".", "DOT"},
                {"!=", "ROP"}

        };
    }

    public static Character singleOperators[];

    static {
        singleOperators = new Character[]{'+', '-', '/', '*', '%', '>', '<', '!', '=', '|', '&'};
    }

    public static Character escapeCharatcers[];

    static {
        escapeCharatcers = new Character[]{'t', 'n', 'r', '0', '\\', '"', '\''};
    }

    public static String[][] punctuators = {
            {"{", "{"},
            {"}", "}"},
            {"(", "("},
            {")", ")"},
            {":", ":"}, {";", ";"},
            {",", ","}, {"?", "?"},
            {"[", "["}, {"]", "]"}

    };
    public static Character[] singlePunctuators = {
            '{', '}', '(', ')', ':', ';', ',', '?', '[', ']'
    };

    public static String[] singleKeywords = {
            "if", "main", "switch", "case", "else", "default", "break", "continue", "abstract", "private",
            "protected",
            "public",
            "static",
            "int",
            "void",
            "float",
            "boolean",
            "new",
            "for",
            "return",
            "this",
            "char",
            "long",
            "final",
            "base",
            "while",
            "class",
            "string",
            "constructor",
            "do",
            "extends",
            "List",
            "Arrays",
            "asList",
            "ArrayList",
            "true",
            "false"

    };

    public static String[][] keyWords = {
            {"if", "if"},
            {"main", "main"},
            {"switch", "switch"},
            {"case", "case"},
            {"else", "else"},
            {"default", "default"},
            {"break", "break"},
            {"continue", "continue"},
            {"abstract", "abstract"},
            {"private", "AM"},
            {"protected", "AM"},
            {"public", "AM"},
            {"static", "static"},
            {"int", "DT"},
            {"void", "void"},
            {"float", "DT"},
            {"boolean", "DT"},
            {"new", "new"},
            {"for", "for"},
            {"return", "return"},
            {"this", "THIS_BASE"},
            {"char", "DT"},
            {"long", "DT"},
            {"final", "final"},
            {"base", "THIS_BASE"},
            {"while", "while"},
            {"class", "class"},
            {"string", "DT"},
            {"constructor", "constructor"},
            {"do", "do"},
            {"extends", "extends"},
            {"List", "List"},
            {"Arrays", "Arrays"},
            {"asList", "asList"},
            {"ArrayList", "ArrayList"},
            {"false", "BOOLEAN_CONSTANT"},
            {"true", "BOOLEAN_CONSTANT"},
            {"++","INC_DEC"}

    };

    public static boolean checkFirstOperator(Character first) {
        for (int i = 0; i < singleOperators.length; i++) {
            if (first == singleOperators[i]) {
                return true;
            }
        }
        return false;

    }

    public static boolean checkFirstPunctuationMark(Character first) {
        for (int i = 0; i < singlePunctuators.length; i++) {
            if (first == singlePunctuators[i]) {
                return true;
            }
        }
        return false;

    }

    public static String checkPunctuators(String str) {
        Integer p = 0;
        String ClassPart = "";
        while (p < punctuators.length) {
            if (punctuators[p][0].equals(str)) {
                ClassPart += punctuators[p][1];
                break;
            }
            p++;
        }
        return ClassPart;
    }

    public static String returnWhatStoredInTemp(String temp, char line[], int i) {
        temp += line[i];
        i++;
        count = count - 1;
        Topindex = i;
        return temp;
    }


    public static boolean checkKeywords(String first) {
        for (int i = 0; i < singleKeywords.length; i++) {
            if (first.equals(singleKeywords[i])) {
                return true;
            }
        }
        return false;
    }

    public static String isKeyword(String str) {
        Integer p = 0;
        String ClassPart = "";
        while (p < keyWords.length) {
            if (keyWords[p][0].equals(str)) {
                ClassPart += keyWords[p][1];
                break;
            }
            p++;
        }
        return ClassPart;
    }

    //    char ch = '\b';
    public static void printToken(String cp, String vp, Integer lno) {
        try {
            FileWriter fw = new FileWriter("C:\\Users\\hp\\Desktop\\Compiler-ConstructionIbrahim\\LEXEMES.txt", true);
            fw.write("CLASSPART(" + cp + ")," + "VALUEPART(" + vp + ")," + "LINE NUMBER(" + lno + ")\n");
            fw.close();
            TOKEN token = new TOKEN(cp, vp, lno);
            tokenList.add(token);

        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public void tokenSeperator() {
        try {
            File file = new File("C:\\Users\\hp\\Desktop\\Compiler-ConstructionIbrahim\\LEXEMES.txt");
            if (file.exists()) {
                file.delete();
            }

            File filepath = new File("C:\\Users\\hp\\Desktop\\Compiler-ConstructionIbrahim\\INPUT.txt");
            FileReader fr = new FileReader(filepath);
            BufferedReader reader = new BufferedReader(fr);
            String single_line = "";
            String all_line = "";


            while ((single_line = reader.readLine()) != null) {

                all_line += single_line + '\n';
            }


            count = all_line.length();
            while (count > 0) {
                String temp = BreakWord(all_line, Topindex);
//                stringList.add(temp);
                Integer length = temp.toCharArray().length;
                if (temp != "") {
                    if (temp.charAt(0) == '_') {
                        if (temp.toCharArray()[0] == '_' && temp.length() < 2) {

                            printToken("INVALID LEXEME", temp, count_line);

                        } else if (isStringOnlyAlphabet(temp)) {

                            printToken("IDENTIFIER", temp, count_line);

                        } else {
                            printToken("INVALID LEXEME", temp, count_line);
                        }
                    } else if (Character.isLetter(temp.toCharArray()[0])) {
                        if (isStringOnlyAlphabet(temp)) {
                            if (checkKeywords(temp)) {
                                printToken(isKeyword(temp), temp, count_line);
                            } else {
                                printToken("IDENTIFIER", temp, count_line);
                            }
                        } else {
                            printToken("INVALID LEXEME", temp, count_line);
                        }
                    } else if (Character.isDigit(temp.toCharArray()[0]) || temp.matches("^(\\+|-)?\\d+")) {
                        if (isIntegerConstant(temp)) {

                            printToken("INTEGER_CONSTANT", temp, count_line);
                            integerList.add(Integer.parseInt(temp));
                        } else if (isFloatConstant(temp)) {

                            printToken("FLOAT_CONSTANT", temp, count_line);
//                            integerList.add(Integer.parseInt(temp));
                        } else {

                            printToken("INVALID LEXEME", temp, count_line);
                        }

                    } else if (checkFirstOperator(temp.toCharArray()[0])) {

                        if (temp.length() < 2 && temp.charAt(0) == '/') {
                            printToken(checkOperators(temp), temp, count_line);
                        } else if (temp.length() > 1 && temp.charAt(0) == '/' && temp.charAt(1) == '/') {
//                            printToken(" IGNORE COMMEENT", temp, count_line);
                        } else if (temp.charAt(0) == '/' && temp.charAt(1) == '*' && temp.charAt(temp.length() - 2) == '*' && temp.charAt(temp.length() - 1) == '/') {
//                            printToken(" IGNORE COMMEENT", temp, count_line);
                        } else {
                            printToken(checkOperators(temp), temp, count_line);
                        }


                    } else if (checkFirstPunctuationMark(temp.toCharArray()[0])) {
                        printToken(checkPunctuators(temp), temp, count_line);

                    } else if (temp.toCharArray()[0] == '"') {

                        if (temp.toCharArray()[temp.length() - 1] == '"') {

                            temp = temp.substring(1, temp.length() - 1);

                            if (temp instanceof String) {
                                printToken("STRING_CONSTANT", temp, count_line);
                            }
                        } else {
                            printToken("INVALID LEXEME", temp, count_line);
                        }


                    } else if (temp.toCharArray()[0] == '\'') {
                        int j = temp.length() - 1;

                        if (isCharConstant(temp)) {
                            temp = temp.substring(1);
                            j--;
                            if (temp.charAt(temp.length() - 1) == '\'') {
                                temp = temp.substring(0, temp.length() - 1);
                                j--;
                            }
                            printToken("CHARACTER_CONSTANT", temp, count_line);
                        } else if (Character.isLetter(temp.toCharArray()[0])) {
                            printToken("CHARACTER_CONSTANT", temp, count_line);
                        } else {
                            printToken("INVALID LEXEME", temp, count_line);
                        }
                    } else if (temp.toCharArray()[0] == '.') {
                        if (isFloatConstant(temp)) {
                            printToken("FLOAT_CONSTANT", temp, count_line);
                        } else if (temp.toCharArray()[0] == '.' && temp.length() == 1) {
                            printToken(".", temp, count_line);
                        } else {
                            printToken("INVALID LEXEME", temp, count_line);

                        }
                    } else {
                        printToken("INVALID LEXEME", temp, count_line);
                    }


                }


            }


        } catch (IOException e) {
            System.out.println(e);
        }


    }


//    public static void main(String[] args) {

}
