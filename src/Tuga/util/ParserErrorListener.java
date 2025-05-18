package Tuga.util;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Ouvinte de erros sintaticos personalizado para o analisador sintatico da linguagem Tuga.
 * Captura erros sintaticos (como estruturas gramaticais invalidas)
 * e fornece uma forma de verificar se ocorreram erros durante a analise.
 */
public class ParserErrorListener extends BaseErrorListener {
    /** Indica se foram detetados erros sintaticos */
    private boolean hasErrors = false;
    /** Controla se os erros devem ser apresentados na saida padrao de erro */
    private final boolean showErros;

    /**
     * Cria um novo ouvinte de erros sintaticos.
     *
     * @param showErros true para mostrar erros na consola, false para silenciar
     */
    public ParserErrorListener(boolean showErros){
        this.showErros = showErros;
    }

    /**
     * Processa um erro de sintaxe reportado pelo analisador sintatico.
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
    public void syntaxError(Recognizer<? ,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        hasErrors = true;
        if (showErros){
            System.err.println("Erro sintatico na linha " + line + ":" + charPositionInLine + " - " + msg);
        }
    }

    /**
     * Verifica se foram detetados erros sintaticos.
     *
     * @return true se existirem erros, false caso contrario
     */
    public boolean hasErrors(){
        return hasErrors;
    }
}
