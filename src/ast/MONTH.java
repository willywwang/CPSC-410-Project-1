package ast;

import libs.Node;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class MONTH extends Node {
    Integer month;
    @Override
    public void parse() {
        String monthWritten = tokenizer.getNext().toLowerCase();
        Integer monthNumber = isValidMonth(monthWritten);
        if (monthNumber == 0)
            System.exit(0);
        else month =  monthNumber;
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        return null;
    }

    private Integer isValidMonth(String month) {
        switch(month) {
            case "january": return 1;
            case "february": return 2;
            case "march": return 3;
            case "april": return 4;
            case "may": return 5;
            case "june": return 6;
            case "july": return 7;
            case "august": return 8;
            case "september": return 9;
            case "october": return 10;
            case "november": return 11;
            case "december": return 12;
            default: return 0;
        }
    }
}
