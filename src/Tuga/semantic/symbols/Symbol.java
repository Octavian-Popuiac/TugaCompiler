package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

public class Symbol {
    public final String name;
    public final Type type;
    public Token token; // Token de origem para mensagens de erro
    public Scope scope; // Todos os simbolos sabem qual escopo os contem

    protected Symbol(String name, Type type){
        this.name = name;
        this.type = type;
    }

    protected Symbol(Token token, Type type){
        this.token = token;
        this. name = token.getText();
        this.type = type;
    }

    public Token getToken(){ return token;}

    public String lexeme(){ return name;}

    @Override
    public String toString(){
        if (type != null){
            return '<' + lexeme() + ":" + type + '>';
        }
        return lexeme();
    }
}
