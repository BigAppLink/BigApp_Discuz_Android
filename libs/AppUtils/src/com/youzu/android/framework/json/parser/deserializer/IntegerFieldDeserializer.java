package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.parser.ParserConfig;
import com.youzu.android.framework.json.util.FieldInfo;
import com.youzu.android.framework.json.util.TypeUtils;

public class IntegerFieldDeserializer extends FieldDeserializer {

    public IntegerFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Integer value;

        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), val);
            } else {
                setValue(object, val);
            }
            return;
        } else if (lexer.token() == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);
        } else {
            Object obj = parser.parse();

            value = TypeUtils.castToInt(obj);
        }

        if (value == null && getFieldClass() == int.class) {
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
        return JSONToken.LITERAL_INT;
    }
}
