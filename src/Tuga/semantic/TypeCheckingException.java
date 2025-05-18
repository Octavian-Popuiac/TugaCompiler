package Tuga.semantic;

/**
 * Excecao lancada durante a verificacao de tipos na analise semantica.
 * Esta excecao e utilizada para sinalizar erros de tipo, como operacoes
 * invalidas entre tipos incompativeis ou declaracoes duplicadas.
 */
public class TypeCheckingException extends RuntimeException{
    /**
     * Cria uma nova excecao de verificacao de tipos com a mensagem especificada.
     *
     * @param message A mensagem de erro a apresentar
     */
    public TypeCheckingException(String message){
        super(message);
    }
}
