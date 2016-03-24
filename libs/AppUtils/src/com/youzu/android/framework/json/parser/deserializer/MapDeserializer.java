package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.youzu.android.framework.json.JSONException;
import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONScanner;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.parser.ParseContext;

public class MapDeserializer implements ObjectDeserializer {

    public final static MapDeserializer instance = new MapDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONScanner lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        Map<Object, Object> map = createMap(type);

        ParseContext context = parser.getContext();

        try {
            parser.setContext(context, map, fieldName);

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                return (T) map;
            }

            return (T) deserialze(parser, type, fieldName, map);
        } finally {
            parser.setContext(context);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type keyType = parameterizedType.getActualTypeArguments()[0];
            Type valueType = parameterizedType.getActualTypeArguments()[1];

            if (String.class == keyType) {
                return DefaultObjectDeserializer.instance.parseMap(parser, (Map<String, Object>) map, valueType,
                                                                   fieldName);
            } else {
                return DefaultObjectDeserializer.instance.parseMap(parser, map, keyType, valueType, fieldName);
            }
        } else {
            return parser.parseObject(map, fieldName);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }

        if (type == Hashtable.class) {
            return new Hashtable();
        }

        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }

        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }

        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }

        if (type == Map.class || type == HashMap.class) { //
            return new HashMap();
        }

        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            return createMap(parameterizedType.getRawType());
        }

        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isInterface()) {
                throw new JSONException("unsupport type " + type);
            }

            try {
                return (Map<Object, Object>) clazz.newInstance();
            } catch (Exception e) {
                throw new JSONException("unsupport type " + type, e);
            }
        }

        throw new JSONException("unsupport type " + type);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
