package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class PROCCALL extends STATEMENT {
    private String name;


    @Override
    public void parse() {
        tokenizer.getAndCheckNext("call");
        name = tokenizer.getNext();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
