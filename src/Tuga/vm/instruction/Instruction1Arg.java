package Tuga.vm.instruction;

import Tuga.vm.OpCode;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Representa uma instrucao com um argumento
 */
public class Instruction1Arg extends Instruction{
    private int arg;

    public Instruction1Arg(OpCode opCode, int arg) {
        super(opCode);
        this.arg = arg;
    }

    public int getArg() {
        return arg;
    }

    public void setArg(int arg) {
        this.arg = arg;
    }

    @Override
    public int nArgs(){
        return 1;
    }

    @Override
    public String toString(){
        return opCode.toString() + " " + arg;
    }

    @Override
    public void writeTo(DataOutputStream out) throws IOException{
        out.writeByte(opCode.ordinal());
        out.writeInt(arg);
    }
}
