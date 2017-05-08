package parser;

import parser.symbol.*;

import java.util.ArrayList;
import java.util.HashMap;

import tokenizer.TokenTypes.TokenType;

/**
 * Created by olegstotsky on 16.04.17.
 */
public class TypeManager {
    public enum BinaryOperationType { ARITHMETICAL, LOGICAL, COMPARISON }

    public static TypeManager instance = new TypeManager();
    public ArrayList<Pair<SymType, SymType>> legalTypeCasts;
    public HashMap<Pair<SymType, SymType>, SymType> arithmeticalTypeCasts;
    public HashMap<Pair<SymType, SymType>, SymType> logicalTypeCasts;
    public HashMap<Pair<SymType, SymType>, SymType> comparisonTypeCasts;
    public HashMap<TokenType, BinaryOperationType> tokTypeToOperationType;


    public TypeManager() {
        this.legalTypeCasts = new ArrayList<>();
        this.legalTypeCasts.add(new Pair<>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeChar.getInstance(), SymTypeChar.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeBoolean.getInstance(), SymTypeBoolean.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeFloat.getInstance(), SymTypeFloat.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeBoolean.getInstance(), SymTypeInteger.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeInteger.getInstance(), SymTypeChar.getInstance()));
        this.legalTypeCasts.add(new Pair<>(SymTypeChar.getInstance(), SymTypeInteger.getInstance()));

        this.arithmeticalTypeCasts = new HashMap<>();
        this.arithmeticalTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.arithmeticalTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()), SymTypeFloat.getInstance());
        this.arithmeticalTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.arithmeticalTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance()), SymTypeInteger.getInstance());


        this.logicalTypeCasts = new HashMap<>();
        this.logicalTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.logicalTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()), SymTypeFloat.getInstance());
        this.logicalTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeFloat.getInstance()), SymTypeFloat.getInstance());
        this.logicalTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance()), SymTypeInteger.getInstance());

        this.comparisonTypeCasts = new HashMap<>();
        this.comparisonTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeFloat.getInstance()), SymTypeBoolean.getInstance());
        this.comparisonTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeInteger.getInstance()), SymTypeBoolean.getInstance());
        this.comparisonTypeCasts.put(new Pair<SymType, SymType>(SymTypeFloat.getInstance(), SymTypeFloat.getInstance()), SymTypeBoolean.getInstance());
        this.comparisonTypeCasts.put(new Pair<SymType, SymType>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance()), SymTypeBoolean.getInstance());

        this.tokTypeToOperationType = new HashMap<>();
        this.tokTypeToOperationType.put(TokenType.PLUS, BinaryOperationType.ARITHMETICAL);
        this.tokTypeToOperationType.put(TokenType.MINUS, BinaryOperationType.ARITHMETICAL);
        this.tokTypeToOperationType.put(TokenType.MUL, BinaryOperationType.ARITHMETICAL);
        this.tokTypeToOperationType.put(TokenType.DIV, BinaryOperationType.ARITHMETICAL);

        this.tokTypeToOperationType.put(TokenType.XOR, BinaryOperationType.LOGICAL);
        this.tokTypeToOperationType.put(TokenType.OR, BinaryOperationType.LOGICAL);
        this.tokTypeToOperationType.put(TokenType.AND, BinaryOperationType.LOGICAL);

        this.tokTypeToOperationType.put(TokenType.GRT, BinaryOperationType.COMPARISON);
        this.tokTypeToOperationType.put(TokenType.LESS, BinaryOperationType.COMPARISON);
        this.tokTypeToOperationType.put(TokenType.GRT_E, BinaryOperationType.COMPARISON);
        this.tokTypeToOperationType.put(TokenType.LESS_E, BinaryOperationType.COMPARISON);
        this.tokTypeToOperationType.put(TokenType.EQUAL, BinaryOperationType.COMPARISON);
    }

    public static TypeManager getInstance() {
        return instance;
    }

    public SymType resolveBinOperationResultType(SymType first, SymType second, TokenType operation) throws Exception {
        BinaryOperationType opType = this.tokTypeToOperationType.get(operation);

        Pair<SymType, SymType> fi = new Pair<>(SymTypeInteger.getInstance(), SymTypeInteger.getInstance());
        Pair<SymType, SymType> se = new Pair<>(first, second);
        boolean q = fi.equals(se);
        boolean a = fi.hashCode() == se.hashCode();
        if (opType == BinaryOperationType.ARITHMETICAL) {
            return this.arithmeticalTypeCasts.get(new Pair<>(first ,second));
        }
        if (opType == BinaryOperationType.LOGICAL) {
            return this.logicalTypeCasts.get(new Pair<>(first, second));
        }
        if (opType == BinaryOperationType.COMPARISON) {
            return this.comparisonTypeCasts.get(new Pair<>(first, second));
        }

        return null;
    }

    public Boolean isLegalTypeCast(SymType from, SymType to) {
        Pair<SymType, SymType> target = new Pair<>(from, to);
        return this.legalTypeCasts.contains(target);
    }
}
