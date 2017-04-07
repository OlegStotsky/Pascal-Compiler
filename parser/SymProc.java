package parser;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymProc extends Symbol {
    public ArrayList<SymVar> args;
    public ArrayList<SymVar> locals;

    public SymProc() {
        this.args = null;
        this.locals = null;
    }

    public SymProc(ArrayList<SymVar> args, ArrayList<SymVar> locals) {
        this.args = (ArrayList<SymVar>)args.clone();
        this.locals = (ArrayList<SymVar>)locals.clone();
    }
}
