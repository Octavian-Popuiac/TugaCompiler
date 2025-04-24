package Tuga.semantic;

import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, VarSymbol> symbols = new HashMap<>();

    public void declare(String name, Type type, Token token){
        if (symbols.containsKey(name)){
            throw new TypeCheckingException(
                    String.format(
                            "Erro na linha %d:%d - Variavel '%s' ja foi declarada",
                            token.getLine(),
                            token.getCharPositionInLine(),
                            name
                    )
            );
        }symbols.put(name, new VarSymbol(type));
    }

    public Type lookup(String name, Token token){
        VarSymbol symbol = symbols.get(name);
        if (symbol == null){
            throw new TypeCheckingException(
                    String.format(
                            "Erro na linha %d:%d - Variavel '%s' nao foi declarada",
                            token.getLine(),
                            token.getCharPositionInLine(),
                            name
                    )
            );
        }
        return symbol.type;
    }


    class VarSymbol{
        Type type;

        VarSymbol(Type type){
            this.type = type;
        }
    }
}
