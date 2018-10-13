package ast;

import libs.Node;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PROGRAM extends Node{
    private List<STATEMENT> statements = new ArrayList<>();


    @Override
    public void parse() {
        System.out.println("start parser");
        while(tokenizer.moreTokens()){
            STATEMENT s;
            if(tokenizer.checkToken("display debts as ")){
                s = new DISPLAY();
            } else {
                s = new PAYSOWES();
            }
            statements.add(s);
            s.parse();
            tokenizer.getNext(); //must run getNext to increment
        }
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        System.out.println("evaluating");
        writer = new PrintWriter("output.dot", "UTF-8");
        writer.println("digraph G{");
        writer.println("info [shape = box label=\"An arrow from X to Y means X owes Y \n " +
                "A red name means that graph contains all debts for that name\n" +
                "A graph with all blue notes is the total debts owed so far\"]");
        for (STATEMENT s : statements){
            s.evaluate();
        }

        writer.println("}");
        writer.close();
        return null;
    }
}
