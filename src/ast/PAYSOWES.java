package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PAYSOWES extends STATEMENT {
    List<NAME> beforePaysOwes = new ArrayList<>();
    List<NAME> afterPaysOwes = new ArrayList<>();
    AMOUNT amount;
    Boolean owes = false; //if true, make amount a negative number
    MONTH startMonth;
    MONTH endMonth;

    @Override
    public void parse() {
        System.out.println("in statement");

        // deal with names
        Boolean hasMoreNames = true;
        while (!tokenizer.checkToken(" pays ") && !tokenizer.checkToken(" owes ")){
            //TODO add to beforePaysOwes

            //make sure that there are actually more names, and someone didn't make a typo with pays/owes
            if (!hasMoreNames) System.exit(0);

            NAME n = new NAME();
            n.parse();
            beforePaysOwes.add(n);
            hasMoreNames = false;
            if (tokenizer.checkToken(" and ")) {
                tokenizer.getNext();
                hasMoreNames = true;
            }

        }

        // pays or owes
        if(tokenizer.checkToken(" owes ")){
            owes = true;
        }
        tokenizer.getNext();

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


       // String next = tokenizer.getNext();
        //check if it is a recurring payment, if so process start and end month
        if (tokenizer.checkToken(" every month")) {
            tokenizer.getNext();
            Boolean b = tokenizer.checkToken("\\|");
            tokenizer.getNext();
            MONTH sm = new MONTH();
            sm.parse();
            startMonth = sm;

            tokenizer.getAndCheckNext("\\|");
            MONTH em = new MONTH();
            em.parse();
            endMonth = em;
        }





        if (!tokenizer.checkToken(",")) System.exit(0);
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
