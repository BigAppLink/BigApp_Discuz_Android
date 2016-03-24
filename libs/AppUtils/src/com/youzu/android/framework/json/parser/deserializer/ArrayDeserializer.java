package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

import com.youzu.android.framework.json.JSONArray;
import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.util.TypeUtils;

public class ArrayDeserializer implements ObjectDeserializer {

    public final static ArrayDeserializer instance = new ArrayDeserializer();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            byte[] bytes = lexer.bytesValue();
            lexer.nextToken(JSONToken.COMMA);
            return (T) bytes;
        }
        
        Class clazz = (Class) type;
        Class componentType = clazz.getComponentType();
        JSONArray array = new JSONArray();
        parser.parseArray(componentType, array, fieldName);

        return (T) toObjectArray(parser, clazz, array);
    }

    @SuppressWarnings("unchecked")
    private <T> T toObjectArray(DefaultJSONParser parser, Class<T> clazz, JSONArray array) {
        if (array == null) {
            return null;
        }
        
        int size = array.size();

        Class<?> componentType = clazz.getComponentType();
        Object objArray = Array.newInstance(componentType, size);
        for (int i = 0; i < size; ++i) {
            Object value = array.get(i);

            if (componentType.isArray()) {
                Object element;
                if (componentType.isInstance(value)) {
                    element = value;    
                } else {
                    element = toObjectArray(parser, componentType, (JSONArray) value);
                }
                
                Array.set(objArray, i, element);
            } else {
                Object element = TypeUtils.cast(value, componentType, parser.getConfig());
                Array.set(objArray, i, element);
            }
        }
        
        array.setRelatedArray(objArray);
        array.setComponentType(componentType);
        return (T) objArray; // TODO
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
