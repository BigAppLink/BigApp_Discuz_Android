package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.parser.ParserConfig;
import com.youzu.android.framework.json.util.FieldInfo;

public class ArrayListStringFieldDeserializer extends FieldDeserializer {

    public ArrayListStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        ArrayList<Object> list;

        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            list = null;
        } else {
            list = new ArrayList<Object>();

            ArrayListStringDeserializer.parseArray(parser, list);
        }
        if (object == null) {
            fieldValues.put(fieldInfo.getName(), list);
        } else {
            setValue(object, list);
        }
    }
}
