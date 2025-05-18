package Tuga.util;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Ouvinte de erros lexicos personalizado para o analisador lexico da linguagem Tuga.
 * Captura erros lexicos (como caracteres invalidos ou tokens mal formados)
 * e fornece uma forma de verificar se ocorreram erros durante a analise.
 */
public class LexerErrorListener extends BaseErrorListener {
    /** Indica se foram detetados erros lexicos */
    private boolean hasErrors = false;
    /** Controla se os erros devem ser apresentados na saida padrao de erro */
    private final boolean showErrors;

    /**
     * Cria um novo ouvinte de erros lexicos.
     * 
     * @param showErrors true para mostrar erros na consola, false para silenciar
     */
    public LexerErrorListener(boolean showErrors){
        this.showErrors = showErrors;
    }

    /**
     * Processa um erro de sintaxe reportado pelo analisador lexico.
     * Marca que ocorreu um erro e opcionalmente apresenta-o na consola.
     * 
     * @param recognizer O analisador que encontrou o erro
     * @param offendingSymbol O simbolo que causou o erro
     * @param line A linha onde ocorreu o erro
     * @param charPositionInLine A posicao na linha onde ocorreu o erro
     * @param msg A mensagem de erro
     * @param e A excecao de reconhecimento, se disponivel
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        hasErrors = true;
        if (showErrors) {
            System.err.println("Erro lexical na linha " + line + ":" + charPositionInLine + " - " + msg);
        }
    }

    /**
     * Verifica se foram detetados erros lexicos.
     * 
     * @return true se existirem erros, false caso contrario
     */
    public boolean hasErrors(){
        return hasErrors;
    }
}
