package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.parser.ParserConfig;
import com.youzu.android.framework.json.util.FieldInfo;
import com.youzu.android.framework.json.util.TypeUtils;

public class BooleanFieldDeserializer extends FieldDeserializer {

    public BooleanFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Boolean value;

        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.TRUE) {
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), Boolean.TRUE);
            } else {
                setValue(object, true);
            }
            return;
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            boolean booleanValue = val == 1;
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), booleanValue);
            } else {
                setValue(object, booleanValue);
            }
            return;
        }

        if (lexer.token() == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);

            if (getFieldClass() == boolean.class) {
                // skip
                return;
            }

            setValue(object, null);
            return;
        }

        if (lexer.token() == JSONToken.FALSE) {
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), Boolean.FALSE);
            } else {
                setValue(object, false);
            }
            return;
        }

        Object obj = parser.parse();

        value = TypeUtils.castToBoolean(obj);

        if (value == null && getFieldClass() == boolean.class) {
            // skip
            return;
        }

        if (object == null) {
            fieldValues.put(fieldInfo.getName(), value);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.TRUE;
    }
}
