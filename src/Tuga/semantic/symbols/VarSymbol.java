package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

public class VarSymbol extends Symbol{
    public final boolean isParameter;

    public VarSymbol(String name, Type type, boolean isParameter) {
        super(name, type);
        this.isParameter = isParameter;
    }

    public VarSymbol(Token token, Type type, boolean isParameter){
        super(token.getText(), type);
        this.token = token;
        this.isParameter = isParameter;
    }

    public VarSymbol(String name, Type type) {
        this(name, type, false);
    }

    public VarSymbol(Token token, Type type){
        this(token, type, false);
    }
}
