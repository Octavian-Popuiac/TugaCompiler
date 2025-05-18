package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

/**
 * Representa um simbolo de variavel na tabela de simbolos.
 * As variaveis podem ser parametros de funcoes ou variaveis normais
 * declaradas em qualquer scope do programa.
 * Esta classe estende Symbol, adicionando informacao especifica de variaveis.
 */
public class VarSymbol extends Symbol{
    /** Indica se esta variavel e um parametro de funcao */
    public final boolean isParameter;

    /**
     * Cria um simbolo de variavel com nome, tipo e indicador de parametro especificados.
     *
     * @param name O nome da variavel
     * @param type O tipo da variavel
     * @param isParameter true se for parametro de funcao, false caso contrario
     */
    public VarSymbol(String name, Type type, boolean isParameter) {
        super(name, type);
        this.isParameter = isParameter;
    }

    /**
     * Cria um simbolo de variavel a partir de um token, com tipo e indicador de parametro.
     *
     * @param token O token que representa a variavel
     * @param type O tipo da variavel
     * @param isParameter true se for parametro de funcao, false caso contrario
     */
    public VarSymbol(Token token, Type type, boolean isParameter){
        super(token.getText(), type);
        this.token = token;
        this.isParameter = isParameter;
    }

    /**
     * Cria um simbolo de variavel normal (nao-parametro) com nome e tipo especificados.
     *
     * @param name O nome da variavel
     * @param type O tipo da variavel
     */
    public VarSymbol(String name, Type type) {
        this(name, type, false);
    }

    /**
     * Cria um simbolo de variavel normal (nao-parametro) a partir de um token e tipo.
     *
     * @param token O token que representa a variavel
     * @param type O tipo da variavel
     */
    public VarSymbol(Token token, Type type){
        this(token, type, false);
    }
}
