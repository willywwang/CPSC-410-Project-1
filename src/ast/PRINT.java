package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class PRINT extends STATEMENT {
    USE useStmt;
    String simpleVal;
    @Override
    public void parse() {
        tokenizer.getAndCheckNext("print");
        if (tokenizer.checkToken("get")) {
            useStmt = new USE();
            useStmt.parse();
        }
        else {
            simpleVal = tokenizer.getNext();
        }
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        if (useStmt!=null) {
            System.out.println("PRINTING: " + useStmt.evaluate());
        }
        else System.out.println("SIMPLE VAL PRINTING: "+simpleVal);
        return null;
    }
}
