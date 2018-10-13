package ast;

import ui.Main;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DISPLAY extends STATEMENT {
    NAME name;
    Boolean debts = false;
    Boolean everyone = false;
    MONTH startMonth;
    MONTH endMonth;

    @Override
    public void parse() {

        tokenizer.getNext();
        if (tokenizer.checkToken("debts")) debts = true;
        else if (!tokenizer.checkToken("transactions")) System.exit(0);

        tokenizer.getNext();
        tokenizer.getAndCheckNext(" for ");

        if (tokenizer.checkToken("everyone")) {
            everyone = true;
            tokenizer.getNext();
        }
        else {
            name = new NAME();
            name.parse();
        }

        if (tokenizer.checkToken(" from ")) {
            tokenizer.getNext();
            startMonth = new MONTH();
            startMonth.parse();

            tokenizer.getAndCheckNext(" to ");

            endMonth = new MONTH();
            endMonth.parse();
        }

        else if(!tokenizer.checkToken(",")) System.exit(0);
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        int root = Main.displayDebtCount;

        if(debts){
            if(everyone) {
                HashMap<String, Integer> nameNumber= new HashMap<String, Integer>();
                //need to loop through each entry in symbol table and add anything that is a negative number
                for (Map.Entry<String, HashMap<String, Float>> current: Main.symbolTable.entrySet()) {
                    HashMap<String, Float> d = current.getValue();
                    String n = current.getKey();
                    for (Map.Entry<String, Float> entry : d.entrySet()) {

                        if (entry.getValue() <= 0) {
                            Integer positionFrom = nameNumber.get(n);
                            Integer positionTo = nameNumber.get(entry.getKey());
                            if (positionFrom == null) {
                                nameNumber.put(n, Main.displayDebtCount);
                                positionFrom = Main.displayDebtCount;
                                Main.displayDebtCount++;
                            }
                            if (positionTo == null) {
                                nameNumber.put(entry.getKey(), Main.displayDebtCount);
                                positionTo = Main.displayDebtCount;
                                Main.displayDebtCount++;
                            }
                            createNodeEdge(positionFrom, n, positionTo, entry.getKey(), Math.abs(Float.valueOf(entry.getValue())));
                        }
                    }
                }
                nameNumber.clear();
            }
            else {
                //return dot program string a -> b thing
                String n = name.evaluate();
                HashMap<String, Float> d = Main.symbolTable.get(n);
                //loop through each entry in
                for (Map.Entry<String, Float> entry : d.entrySet()) {
                    if (entry.getValue() <= 0) {
                        Main.displayDebtCount++;
                        createNodeEdge(root, n, Main.displayDebtCount, entry.getKey(), Math.abs(Float.valueOf(entry.getValue())));
                        Main.displayDebtCount++;
                    } else {
                        Main.displayDebtCount++;
                        createNodeEdge(Main.displayDebtCount, entry.getKey(), root, n, Math.abs(Float.valueOf(entry.getValue())));
                        Main.displayDebtCount++;
                    }

                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
                writer.println(root + " [fontcolor = \"red\"];");
            }
        } else {
            //return what it would look like as a transaction
        }
        return null;
    }


    private void createNodeEdge(Integer from, String fromLabel, Integer to, String toLabel, Float edgeValue) {
            writer.println(from + " [label = \"" + fromLabel + "\"];");
            writer.println(to + " [label = \"" + toLabel + "\"];");
            writer.println(from + "->" + to + " [label = \"" + edgeValue + "\"];");
            if (everyone) {
                writer.println(from + " [fontcolor = \"blue\"];");
                writer.println(to + " [fontcolor = \"blue\"];");
            }
    }
}
