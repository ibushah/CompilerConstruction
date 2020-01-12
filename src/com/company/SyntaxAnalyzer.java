package com.company;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {

    public static List<TOKEN> tokenSet = new ArrayList<TOKEN>();
    public static List<Error> errorSet = new ArrayList<>();
    public static Integer index = 0;

    //ICG GLOBAL VARIABLES
    public static String temp, opr, currentID = "", currentTemp, forTemp, ifElseVar;

    public SyntaxAnalyzer() {
        Main main = new Main();
        main.tokenSeperator();
        tokenSet = Main.tokenList;
    }

    public Boolean ACCESS_MODIFIER() {
        if (tokenSet.get(index).CP.equals("AM")) {
            //SEMENTIC
            SementicAnalyzer.memberAM = tokenSet.get(index).VP;
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("static") || tokenSet.get(index).CP.equals("abstract") || tokenSet.get(index).CP.equals("List") || tokenSet.get(index).CP.equals("void") || tokenSet.get(index).CP.equals("IDENTIFIER"))
            return true;

        return false;
    }

    public Boolean CONSTANT() {
        if (tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("INTEGER_CONSTANT"))
            return true;
        return false;
    }

    public Boolean THIS_BASE() {
        if (tokenSet.get(index).CP.equals("THIS_BASE")) {
            ICG.variableName += tokenSet.get(index).VP;
            index++;
            if (tokenSet.get(index).CP.equals(".")) {
                ICG.variableName += tokenSet.get(index).VP;
                index++;
                return true;
            }
        }
        return false;
    }


    public Boolean STATIC() {
        if (tokenSet.get(index).CP.equals("static")) {
            //SEMENTIC
            SementicAnalyzer.memberTM = tokenSet.get(index).VP;
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("void") || tokenSet.get(index).CP.equals("List") || tokenSet.get(index).CP.equals("IDENTIFIER"))
            return true;

        return false;
    }

    public Boolean TYPE_OF_CLASS() {
        if (tokenSet.get(index).CP.equals("abstract") || tokenSet.get(index).CP.equals("final")) {
            SementicAnalyzer.classCat = tokenSet.get(index).VP;
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("class"))
            return true;

        return false;
    }

    public Boolean TYPE_OF_FUNCTION() {
        if (tokenSet.get(index).CP.equals("abstract") || tokenSet.get(index).CP.equals("static")) {
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("$"))
            return true;

        return false;
    }

    public Boolean VALUE_TYPE() {
        if (tokenSet.get(index).CP.equals("DT")) {
            index++;
            return true;
        }


        return false;
    }

    public Boolean REFERENCE_TYPE() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            index++;
            return true;
        }

        return false;
    }

    public Boolean FINAL_DATA_TYPE() {
        if (tokenSet.get(index).CP.equals("DT")) {
            SementicAnalyzer.memberType += tokenSet.get(index).VP;
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            //SEMENTIC
            SementicAnalyzer.inheritanceCheck = false;
            if (!SementicAnalyzer.lookupInDefinitionTable(tokenSet.get(index).VP)) {
//             SementicAnalyzer.classNotFoundError(tokenSet.get(index).VP);
            }
            SementicAnalyzer.memberType += tokenSet.get(index).VP;
            index++;
            return true;
        }

        return false;

    }

    public Boolean OPERATOR() {
        if (tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals("MD") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals("NOT") || tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("$"))
            return true;

        return false;

    }

    public Boolean ASSIGNMENT() {
        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            return true;
        }
        return false;
    }


    public Boolean AO() {
        if (tokenSet.get(index).CP.equals("AO")) {
            index++;
            return true;
        }
        return false;
    }

    public Boolean ROP() {
        if (tokenSet.get(index).CP.equals("ROP")) {
            index++;
            return true;
        }
        return false;
    }

    public Boolean OA() {
        if (tokenSet.get(index).CP.equals("OA")) {
            index++;
            return true;
        }
        return false;
    }

    public Boolean NOT() {
        if (tokenSet.get(index).CP.equals("NOT")) {
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("$"))
            return true;
        return false;
    }


    //INCREMENT DECREMENT

    public Boolean INC_DEC() {
        if (tokenSet.get(index).CP.equals("INC_DEC")) {
            index++;
            return true;
        }
        return true;
    }


    //ID & CONSTANT

    public Boolean ID_CONSTANT() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER") || CONSTANT()) {
            index++;
            return true;
        }
        return false;
    }

    //CONDITION

    public Boolean CONDITION() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER") || CONSTANT()) {
            if (ID_CONSTANT())
                if (ROP())
                    if (ID_CONSTANT())
                        return true;
        }
        return false;
    }

    //STATEMENT_INC_DEC

    public Boolean STATEMENT_INC_DEC() {
        String currentVar = tokenSet.get(index).VP;
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {

            index++;
            //***ICG
            if (tokenSet.get(index).VP.equals("++")) {
                forTemp = currentVar + " = " + currentVar + " + 1";
            } else if (tokenSet.get(index).VP.equals("--")) {
                forTemp = currentVar + " = " + currentVar + " - 1";
            }
            //***
            if (INC_DEC())
                return true;
        } else if (INC_DEC()) {
            if (tokenSet.get(index).VP.equals("++")) {
                forTemp = currentVar + " = " + currentVar + " + 1";
            } else if (tokenSet.get(index).VP.equals("--")) {
                forTemp = currentVar + " = " + currentVar + " - 1";
            }
        }
        if (tokenSet.get(index).CP.equals("IDENTIFIER"))
            return true;

        return false;
    }

    //IFFF ELSE


    public Boolean IF() {
        if (tokenSet.get(index).CP.equals("if")) {
            //***ICG***
            ifElseVar = ICG.createLabel();
            //****
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                index++;
                if (OE())
                    if (tokenSet.get(index).CP.equals(")")) {
                        if(SementicAnalyzer.typeStack.size()==0 || !SementicAnalyzer.typeStack.peek().equals("boolean"))
                            SementicAnalyzer.errors.add("ERROR:condition must return an boolean type at line no "+tokenSet.get(index).L_N0);

                        SementicAnalyzer.typeStack.clear();
                        SementicAnalyzer.operatorStack.clear();
                        ICG.varNameStack.clear();
                        ICG.operatorStack.clear();
                        index++;
                        //***ICG***
                        ICG.generateOutput("if( " + ICG.currentTemp + "== false ) jmp" + ifElseVar);
                        //****
                        if (BODY()) {

                            return true;
                        }
                    }
            }
        }
//        else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
//                tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
//                tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT")  ||
//                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
//            return true;
        return false;

    }

    public Boolean ELSE() {
        if (tokenSet.get(index).CP.equals("else")) {
            //***ICG***
            String L2 = ICG.createLabel();
            ICG.generateOutput("jmp " + L2);
            //****
            index++;
            if (BODY()) {
                //***ICG***
                ICG.generateOutput(ifElseVar + " :");
                //****
                return true;
            }
        } else if (tokenSet.get(index).equals("$")) {
            //***ICG***
            ICG.generateOutput(ifElseVar + " :");
            //****
            return true;
        } else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
                tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
                tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("if") ||
                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
            return true;
        return false;
    }

    public Boolean IF_ELSE() {
        if (tokenSet.get(index).CP.equals("if"))
            if (IF())
                if (tokenSet.get(index).CP.equals("else")) {
                    if (ELSE())
                        return true;

                } else if (tokenSet.get(index).CP.equals("}"))
                    return true;
                else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
                        tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
                        tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT") ||
                        tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
                    return true;
        return false;
    }

    // loops

    public Boolean FOR_LOOP() {
        if (tokenSet.get(index).CP.equals("for")) {
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                index++;
                //****ICG CODE****
                String L1 = ICG.createLabel(), L2 = ICG.createLabel();
                ICG.generateOutput(L1 + ":");
                //****
                //SEMENTIC SCOPE
                SementicAnalyzer.createScope();
                if (DECLARATION()) {
                    if (OE())
                        if (tokenSet.get(index).CP.equals(";")) {
                            //****ICG CODE****
                            ICG.generateOutput("if(" + ICG.currentTemp + "== false)jmp" + L2);
                            //****
                            index++;
                            if (STATEMENT_INC_DEC())
                                //****ICG CODE****
                                ICG.generateOutput(forTemp);
                            //****
                            if (tokenSet.get(index).CP.equals(")")) {
                                index++;
                                if (BODY())
                                    ////****ICG CODE****
                                    ICG.generateOutput("jmp" + L1);
                                ICG.generateOutput(L2 + ":");
                                //****
                                return true;

                            }


                        }
                }


            }

        }
        return false;

    }

//
//    public Boolean C1()
//    {
//        if(tokenSet.get(tokenSet))
//    }

    //WHILE LOOP

    public Boolean WHILE_LOOP() {
        if (tokenSet.get(index).CP.equals("while")) {
            //****ICG CODE****
            String L1 = ICG.createLabel(), L2 = ICG.createLabel();
            ICG.generateOutput(L1 + ":");
            //****
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                index++;
                //SEMENTIC SCOPE
                SementicAnalyzer.createScope();
                if (OE())
                    if (tokenSet.get(index).CP.equals(")")) {

                        if(SementicAnalyzer.typeStack.size()==0 || !SementicAnalyzer.typeStack.peek().equals("boolean"))
                            SementicAnalyzer.errors.add("ERROR:condition must return an boolean type at line no "+tokenSet.get(index).L_N0);

                        SementicAnalyzer.typeStack.clear();
                        SementicAnalyzer.operatorStack.clear();
                        ICG.varNameStack.clear();
                        ICG.operatorStack.clear();
                        //****ICG CODE****
                        ICG.generateOutput("if(" + ICG.currentTemp + "== false)jmp" + L2);
                        //***
                        index++;
                        if (BODY())
                            //****ICG CODE****
                            ICG.generateOutput("jmp" + L1);
                        ICG.generateOutput(L2 + ":");
                        //****
                        return true;
                    }
            }

        }
        return false;
    }


    public Boolean DO_WHILE() {

        if (tokenSet.get(index).CP.equals("do")) {
            //****ICG CODE****
            String L1 = ICG.createLabel();
            ICG.generateOutput(L1 + " :");
            //****
            index++;
            if (tokenSet.get(index).CP.equals("{")) {
                index++;
                SementicAnalyzer.createScope();
                if (MST())
                    if (tokenSet.get(index).CP.equals("}")) {
                        index++;

                        if (tokenSet.get(index).CP.equals("while")) {
                            index++;
                            if (tokenSet.get(index).CP.equals("(")) {
                                index++;

                                //SEMENTIC
                                SementicAnalyzer.createScope();
                                if (OE())
                                    if (tokenSet.get(index).CP.equals(")")) {

                                        if(SementicAnalyzer.typeStack.size()==0 || !SementicAnalyzer.typeStack.peek().equals("boolean"))
                                            SementicAnalyzer.errors.add("ERROR:condition must return an boolean type at line no "+tokenSet.get(index).L_N0);

                                        SementicAnalyzer.typeStack.clear();
                                        SementicAnalyzer.operatorStack.clear();
                                        ICG.varNameStack.clear();
                                        ICG.operatorStack.clear();
                                        //****ICG CODE****
                                        ICG.generateOutput("if( " + ICG.currentTemp + "== true) jmp" + L1);
                                        //****
                                        index++;
                                        SementicAnalyzer.destroyScope();
                                        return true;
                                    }
                            }

                        }
                    }
            }
        }
        return false;
    }

    //SWICTH STATEMENT

    public Boolean SWITCH_STATEMENT() {
        if (tokenSet.get(index).CP.equals("switch")) {
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                index++;

                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    index++;
                    if (tokenSet.get(index).CP.equals(")")) {
                        index++;
                        if (tokenSet.get(index).CP.equals("{")) {
                            index++;
                            //SEMENTIC
                            SementicAnalyzer.createScope();
                            if (CASE_HANDLERS())
                                if (DEFAULT())
                                    if (tokenSet.get(index).CP.equals("}")) {
                                        index++;
                                        //SEMENTIC SCOPE
                                        SementicAnalyzer.destroyScope();
                                        return true;
                                    }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Boolean DEFAULT() {
        if (tokenSet.get(index).CP.equals("default")) {
            index++;
            if (tokenSet.get(index).CP.equals(":")) {
                //SEMENTIC
                SementicAnalyzer.createScope();
                if (BODY()) {
                    //SEMENTIC
                    SementicAnalyzer.destroyScope();
                    return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("}")) {
            return true;
        }
        return false;
    }


    public Boolean CASE_HANDLERS() {
        if (tokenSet.get(index).CP.equals("case")) {
            index++;
            if (tokenSet.get(index).CP.equals("CONSTANT"))
                if (tokenSet.get(index).CP.equals(":")) {
                    index++;
                    SementicAnalyzer.createScope();
                    if (BODY())
                        if (tokenSet.get(index).CP.equals("break")) {
                            index++;
                            SementicAnalyzer.destroyScope();
                            if (tokenSet.get(index).CP.equals(";"))
                                if (CASE_HANDLERS())
                                    return true;
                        }

                }
        } else if (tokenSet.get(index).CP.equals("}"))
            return true;
        else if (tokenSet.get(index).CP.equals("default"))
            return true;

        return false;
    }


    //RETURN

    public Boolean RETURN() {
        if (tokenSet.get(index).CP.equals("return")) {
            index++;
            if (OE()) {


                return true;
            }

        }
        return false;
    }


    //PARAMETER_LIST

    public Boolean PARAMTER_LIST() {
        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (OE()) {
                SementicAnalyzer.objType += (SementicAnalyzer.globalTypeParent != null) ? SementicAnalyzer.globalTypeParent : "null";
                if (PARAMETER_LIST2())
                    return true;
            }
        } else if (tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals(","))
            return true;

        return false;

    }

    public Boolean PARAMETER_LIST2() {
        if (tokenSet.get(index).CP.equals(",")) {
            SementicAnalyzer.objType += tokenSet.get(index).VP;
            ICG.variableName += tokenSet.get(index).VP;

            index++;
            if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
                if (OE()) {
                    SementicAnalyzer.objType += (SementicAnalyzer.globalTypeParent != null) ? SementicAnalyzer.globalTypeParent : "null";
                    if (PARAMETER_LIST2())
                        return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals(","))
            return true;


        return false;
    }


    //ARGUMENT LISTT

    public Boolean ARGUMENT_LIST() {
        if (tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            //Sementic

            SementicAnalyzer.FT_type = tokenSet.get(index).VP;
            //***ICG
            ICG.functionArgs += tokenSet.get(index).VP;
            ICG.functionArgs += "_";
            //***
            if (FINAL_DATA_TYPE())
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    //SEMENTIC ANALYZER
                    SementicAnalyzer.FT_name = tokenSet.get(index).VP;
                    SementicAnalyzer.FT_scope = SementicAnalyzer.currentScope();
                    index++;
                    if (!SementicAnalyzer.insertIntoFunctionTable()) {
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);

                    }
                    SementicAnalyzer.resetFunctionTableVariables();
                    if (ARGUMENT_LIST2())
                        return true;
                }
        } else if (tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals(","))
            return true;

        return false;

    }

    public Boolean ARGUMENT_LIST2() {
        if (tokenSet.get(index).CP.equals(",")) {
            //SEMENTIC

            SementicAnalyzer.memberType += tokenSet.get(index).VP;
            SementicAnalyzer.FT_type = tokenSet.get(index).VP;
            index++;
            //***ICG***
            ICG.functionArgs += tokenSet.get(index).VP;
            ICG.functionArgs += "_";
            //***
            if (FINAL_DATA_TYPE())
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    SementicAnalyzer.FT_name = tokenSet.get(index).VP;
                    SementicAnalyzer.FT_scope = SementicAnalyzer.currentScope();
                    index++;
                    if (!SementicAnalyzer.insertIntoFunctionTable()) {
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);

                    }
                    SementicAnalyzer.resetFunctionTableVariables();
                    if (ARGUMENT_LIST2())
                        return true;
                }
        } else if (tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals(","))
            return true;
        return false;
    }


    //LIST

    public Boolean LIST() {
        if (tokenSet.get(index).CP.equals("List")) {
            index++;
            if (LT())
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    index++;
                    if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
                        index++;
                        if (L_INIT())
                            if (L_LIST())
                                return true;
                    }
                }

        }
        return false;
    }

    public Boolean L_INIT() {


        if (tokenSet.get(index).CP.equals("new")) {
            index++;
            if (tokenSet.get(index).CP.equals("ArrayList")) {
                index++;
                if (LT())
                    if (tokenSet.get(index).CP.equals("(")) {
                        index++;
                        if (tokenSet.get(index).CP.equals(")")) {
                            index++;

                            return true;

                        }
                    }
            }
        } else if (tokenSet.get(index).CP.equals("Arrays")) {
            index++;
            if (tokenSet.get(index).CP.equals(".")) {
                index++;
                if (tokenSet.get(index).CP.equals("asList")) {
                    index++;
                    if (tokenSet.get(index).CP.equals("(")) {
                        index++;
                        if (OE_LIST())
                            if (tokenSet.get(index).CP.equals(")")) {
                                index++;

                                return true;

                            }
                    }
                }
            }
        } else if (tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";"))
            return true;

        return false;
    }


    public Boolean L_LIST() {
        if (tokenSet.get(index).CP.equals(",")) {
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                index++;
                if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
                    index++;
                    if (L_INIT())
                        if (L_LIST())
                            return true;
                }
            }

        } else if (tokenSet.get(index).CP.equals(";")) {
            index++;
            return true;
        }
        return false;
    }


    public Boolean LT() {
        if (tokenSet.get(index).VP.equals("<")) {
            index++;
            if (FINAL_DATA_TYPE())
                if (tokenSet.get(index).VP.equals(">")) {
                    index++;
                    return true;
                }
        } else if (tokenSet.get(index).CP.equals("(") || tokenSet.get(index).CP.equals("IDENTIFIER"))
            return true;

        return false;
    }


    //CONSTRUCTOR

    public Boolean CONSTRUCTOR() {
        if (tokenSet.get(index).CP.equals("constructor")) {

            //*****SEMENTIC**********
            SementicAnalyzer.memberName = tokenSet.get(index).VP;
            SementicAnalyzer.memberAM = "public";
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                index++;
                SementicAnalyzer.createScope();
                if (ARGUMENT_LIST())
                    if (tokenSet.get(index).CP.equals(")")) {
                        index++;

                        //SEMENTIC
                        if (SementicAnalyzer.memberType.isEmpty())
                            SementicAnalyzer.memberType = "void";

                        SementicAnalyzer.memberType += "->void";
                        if (!SementicAnalyzer.insertToClassTable()) {
                            SementicAnalyzer.constructorError(tokenSet.get(index - 1).L_N0);
                        }
                        SementicAnalyzer.resetClassMembers();
                    }
                if (tokenSet.get(index).CP.equals("{")) {
                    index++;
                    if (MST())
                        if (tokenSet.get(index).CP.equals("}")) {
                            index++;

                            //SEMENTIC SCOPE
                            SementicAnalyzer.destroyScope();
                            return true;
                        }
                }
            }
        }


        return false;
    }


    //OE

    public Boolean OE() {
        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (AE())
                if (OE2())
                    return true;
        }
        return false;
    }

    public Boolean OE2() {
        if (tokenSet.get(index).VP.equals("||")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (AE())
                if (OE2()) {
                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    return true;
                }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }

    public Boolean AE() {
        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (RE())
                if (AE2())
                    return true;
        }
        return false;
    }


    public Boolean AE2() {
        if (tokenSet.get(index).VP.equals("&&")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (RE())
                if (AE2()) {
                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    return true;
                }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).VP.equals("|") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }

    public Boolean RE() {
        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (E())
                if (RE2())
                    return true;
        }
        return false;
    }

    public Boolean RE2() {
        if (tokenSet.get(index).CP.equals("ROP")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (E())
                if (RE2()) {
                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    return true;
                }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }

    public Boolean E() {
        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (T())
                if (E2())
                    return true;
        }
        return false;
    }


    public Boolean E2() {
        if (tokenSet.get(index).CP.equals("PM")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (T())
                if (E2()) {
                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    return true;
                }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }


    public Boolean T() {

        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).VP.equals("!") || tokenSet.get(index).CP == "INC_DEC" || tokenSet.get(index).CP.equals("THIS_BASE") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (F()) {

                if (T2())
                    return true;
            }
        }
        return false;
    }

    public Boolean T2() {
        if (tokenSet.get(index).CP.equals("MDM")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (F()) {

                if (T2()) {

                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }

    public Boolean F() {

        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("BOOLEAN_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT")) {
            //SEMENTIC


            if (!SementicAnalyzer.paramsCheck) {
                SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);
            } else {
                ICG.calledFuncParams += "_" + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);

                String t = ICG.createTemp();
                String params = tokenSet.get(index).VP;
                ICG.parameterStack.push(t);
                ICG.generateOutput(t + "=" + params);


            }

            if (tokenSet.get(index + 1).CP.equals(";") || tokenSet.get(index + 1).CP.equals("ROP") || tokenSet.get(index + 1).CP.equals("PM")
                    || tokenSet.get(index + 1).CP.equals("MDM") || tokenSet.get(index + 1).CP.equals("OA") || (tokenSet.get(index+1).VP.equals(")") && tokenSet.get(index+2).VP.equals("{"))) {
                SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);
                //ICG
                ICG.varNameStack.push(tokenSet.get(index).VP);
                ICG.variableName = "";
                //*ICG*
            }
            if (!SementicAnalyzer.globalParams.isEmpty()) {

                SementicAnalyzer.globalParams += "," + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);

            } else
                SementicAnalyzer.globalParams += SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);


            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("(")) {
            index++;
            if (OE())
                if (tokenSet.get(index).CP.equals(")")) {
                    index++;
                    return true;
                }
        } else if (tokenSet.get(index).VP.equals("!")) {
            index++;
            if (F())
            {
                if(SementicAnalyzer.Compatibility("!")==null)
                    SementicAnalyzer.errors.add("ERROR:Boolean type not found with NOT operator at line no "+tokenSet.get(index).L_N0);

                return true;
            }

        } else if (tokenSet.get(index).CP.equals("INC_DEC")) {
            index++;
            if (tokenSet.get(index).CP.equals("THIS_BASE")) {
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    index++;
                    return true;
                }

            }
        } else if (tokenSet.get(index).CP.equals("THIS_BASE")) {
            if (THIS_BASE()) {
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    //SEMENTIC
                    ICG.variableName += tokenSet.get(index).VP;
                    if (!tokenSet.get(index + 1).VP.equals("(")) {
                        if (!SementicAnalyzer.paramsCheck) {
                            if (tokenSet.get(index - 2).VP.equals("this"))
                                SementicAnalyzer.globalTypeParent = SementicAnalyzer.THIS_LOOKUP(tokenSet.get(index).VP);
                            else if (tokenSet.get(index - 2).VP.equals("base"))
                                SementicAnalyzer.globalTypeParent = SementicAnalyzer.BASE_LOOKUP(tokenSet.get(index).VP);
                            else
                                SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);

                        } else {
                            ICG.calledFuncParams += "_" + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);
                            String t = ICG.createTemp();
                            String params = tokenSet.get(index).VP;
                            ICG.parameterStack.push(t);
                            ICG.generateOutput(t + "=" + params);

                        }

                        if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index + 1).CP.equals("ASSIGNMENT") || tokenSet.get(index + 1).CP.equals(";") || tokenSet.get(index + 1).CP.equals("ROP") || tokenSet.get(index + 1).CP.equals("PM")
                                || tokenSet.get(index + 1).CP.equals("MDM") || tokenSet.get(index + 1).CP.equals("OA"))) {
                            SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);

                            //ICG
                            ICG.varNameStack.push(ICG.variableName);
                            ICG.variableName = "";
                            //*ICG*
                        }
                        if (SementicAnalyzer.globalTypeParent == null) {

                            SementicAnalyzer.errors.add("ERROR:Variable not found at line no " + tokenSet.get(index).L_N0);
                        }
                    } else {
                        SementicAnalyzer.globalTypeParent = SementicAnalyzer.className;
                        SementicAnalyzer.globalChild = tokenSet.get(index).VP;
                    }


                    if (!SementicAnalyzer.globalParams.isEmpty())
                        SementicAnalyzer.globalParams += "," + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
                    else
                        SementicAnalyzer.globalParams += SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
                    index++;
                    if (F2())
                        return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            ICG.variableName += tokenSet.get(index).VP;
            //SEMENTIC
            ICG.calledFunc = tokenSet.get(index).VP;
            if (!tokenSet.get(index + 1).VP.equals("(")) {
                if (!SementicAnalyzer.paramsCheck) {
                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
                    ICG.calledFuncParams += "_" + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
                }
                //TYPE CHECK N BREAK IDENTIFIER
                else {
                    ICG.calledFuncParams += "_" + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, tokenSet.get(index).CP);
                    String t = ICG.createTemp();
                    String params = tokenSet.get(index).VP;
                    ICG.parameterStack.push(t);
                    ICG.generateOutput(t + "=" + params);

                }
                if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index + 1).CP.equals("ASSIGNMENT") || tokenSet.get(index + 1).CP.equals(";") || tokenSet.get(index + 1).CP.equals("ROP") || tokenSet.get(index + 1).CP.equals("PM")
                        || tokenSet.get(index + 1).CP.equals("MDM") || tokenSet.get(index + 1).CP.equals("OA")) || (tokenSet.get(index+1).VP.equals(")") && tokenSet.get(index+2).VP.equals("{"))) {
                    SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);

                    //ICG
                    ICG.varNameStack.push(ICG.variableName);
                    ICG.variableName = "";
                    //*ICG*
                }

                if (SementicAnalyzer.globalTypeParent == null) {
                    SementicAnalyzer.errors.add("ERROR:Variable not found at line no " + tokenSet.get(index).L_N0);
                }
                if (!SementicAnalyzer.globalParams.isEmpty())
                    SementicAnalyzer.globalParams += "," + SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
                else
                    SementicAnalyzer.globalParams += SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
            } else {
                SementicAnalyzer.globalTypeParent = SementicAnalyzer.className;
                SementicAnalyzer.globalChild = tokenSet.get(index).VP;
            }
            index++;
            if (F2())
                return true;
        }

        return false;
    }

    public Boolean F2() {
        if (tokenSet.get(index).CP.equals("INC_DEC")) {
            index++;
            if (tokenSet.get(index).CP.equals(";")) {
                index++;

            }
        } else if (tokenSet.get(index).CP.equals(".") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).CP.equals("ASSIGNMENT") || tokenSet.get(index).CP.equals("[")) {
            if (ASSIGN_LIST())
                return true;

        } else if (tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]"))
            return true;
        return false;
    }


    //ASSIGN LIST


    public Boolean ASSIGN_LIST() {
        if (tokenSet.get(index).CP.equals(".")) {
            ICG.variableName += tokenSet.get(index).VP;
            index++;
            ICG.variableName += tokenSet.get(index).VP;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                ICG.calledFunc = tokenSet.get(index).VP;
                index++;
                SementicAnalyzer.globalChild = tokenSet.get(index - 1).VP;
                //SEMENTIC
                if (!tokenSet.get(index).VP.equals("(")) {

                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.dotOperatorType(false);
                    //TYPE CHECK N BREAK IDENTIFIER
                    if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("PM")
                            || tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("OA")) || (tokenSet.get(index+1).VP.equals(")") && tokenSet.get(index+2).VP.equals("{"))) {
                        SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);
                        //ICG
                        ICG.varNameStack.push(ICG.variableName);
                        ICG.variableName = "";
                        //*ICG*
                    }
                    if (SementicAnalyzer.globalTypeParent == null) {
                        SementicAnalyzer.errors.add("ERROR:Could not find the variable type at line no " + tokenSet.get(index).L_N0);
                    }
                }
                if (ASSIGN_LIST())
                    return true;
            }

        } else if (tokenSet.get(index).CP.equals("[")) {
            index++;
            if (OE()) {
                if (tokenSet.get(index).CP.equals("]")) {
                    index++;
                    if (A_EXPAND())
                        return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("(")) {
            ICG.variableName += tokenSet.get(index).VP;
            index++;
            SementicAnalyzer.paramsCheck = true;
            SementicAnalyzer.globalParams = "";
            ICG.calledFuncParams = "";
            SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalTypeParent.isEmpty() ? SementicAnalyzer.className : SementicAnalyzer.globalTypeParent;

            if (PARAMTER_LIST())
                if (tokenSet.get(index).CP.equals(")")) {
                    //SEMENTIC
                    ICG.variableName += tokenSet.get(index).VP;
                    if (SementicAnalyzer.globalParams.isEmpty())
                        SementicAnalyzer.globalParams = "void";
                    SementicAnalyzer.paramsCheck = false;
                    //ICG
                    Object[] paramArray = ICG.parameterStack.toArray();
                    for (int i = paramArray.length - 1; i >= 0; i--) {
                        String printParams = (String) paramArray[i];
                        ICG.generateOutput("param " + printParams);
                    }
//                    ICG.parameterStack.clear();
//                    String callT = ICG.createTemp();
//                    ICG.generateOutput( callT + "=" + "call " + ICG.calledFunc);
//

                    ICG.calledFunc += ICG.calledFuncParams + "_proc";
                    ICG.calledFuncParams = "";
                    ICG.parameterStack.clear();
                    String callT = ICG.createTemp();
                    ICG.generateOutput(callT + "=" + "call " + ICG.calledFunc);
                    ICG.calledFunc = "";


                    //****
                    index++;
                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.dotOperatorType(true);
                    if(SementicAnalyzer.globalTypeParent==null)
                        SementicAnalyzer.errors.add("ERROR:FUNCTION not found at line no "+tokenSet.get(index).L_N0);
                    if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("PM")
                            || tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("OA") || (tokenSet.get(index+1).VP.equals(")") && tokenSet.get(index+2).VP.equals("{")))) {
                        SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);

                        //ICG
                        ICG.varNameStack.push(callT);
                        ICG.variableName = "";
                        //*ICG*
                    }

                    if (SementicAnalyzer.globalTypeParent == null) {
                        SementicAnalyzer.errors.add("ERROR:Could not find the variable type at line no " + tokenSet.get(index).L_N0);
                    }
                    SementicAnalyzer.globalParams = "";


                    if (F_EXPAND())
                        return true;
                }
        } else if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            //ICG

            ICG.varNameStack.push(ICG.variableName);
            ICG.variableName = "";
            ICG.operatorStack.push(tokenSet.get(index).VP);
            //ICG
            if (SementicAnalyzer.globalTypeParent != null)
                SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);
            else
                SementicAnalyzer.errors.add("ERROR:Variable not found at line no " + tokenSet.get(index).L_N0);
            SementicAnalyzer.globalTypeParent = "";
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);

            index++;
            if (OE()) {
                SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                SementicAnalyzer.typeStack.clear();
                SementicAnalyzer.operatorStack.clear();
                ICG.operatorStack.clear();
                ICG.varNameStack.clear();
                if (tokenSet.get(index).CP.equals(";")) {
                    index++;
                    return true;
                }
            }

        } else if (tokenSet.get(index).CP.equals("INC_DEC")) {
            index++;

            if (tokenSet.get(index).CP.equals(";")) {
                index++;
                return true;
            }

        } else if (tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("]") || tokenSet.get(index).CP.equals("}")) {
            return true;
        }
        return false;
    }


    public Boolean A_EXPAND() {
        if (tokenSet.get(index).CP.equals(".")) {
            ICG.variableName += tokenSet.get(index).VP;
            index++;
            ICG.variableName += tokenSet.get(index).VP;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                index++;
                SementicAnalyzer.globalChild = tokenSet.get(index - 1).VP;
                //SEMENTIC
                if (!tokenSet.get(index + 1).VP.equals("(")) {

                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.dotOperatorType(false);

                    if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("PM")
                            || tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("OA"))) {
                        SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);
                        //ICG
                        ICG.varNameStack.push(ICG.variableName);
                        ICG.variableName = "";
                    }
                    if (SementicAnalyzer.globalTypeParent == null) {
                        SementicAnalyzer.errors.add("ERROR:Could not find the variable type at line no " + tokenSet.get(index).L_N0);
                    }
                }

                if (ASSIGN_LIST())
                    return true;
            }
        } else if (tokenSet.get(index).CP.equals("(")) {
            SementicAnalyzer.paramsCheck = true;
            SementicAnalyzer.globalParams = "";
            ICG.calledFuncParams = "";
            ICG.variableName += tokenSet.get(index).VP;
            index++;
            ICG.variableName += tokenSet.get(index).VP;
            if (PARAMTER_LIST())
                if (tokenSet.get(index).CP.equals(")")) {
                    //SEMENTIC
                    if (SementicAnalyzer.globalParams.isEmpty())
                        SementicAnalyzer.globalParams = "void";
                    SementicAnalyzer.paramsCheck = false;
                    //ICG
                    Object[] paramArray = ICG.parameterStack.toArray();
                    for (int i = paramArray.length - 1; i >= 0; i--) {
                        String printParams = (String) paramArray[i];
                        ICG.generateOutput("param " + printParams);
                    }


                    ICG.calledFunc += ICG.calledFuncParams + "_proc";
                    ICG.calledFuncParams = "";
                    ICG.parameterStack.clear();
                    String callT = ICG.createTemp();
                    ICG.generateOutput(callT + "=" + "call " + ICG.calledFunc);
                    ICG.calledFunc = "";
                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.dotOperatorType(true);


                    if (SementicAnalyzer.globalTypeParent != null && (tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("PM")
                            || tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("OA"))) {
                        SementicAnalyzer.typeStack.push(SementicAnalyzer.globalTypeParent);
                        //ICG
                        ICG.varNameStack.push(ICG.variableName);
                        ICG.variableName = "";
                        //*ICG
                    }
                    if (SementicAnalyzer.globalTypeParent == null) {
                        SementicAnalyzer.errors.add("ERROR:Could not find the variable type at line no " + tokenSet.get(index).L_N0);
                    }
                    SementicAnalyzer.globalParams = "";

                    if (ASSIGN_LIST())
                        return true;
                }
        } else if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            if (OE()) {

                if (tokenSet.get(index).CP.equals(";")) {

                    index++;
                    return true;
                }
            }

        } else if (tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]")) {
            return true;
        }
        return false;
    }


    public Boolean F_EXPAND() {
        if (tokenSet.get(index).CP.equals(".")) {
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                index++;
                SementicAnalyzer.globalChild = tokenSet.get(index - 1).VP;
                //SEMENTIC
                if (!tokenSet.get(index + 1).VP.equals("(")) {

                    SementicAnalyzer.globalTypeParent = SementicAnalyzer.dotOperatorType(false);
                    if (SementicAnalyzer.globalTypeParent == null) {
                        SementicAnalyzer.errors.add("ERROR:Could not find the variable type at line no " + tokenSet.get(index).L_N0);
                    }
                }
                if (ASSIGN_LIST())
                    return true;
            }
        } else if (tokenSet.get(index).CP.equals("[")) {
            index++;
            SementicAnalyzer.globalParams = "";
            if (OE())
                if (tokenSet.get(index).CP.equals("]")) {
                    index++;
                    if (A_EXPAND())
                        return true;
                }
        } else if (tokenSet.get(index).CP.equals("MDM") || tokenSet.get(index).CP.equals("PM") || tokenSet.get(index).CP.equals("ROP") || tokenSet.get(index).CP.equals("OA") || tokenSet.get(index).CP.equals(")") || tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals("]")) {
            return true;
        }

        return false;
    }


    public Boolean INIT_EMPTY() {
        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            if (OE())
                if (DEC_LIST())
                    if (tokenSet.get(index).CP.equals(";")) {
                        index++;
                        return true;
                    }

        } else if (tokenSet.get(index).CP.equals(";")) {
            index++;
            return true;

        }

        return false;
    }

    public Boolean DEC_LIST() {
        if (tokenSet.get(index).CP.equals(",")) {
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                index++;
                if (DEC_LIST2())
                    if (DEC_LIST())
                        return true;
            }
        } else if (tokenSet.get(index).CP.equals(";"))
            return true;

        return false;
    }

    public Boolean DEC_LIST2() {
        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            if (OE())
                return true;

        } else if (tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";"))
            return true;

        return false;
    }

    //OE LIST


    public Boolean OE_LIST() {

        if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("STRING_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).CP.equals("NOT") || tokenSet.get(index).CP.equals("INC_DEC") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            if (OE())
                if (OE_LIST2())
                    return true;
        } else if (tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(")"))
            return true;
        return false;
    }

    public Boolean OE_LIST2() {
        if (tokenSet.get(index).CP.equals(",")) {
            index++;
            if (OE())
                if (OE_LIST2())
                    return true;
        } else if (tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals(")"))
            return true;

        return false;
    }


    //DECLARATION


    public Boolean DECLARATION() {
        if (tokenSet.get(index).CP.equals("DT")) {
            //SEMENTIC
            SementicAnalyzer.typeStack.push(tokenSet.get(index).VP);
            SementicAnalyzer.FT_type = tokenSet.get(index).VP;
            index++;
            if (DEC_TYPE())
                return true;
        } else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
                tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
                tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("if") ||
                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
            return true;
        return false;
    }

    public Boolean DEC_TYPE() {
        if (tokenSet.get(index).CP.equals("[")) {
            index++;
            if (tokenSet.get(index).CP.equals("]")) {
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    //SEMENTIC
                    SementicAnalyzer.FT_name = tokenSet.get(index).VP;
                    SementicAnalyzer.FT_scope = SementicAnalyzer.currentScope();
                    index++;
                    if (!SementicAnalyzer.insertIntoFunctionTable())
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
                    if (A_INIT())
                        if (A_LIST())
                            return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            SementicAnalyzer.FT_name = tokenSet.get(index).VP;
            SementicAnalyzer.FT_scope = SementicAnalyzer.currentScope();
            ICG.varNameStack.push(tokenSet.get(index).VP);
            index++;
            if (!SementicAnalyzer.insertIntoFunctionTable())
                SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
            if (V_INIT())
                if (V_LIST())
                    return true;
        }

        return false;
    }


    public Boolean A_INIT() {
        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            index++;
            if (A_INIT2())
                return true;
        } else if (tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";"))
            return true;

        return false;
    }

    public Boolean A_INIT2() {
        if (tokenSet.get(index).CP.equals("new")) {
            index++;
            if (tokenSet.get(index).CP.equals("DT")) {
                index++;
                if (tokenSet.get(index).CP.equals("[")) {
                    index++;
                    if (OE())
                        if (tokenSet.get(index).CP.equals("]")) {
                            index++;
                            return true;

                        }
                }
            }
        } else if (tokenSet.get(index).CP.equals("{")) {
            index++;
            if (OE_LIST())
                if (tokenSet.get(index).CP.equals("}")) {
                    index++;
                    return true;

                }

        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            index++;
            if (ASSIGN_LIST())
                return true;
        } else if (tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";"))
            return true;

        return false;
    }


    public Boolean A_LIST() {
        if (tokenSet.get(index).CP.equals(",")) {
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                //SEMENTIC
                SementicAnalyzer.FT_name = tokenSet.get(index).VP;
                index++;
                if (!SementicAnalyzer.insertIntoFunctionTable())
                    SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
                if (A_INIT())
                    if (A_LIST())
                        return true;
            }
        } else if (tokenSet.get(index).CP.equals(";")) {
            index++;
            SementicAnalyzer.resetFunctionTableVariables();
            return true;
        }
        return false;
    }


    public Boolean V_INIT() {
        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            ICG.operatorStack.push(tokenSet.get(index).VP);
            index++;
            if (OE()) {
                SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                SementicAnalyzer.typeStack.clear();
                SementicAnalyzer.operatorStack.clear();
                ICG.operatorStack.clear();
                ICG.varNameStack.clear();
                return true;
            }
        } else if (tokenSet.get(index).CP.equals(";") || tokenSet.get(index).CP.equals(","))
            return true;

        return false;
    }

    public Boolean V_INIT2() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            index++;
            if (ASSIGN_LIST())
                if (V_INIT())
                    return true;
        } else if (tokenSet.get(index).CP.equals("INTEGER_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT") || tokenSet.get(index).CP.equals("FLOAT_CONSTANT") || tokenSet.get(index).CP.equals("CHARACTER_CONSTANT")) {
            index++;

            return true;
        }

        return false;
    }

    public Boolean V_LIST() {
        if (tokenSet.get(index).CP.equals(",")) {
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                //SEMENTIC
                SementicAnalyzer.FT_name = tokenSet.get(index).VP;
                if (!SementicAnalyzer.insertIntoFunctionTable())
                    SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
                index++;
                if (V_INIT())
                    if (V_LIST())
                        return true;
            }
        } else if (tokenSet.get(index).CP.equals(";")) {
            SementicAnalyzer.resetFunctionTableVariables();
            //ICG
            ICG.operatorStack.clear();
            ICG.varNameStack.clear();
            //ICG
            index++;
            return true;
        } else if (tokenSet.get(index).CP.equals("while")) {
            return true;
        }
        return false;
    }


    //SST

    public Boolean SST() {
        if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for")) {
            if (ITERATIVE_STATEMENTS())
                return true;
        } else if (tokenSet.get(index).CP.equals("switch")) {
            if (SWITCH_STATEMENT())
                return true;
        } else if (tokenSet.get(index).CP.equals("return") || tokenSet.get(index).CP.equals("continue")) {
            if (JUMP_STATMENT())
                return true;
        } else if (tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base")) {
            if (THIS_BASE()) {
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    index++;
                    if (DIFF_LIST())
                        return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalLookup(tokenSet.get(index).VP, null);
            if (tokenSet.get(index + 1).VP.equals("(")) {
                SementicAnalyzer.globalTypeParent=SementicAnalyzer.className;
                SementicAnalyzer.globalChild = tokenSet.get(index).VP;
            }


            SementicAnalyzer.FT_type = tokenSet.get(index).VP;
            //ICG
            ICG.variableName = tokenSet.get(index).VP;
            //ICG
            index++;
            if (DIFF_LIST())
                return true;
        } else if (tokenSet.get(index).CP.equals("DT")) {
            if (DECLARATION())
                return true;
        } else if (tokenSet.get(index).CP.equals("if"))
            return IF_ELSE();

        else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
            return true;

        return false;
    }

    public Boolean JUMP_STATMENT() {
        if (tokenSet.get(index).CP.equals("return")) {
            index++;
            if (OE()) {


                SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                SementicAnalyzer.returnStatementError(tokenSet.get(index).L_N0);
                SementicAnalyzer.typeStack.clear();
                SementicAnalyzer.operatorStack.clear();
                ICG.operatorStack.clear();
                ICG.varNameStack.clear();
                if (tokenSet.get(index).CP.equals(";")) {
                    index++;
                    return true;
                }
            }

        } else if (tokenSet.get(index).CP.equals("continue")) {
            index++;
            if (tokenSet.get(index).CP.equals(";")) {
                return true;
            }
        } else if (tokenSet.get(index).CP.equals("break")) {
            index++;
            if (tokenSet.get(index).CP.equals(";")) {
                return true;
            }
        }
//        else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
//                tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
//                tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("if") ||
//                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
//            return true;


        return false;
    }


    public Boolean ITERATIVE_STATEMENTS() {
        if (tokenSet.get(index).CP.equals("for")) {
            if (FOR_LOOP())
                return true;
        } else if (tokenSet.get(index).CP.equals("while")) {
            if (WHILE_LOOP())
                return true;
        } else if (tokenSet.get(index).CP.equals("do")) {
            if (DO_WHILE())
                return true;
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("if") ||
                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}"))
            return true;
        return false;
    }

    public Boolean DIFF_LIST() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            SementicAnalyzer.typeStack.push(tokenSet.get(index - 1).VP);
            if (OBJECT_CREATION())
                return true;
        } else if (tokenSet.get(index).CP.equals(".") || tokenSet.get(index).CP.equals("[") || tokenSet.get(index).CP.equals("ASSIGNMENT") || tokenSet.get(index).CP.equals("(") || tokenSet.get(index).CP.equals(";")) {
            //calledFUNCTION
            ICG.calledFunc = tokenSet.get(index - 1).VP;
            //calledFUNCTION
//            SementicAnalyzer.globalTypeParent = SementicAnalyzer.globalLookup(tokenSet.get(index - 1).VP, null);
//            if (SementicAnalyzer.globalTypeParent == null) {
//                SementicAnalyzer.errors.add("ERROR:Variable not found at line no " + tokenSet.get(index - 1).L_N0);
//            }
            if (ASSIGN_LIST())
                if (tokenSet.get(index - 1).CP.equals(")")) {
                    if (tokenSet.get(index).CP.equals(";")) {
                        index++;
                        return true;
                    }

                } else {
                    return true;
                }
        }
        return false;
    }

    public Boolean CLASS_OBJECT_CREATION_N_FUNCTION() {

        if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
            SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
            if (!SementicAnalyzer.insertToClassTable()) {
                SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);
            }
            SementicAnalyzer.resetClassMembers();
            index++;
            if (tokenSet.get(index).CP.equals("new")) {
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    SementicAnalyzer.typeStack.push(tokenSet.get(index).VP);
                    SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                    SementicAnalyzer.typeStack.clear();
                    SementicAnalyzer.operatorStack.clear();
                    SementicAnalyzer.objName = tokenSet.get(index).VP;
                    index++;
                    if (tokenSet.get(index).CP.equals("(")) {
                        SementicAnalyzer.objType = "";
                        index++;
                        if (PARAMTER_LIST()) {
                            if (tokenSet.get(index).CP.equals(")")) {
                                index++;
                                //SEMENTIC
                                //CONSTRUCTOR CHECK
                                if (SementicAnalyzer.objType.isEmpty())
                                    SementicAnalyzer.objType = "void";
                                SementicAnalyzer.objectTypeCheckError(tokenSet.get(index).L_N0);
                                SementicAnalyzer.resetObjAttr();


                                if (SementicAnalyzer.memberType.isEmpty())
                                    SementicAnalyzer.memberType = "void";
                                SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;

                                if (!SementicAnalyzer.insertToClassTable())
                                    SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);

                                SementicAnalyzer.resetClassMembers();
                                if (tokenSet.get(index).CP.equals(";")) {
                                    index++;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokenSet.get(index).CP.equals("(")) {
            index++;
            SementicAnalyzer.typeStack.clear();
            SementicAnalyzer.operatorStack.clear();
            SementicAnalyzer.createScope();
            SementicAnalyzer.memberType = "";
            if (ARGUMENT_LIST())
                if (tokenSet.get(index).CP.equals(")")) {
                    //ICG
                    ICG.callfunctionName = ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc";
                    ICG.generateOutput(ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc");
                    ICG.functionName = "";
                    ICG.functionArgs = "";
                    index++;
                    //SEMENTIC

                    if (SementicAnalyzer.memberType.isEmpty())
                        SementicAnalyzer.memberType = "void";
                    SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;

                    if (!SementicAnalyzer.insertToClassTable())
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);

                    SementicAnalyzer.resetClassMembers();
                    if (tokenSet.get(index).CP.equals("{")) {
                        index++;
                        if (MST())
                            if (tokenSet.get(index).CP.equals("}")) {
                                index++;

                                SementicAnalyzer.destroyScope();
                                return true;
                            }
                    }

                }
        }

        return false;
    }


    public Boolean OBJECT_CREATION() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            //SEMENTIC
            SementicAnalyzer.FT_name = tokenSet.get(index).VP;
            SementicAnalyzer.FT_scope = SementicAnalyzer.currentScope();

            index++;
            if (!SementicAnalyzer.insertIntoFunctionTable())
                SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
            if (tokenSet.get(index).CP.equals("ASSIGNMENT")) {
                SementicAnalyzer.operatorStack.push(tokenSet.get(index).VP);
                index++;
                if (tokenSet.get(index).CP.equals("new")) {
                    index++;
                    if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                        //SEMENTIC
                        SementicAnalyzer.typeStack.push(tokenSet.get(index).VP);
                        SementicAnalyzer.FT_name = SementicAnalyzer.objName = tokenSet.get(index).VP;

                        SementicAnalyzer.checkCompatibleTypes(tokenSet.get(index).L_N0);
                        SementicAnalyzer.operatorStack.clear();
                        SementicAnalyzer.typeStack.clear();
                        index++;
                        if (tokenSet.get(index).CP.equals("(")) {
                            index++;
                            SementicAnalyzer.objType = "";
                            if (PARAMTER_LIST()) {
                                if (tokenSet.get(index).CP.equals(")")) {
                                    index++;
                                    //SEMENTIC
                                    if (SementicAnalyzer.objType.isEmpty())
                                        SementicAnalyzer.objType = "void";
                                    SementicAnalyzer.objectTypeCheckError(tokenSet.get(index).L_N0);
                                    SementicAnalyzer.resetObjAttr();
                                    SementicAnalyzer.resetFunctionTableVariables();
                                    if (tokenSet.get(index).CP.equals(";")) {
                                        index++;
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    //MST

    public Boolean MST() {
        if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") || tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("break") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("if") || tokenSet.get(index).CP.equals("continue") || tokenSet.get(index).CP.equals("return")) {
            if (SST())
                if (MST())
                    return true;

        } else if (tokenSet.get(index).CP.equals("}"))
            return true;
        return false;
    }


    //BODY

    public Boolean BODY() {
        if (tokenSet.get(index).CP.equals("{")) {
            index++;
            //SEMENTIC
            SementicAnalyzer.createScope();
            if (MST()) {
                if (tokenSet.get(index).CP.equals("}")) {
                    index++;
                    SementicAnalyzer.destroyScope();
                    return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("do") || tokenSet.get(index).CP.equals("if") || tokenSet.get(index).CP.equals("while") || tokenSet.get(index).CP.equals("for") ||
                tokenSet.get(index).CP.equals("switch") || tokenSet.get(index).CP.equals("this") || tokenSet.get(index).CP.equals("base") ||
                tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("if") ||
                tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals("continue") || tokenSet.get(index).CP.equals("return"))
            if (SST())
                return true;
            else if (tokenSet.get(index).CP.equals("$"))
                return true;

        return false;
    }


    //DEF

    public Boolean DEF() {
        if (tokenSet.get(index).CP.equals("abstract") || tokenSet.get(index).CP.equals("final") || tokenSet.get(index).CP.equals("class")) {
            if (TYPE_OF_CLASS())
                if (tokenSet.get(index).CP.equals("class")) {
                    //Sementic
                    SementicAnalyzer.inheritanceCheck = true;
                    index++;
                    if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                        SementicAnalyzer.className = tokenSet.get(index).VP;
                        index++;
                        if (INHERITANCE()) {

                            //*********SEMENTIC*************
                            if (!SementicAnalyzer.insertInDefinitionTable()) {
                                SementicAnalyzer.redeclarationError(tokenSet.get(index).L_N0);
                            }
                            SementicAnalyzer.resetDefinitionTable();

                            if (tokenSet.get(index).CP.equals("{")) {
                                index++;
                                if (CBODY())

                                    if (CBODY())
                                        if (tokenSet.get(index).CP.equals("}")) {
                                            index++;
                                            if (DEF())
                                                return true;
                                        }
                            }

                        }
                    }
                }
        } else if (tokenSet.get(index).CP.equals("$") || tokenSet.get(index).CP.equals("class"))
            return true;

        return false;

    }


    //CBODY

    public Boolean CBODY() {

        if (tokenSet.get(index).CP.equals("constructor")) {
            if (CONSTRUCTOR())
                if (CBODY())
                    return true;
        } else if (tokenSet.get(index).CP.equals("AM") || tokenSet.get(index).CP.equals("static") || tokenSet.get(index).CP.equals("List") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("void")) {
            if (ACCESS_MODIFIER())
                if (A())
                    return true;
        } else if (tokenSet.get(index).CP.equals("abstract")) {

            //**************SEMENTIC*************
            SementicAnalyzer.classNotAbstractError(tokenSet.get(index).L_N0);
            SementicAnalyzer.memberTM = tokenSet.get(index).VP;
            index++;
            if (tokenSet.get(index).CP.equals("void") || tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("DT")) {
                SementicAnalyzer.memberReturnType = tokenSet.get(index).VP;
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    SementicAnalyzer.memberName = tokenSet.get(index).VP;
                    index++;

                    if (tokenSet.get(index).CP.equals("(")) {
                        index++;
                        if (ARGUMENT_LIST())
                            if (tokenSet.get(index).CP.equals(")")) {
                                index++;
                                if (SementicAnalyzer.memberType.isEmpty())
                                    SementicAnalyzer.memberType = "void";
                                SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;
                                if (!SementicAnalyzer.insertToClassTable()) {
                                    SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
                                }
                                SementicAnalyzer.resetClassMembers();
                                if (tokenSet.get(index).CP.equals(";")) {
                                    index++;
                                    if (CBODY())
                                        return true;
                                }
                            }
                    }
                }
            }
        } else if (tokenSet.get(index).CP.equals("}") || tokenSet.get(index).CP.equals("main"))
            return true;
        return false;
    }

    public Boolean CBODY2() {
        if (tokenSet.get(index).CP.equals("List")) {
            if (LIST())
                return true;
        } else if (tokenSet.get(index).CP.equals("DT")) {
            //SEMENTIC
            SementicAnalyzer.memberType = tokenSet.get(index).VP;
            SementicAnalyzer.memberReturnType = tokenSet.get(index).VP;
            ICG.functionReturnType = tokenSet.get(index).VP;
            index++;
            if (CBODY3())
                return true;
        } else if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            //SEMENTIC
            if (!SementicAnalyzer.lookupInDefinitionTable(tokenSet.get(index).VP)) {
                SementicAnalyzer.errors.add(tokenSet.get(index).VP + " is not a known class at line no " + tokenSet.get(index).L_N0);
            }


            SementicAnalyzer.typeStack.push(tokenSet.get(index).VP);
            SementicAnalyzer.memberType = tokenSet.get(index).VP;
            SementicAnalyzer.memberReturnType = tokenSet.get(index).VP;
            ICG.functionReturnType = tokenSet.get(index).VP;
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                //SEMENTIC
                SementicAnalyzer.objType = tokenSet.get(index).VP;
                SementicAnalyzer.memberName = tokenSet.get(index).VP;
                ICG.functionName = tokenSet.get(index).VP;
                index++;

                if (CLASS_OBJECT_CREATION_N_FUNCTION()) {
                    return true;
                }
            }
        } else if (tokenSet.get(index).CP.equals("void")) {
            SementicAnalyzer.memberReturnType = tokenSet.get(index).VP;
            ICG.functionReturnType = tokenSet.get(index).VP;
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                SementicAnalyzer.memberName = tokenSet.get(index).VP;
                ICG.functionName = tokenSet.get(index).VP;
                index++;
                if (tokenSet.get(index).CP.equals("(")) {
                    index++;
                    SementicAnalyzer.createScope();
                    if (ARGUMENT_LIST())
                        if (tokenSet.get(index).CP.equals(")")) {
                            //ICG
                            ICG.callfunctionName = ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc";
                            ICG.generateOutput(ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc");
                            ICG.functionName = "";
                            ICG.functionArgs = "";
                            index++;
                            //SEMENTIC
                            if (SementicAnalyzer.memberType.isEmpty())
                                SementicAnalyzer.memberType = "void";
                            SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;
                            if (!SementicAnalyzer.insertToClassTable())
                                SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);

                            SementicAnalyzer.resetClassMembers();
                            if (tokenSet.get(index).CP.equals("{")) {
                                index++;
                                if (MST())
                                    if (tokenSet.get(index).CP.equals("}")) {
                                        //ICG
                                        ICG.generateOutput("endP");
                                        index++;
                                        SementicAnalyzer.destroyScope();
                                        return true;
                                    }
                            }

                        }
                }
            }
        }
        return false;
    }

    public Boolean CBODY3() {
        if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
            SementicAnalyzer.memberName = tokenSet.get(index).VP;
            ICG.functionName = tokenSet.get(index).VP;
            index++;
            if (FUNC_N_DEC())
                return true;
        } else if (tokenSet.get(index).CP.equals("[")) {
            index++;
            if (tokenSet.get(index).CP.equals("]")) {
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {

                    //SEMENTIC
                    SementicAnalyzer.memberName = tokenSet.get(index).VP;
                    if (!SementicAnalyzer.insertToClassTable())
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
                    SementicAnalyzer.resetClassMembers();
                    index++;
                    if (A_INIT())
                        if (A_LIST())
                            return true;
                }
            }
        }
        return false;
    }


    public Boolean A() {
        if (tokenSet.get(index).CP.equals("static") || tokenSet.get(index).CP.equals("List") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("IDENTIFIER") || tokenSet.get(index).CP.equals("void")) {
            if (STATIC())
                if (CBODY2())
                    if (CBODY())
                        return true;
        } else if (tokenSet.get(index).CP.equals("abstract")) {
            //SEMENTIC
            SementicAnalyzer.classNotAbstractError(tokenSet.get(index).L_N0);
            SementicAnalyzer.memberTM = tokenSet.get(index).VP;
            index++;

            if (tokenSet.get(index).CP.equals("void") || tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
                SementicAnalyzer.memberReturnType = tokenSet.get(index).VP;
                //ICG
                ICG.functionReturnType = tokenSet.get(index).VP;
                index++;
                if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                    SementicAnalyzer.memberName = tokenSet.get(index).VP;
                    //ICG
                    ICG.functionName = tokenSet.get(index).VP;
                    index++;

                    if (tokenSet.get(index).CP.equals("(")) {
                        index++;
                        if (ARGUMENT_LIST())
                            if (tokenSet.get(index).CP.equals(")")) {
                                ICG.callfunctionName = ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc";
                                ICG.generateOutput(ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc");
                                ICG.functionName = "";
                                ICG.functionArgs = "";
                                index++;
                                if (SementicAnalyzer.memberType.isEmpty())
                                    SementicAnalyzer.memberType = "void";
                                SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;
                                if (!SementicAnalyzer.insertToClassTable()) {
                                    SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);
                                }
                                SementicAnalyzer.resetClassMembers();
                                if (tokenSet.get(index).CP.equals(";")) {
                                    index++;
                                    if (CBODY())
                                        return true;
                                }
                            }
                    }
                }
            }
        } else if (tokenSet.get(index).CP.equals("DT") || tokenSet.get(index).CP.equals("IDENTIFIER")) {
            //ICG
            ICG.functionReturnType = tokenSet.get(index).VP;
            index++;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                //ICG
                ICG.functionName = tokenSet.get(index).VP;
                index++;

                if (tokenSet.get(index).CP.equals("(")) {
                    index++;
                    if (ARGUMENT_LIST())
                        if (tokenSet.get(index).CP.equals(")")) {
                            //ICG
                            ICG.callfunctionName = ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc";
                            ICG.generateOutput(ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc");
                            ICG.functionName = "";
                            ICG.functionArgs = "";
                            index++;
                            return true;
                        }
                }
            }
        }


        return false;
    }


    public Boolean FUNC_N_DEC() {
        if (tokenSet.get(index).CP.equals("(")) {
            SementicAnalyzer.memberType = "";
            index++;
            SementicAnalyzer.createScope();
            if (ARGUMENT_LIST())
                if (tokenSet.get(index).CP.equals(")")) {
                    //ICG
                    ICG.callfunctionName = ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc";
                    ICG.generateOutput(ICG.functionName + "_" + ICG.functionArgs + "_" + "Proc");
                    ICG.functionName = "";
                    ICG.functionArgs = "";
                    index++;
                    //SEMENTIC
                    if (SementicAnalyzer.memberType.isEmpty())
                        SementicAnalyzer.memberType = "void";
                    SementicAnalyzer.memberType += "->" + SementicAnalyzer.memberReturnType;

                    if (!SementicAnalyzer.insertToClassTable())
                        SementicAnalyzer.memberRedeclarationError(tokenSet.get(index - 1).L_N0);

                    SementicAnalyzer.resetClassMembers();
                    if (tokenSet.get(index).CP.equals("{")) {
                        index++;
                        if (MST())
                            if (tokenSet.get(index).CP.equals("}")) {
                                //ICG
                                ICG.generateOutput("endP");
                                index++;
                                SementicAnalyzer.destroyScope();
                                return true;
                            }
                    }
                }
        } else if (tokenSet.get(index).CP.equals("ASSIGNMENT") || tokenSet.get(index).CP.equals(",") || tokenSet.get(index).CP.equals(";")) {
            if (!SementicAnalyzer.insertToClassTable())
                SementicAnalyzer.memberRedeclarationError(tokenSet.get(index).L_N0);
            SementicAnalyzer.resetClassMembers();
            if (INIT_EMPTY())
                return true;

        }
        return false;
    }
    //INHERITANCE

    public Boolean INHERITANCE() {
        if (tokenSet.get(index).CP.equals("extends")) {
            index++;
            SementicAnalyzer.inheritanceCheck = true;
            if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                SementicAnalyzer.classParent = tokenSet.get(index).VP;
                index++;
                //***********SEMENTIC*********
                if (!SementicAnalyzer.lookupInDefinitionTable(tokenSet.get(index - 1).VP)) {
                    SementicAnalyzer.inheritError(tokenSet.get(index - 1).L_N0);
                }

                return true;
            }
        } else if (tokenSet.get(index).CP.equals("{"))
            return true;

        return false;
    }

    //main

    public Boolean MAIN() {
        if (tokenSet.get(index).CP.equals("main")) {
            ICG.generateOutput("Main_Proc");
            index++;
            if (tokenSet.get(index).CP.equals("(")) {
                SementicAnalyzer.createScope();
                index++;
                if (ARGUMENT_LIST())
                    if (tokenSet.get(index).CP.equals(")")) {
                        index++;
                        if (tokenSet.get(index).CP.equals("{")) {
                            index++;
                            if (MST())
                                if (tokenSet.get(index).CP.equals("}")) {
                                    ICG.generateOutput("endP");
                                    index++;
                                    SementicAnalyzer.destroyScope();
                                    return true;
                                }
                        }

                    }
            }
        }
        return false;
    }


    //STARTING CFG

    public Boolean STARTING_NON_TERMINAL() {
        if (tokenSet.get(index).CP.equals("abstract") || tokenSet.get(index).CP.equals("final") || tokenSet.get(index).CP.equals("class")) {
            if (TYPE_OF_CLASS())
                if (tokenSet.get(index).CP.equals("class")) {
                    index++;
                    if (tokenSet.get(index).CP.equals("IDENTIFIER")) {
                        SementicAnalyzer.inheritanceCheck = true;
                        SementicAnalyzer.className = tokenSet.get(index).VP;
                        index++;
                        if (INHERITANCE()) {
                            //**********SEMENTIC*****************
                            if (!SementicAnalyzer.insertInDefinitionTable()) {
                                SementicAnalyzer.redeclarationError(tokenSet.get(index).L_N0);
                            }
                            SementicAnalyzer.resetDefinitionTable();

                            if (tokenSet.get(index).CP.equals("{")) {
                                index++;
                                if (CBODY())
                                    if (MAIN())
                                        if (CBODY())
                                            if (tokenSet.get(index).CP.equals("}")) {
                                                index++;
                                                if (DEF())
                                                    return true;
                                            }
                            }

                        }
                    }
                }


        }

        return false;
    }

//    public String[] a()
//    {
//
//    }


    public static void main(String[] args) {

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
        tokenSet.add(new TOKEN("$", "$", 100));


//        System.out.println(tokenSet.get(0));
//        tokenSet.forEach(v->System.out.println(v))
        if (syntaxAnalyzer.STARTING_NON_TERMINAL()) {
            System.out.println("CFG COMPLETELY PARSED");
            try {
                FileWriter fw = new FileWriter("C:\\Users\\hp\\Desktop\\Compiler-ConstructionIbrahim\\IntermediateCode.txt", false);
                if (SementicAnalyzer.errors.size() == 0) {


                    fw.write(ICG.intermediateCode);
                    fw.close();
                } else {
                    fw.write("");
                    fw.close();

                }
            } catch (Exception e) {

            }


        } else {
            System.out.println("ERROR-----> \n Line  no : " + tokenSet.get(index).L_N0 +
                    "\n Token no : " + index + "\n Classpart : " + tokenSet.get(index).CP);


        }

        SementicAnalyzer.errors.forEach(v -> System.out.println(v));


    }
}
