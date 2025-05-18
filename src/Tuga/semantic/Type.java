package Tuga.semantic;

/**
 * Define os tipos de dados suportados pela linguagem Tuga.
 * Este enum e utilizado durante a analise semantica para
 * verificacao de tipos e durante a geracao de codigo.
 */
public enum Type {
    /** Tipo booleano, representa valores verdadeiro/falso */
    BOOLEAN,
    /** Tipo inteiro, representa numeros sem parte decimal */
    INTEGER,
    /** Tipo string, representa sequencias de caracteres */
    STRING,
    /** Tipo real, representa numeros com parte decimal */
    REAL,
    /** Tipo especial para indicar erros de tipo */
    ERROR,
    /** Tipo vazio, usado para funcoes sem valor de retorno */
    VOID;

    /**
     * Devolve a representacao em texto do tipo na linguagem Tuga.
     *
     * @return Nome do tipo em portugues
     */
    @Override
    public String toString(){
        return switch (this) {
            case BOOLEAN -> "booleano";
            case INTEGER -> "inteiro";
            case STRING -> "string";
            case REAL -> "real";
            case VOID -> "vazio";
            case ERROR -> "erro";
        };
    }
}
