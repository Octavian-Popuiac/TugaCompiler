package Tuga.semantic;

public enum Type {
    BOOLEAN,
    INTEGER,
    STRING,
    REAL;

    @Override
    public String toString(){
        return switch (this) {
            case BOOLEAN -> "booleano";
            case INTEGER -> "inteiro";
            case STRING -> "string";
            case REAL -> "real";
        };
    }
}
