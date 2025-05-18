package Tuga.semantic.symbols;

import Tuga.semantic.Type;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o simbolo de uma funcao na tabela de simbolos.
 * Armazena informacao sobre o nome da funcao, o seu tipo de retorno,
 * parametros e se contem uma instrucao de retorno.
 */
public class FunctionSymbol extends Symbol{
    /** Lista dos parametros da funcao, na ordem em que aparecem na declaracao */
    private final List<VarSymbol> parameters = new ArrayList<>();
    /** Indica se a funcao contem uma instrucao 'retorna' */
    private boolean hasReturn = false;

    /**
     * Cria um simbolo de funcao com o nome e tipo de retorno especificados.
     *
     * @param name O nome da funcao
     * @param returnType O tipo de retorno da funcao
     */
    public FunctionSymbol(String  name, Type returnType){
        super(name, returnType);
    }

    /**
     * Cria um simbolo de funcao a partir de um token e tipo de retorno.
     *
     * @param token O token que representa o nome da funcao
     * @param returnType O tipo de retorno da funcao
     */
    public FunctionSymbol(Token token, Type returnType){
        super(token.getText(), returnType);
        this.token = token;
    }

    /**
     * Adiciona um parametro a lista de parametros da funcao.
     * Os parametros sao armazenados na ordem em que sao adicionados.
     *
     * @param param O simbolo da variavel que representa o parametro
     */
    public void addParameter(VarSymbol param){
        parameters.add(param);
    }

    /**
     * Retorna a lista de parametros da funcao.
     *
     * @return Lista dos simbolos de parametros na ordem da declaracao
     */
    public List<VarSymbol> getParameters() {
        return parameters;
    }

    /**
     * Define se a funcao contem uma instrucao de retorno.
     * Esta informacao e utilizada para verificar se funcoes
     * com tipo de retorno nao-vazio incluem 'retorna'.
     *
     * @param hasReturn true se a funcao contem 'retorna', false caso contrario
     */
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    /**
     * Verifica se a funcao contem uma instrucao de retorno.
     *
     * @return true se a funcao contem 'retorna', false caso contrario
     */
    public boolean hasReturn() {
        return hasReturn;
    }

    /**
     * Gera uma representacao textual do simbolo da funcao.
     * Util para debug e apresentacao da tabela de simbolos.
     *
     * @return String representando a funcao, incluindo nome, tipo e parametros
     */
    @Override
    public String toString(){
        return "function<" + name + ":" + type + '>' + parameters;
    }
}
