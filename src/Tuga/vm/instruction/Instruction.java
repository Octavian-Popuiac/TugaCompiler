package Tuga.vm.instruction;

import Tuga.vm.OpCode;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Representa uma instrucao basica (sem argumentos)
 */
public class Instruction {
    protected OpCode opCode;

    public Instruction(OpCode opCode) {
        this.opCode = opCode;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public  int nArgs(){
        return  0;
    }

    @Override
    public String toString(){
        return opCode.toString();
    }

    public  void writeTo(DataOutputStream out) throws IOException{
        out.writeByte(opCode.ordinal());
    }
}
