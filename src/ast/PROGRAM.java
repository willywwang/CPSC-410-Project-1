package ast;

import libs.Node;

import java.io.FileNotFoundException;
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
            if(tokenizer.checkToken("display ")){
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
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        for (STATEMENT s : statements){
            s.evaluate();
        }
        return null;
    }
}
