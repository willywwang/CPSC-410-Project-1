package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class DISPLAY extends STATEMENT {
    NAME name;
    Boolean debts = false;
    Boolean everyone = false;
    MONTH startMonth;
    MONTH endMonth;

    @Override
    public void parse() {

        String x = tokenizer.getNext();
        if (tokenizer.checkToken("debts")) debts = true;
        else if (!tokenizer.checkToken("transactions")) System.exit(0);

        tokenizer.getNext();
        tokenizer.getAndCheckNext(" for ");

        if (tokenizer.checkToken("everyone")) {
            everyone = true;
            tokenizer.getNext();
        }
        else {
            NAME n = new NAME();
            n.parse();
        }

        if (tokenizer.checkToken(" from ")) {
            tokenizer.getNext();
            MONTH sm = new MONTH();
            sm.parse();
            startMonth = sm;

            tokenizer.getAndCheckNext(" to ");

            MONTH em = new MONTH();
            em.parse();
            endMonth = em;
        }

        else if(!tokenizer.checkToken(",")) System.exit(0);
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
