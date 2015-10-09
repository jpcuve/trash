package parser.pdf.impl;

public enum Token {
    UNKNOWN,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    OPERATOR,
    NULL,
    BOOLEAN,
    NUMBER,
    STRING,
    NAME,
    REFERENCE,
    DICTIONARY_INIT,
    DICTIONARY_DONE,
    STREAM_INIT,
    STREAM,
    STREAM_DONE,
    OBJ_INIT,
    OBJ_DONE,
}
