package ast;

import ui.Main;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PAYSOWES extends STATEMENT {
    List<NAME> beforePaysOwes = new ArrayList<>();
    List<NAME> afterPaysOwes = new ArrayList<>();
    AMOUNT amount;
    Boolean owes = false; //if true, make amount a negative number
    Boolean monthData = false;
    MONTH startMonth;
    MONTH endMonth;

    @Override
    public void parse() {
        System.out.println("in statement");

        // deal with names
        Boolean hasMoreNames = true;
        while (!tokenizer.checkToken(" pays ") && !tokenizer.checkToken(" owes ")){

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
            monthData = true;
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
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        System.out.println("paysowes evaluating");
        Float amt = Float.valueOf(amount.evaluate());
        if(owes){
            amt = -amt;
        }

        //handle months
        if(monthData){
            amt *= getNumberOfMonths();
        }

        setSymbolTableEntries(beforePaysOwes, afterPaysOwes, amt);
        setSymbolTableEntries(afterPaysOwes, beforePaysOwes, -amt);
        return null;
    }

    private Integer getNumberOfMonths() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        Integer sMonth = Integer.parseInt(startMonth.evaluate());
        Integer eMonth = Integer.parseInt(endMonth.evaluate());
        if (eMonth < sMonth) {
            return (12-sMonth)+eMonth + 1;
        }
        return eMonth - sMonth + 1;
    }


    private void setSymbolTableEntries(List<NAME> outerLoop, List<NAME> innerLoop, Float amt) throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        HashMap<String, Float> innerMap;
        Float totalAmount;
        for (NAME outer : outerLoop){
            String outerName = outer.evaluate();
            innerMap = Main.symbolTable.get(outerName);

            if(innerMap == null){
                innerMap = new HashMap<>();
            }

            for (NAME inner : innerLoop){
                String innerName = inner.evaluate();

                //set total amount
                totalAmount = innerMap.get(innerName);
                if(totalAmount == null) {
                    totalAmount = amt;
                } else {
                    totalAmount += amt;
                }

                BigDecimal roundedAmount = new BigDecimal(totalAmount.toString());
                roundedAmount = roundedAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

                innerMap.put(innerName, roundedAmount.floatValue());
                Main.symbolTable.put(outerName,innerMap);
            }
        }
    }
}
