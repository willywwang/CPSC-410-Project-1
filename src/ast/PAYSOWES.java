package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class PAYSOWES extends STATEMENT {
    List<NAME> beforePaysOwes;
    List<NAME> afterPaysOwes;
    AMOUNT amount;
    Boolean owes = false; //if true, make amount a negative number

    @Override
    public void parse() {
        System.out.println("in statement");

        // deal with names
        while (!tokenizer.checkToken(" pays ") && !tokenizer.checkToken(" owes ")){
            NAME n;
            //TODO add to beforePaysOwes
            n = new NAME();
            beforePaysOwes.add(n);
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
        amount = new AMOUNT();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
