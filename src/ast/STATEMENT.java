package ast;

import libs.Node;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class STATEMENT extends Node {
    NAME beforePaysOwes;
    NAME afterPaysOwes;
    AMOUNT amount;
    Boolean owes = false; //if true, make amount a negative number

    @Override
    public void parse() {
        System.out.println("in statement");

        // deal with names
        while (!tokenizer.checkToken(" pays ") && !tokenizer.checkToken(" owes ")){
            //TODO add to beforePaysOwes
            tokenizer.getNext();
        }

        // pays or owes
        if(tokenizer.checkToken(" owes ")){
            owes = true;
        }
        tokenizer.getNext();

        // deal with names part 2
        while (!tokenizer.checkToken(":")){
            //TODO add to afterPaysOwes
            tokenizer.getNext();
        }

        // amount
        //TODO call amount
        tokenizer.getNext();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
