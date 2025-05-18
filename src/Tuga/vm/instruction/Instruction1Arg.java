package Tuga.vm.instruction;

import Tuga.vm.OpCode;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Representa uma instrucao com um argumento na maquina virtual Tuga.
 * Esta classe estende a instrucao basica para permitir operacoes
 * que necessitam de um valor inteiro como parametro.
 */
public class Instruction1Arg extends Instruction{
    /** O argumento inteiro associado a esta instrucao */
    private int arg;

    /**
     * Cria uma nova instrucao com um argumento.
     *
     * @param opCode O codigo de operacao da instrucao
     * @param arg O valor do argumento da instrucao
     */
    public Instruction1Arg(OpCode opCode, int arg) {
        super(opCode);
        this.arg = arg;
    }

    /**
     * Obtem o valor do argumento da instrucao.
     *
     * @return O valor do argumento
     */
    public int getArg() {
        return arg;
    }

    /**
     * Define um novo valor para o argumento da instrucao.
     *
     * @param arg O novo valor do argumento
     */
    public void setArg(int arg) {
        this.arg = arg;
    }

    /**
     * Devolve o numero de argumentos desta instrucao.
     * Para instrucoes com um argumento, retorna sempre 1.
     *
     * @return O numero de argumentos (1)
     */
    @Override
    public int nArgs(){
        return 1;
    }

    /**
     * Gera uma representacao textual da instrucao com o seu argumento.
     *
     * @return O nome do codigo de operacao seguido do valor do argumento
     */
    @Override
    public String toString(){
        return opCode.toString() + " " + arg;
    }

    /**
     * Escreve a instrucao e o seu argumento num fluxo de dados binario.
     * Primeiro escreve o codigo de operacao e depois o valor do argumento.
     *
     * @param out O fluxo de saida para onde a instrucao sera escrita
     * @throws IOException Se ocorrer um erro de escrita
     */
    @Override
    public void writeTo(DataOutputStream out) throws IOException{
        out.writeByte(opCode.ordinal());
        out.writeInt(arg);
    }
}
