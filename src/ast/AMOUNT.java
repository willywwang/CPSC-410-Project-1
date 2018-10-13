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

    private List<Character> validOperators = Arrays.asList('(',')','*','/','+','-','.');


    @Override
    public void parse() {
        amountVal = tokenizer.getNext();
        boolean isValid = isValid();

        if (!isValid) {
            System.out.println("invalid");
            System.exit(0);
        }
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
        String trimmedVal = amountVal.trim();
        Character prevChar = ' ';
        Character currentChar = ' ';
        boolean isFirstIteration = true;
        int leftBracketCount = 0;
        int rightBracketCount = 0;

        // Iterate through the string
        for (int i = 0; i < trimmedVal.length(); i++) {
            currentChar = trimmedVal.charAt(i);

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
                // Only ) character that cannot precede a number
                if (prevChar == ')') {
                    return false;
                }
            } else {
                // If the operator is invalid
                if (!validOperators.contains(currentChar)) {
                    return false;
                }

                // Operation before value
                if (isFirstIteration && currentChar != '(') {
                    return false;
                } else if (!isFirstIteration && !Character.isDigit(prevChar)) {
                    // If we have 2 operators in succession

                    // Two operators can only be contiguous if the previous one is a closing bracket
                    // ie. (1+(2+3))
                    if (prevChar != ')') {
                        return false;
                    }
                }
            }

            isFirstIteration = false;
            prevChar = currentChar;
            currentChar = ' ';
        }

        // Inaccurate number of brackets
        if (leftBracketCount != rightBracketCount) {
            return false;
        }

        return true;
    }
}
