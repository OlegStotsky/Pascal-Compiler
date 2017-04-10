package parser.symbol;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymFunc extends SymProc {
    public SymType returnType;

    public SymFunc() {
        this.returnType = null;
    }

    public SymFunc(SymType returnType, ArrayList<SymVar> args, ArrayList<SymVar> locals) {
        super(args, locals);
        this.returnType = returnType;
    }
}
