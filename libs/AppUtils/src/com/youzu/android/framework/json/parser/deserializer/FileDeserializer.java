package com.youzu.android.framework.json.parser.deserializer;

import java.io.File;
import java.lang.reflect.Type;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONToken;

public class FileDeserializer implements ObjectDeserializer {
    public final static FileDeserializer instance = new FileDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        String path = (String) value;
        
        return (T) new File(path);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
