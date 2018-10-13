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
                //need to loop through each entry in symbol table and add anything that is a negative number
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

    }
}
