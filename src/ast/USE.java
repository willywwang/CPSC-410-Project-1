package ast;

import ui.Main;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class USE extends STATEMENT {
    String name;
    @Override
    public void parse() {
        tokenizer.getAndCheckNext("get");
        name = tokenizer.getNext();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Getting "+name+" from Symbol Table");
        return String.valueOf(Main.symbolTable.get(name));
    }
}
