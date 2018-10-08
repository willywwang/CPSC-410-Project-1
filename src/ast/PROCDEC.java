package ast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class PROCDEC extends STATEMENT {
    private String name;
    private BLOCK codeblock;


    @Override
    public void parse() {
        tokenizer.getAndCheckNext("def");
        name = tokenizer.getNext();
        tokenizer.checkToken(("start"));
        codeblock = new BLOCK();
        codeblock.parse();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        return null;
    }
}
