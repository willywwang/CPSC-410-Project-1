package ast;

import ui.Main;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DISPLAY extends STATEMENT {
    NAME name;
    Boolean graph = false;
    Boolean everyone = false;
    MONTH startMonth;
    MONTH endMonth;

    @Override
    public void parse() {

        tokenizer.getNext();
        tokenizer.getNext();
        if (tokenizer.checkToken("graph")) graph = true;
        else if (!tokenizer.checkToken("text")) System.exit(0);

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
        int root = Main.displayGraphCount;

        if(graph){
            if(everyone) {
                //keep track of value of each node based on name
                HashMap<String, Integer> nameNumber= new HashMap<String, Integer>();
                //need to loop through each entry in symbol table and add anything that is a negative number
                for (Map.Entry<String, HashMap<String, Float>> current: Main.symbolTable.entrySet()) {
                    HashMap<String, Float> d = current.getValue();
                    String n = current.getKey();
                    for (Map.Entry<String, Float> entry : d.entrySet()) {

                        if (entry.getValue() <= 0) {
                            Integer positionFrom = nameNumber.get(n);
                            Integer positionTo = nameNumber.get(entry.getKey());
                            /*check if nodes already have a number associated with name, or if we need to create a new number
                            this is due to the fact that nodes are identified by their name, so we have to assign numbers
                            to nodes based on the current graph.

                            */
                            if (positionFrom == null) {
                                nameNumber.put(n, Main.displayGraphCount);
                                positionFrom = Main.displayGraphCount;
                                Main.displayGraphCount++;
                            }
                            if (positionTo == null) {
                                nameNumber.put(entry.getKey(), Main.displayGraphCount);
                                positionTo = Main.displayGraphCount;
                                Main.displayGraphCount++;
                            }
                            Float temp = Math.abs(entry.getValue());
                            String amountOwed = String.format("%.2f", temp);
                            createNodeEdge(positionFrom, n, positionTo, entry.getKey(),amountOwed);
                        }
                    }
                }
                //clear map so that next time "display debts for everyone is called", those results aren't merged with this current graph
                nameNumber.clear();
            }
            else {
                //return dot program string a -> b thing
                String n = name.evaluate();
                HashMap<String, Float> d = Main.symbolTable.get(n);
                //loop through each entry in
                for (Map.Entry<String, Float> entry : d.entrySet()) {
                    Float temp = Math.abs(entry.getValue());
                    String amountOwed = String.format("%.2f", temp);
                    if (entry.getValue() <= 0) {
                        Main.displayGraphCount++;
                        createNodeEdge(root, n, Main.displayGraphCount, entry.getKey(), amountOwed);
                        Main.displayGraphCount++;
                    } else {
                        Main.displayGraphCount++;
                        createNodeEdge(Main.displayGraphCount, entry.getKey(), root, n, amountOwed);
                        Main.displayGraphCount++;
                    }

                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
                writer.println(root + " [fontcolor = \"red\"];");
            }
        } else {
            //return what it would look like as a text
            if (everyone) {
                writer.println(Main.displayTextCount + " [shape = box label =\" everyone\'s debts\"];");
                writer.println((Main.displayTextCount-0.5) + " [shape = box label =\"");
                //need to loop through each entry in symbol table and add anything that is a negative number
                for (Map.Entry<String, HashMap<String, Float>> current: Main.symbolTable.entrySet()) {
                    HashMap<String, Float> d = current.getValue();
                    String n = current.getKey();
                    for (Map.Entry<String, Float> entry : d.entrySet()) {

                        if (entry.getValue() <= 0) {
                            Float temp = Math.abs(entry.getValue());
                            String amountOwed = String.format("%.2f", temp);
                            createTextForm(n, entry.getKey(), amountOwed);
                        }
                    }
                }
                writer.print("\"];\n");
                writer.println(Main.displayTextCount + "->" + (Main.displayTextCount-0.5) + ";");
                Main.displayTextCount--;

            }
            else {
                String n = name.evaluate();
                HashMap<String, Float> d = Main.symbolTable.get(n);
                writer.println(Main.displayTextCount + " [shape = box label =\"" + n + "\'s debts\"];");
                writer.println((Main.displayTextCount-0.5) + " [shape = box label =\"");
                for (Map.Entry<String, Float> entry : d.entrySet()) {
                    Float temp = Math.abs(entry.getValue());
                    String amountOwed = String.format("%.2f", temp);
                    if (entry.getValue() <= 0) {
                            createTextForm(n, entry.getKey(), amountOwed);
                    }
                    else {
                        createTextForm(entry.getKey(), n,amountOwed);
                    }
                }
                writer.print("\"];\n");
                writer.println(Main.displayTextCount + "->" + (Main.displayTextCount-0.5) + ";");
                Main.displayTextCount--;
            }
        }
        return null;
    }


    private void createNodeEdge(Integer from, String fromLabel, Integer to, String toLabel, String edgeValue) {
            writer.println(from + " [label = \"" + fromLabel + "\"];");
            writer.println(to + " [label = \"" + toLabel + "\"];");
            writer.println(from + "->" + to + " [label = \"" + edgeValue + "\"];");
            if (everyone) {
                writer.println(from + " [fontcolor = \"blue\"];");
                writer.println(to + " [fontcolor = \"blue\"];");
            }
    }

    private void createTextForm(String owes, String isOwed, String amount) {
            writer.print(owes + " owes " + isOwed + " $" + amount + " \n ");
        }
}
