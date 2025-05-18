package Tuga.vm.instruction;

import Tuga.vm.OpCode;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Representa uma instrucao basica (sem argumentos) na maquina virtual Tuga.
 * Esta classe serve como base para todas as instrucoes suportadas pela VM
 * e fornece comportamento padrao para operacoes sem argumentos.
 */
public class Instruction {
    /** Codigo de operacao que define o comportamento da instrucao */
    protected OpCode opCode;

    /**
     * Cria uma nova instrucao com o codigo de operacao especificado.
     *
     * @param opCode O codigo de operacao da instrucao
     */
    public Instruction(OpCode opCode) {
        this.opCode = opCode;
    }

    /**
     * Obtem o codigo de operacao da instrucao.
     *
     * @return O codigo de operacao
     */
    public OpCode getOpCode() {
        return opCode;
    }

    /**
     * Devolve o numero de argumentos desta instrucao.
     * Para instrucoes basicas, o numero de argumentos e sempre zero.
     * Classes derivadas podem sobrescrever este metodo.
     *
     * @return O numero de argumentos (0 para instrucoes basicas)
     */
    public  int nArgs(){
        return  0;
    }

    /**
     * Gera uma representacao textual da instrucao.
     *
     * @return O nome do codigo de operacao como string
     */
    @Override
    public String toString(){
        return opCode.toString();
    }

    /**
     * Escreve a instrucao num fluxo de dados binario.
     * Para instrucoes basicas, apenas escreve o codigo de operacao.
     *
     * @param out O fluxo de saida para onde a instrucao sera escrita
     * @throws IOException Se ocorrer um erro de escrita
     */
    public  void writeTo(DataOutputStream out) throws IOException{
        out.writeByte(opCode.ordinal());
    }
}
