package Tuga.util;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
public class ParserErrorListener extends BaseErrorListener {
    private boolean hasErrors = false;
    private final boolean showErros;
    public ParserErrorListener(boolean showErros){
        this.showErros = showErros;
    }

    @Override
    public void syntaxError(Recognizer<? ,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        hasErrors = true;
        if (showErros){
            System.err.println("Erro sintatico na linha " + line + ":" + charPositionInLine + " - " + msg);
        }
    }

    public boolean hasErrors(){
        return hasErrors;
    }
}
