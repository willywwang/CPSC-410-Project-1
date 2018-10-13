package ui;

import ast.PROGRAM;
import libs.Tokenizer;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static HashMap<String,HashMap<String, Float>> symbolTable = new HashMap<>();
    public static Integer displayDebtCount = 1;
    public static Integer displayTransactionCount = -1;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        List<String> literals = Arrays.asList(" owes ", " pays ", " and ", ":", ",", " every month", "|",
                "display ", "debts", "transactions", " for ", " from ", " to ");

        Tokenizer.makeTokenizer("input.divide",literals);
        PROGRAM p = new PROGRAM();
        p.parse();
        p.evaluate();
        System.out.println("completed successfully");
        System.out.println(symbolTable);
    }

}
