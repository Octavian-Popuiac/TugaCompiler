package Tuga.vm;

import Tuga.vm.instruction.Instruction;
import Tuga.vm.instruction.Instruction1Arg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Implementacao da maquina virtual Tuga.
 * Executa os bytecodes gerados pelo compilador, gerenciando a pilha de execucao,
 * funcoes, variaveis globais e locais, e operacoes aritmeticas e logicas.
 * Fornece suporte para todos os tipos da linguagem Tuga (inteiros, reais, strings e booleanos).
 */
public class SVirtualMachine {
    /** Frame pointer - base do frame de funcao atual */
    private int fp = 0;
    /** Flag para ativar modo de depuracao com saida detalhada */
    private final boolean trace;
    /** Instrucoes decodificadas prontas para execucao */
    private Instruction[] code;
    /** Instruction pointer - indice da instrucao atual */
    private int ip;
    /** Pilha de execucao da maquina virtual */
    private final Stack<Object> stack;
    /** Pool de constantes (strings e valores reais) */
    private final ConstantPool constantPool;
    /** Array para armazenamento de variaveis globais */
    private List<Object> globals = new ArrayList<>();

    /**
     * Cria uma nova instancia da maquina virtual com modo trace desativado.
     */
    public SVirtualMachine(){
        this(false);
    }

    /**
     * Cria uma nova instancia da maquina virtual.
     *
     * @param trace Se true, imprime informacoes detalhadas durante a execucao
     */
    public SVirtualMachine(boolean trace){
        this.trace = trace;
        this.stack = new Stack<>();
        this.constantPool = new ConstantPool();
        this.ip = 0;
    }

    /**
     * Executa o programa em bytecode do arquivo fornecido.
     * Le a pool de constantes e depois as instrucoes.
     *
     * @param bytecodeFile Caminho para o arquivo de bytecode
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
     * Le a constant pool do arquivo de bytecodes.
     * Cada constante tem um tipo (1=double, 2=string) seguido do valor.
     *
     * @param dis Stream de entrada para leitura dos bytecodes
     * @throws IOException Em caso de erro de leitura
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
                    System.out.println("Entrada "+ i + ": string \"" + value + "\"");
                }
            }else {
                throw new IOException("Tipo de constante desconhecido: "+ type);
            }
        }
    }

    /**
     * Le e executa as instrucoes do arquivo de bytecodes.
     *
     * @param dis Stream de entrada para leitura dos bytecodes
     * @throws IOException Em caso de erro de leitura
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
     * Imprime as instrucoes decodificadas para depuracao.
     * Mostra o indice e a representacao textual de cada instrucao.
     */
    private void dumpInstructions(){
        for (int i = 0; i < code.length; i++){
            System.out.println(i + ": "+ code[i]);
        }
    }

    /**
     * Executa o programa carregado na maquina virtual.
     * Processa cada instrucao sequencialmente ate o fim do codigo
     * ou ate encontrar uma instrucao HALT.
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
     * Executa uma instrucao da maquina virtual.
     * Seleciona o metodo apropriado com base no OpCode da instrucao.
     *
     * @param inst A instrucao a ser executada
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
            case jump -> execJump((Instruction1Arg) inst);
            case jumpf -> execJumpf((Instruction1Arg) inst);
            case galloc -> execGalloc((Instruction1Arg) inst);
            case gload -> execGload((Instruction1Arg) inst);
            case gstore -> execGstore((Instruction1Arg) inst);
            case lalloc -> execLalloc((Instruction1Arg) inst);
            case lload -> execLload((Instruction1Arg) inst);
            case lstore -> execLstore((Instruction1Arg) inst);
            case pop -> execPop((Instruction1Arg) inst);
            case call -> execCall((Instruction1Arg) inst);
            case retval -> execRetval((Instruction1Arg) inst);
            case ret -> execRet((Instruction1Arg) inst);

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

    private void execHalt() {
        // Termina a execução do programa, defenindo o ip para o final do código
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
            // Verificar se sao booleanos (0 ou 1)
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
            // Verificar se sao booleanos (0 ou 1)
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
            // Verificar se sao booleanos (0 ou 1)
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
            // Verificar se sao booleanos (0 ou 1)
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
                runtimeError("Divisao por zero");
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
            stack.push(((Integer)a).equals(b) ? 1 : 0); // Representacao de booleano como inteiro
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
            runtimeError("Operandos incompativeis para IDIV");
        }
    }

    private void execImul() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a * (Integer) b);
        }else {
            runtimeError("Operandos incompativeis para IMUL");
        }
    }

    private void execIsub() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a - (Integer) b);
        }else {
            runtimeError("Operandos incompativeis para ISUB");
        }
    }

    private void execIadd() {
        checkStackSize(2);
        Object b = stack.pop();
        Object a = stack.pop();

        if (a instanceof Integer && b instanceof Integer){
            stack.push((Integer) a + (Integer) b);
        }else {
            runtimeError("Operandos incompativeis para IADD");
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

    private void execJump(Instruction1Arg inst) {
        // Atualiza a instruction pointer para o endereco especificado
        // Subtrai 1 porque o ip sera incrementado no final do loop
        ip = inst.getArg() - 1;
    }

    private void execJumpf(Instruction1Arg inst){
        checkStackSize(1);
        Object value = stack.pop();

        // Se o valor for 0 (false), faz o jump
        if (value instanceof Integer && (Integer)value == 0){
            // Subtrai 1 porque o ip sera incrementado no final do loop
            ip = inst.getArg() - 1;
        }
    }

    private void execGalloc(Instruction1Arg inst){
        int size = inst.getArg();
        // Inicia as variaveis globais como NULO (null)
        for (int i = 0; i < size; i++){
            globals.add(null);
        }
    }

    private void execGload(Instruction1Arg inst){
        int addr = inst.getArg();
        if (addr >= 0 && addr < globals.size()){
            Object value = globals.get(addr);
            if (value == null){
                runtimeError("erro de runtime: tentativa de acesso a valor NULO");
            }
            stack.push(value);
        }else {
            runtimeError("Indice de variavel global invalido: " + addr);
        }
    }

    private void execGstore(Instruction1Arg inst){
        checkStackSize(1);
        int addr = inst.getArg();
        Object value = stack.pop();

        if (addr >= 0 && addr < globals.size()){
            globals.set(addr, value);
        }else {
            runtimeError("Indice de variavel global invalido: " + addr);
        }
    }

    private void execLalloc(Instruction1Arg inst){
        int n = inst.getArg();
        // Aloca n posicoes no topo da pilha com valor NIL (null)
        for (int i = 0; i < n; i++){
            stack.push(null);
        }
    }

    private void execLload(Instruction1Arg inst){
        int addr = inst.getArg();
        int actualAddr = fp + addr;

        if (actualAddr < stack.size()){
            Object value = stack.get(actualAddr);
            if (value == null){
                runtimeError("erro de runtime: tentativa de acesso a valor NULO");
            }
            stack.push(value);
        }else {
            runtimeError("Indice de variavel local invalido;: " + addr);
        }
    }

    private void execLstore(Instruction1Arg inst){
        checkStackSize(1);
        int addr = inst.getArg();
        int actualAddr = fp + addr;
        Object value = stack.pop();

        if (actualAddr < stack.size()){
            stack.set(actualAddr, value);
        }else {
            runtimeError("Indice de variavel local invalido: " + addr);
        }
    }

    private void execPop(Instruction1Arg inst){
        int n = inst.getArg();
        checkStackSize(n);
        if (stack.size() >= n){
            for (int i = 0; i < n; i++){
                stack.pop();
            }
        }else {
            runtimeError("Nao ha elementos suficientes para desempilhar");
        }
    }

    private void execCall(Instruction1Arg inst){
        // Salvar o FP atual (frame anterior)
        stack.push(fp);

        // Salvar o endereco de retorno (IP+1)
        stack.push(ip + 1);

        // Atualizar FP para apontar para o indice do novo frame
        fp = stack.size() - 2; // -2 para considerar o FP e IP que foram empilhados

        // Atualizar IP para o endereco da funcao (-1 porque ip sera incrementado depois da execucao)
        ip = inst.getArg() - 1;
    }

    private void execRetval(Instruction1Arg inst){
        // Primeiro, obter o valor de retorno (deve estar no topo da pilha)
        if (stack.isEmpty()) {
            runtimeError("Pilha vazia ao tentar retornar valor");
            return;
        }
        // Guardar valor de retorno
        Object returnValue = stack.pop();


        // Obter valores de FP e IP salvos corretamente
        if (fp < 0 || fp >= stack.size()) {
            runtimeError("Frame pointer invalido: " + fp);
            return;
        }

        // Verificar se podemos acessar o endereço de retorno
        if (fp + 1 >= stack.size()) {
            runtimeError(
                    String.format(
                            "Frame inconsistente: impossivel acessar IP de retorno | IP : %d | Stack Size %d",
                            fp+1,
                            stack.size()
                    )
            );
            return;
        }

        // Recuperar IP
        int savedIP = (Integer) stack.get(fp + 1);

        // Recuperar FP antigo
        int savedFP = (Integer) stack.get(fp);

        // Recuperar FP e IP do frame atual
        int frameSize = stack.size() - fp;

        // Remover a frame atual
        for (int i = 0; i < frameSize; i++){
            stack.pop();
        }

        // Remover argumentos
        int nArgs = inst.getArg();
        for (int i = 0; i < nArgs && !stack.isEmpty(); i++){
            stack.pop();
        }

        // Empilhar valor de retorno
        stack.push(returnValue);

        // Restaurar IP e FP
        ip = savedIP - 1; // -1 porque ele e incrementado depois da execucao
        fp = savedFP;
    }

    private void execRet(Instruction1Arg inst){

        // Verificar se o fp é válido
        if (fp < 0 || fp >= stack.size()) {
            runtimeError("Frame pointer invalido: " + fp);
            return;
        }

        // Recuperar FP e IP do frame atual
        int frameSize = stack.size() - fp;

        // Recuperar IP
        if (fp + 1 >= stack.size()) {
            runtimeError("Frame inconsistente");
        }
        Object savedIP = stack.get(fp + 1);

        // Recuperar FP
        Object savedFP = stack.get(fp);

        // Remover frame atual
        for (int i = 0; i < frameSize; i++){
            stack.pop();
        }

        // Remover argumentos
        int nArgs = inst.getArg();
        checkStackSize(nArgs);
        for (int i = 0; i < nArgs; i++){
            stack.pop();
        }

        // Restaurar IP e FP
        ip = (Integer) savedIP - 1;
        fp = (Integer) savedFP;
    }

    // Códigos utilitarios
    private void checkStackSize(int size){
        if (stack.size() < size){
            runtimeError("Pilha nao tem elementos suficientes");
        }
    }

    private void runtimeError(String message){
        System.out.println(message);
        if (trace){
            System.err.println("Estado da pilha: " + stack);
        }

        throw new RuntimeException("__VM_ERROR__");
    }

}
