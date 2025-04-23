package Tuga.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gerencia pools de cosntantes para valores reais e strings
 */
public class ConstantPool {
    private List<Object> constants; // Uma unica lsita para todos os tipos de constantes
    private Map<Object, Integer> constantMap; // Mapa para busca rapida

    public ConstantPool() {
        this.constants = new ArrayList<>();
        this.constantMap = new HashMap<>();
    }

    /**
     * Adicionar um valor a pool e retorna o seu indice
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
     * Adiciona um valor real a pool d retorna o seu indice
     */
    public int addReal(double value){
        return addConstant(value);
    }

    /**
     * Adiciona uma string a pool e retorna o seu indice
     * Indices das strings comecam apos todos os reais
     */
    public int addString(String value){
        return addConstant(value);
    }

    /**
     * Obtem um valor real pelo indice
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
     * Obtem uma string pelo indice
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
     * Retorna o tamanho total da pool
     */
    public int size(){
        return constants.size();
    }

    /**
     * Verifica se a constant pool esta vazia
     */
    public boolean isEmpty(){
        return constants.isEmpty();
    }

    /**
     * Retorna uma lista com todas as constantes
     */
    public List<Object> getAllConstants(){
        return new ArrayList<>(constants);
    }
}
