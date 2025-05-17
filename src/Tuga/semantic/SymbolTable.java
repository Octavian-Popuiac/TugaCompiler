package Tuga.semantic;

import Tuga.semantic.symbols.Scope;
import Tuga.semantic.symbols.Symbol;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Scope> savedScopes = new HashMap<>();

    private Scope currentScope;

    public SymbolTable(){
        // Inicializa o escopo global
        currentScope = new Scope(null, "__global__");
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public void saveScope(String functionName){
        // Nao posso expor Scope diretamente, entao guardamos uma referencia interna
        savedScopes.put(functionName, currentScope);
    }

    // Restaura um escopo previamente salvo
    public boolean restoreScope(String functionName){
        if (savedScopes.containsKey(functionName)){
            currentScope = savedScopes.get(functionName);
            return true;
        }

        return false;
    }

    // Entra em um novo escopo (funcao ou bloco)
    public void enterScope(String name){
        for (Scope childScope : currentScope.getChildScopes()){
            if (childScope.getName().equals(name)){
                currentScope = childScope;
                return;
            }
        }

        currentScope = new Scope(currentScope, name);
    }

    public void enterScope(){
        currentScope = new Scope(currentScope, "__local__");
    }

    // Sai do escopo atual, retorna ao escopo pai
    public void exitScope(){
        Scope encolsingScope = currentScope.getEnclosingScope();

        if (encolsingScope != null){
            currentScope = encolsingScope;
        }
    }

    // Declara um simbolo no escopo atual
    public void declare(Symbol symbol, Token token){
        String name = symbol.lexeme();
        if (currentScope.contains(name)){
            throw new TypeCheckingException(
                    String.format(
                            "erro na linha %d: '%s' ja foi declarado",
                            token.getLine(),
                            name
                    )
            );
        }

        currentScope.define(symbol);
    }

    // Procura um simbolo em todos os escopos encadeados
    public Symbol lookup(String name, Token token) {
        Symbol symbol = currentScope.resolve(name);

        if (symbol != null){
            return symbol;
        }

        // Simbolo nao encontrado em nenhum escopo
        throw new TypeCheckingException(
                String.format("erro na linha %d: '%s' nao foi declarado",
                        token.getLine(), name));
    }

    // Procura em todos os escopos sem gerar erro
    public Symbol lookupSymbol(String name) {
        return currentScope.resolve(name);
    }

}
