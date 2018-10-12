package ast;

import ui.Main;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
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
        if(everyone){
            //need to loop through each entry in symbol table and add anything that is a negative number
        } else {
            String n = name.evaluate();
            HashMap<String, Float> d = Main.symbolTable.get(n);
            //loop through each entry in
            for (Map.Entry<String, Float> entry : d.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
        if(debts){
            //return dot program string a -> b thing
        } else {
            //return what it would look like as a transaction
        }
        return null;
    }
}
