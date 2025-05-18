package Tuga.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gerencia pools de constantes para valores reais e strings.
 * Armazena valores constantes do programa compilado, evitando
 * duplicacoes e permitindo acesso eficiente durante a execucao.
 */
public class ConstantPool {
    /** Lista que armazena todos os tipos de constantes */
    private List<Object> constants;
    /** Mapa para busca rapida de indices por valor */
    private Map<Object, Integer> constantMap;

    /**
     * Cria uma nova pool de constantes vazia.
     */
    public ConstantPool() {
        this.constants = new ArrayList<>();
        this.constantMap = new HashMap<>();
    }

    /**
     * Adiciona um valor a pool e retorna o seu indice.
     * Se o valor ja existir, retorna o indice existente.
     *
     * @param value O valor a adicionar
     * @return O indice do valor na pool
     */
    public int addConstant(Object value){
        Integer index = constantMap.get(value);
        if (index != null){
            return index;
        }

        constants.add(value);
        index = constants.size() - 1;
        constantMap.put(value, index);
        return index;
    }

    /**
     * Adiciona um valor real a pool e retorna o seu indice.
     *
     * @param value O valor real a adicionar
     * @return O indice do valor na pool
     */
    public int addReal(double value){
        return addConstant(value);
    }

    /**
     * Adiciona uma string a pool e retorna o seu indice.
     *
     * @param value A string a adicionar
     * @return O indice da string na pool
     */
    public int addString(String value){
        return addConstant(value);
    }

    /**
     * Obtem um valor real pelo seu indice na pool.
     *
     * @param index O indice do valor real
     * @return O valor real
     * @throws RuntimeException Se o indice for invalido
     * @throws IndexOutOfBoundsException Se o valor no indice nao for um real
     */
    public double getReal(int index){
        if (index < 0 || index >= constants.size()){
            throw new RuntimeException("Indice invalido na constant pool: "+ index);
        }

        Object value = constants.get(index);
        if (value instanceof Double){
            return (Double) value;
        }
        throw new IndexOutOfBoundsException("Indice nao e um real: " + index);
    }

    /**
     * Obtem uma string pelo seu indice na pool.
     *
     * @param index O indice da string
     * @return A string
     * @throws RuntimeException Se o indice for invalido ou nao referir uma string
     */
    public String getString(int index){
        if (index < 0 || index >= constants.size()){
            throw new RuntimeException("Indice invalido na constant pool: " + index);
        }
        Object value = constants.get(index);
        if (value instanceof String){
            return (String) value;
        }
        throw new RuntimeException("Indice nao e uma string: " + index);
    }

    /**
     * Retorna o tamanho total da pool de constantes.
     *
     * @return O numero de constantes na pool
     */
    public int size(){
        return constants.size();
    }

    /**
     * Verifica se a pool de constantes esta vazia.
     *
     * @return true se a pool estiver vazia, false caso contrario
     */
    public boolean isEmpty(){
        return constants.isEmpty();
    }

    /**
     * Retorna uma copia da lista com todas as constantes.
     *
     * @return Uma nova lista contendo todas as constantes
     */
    public List<Object> getAllConstants(){
        return new ArrayList<>(constants);
    }
}
