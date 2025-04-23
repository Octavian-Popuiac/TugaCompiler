package Tuga.util;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class LexerErrorListener extends BaseErrorListener {
    private boolean hasErrors = false;
    private final boolean showErrors;
    public LexerErrorListener(boolean showErrors){
        this.showErrors = showErrors;
    }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        hasErrors = true;
        if (showErrors) {
            System.err.println("Erro lexical na linha " + line + ":" + charPositionInLine + " - " + msg);
        }
    }

    public boolean hasErrors(){
        return hasErrors;
    }
}
