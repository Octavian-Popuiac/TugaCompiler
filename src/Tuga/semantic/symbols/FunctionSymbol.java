package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol{
    private final List<VarSymbol> parameters = new ArrayList<>();
    private boolean hasReturn = false;

    public FunctionSymbol(String  name, Type returnType){
        super(name, returnType);
    }
    public FunctionSymbol(Token token, Type returnType){
        super(token.getText(), returnType);
        this.token = token;
    }

    public void addParameter(VarSymbol param){
        parameters.add(param);
    }

    public List<VarSymbol> getParameters() {
        return parameters;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public boolean hasReturn() {
        return hasReturn;
    }

    @Override
    public String toString(){
        return "function<" + name + ":" + type + '>' + parameters;
    }
}
