package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.util.TypeUtils;

public class BooleanDeserializer implements ObjectDeserializer {
    public final static BooleanDeserializer instance = new BooleanDeserializer();
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONScanner lexer = parser.getLexer();
        
        if (lexer.token() == JSONToken.TRUE) {
            lexer.nextToken(JSONToken.COMMA);
            return (T) Boolean.TRUE;
        }
        
        if (lexer.token() == JSONToken.FALSE) {
            lexer.nextToken(JSONToken.COMMA);
            return (T) Boolean.FALSE;
        }
        
        if (lexer.token() == JSONToken.LITERAL_INT) {
            int intValue = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            
            if (intValue == 1) {
                return (T) Boolean.TRUE;
            } else {
                return (T) Boolean.FALSE;
            }
        }
        
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        return (T) TypeUtils.castToBoolean(value);
    }

    public int getFastMatchToken() {
        return JSONToken.TRUE;
    }
}
