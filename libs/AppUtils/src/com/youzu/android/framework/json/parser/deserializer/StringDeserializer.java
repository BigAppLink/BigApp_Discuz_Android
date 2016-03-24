package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;

public class StringDeserializer implements ObjectDeserializer {

    public final static StringDeserializer instance = new StringDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }
        
        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) value.toString();
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
