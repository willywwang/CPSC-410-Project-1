package ast;

import libs.Node;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class AMOUNT extends Node {
    String amountVal;

    private List<Character> validOperators = Arrays.asList('*','/','+','-');


    @Override
    public void parse() {
        amountVal = tokenizer.getNext();
        boolean isValid = isValid();

        // throw exception based off validity
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval(amountVal);

        return result.toString();
    }

    // Determines if the given string is valid
    private boolean isValid() {
        String prevVal = null;
        String currentVal = null;
        Character currentChar;
        boolean isFirstIteration = true;
        int leftBracketCount = 0;
        int rightBracketCount = 0;

        // Iterate through the string
        for (int i = 0; i < amountVal.length(); i++) {
            currentChar = amountVal.charAt(i);

            if (currentChar == '(') {
                leftBracketCount++;
            } else if (currentChar == ')') {
                rightBracketCount++;
            }

            // If there are ever more right brackets compared to left brackets
            if (rightBracketCount > leftBracketCount) {
                return false;
            }

            // If a character is a number
            if (Character.isDigit(currentChar)) {
                if (currentVal == null) {
                    currentVal = currentChar.toString();
                } else {
                    currentVal = currentVal + currentChar.toString();
                }
            } else {
                // If the operator is invalid
                if (!validOperators.contains(currentChar)) {
                    return false;
                }

                // Operation before value
                if (isFirstIteration && currentVal == null && currentChar != '(') {
                    return false;
                } else if (!isFirstIteration && prevVal == null && currentChar != ')') {
                    // only exception to this is if we somehow end up with 2 right brackets together
                    // therefore we allow an exception for right brackets
                    // ie. 9 + (5 * (4 / 2))
                    return false;
                }

                prevVal = currentVal;
                currentVal = null;
            }

            isFirstIteration = false;
        }

        // Inaccurate number of brackets
        if (leftBracketCount != rightBracketCount) {
            return false;
        }

        return true;
    }
}
