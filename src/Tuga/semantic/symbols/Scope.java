package Tuga.semantic.symbols;

import java.util.*;

public class Scope {
    Scope enclosingScope; // null if global
    String name; // for debug
    public Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();
    private List<Scope> childScopes = new ArrayList<>();

    public Scope(Scope enclosingScope){
        this.enclosingScope = enclosingScope;
        this.name = "__no_name__";
    }

    public Scope(Scope enclosingScope, String name){
        this.enclosingScope = enclosingScope;
        this.name = name;
        if (enclosingScope != null){
            enclosingScope.addChildScope(this);
        }
    }

    public String getName() {return name;}

    public void setName(String name){this.name = name;}


    // Retorna true se identificar o nome no scope
    public boolean contains(String name){
        return resolve_local(name) != null;
    }

    public void define(Symbol sym){
        symbols.put(sym.lexeme(), sym);
        sym.scope = this;
    }

    public Symbol resolve_local(String name){
        return symbols.get(name); // Retorna um Symbol ou null
    }

    public Symbol resolve(String name){
        Symbol s = resolve_local(name);
        if (s != null){
            return s;
        }

        // Se nao estiver aqui, procura noutro qualquer scope
        if (enclosingScope != null){
            return enclosingScope.resolve(name);
        }

        return null; // Caso nao seja encontrado
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public String toString(){
        if (enclosingScope != null){
            return getName() + ":" + symbols.keySet().toString() + " --> " + enclosingScope.toString();
        }else {
            return getName() + ":" + symbols.keySet().toString(); // global scope
        }
    }

    public Collection<Symbol> getSymbols() {
        return symbols.values();
    }

    public void addChildScope(Scope child) {
        childScopes.add(child);
    }
    public List<Scope> getChildScopes() {
        return childScopes;
    }
}
