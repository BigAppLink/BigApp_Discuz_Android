package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;

import com.youzu.android.framework.json.JSONException;
import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.Feature;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;

public abstract class AbstractDateDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        JSONScanner lexer = (JSONScanner) parser.getLexer();

        Object val;
        if (lexer.token() == JSONToken.LITERAL_INT) {
            val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);
        } else if (lexer.token() == JSONToken.LITERAL_STRING) {
            String strVal = lexer.stringVal();
            val = strVal;
            lexer.nextToken(JSONToken.COMMA);
            
            if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner iso8601Lexer = new JSONScanner(strVal);
                if (iso8601Lexer.scanISO8601DateIfMatch()) {
                    val = iso8601Lexer.getCalendar().getTime();
                }
            }
        } else if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            val = null;
        } else if (parser.getResolveStatus() == DefaultJSONParser.TypeNameRedirect) {
            parser.accept(JSONToken.COMMA);

            if (lexer.token() == JSONToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            parser.accept(JSONToken.COLON);

            val = parser.parse();

            parser.accept(JSONToken.RBRACE);

            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            val = parser.parse();
        }

        return (T) cast(parser, clazz, fieldName, val);
    }

    protected abstract <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object value);
}
