import Tuga.codegen.BytecodeGenerator;
import Tuga.parser.TugaLexer;
import Tuga.parser.TugaParser;
import Tuga.semantic.TypeChecker;
import Tuga.semantic.TypeCheckingException;
import Tuga.util.LexerErrorListener;
import Tuga.util.ParserErrorListener;
import Tuga.vm.SVirtualMachine;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;


import java.io.IOException;

public class TugaCompileAndRun {
    public static boolean showLexerErrors = false;
    public static boolean showParserErrors = false;
    public static boolean showTypeCheckingErrors = false;
    public static boolean showAsm = true;  // Mostrar o código gerado em assembly

    public static void main(String[] args) {

        try {
            // Preparar input (stdin ou arquivo)
            CharStream input;
            if (args.length > 0){
                String inputFile = args[0];
                input = CharStreams.fromFileName(inputFile);
            }else {
                input = CharStreams.fromStream(System.in);
            }

            // Nomde do arquivo de bytecodes
            String outputFilename = "bytecodes.bc";

            // 1. Analise lexica
            TugaLexer lexer = new TugaLexer(input);
            lexer.removeErrorListeners();
            LexerErrorListener lexerErrorListener = new LexerErrorListener(showLexerErrors);
            lexer.addErrorListener(lexerErrorListener);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill(); // Forcar lexer para processar todos os tokens

            if (lexerErrorListener.hasErrors()){
                System.out.println("Input has lexical errors");
                return;
            }

            // 2. Analise sintatica
            TugaParser parser = new TugaParser(tokens);
            parser.removeErrorListeners();
            ParserErrorListener parserErrorListener = new ParserErrorListener(showParserErrors);
            parser.addErrorListener(parserErrorListener);

            ParseTree tree = parser.program();

            // Verifica erros sintáticos
            if (parserErrorListener.hasErrors()){
                System.out.println("Input has parsing errors");
                return;
            }

            TypeChecker typeChecker = new TypeChecker();


            // Executa analise semantica
            typeChecker.visit(tree);

            // Verifica se houve erros semanticos
            if (typeChecker.hasErrors()){
                for (String error : typeChecker.getErrors()){
                    System.out.println(error);
                }

                if (showTypeCheckingErrors){
                    System.err.println("Input has type checking errors");
                }

                return;
            }



            // 4. Geracao de bytecodes
            BytecodeGenerator bytecodeGenerator = new BytecodeGenerator(typeChecker, typeChecker.getSymbolTable());
            bytecodeGenerator.visit(tree);

            // Exibir codigo assembly se a flag estiver ativa
            if (showAsm){
                System.out.println("*** Constant pool ***");
                bytecodeGenerator.dumpConstantPool();

                System.out.println("*** Instructions ***");
                bytecodeGenerator.dumpCode();
            }

            // Salver bytecode no arquivo de saida
            bytecodeGenerator.saveBytecodes(outputFilename);


            // 5. Executar o programa compilado
            System.out.println("*** VM output ***");
            SVirtualMachine vm = new SVirtualMachine();
            vm.execute(outputFilename);
        }catch (RuntimeException e){
            if (!"__VM_ERROR__".equals(e.getMessage())){
                System.err.println("Erro: " +e.getMessage());
            }
        } catch (IOException e){
            System.err.println("Erro de I/O: " + e.getMessage());
        }catch (Exception e){
            if (showLexerErrors || showParserErrors || showTypeCheckingErrors){
                e.printStackTrace();
            }else {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
}
