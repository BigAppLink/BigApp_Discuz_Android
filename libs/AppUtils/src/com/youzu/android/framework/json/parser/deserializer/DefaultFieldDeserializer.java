package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONToken;
import com.youzu.android.framework.json.parser.ParserConfig;
import com.youzu.android.framework.json.parser.DefaultJSONParser.ResolveTask;
import com.youzu.android.framework.json.util.FieldInfo;

public class DefaultFieldDeserializer extends FieldDeserializer {

    private ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (fieldValueDeserilizer == null) {
            fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldInfo);
        }

        Object value = fieldValueDeserilizer.deserialze(parser, getFieldType(), fieldInfo.getName());
        if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
            ResolveTask task = parser.getLastResolveTask();
            task.setFieldDeserializer(this);
            task.setOwnerContext(parser.getContext());
            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), value);
            } else {
                setValue(object, value);
            }
        }
    }

    public int getFastMatchToken() {
        if (fieldValueDeserilizer != null) {
            return fieldValueDeserilizer.getFastMatchToken();
        }

        return JSONToken.LITERAL_INT;
    }
}
