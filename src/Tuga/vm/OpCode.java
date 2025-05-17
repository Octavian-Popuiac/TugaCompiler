package Tuga.vm;

/**
 *  Codigos de instrucao da maquina virtual
 */
public enum OpCode {
    //  Instrucoes com 1 argumento (5 bytes: 1 byte opcode + 4 bytes argumento)
    iconst(1),  //  0: Empilha o valor inteiro n no stack
    dconst(1),  //  1: Empilha o valor real da position n na constant pool, no stack
    sconst(1),  //  2: Empilha a string da position n na constant pool, no stack
    jump(1),    // 41: Unconditional jump. Atualiza o instruction pointer de modo a que a proxima instrucao a ser executada seja aquela que se encontra na posicao addr do array de instrucoes
    jumpf(1),   // 42: Jump if false: faz pop do Stack. Se o valor for false, atualiza o instruction pointer de modo a que a proxima instrucao a ser executada seja aquela que se encontra na posicao addr do array de instrucoes
    galloc(1),  // 43: Global memory allocation: Aloca n posicoes num array que permite armazenar variaveis globais. Array designado por Globals. Essas n posicoes de memoria ficam inicializadas com o valor NULO
    gload(1),   // 44: Global load: Empilha Globals[addr] no stakc
    gstore(1),  // 45: Global store: Faz pop do stack e guarda o valor em Globals[addr]
    lalloc(1),  // 46: Local memory allocation: Aloca n posicoes no topo do stack para armazenar variaveis locais. Essas n posicoes de memoria ficam inicializads com o valor NIL
    lload(1),   // 47: Local load: Empilha o conteudo de Stack[FP + addr] no stack
    lstore(1),  // 48: Local store: Faz pop do stack e guarda o valor em Stack[FP + addr]
    pop(1),     // 49: Desempilha n elementos do stack
    call(1),    // 50: Cria um novo frame no stack, que passara a ser o frame currente. Guarda FP, atualiza FP para a base do novo frame, empilha endereco de retorno. Atualiza IP para o endereco da funcao
    retval(1),  // 51: Return from non-void function: Faz x = pop(), desempilha o espaco reservado para as variaveis locais usadas pela funcao, restaura o estado da maquina virtual, desempilha os n argumentos do stack, e depois empilha x
    ret(1),     // 52: Return from void function: Desempilha os espaco reservado para as variaveis locais usadas pela funcao, restaura o estado da maquina virtual, e desempilha os n argumentos do stack

    //  Instrucoes sem argumentos (1 byte, apenas o opcode)
    iprint(0),  // 3: Faz pop do operando a, e escreve o seu valor no ecra seguido de um caracter de mudanca de linha (inteiro)
    iuminus(0), // 4: Faz pop do operando a, e empilha -a no stack (negação unária de inteiro)
    iadd(0),    // 5: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a + b no stack (soma de inteiros)
    isub(0),    // 6: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a - b no stack (subtração de inteiros)
    imult(0),    // 7: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a * b no stack (multiplicação de inteiros)
    idiv(0),    // 8: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a / b no stack (divisão de inteiros)
    imod(0),    // 9: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o resto da divisao de a por b no stack (módulo de inteiros)
    ieq(0),     // 10: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a == b no stack (igualdade de inteiros)
    ineq(0),    // 11: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a /= b no stack (desigualdade de inteiros)
    ilt(0),     // 12: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a < b no stack (menor que (inteiros))
    ileq(0),    // 13: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a <= b no stack (menor ou igual (inteiros))
    itod(0),    // 14: Converte o valor int que esta no topo do stack para um valor real (conversão inteiro -> real)
    itos(0),    // 15: Converte o valor int que esta no topo do stack para uma string (conversão inteiro -> string)

    dprint(0),  // 16: Faz pop do operando a, e escreve o seu valor no ecra seguido de um caracter de mudanca de linha (double)
    duminus(0), // 17: Faz pop do operando a, e empilha -a no stack (negação unária de double)
    dadd(0),    // 18: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a + b no stack (soma de doubles)
    dsub(0),    // 19: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a - b no stack (subtração de doubles)
    dmult(0),    // 20: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a * b no stack (multiplicação de doubles)
    ddiv(0),    // 21: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha a / b no stack (divisão de doubles)
    deq(0),     // 22: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a == b no stack (igualdade de doubles)
    dneq(0),    // 23: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a /= b no stack (desigualdade de doubles)
    dlt(0),     // 24: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a < b no stack (menor que (doubles))
    dleq(0),    // 25: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a <= b no stack (menor ou igual (doubles))
    dtos(0),    // 26: Converte o valor int que esta no topo do stack para uma string (conversão double -> string)

    sprint(0),  // 27: Faz pop do operando a, e escreve o seu valor no ecra seguido de um caracter de mudanca de linha (string)
    sconcat(0), // 28: Faz pop do operando diretiro b, seguido de pop do operando esquerdo a, e empilha a concatenado com b no stack (concatenação de strings)
    seq(0),     // 29: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a == b no stack (igualdade de strings)
    sneq(0),    // 30: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a /= b no stack (desigualdade de strings)

    tconst(0),  // 31: Empilha o valor true, do tipo boolean, no stack
    fconst(0),  // 32: Empilha o valor false, do tipo boolean, no stack
    bprint(0),  // 33: Faz pop do stack. Se o valor for true escreve no ecra verdadeiro seguido de um caracter de mudanca de linha, se o valor for false escreve no ecra falso seguido de um caracter de mudanca de linha (imprime valor booleano)
    beq(0),     // 34: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a == b no stack (igualdade de booleanos)
    bneq(0),    // 35: Faz pop do operando direito b, seguido de pop do operando esquerdo a, e empilha o valor logico de a /= b no stack (desigualdade de booleanos)
    and(0),     // 36: Boolean and: Faz pop do operando direito b, seguido de pop do operador esquerdo a (supostamente ambos do tipo boolean), e empilha o valor lógico a and b no stack (operador AND)
    or(0),      // 37: Boolean or: Faz pop do operando direito b, seguido de pop do operador esquerdo a (supostamente ambos do tipo boolean), e empilha o valor lógico a or b no stack (operador OR)
    not(0),     // 38: Boolean not: Faz pop do operando direito a (supostamente do tipo boolean), e empilha o valor lógico not a no stack (operador NOT)
    btos(0),    // 39: Converte o valor boolean que esta no topo do stack para uma string (conversão boolean -> string)
    halt(0);    // 40: termina a execução
    private final int nArgs; // numero de argumentos

    OpCode(int nArgs){
        this.nArgs = nArgs;
    }

    public int nArgs(){
        return nArgs;
    }

    public static OpCode convert(byte value){
        return OpCode.values()[value];
    }
}
