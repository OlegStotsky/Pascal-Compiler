package parser;

import parser.symbol.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by olegstotsky on 16.04.17.
 */
public class TypeManager {
    public GlobalSymTable globalSymTable;
    public ArrayList<Pair<SymType, SymType>> legalTypeCasts;
    public HashMap<Pair<SymType, SymType>, SymType> typeCasts;

    public TypeManager(GlobalSymTable globalSymTable) {
        this.globalSymTable = globalSymTable;
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeBoolean.getInstance()));
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeBoolean.getInstance(), SymTypeInteger.getInstance()));
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()));
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()));
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeChar.getInstance()));
        this.legalTypeCasts.add(new Pair<SymType, SymType>(SymTypeChar.getInstance(), SymTypeInteger.getInstance()));

        this.typeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.typeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()), SymTypeFloat.getInstance());
        this.typeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.typeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance()), SymTypeInteger.getInstance());
        this.typeCasts.put(new Pair<SymType, SymType>(SymTypeChar.getInstance(), SymTypeChar.getInstance()), SymTypeChar.getInstance());
    }

    public SymType resolveOperationResultType(SymType first, SymType second) {
        Pair<SymType, SymType> targetPair = new Pair<>(first, second);
        return this.typeCasts.get(targetPair);
    }

    public Boolean isLegalTypeCast(SymType from, SymType to) {
        Pair<SymType, SymType> targetPair = new Pair<SymType, SymType>(from, to);
        for (Pair<SymType, SymType> pair : this.legalTypeCasts) {
            if (pair.first == targetPair.first & pair.second == targetPair.second) {
                return true;
            }
        }

        return false;
    }
}
