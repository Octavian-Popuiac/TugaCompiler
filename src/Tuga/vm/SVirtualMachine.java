package Tuga.vm;

import Tuga.vm.instruction.Instruction;
import Tuga.vm.instruction.Instruction1Arg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Implementacao da maquina virtual
 */
public class SVirtualMachine {
    private final boolean trace;    //  Flag para debug/trace
    private byte[] bytecodes;      //  Bytecodes originais
    private Instruction[] code;    //   Instrucoes decodificadas
    private int ip;                //  Instruction pointes
    private final Stack<Object> stack;  //  Pilha de execucao
    private final ConstantPool constantPool;    //  Pool de constantes

    public SVirtualMachine(){
        this(false);
    }

    public SVirtualMachine(boolean trace){
        this.trace = trace;
        this.stack = new Stack<>();
        this.constantPool = new ConstantPool();
        this.ip = 0;
    }

    /**
     * Executa o programa em bytecode do arquivo fornecido
     */
    public void execute(String bytecodeFile){
        try (DataInputStream dis = new DataInputStream(new FileInputStream(bytecodeFile))){
            readConstantPool(dis);

            readAndExecuteInstructions(dis);
        }catch (IOException e){
            System.err.println("Erro ao executar bytecodes: " + e.getMessage());
        }
    }

    /**
     * Le a constant pool do arquivo de bytecodes
     */
    private void readConstantPool(DataInputStream dis) throws IOException{
        int poolSize = dis.readInt();

        if (trace){
            System.out.println("Lendo constant pool com " + poolSize + " entradas");
        }

        //  Para cada entrada na constant pool
        for (int i = 0; i < poolSize; i++){
            //Le o byte de tipo
            byte type = dis.readByte();

            if (type == 1){ //double
                double value = dis.readDouble();
                constantPool.addReal(value);
                if (trace){
                    System.out.println("    Entrada " + i + ": double " + value);
                }
            } else if (type == 2) {//string
                //Le o tamanha da string
                int length = dis.readInt();

                //Le os caracteres da string
                StringBuilder sb = new StringBuilder(length / 2); //Dividir por dois porque cada caracteres usa 2
                for (int j = 0; j < length / 2; j++){
                    sb.append(dis.readChar());
                }

                String value = sb.toString();
                constantPool.addString(value);
                if (trace){
                    System.out.println("    Entrada "+ i + ": string \"" + value + "\"");
                }
            }else {
                throw new IOException("Tipo de constante desconhecido: "+ type);
            }
        }
    }

    /**
     * Le e executa as instrucoes do arquivo de bytecodes
     */
    private void readAndExecuteInstructions(DataInputStream dis) throws IOException{
        //Decodifica os bytecodes em instrucoes
        List<Instruction> instructions = new ArrayList<>();

        try {
            while (true){
                byte opCode = dis.readByte();
                OpCode op = OpCode.convert(opCode);

                if (op.nArgs() == 0){
                    instructions.add(new Instruction(op));
                }else {
                    int arg = dis.readInt();
                    instructions.add(new Instruction1Arg(op, arg));
                }
            }
        }catch (EOFException e){
            //Fim normal do arquivo
        }

        //Conver a lista em array de instrucoes
        this.code = new Instruction[instructions.size()];
        instructions.toArray(this.code);

        if (trace){
            System.out.println("Bytecodes decodificados em " + code.length + " instrucoes:");
            dumpInstructions();
        }

        run();
    }

    /**
     * Decodigica os bytecodes em instrucoes
     */
    private void decode(byte[] bytecodes){
        List<Instruction> instructions = new ArrayList<>();

        try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytecodes))){
            while (true){
                byte b = din.readByte();
                OpCode opCode = OpCode.convert(b);

                switch (opCode.nArgs()){
                    case 0:
                        instructions.add(new Instruction(opCode));
                        break;
                    case 1:
                        int value = din.readInt();
                        instructions.add(new Instruction1Arg(opCode, value));
                        break;
                    default:
                        throw new RuntimeException("Numero de argumentos invalidos: " + opCode.nArgs());
                }
            }
        } catch (EOFException e) {
            //Fim dos bytecodes, converte a lista em array
            this.code = new Instruction[instructions.size()];

            if (trace){
                System.out.println("Bytecodes decodificados: ");
                dumpInstructions();
            }
        } catch (IOException e){
            System.err.println("Erro ao decodificar bytecodes: " + e.getMessage());
        }
    }

    /**
     * Imprime as instrucoes decodificadas
     */
    private void dumpInstructions(){
        for (int i = 0; i < code.length; i++){
            System.out.println(i + ": "+ code[i]);
        }
    }

    /**
     * Executa o programa
     */
    private void run(){
        if (trace){
            System.out.println("Iniciando execucao na instrucao " + ip);
        }

        while (ip < code.length){
            executeInstruction(code[ip]);
            ip++;
        }

        if (trace){
            System.out.println("Execucao finalizada. Estado da pilha: " + stack);
        }
    }

    /**
     * Executa uma instrucao
     */
    private void executeInstruction(Instruction inst){
        if (trace){
            System.out.printf("%5d: %-15s Stack: %s%n", ip, inst, stack);
        }

        OpCode opCode = inst.getOpCode();

        switch (opCode){
            //  Instrucoes com argumento
            case iconst -> execIconst((Instruction1Arg) inst);
            case dconst -> execDconst((Instruction1Arg) inst);
            case sconst -> execSconst((Instruction1Arg) inst);

            //  Instrucoes para inteiros
            case iprint -> execIprint();
            case iuminus -> execIuminus();
            case iadd -> execIadd();
            case isub -> execIsub();
            case imult -> execImul();
            case idiv -> execIdiv();
            case imod -> execImod();
            case ieq -> execIeq();
            case ineq -> execIneq();
            case ilt -> execIlt();
            case ileq -> execIleq();
            case itod -> execItod();
            case itos -> execItos();

            // Instruções para reais
            case dprint -> execDprint();
            case duminus -> execDuminus();
            case dadd -> execDadd();
            case dsub -> execDsub();
            case dmult -> execDmul();
            case ddiv -> execDdiv();
            case deq -> execDeq();
            case dneq -> execDneq();
            case dlt -> execDlt();
            case dleq -> execDleq();
            case dtos -> execDtos();

            // Instruções para strings
            case sprint -> execSprint();
            case sconcat -> execSconcat();
            case seq -> execSeq();
            case sneq -> execSneq();

            // Instruções para booleanos
            case tconst -> execTconst();
            case fconst -> execFconst();
            case bprint -> execBprint();
            case beq -> execBeq();
            case bneq -> execBneq();
            case and -> execAnd();
            case or -> execOr();
            case not -> execNot();
            case btos -> execBtos();

            // Controle de execução
            case halt -> execHalt();

            default -> throw new RuntimeException("Instrução não implementada: " + opCode);
        }
    }

    /**
     * Troca os dois elemntos no topo da pilha
     */
    private void execSwap() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(b);
        stack.push(a);
    }

    /**
     * Controlo de execucao
     */
    private void execHalt() {
        // Termina a execução do programa, setando o ip para o final do código
        ip = code.length;
    }

    private void execBtos() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            int boolVal = (Integer)value;
            if (boolVal == 0 || boolVal == 1) {
                stack.push(boolVal == 1 ? "true" : "falso");
            } else {
                runtimeError("BTOS espera um booleano (0 ou 1)");
            }
        } else {
            runtimeError("BTOS espera um booleano");
        }
    }

    private void execNot() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            int boolVal = (Integer)value;
            if (boolVal == 0 || boolVal == 1) {
                stack.push(boolVal == 0 ? 1 : 0);
            } else {
                runtimeError("NOT espera um booleano (0 ou 1)");
            }
        } else {
            runtimeError("NOT espera um booleano");
        }
    }

    private void execOr() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            // Verificar se são booleanos (0 ou 1)
            int aVal = (Integer)a;
            int bVal = (Integer)b;
            if ((aVal == 0 || aVal == 1) && (bVal == 0 || bVal == 1)) {
                stack.push((aVal == 1 || bVal == 1) ? 1 : 0);
            } else {
                runtimeError("OR espera dois booleanos (0 ou 1)");
            }
        } else {
            runtimeError("OR espera dois booleanos");
        }
    }

    private void execAnd() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            // Verificar se são booleanos (0 ou 1)
            int aVal = (Integer)a;
            int bVal = (Integer)b;
            if ((aVal == 0 || aVal == 1) && (bVal == 0 || bVal == 1)) {
                stack.push((aVal == 1 && bVal == 1) ? 1 : 0);
            } else {
                runtimeError("AND espera dois booleanos (0 ou 1)");
            }
        } else {
            runtimeError("AND espera dois booleanos");
        }
    }

    private void execBneq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            // Verificar se são booleanos (0 ou 1)
            int aVal = (Integer)a;
            int bVal = (Integer)b;
            if ((aVal == 0 || aVal == 1) && (bVal == 0 || bVal == 1)) {
                stack.push(aVal != bVal ? 1 : 0);
            } else {
                runtimeError("BNEQ espera dois booleanos (0 ou 1)");
            }
        } else {
            runtimeError("BNEQ espera dois booleanos");
        }
    }

    private void execBeq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            // Verificar se são booleanos (0 ou 1)
            int aVal = (Integer)a;
            int bVal = (Integer)b;
            if ((aVal == 0 || aVal == 1) && (bVal == 0 || bVal == 1)) {
                stack.push(aVal == bVal ? 1 : 0);
            } else {
                runtimeError("BEQ espera dois booleanos (0 ou 1)");
            }
        } else {
            runtimeError("BEQ espera dois booleanos");
        }
    }

    private void execBprint() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            int boolVal = (Integer)value;
            if (boolVal == 0 || boolVal == 1) {
                System.out.println(boolVal == 1 ? "verdadeiro" : "falso");
            } else {
                runtimeError("BPRINT espera um booleano (0 ou 1)");
            }
        } else {
            runtimeError("BPRINT espera um booleano");
        }
    }

    private void execFconst() {
        stack.push(0); //False representado como 0
    }

    private void execTconst() {
        stack.push(1); //True representado como 1
    }

    private void execSneq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof String && b instanceof String) {
            stack.push(!((String)a).equals(b) ? 1 : 0);
        } else {
            runtimeError("SNEQ espera duas strings");
        }
    }

    private void execSeq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof String && b instanceof String) {
            stack.push(((String)a).equals(b) ? 1 : 0);
        } else {
            runtimeError("SEQ espera duas strings");
        }
    }

    private void execSconcat() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof String && b instanceof String) {
            stack.push( (String) a + (String)b);
        } else {
            runtimeError("SCONCAT espera duas strings");
        }
    }

    private void execSprint() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof String) {
            System.out.println(value);
        } else {
            runtimeError("SPRINT espera uma string");
        }
    }

    private void execDtos() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Double) {
            stack.push(value.toString());
        } else {
            runtimeError("DTOS espera um real");
        }
    }

    private void execDleq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push(((Double)a) <= ((Double)b) ? 1 : 0);
        } else {
            runtimeError("DLEQ espera dois reais");
        }
    }

    private void execDlt() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push(((Double)a) < ((Double)b) ? 1 : 0);
        } else {
            runtimeError("DLT espera dois reais");
        }
    }

    private void execDneq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push(!((Double)a).equals(b) ? 1 : 0);
        } else {
            runtimeError("DNEQ espera dois reais");
        }
    }

    private void execDeq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push(((Double)a).equals(b) ? 1 : 0);
        } else {
            runtimeError("DEQ espera dois reais");
        }
    }

    private void execDdiv() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            if ((Double)b != 0.0) {
                stack.push((Double)a / (Double)b);
            } else {
                runtimeError("Divisão por zero");
            }
        } else {
            runtimeError("DDIV espera dois reais");
        }
    }

    private void execDmul() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push((Double)a * (Double)b);
        } else {
            runtimeError("DMUL espera dois reais");
        }
    }

    private void execDsub() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push((Double)a - (Double)b);
        } else {
            runtimeError("DSUB espera dois reais");
        }
    }

    private void execDadd() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Double && b instanceof Double) {
            stack.push((Double)a + (Double)b);
        } else {
            runtimeError("DADD espera dois reais");
        }
    }

    private void execDuminus() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Double) {
            stack.push(-(Double)value);
        } else {
            runtimeError("DUMINUS espera um real");
        }
    }

    private void execDprint() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Double) {
            System.out.println(value);
        } else {
            runtimeError("DPRINT espera um real");
        }
    }

    private void execItos() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            stack.push(value.toString());
        } else {
            runtimeError("ITOS espera um inteiro");
        }
    }

    private void execItod() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            stack.push(((Integer)value).doubleValue());
        } else {
            runtimeError("ITOD espera um inteiro");
        }
    }

    private void execIleq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            stack.push(((Integer)a) <= ((Integer)b) ? 1 : 0);
        } else {
            runtimeError("ILEQ espera dois inteiros");
        }
    }

    private void execIlt() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            stack.push(((Integer)a) < ((Integer)b) ? 1 : 0);
        } else {
            runtimeError("ILT espera dois inteiros");
        }
    }

    private void execIneq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            stack.push(!((Integer)a).equals(b) ? 1 : 0);
        } else {
            runtimeError("INEQ espera dois inteiros");
        }
    }

    private void execIeq() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();
        if (a instanceof Integer && b instanceof Integer) {
            stack.push(((Integer)a).equals(b) ? 1 : 0); // Representação de booleano como inteiro
        } else {
            runtimeError("IEQ espera dois inteiros");
        }
    }

    private void execImod() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            if ((Integer) b != 0){
                stack.push((Integer) a % (Integer) b);
            }else {
                runtimeError("Modulo por zero");
            }
        }else {
            runtimeError("Operandos incompatíveis para IMOD");
        }
    }

    private void execIdiv() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            if ((Integer) b != 0){
                stack.push((Integer) a / (Integer) b);
            }else {
                runtimeError("Divisao por zero");
            }
        }else {
            runtimeError("Operandos incompatíveis para IDIV");
        }
    }

    private void execImul() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a * (Integer) b);
        }else {
            runtimeError("Operandos incompatíveis para IMUL");
        }
    }

    private void execIsub() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a - (Integer) b);
        }else {
            runtimeError("Operandos incompatíveis para ISUB");
        }
    }

    private void execIadd() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a + (Integer) b);
        }else {
            runtimeError("Operandos incompatíveis para IADD");
        }
    }

    private void execIuminus() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            stack.push(-(Integer)value);
        } else {
            runtimeError("IUMINUS espera um inteiro");
        }
    }

    private void execIprint() {
        checkStackSize(1);
        Object value = stack.pop();
        if (value instanceof Integer) {
            System.out.println(value);
        } else {
            runtimeError("IPRINT espera um inteiro");
        }
    }

    private void execSconst(Instruction1Arg inst) {
        int index = inst.getArg();
        String value = constantPool.getString(index);
        stack.push(value);
    }

    private void execDconst(Instruction1Arg inst) {
        int index = inst.getArg();
        double value = constantPool.getReal(index);
        stack.push(value);
    }

    private void execIconst(Instruction1Arg inst) {
        int value = inst.getArg();
        stack.push(value);
    }

    // Códigos utilitarios
    private void checkStackSize(int size){
        if (stack.size() < size){
            runtimeError("Pilha nao tem elementos suficientes");
        }
    }

    private void runtimeError(String message){
        System.err.println("Erro de execucao: "+ message);
        if (trace){
            System.err.println("Estado da pilha: " + stack);
        }
        throw new RuntimeException(message);
    }
}
