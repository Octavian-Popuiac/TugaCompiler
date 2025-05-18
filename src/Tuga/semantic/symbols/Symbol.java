package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

/**
 * Representa um simbolo na tabela de simbolos.
 * Um simbolo e um elemento nomeado no programa fonte, como uma variavel ou funcao.
 * Cada simbolo tem um nome, um tipo e pode estar associado a um token e a um scope.
 * Esta classe serve como base para tipos de simbolos mais especificos.
 */
public class Symbol {
    /** Nome do simbolo, como aparece no codigo fonte */
    public final String name;
    /** Tipo do simbolo (inteiro, real, booleano, etc.) */
    public final Type type;
    /** Token original do codigo fonte, util para mensagens de erro */
    public Token token;
    /** Scope onde este simbolo foi definido */
    public Scope scope; // Todos os simbolos sabem qual escopo os contem

    /**
     * Cria um simbolo com o nome e tipo especificados.
     *
     * @param name O nome do simbolo
     * @param type O tipo do simbolo
     */
    protected Symbol(String name, Type type){
        this.name = name;
        this.type = type;
    }

    /**
     * Cria um simbolo a partir de um token e tipo.
     * Extrai o nome do simbolo do texto do token.
     *
     * @param token O token que representa o simbolo
     * @param type O tipo do simbolo
     */
    protected Symbol(Token token, Type type){
        this.token = token;
        this. name = token.getText();
        this.type = type;
    }

    /**
     * Obtem o token associado a este simbolo.
     *
     * @return O token associado ou null se nao houver
     */
    public Token getToken(){ return token;}

    /**
     * Obtem o nome (lexema) do simbolo.
     *
     * @return O nome do simbolo como aparece no codigo fonte
     */
    public String lexeme(){ return name;}

    /**
     * Gera uma representacao textual do simbolo.
     * Se o simbolo tiver um tipo, inclui-o no formato <nome:tipo>.
     *
     * @return Uma string representando o simbolo
     */
    @Override
    public String toString(){
        if (type != null){
            return '<' + lexeme() + ":" + type + '>';
        }
        return lexeme();
    }
}
