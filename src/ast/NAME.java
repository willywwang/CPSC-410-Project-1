package ast;

import libs.Node;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class NAME extends Node {
    String name;
    @Override
    public void parse() {
        if (tokenizer.checkToken("\\D*"))
            name = tokenizer.getNext();
    }

    @Override
    public String evaluate() throws FileNotFoundException, UnsupportedEncodingException, ScriptException {
        return null;
    }
}
