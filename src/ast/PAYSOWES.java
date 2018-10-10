package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PAYSOWES extends STATEMENT {
    List<NAME> beforePaysOwes = new ArrayList<>();
    List<NAME> afterPaysOwes = new ArrayList<>();
    AMOUNT amount;
    Boolean owes = false; //if true, make amount a negative number

    @Override
    public void parse() {
        System.out.println("in statement");

        // deal with names
        while (!tokenizer.checkToken(" pays ") && !tokenizer.checkToken(" owes ")){
            //TODO add to beforePaysOwes
            NAME n = new NAME();
            n.parse();
            beforePaysOwes.add(n);
            if (tokenizer.checkToken(" and ")) tokenizer.getNext();

        }

        // pays or owes
        if(tokenizer.checkToken(" owes ")){
            owes = true;
        }
        String x = tokenizer.getNext();
        System.out.println("made it here " + x);
        // deal with names part 2
        while (!tokenizer.checkToken(":")){
            NAME n = new NAME();
            n.parse();
            afterPaysOwes.add(n);
            if (tokenizer.checkToken(" and ")) tokenizer.getNext();
        }
        //skip ":"
        tokenizer.getNext();

        // amount
        amount = new AMOUNT();
        amount.parse();


        if (!tokenizer.checkToken(",")) System.exit(0);
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
