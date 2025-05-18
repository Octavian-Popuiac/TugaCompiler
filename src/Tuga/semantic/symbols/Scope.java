package Tuga.semantic.symbols;

import java.util.*;

/**
 * Representa um scope na tabela de simbolos.
 * Um scope define uma regiao do codigo onde os simbolos sao validos e visiveis.
 * Cada scope pode conter scopes filhos e referenciar um scope pai.
 * Esta estrutura permite representar hierarquias de scopes como funcoes, blocos,
 * e outros contextos.
 */
public class Scope {
    /** Scope pai e null caso seja o scope global */
    Scope enclosingScope;
    /** Nome do scope, utilizado para debug */
    String name;
    /** Mapa de scopes definidos neste scope, indexados pelo nome */
    public Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();
    /** Lista de scopes filhos contidos neste scope */
    private List<Scope> childScopes = new ArrayList<>();

    /**
     * Cria um novo scope com o scope pai especificado.
     *
     * @param enclosingScope O scope pai ou null se for o scope global
     */
    public Scope(Scope enclosingScope){
        this.enclosingScope = enclosingScope;
        this.name = "__no_name__";
    }

    /**
     * Cria um novo scope com um scope pai e nome especificos.
     * Regista automaticamente este scope como filho do scope pai.
     *
     * @param enclosingScope O scope pai ou null se for o scope global
     * @param name O nome do scope
     */
    public Scope(Scope enclosingScope, String name){
        this.enclosingScope = enclosingScope;
        this.name = name;
        if (enclosingScope != null){
            enclosingScope.addChildScope(this);
        }
    }

    /**
     * Obtem o nome do scope.
     *
     * @return O nome do scope
     */
    public String getName() {return name;}

    /**
     * Define um novo nome para o scope.
     *
     * @param name O novo nome do scope
     */
    public void setName(String name){this.name = name;}

    /**
     * Verifica se um simbolo com o nome especificado esta definido neste scope.
     *
     * @param name O nome a verificar
     * @return verdadeiro se o simbolo existir neste scope, falso caso contrario
     */
    public boolean contains(String name){
        return resolve_local(name) != null;
    }

    /**
     * Define um simbolo neste scope.
     * Tambem define este scope como o scope do simbolo.
     *
     * @param sym O simbolo a definir
     */
    public void define(Symbol sym){
        symbols.put(sym.lexeme(), sym);
        sym.scope = this;
    }

    /**
     * Procura um simbolo apenas neste scope.
     *
     * @param name O nome do simbolo a procurar
     * @return O simbolo encontrado ou null se nao existir
     */
    public Symbol resolve_local(String name){
        return symbols.get(name); // Retorna um Symbol ou null
    }

    /**
     * Procura um simbolo neste scope ou em qualquer scope pai.
     * Implementa a resolucao de nomes em scopes internos, comecando pelo
     * scope atual e subindo na hierarquia ate ao scope global.
     *
     * @param name O nome do simbolo a procurar
     * @return O simbolo encontrado ou null se nao for encontrado em nenhum scope
     */
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

    /**
     * Obtem o scope pai deste scope.
     *
     * @return O scope pai ou null se for o scope global
     */
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    /**
     * Gera uma representacao textual da hierarquia de scopes.
     * Util para depuracao da estrutura de scopes.
     *
     * @return Uma string representando este scope e todos os seus scopes pai
     */
    public String toString(){
        if (enclosingScope != null){
            return getName() + ":" + symbols.keySet().toString() + " --> " + enclosingScope.toString();
        }else {
            return getName() + ":" + symbols.keySet().toString(); // global scope
        }
    }

    /**
     * Adiciona um scope filho a este scope.
     *
     * @param child O scope filho a adicionar
     */
    public void addChildScope(Scope child) {
        childScopes.add(child);
    }

    /**
     * Obtem todos os scopes filhos deste scope.
     *
     * @return Uma lista dos scopes filhos
     */
    public List<Scope> getChildScopes() {
        return childScopes;
    }
}
