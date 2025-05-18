package Tuga.semantic;

import Tuga.semantic.symbols.Scope;
import Tuga.semantic.symbols.Symbol;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementa uma tabela de simbolos.
 * A tabela de simbolos rastreia os identificadores e os seus atributos
 * em diferentes scopes do programa, facilitando o acesso e validacao
 * semantica durante a analise e geracao de codigo.
 */
public class SymbolTable {
    /** Armazena scopes de funcoes pelo nome para reutilizacao posterior */
    private Map<String, Scope> savedScopes = new HashMap<>();
    /** O scope atual onde as declaracoes e pesquisas ocorrem */
    private Scope currentScope;

    /**
     * Constroi uma nova tabela de simbolos com um scope global vazio.
     */
    public SymbolTable(){
        currentScope = new Scope(null, "__global__");
    }

    /**
     * Obtem o scope atual da tabela de simbolos.
     *
     * @return O scope atual
     */
    public Scope getCurrentScope() {
        return currentScope;
    }

    /**
     * Define o scope atual da tabela de simbolos.
     *
     * @param currentScope O novo scope atual
     */
    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }

    /**
     * Guarda o scope atual associado a uma funcao para uso posterior.
     * Isto permite restaurar o scope de uma funcao para adicionar simbolos
     * ou verificar expressoes dentro do corpo da funcao.
     *
     * @param functionName O nome da funcao a associar com o scope atual
     */
    public void saveScope(String functionName){
        savedScopes.put(functionName, currentScope);
    }

    /**
     * Restaura um scope de funcao previamente guardado.
     *
     * @param functionName O nome da funcao cujo scope deve ser restaurado
     * @return true se o scope foi restaurado com sucesso, false caso contrario
     */
    public boolean restoreScope(String functionName){
        if (savedScopes.containsKey(functionName)){
            currentScope = savedScopes.get(functionName);
            return true;
        }

        return false;
    }

    /**
     * Entra num novo scope com o nome especificado.
     * Se ja existir um scope filho com este nome, torna-o o scope atual.
     * Caso contrario, cria um novo scope como filho do atual.
     *
     * @param name O nome do scope a entrar ou criar
     */
    public void enterScope(String name){
        for (Scope childScope : currentScope.getChildScopes()){
            if (childScope.getName().equals(name)){
                currentScope = childScope;
                return;
            }
        }

        currentScope = new Scope(currentScope, name);
    }

    /**
     * Entra num novo scope local sem nome especifico.
     * Util para blocos anonimos no codigo fonte.
     */
    public void enterScope(){
        currentScope = new Scope(currentScope, "__local__");
    }

    /**
     * Sai do scope atual e retorna ao scope pai.
     * Se o scope atual for o global, nao faz nada.
     */
    public void exitScope(){
        Scope encolsingScope = currentScope.getEnclosingScope();

        if (encolsingScope != null){
            currentScope = encolsingScope;
        }
    }

    /**
     * Tenta declarar um simbolo no scope atual.
     * Verifica se ja existe um simbolo com o mesmo nome no scope atual.
     *
     * @param symbol O simbolo a ser declarado
     * @return true se a declaracao foi bem-sucedida, false se o simbolo ja existir
     */
    public boolean declare(Symbol symbol){
        String name = symbol.lexeme();
        if (currentScope.contains(name)){
            return false;
        }

        currentScope.define(symbol);
        return true;
    }

    /**
     * Procura um simbolo por nome em todos os scopes encadeados.
     * Navega pela hierarquia de scopes, comecando no scope atual
     * e subindo ate ao scope global se necessario.
     *
     * @param name O nome do simbolo a procurar
     * @return O simbolo encontrado ou null se nao existir
     */
    public Symbol lookupSymbol(String name) {
        return currentScope.resolve(name);
    }

}
