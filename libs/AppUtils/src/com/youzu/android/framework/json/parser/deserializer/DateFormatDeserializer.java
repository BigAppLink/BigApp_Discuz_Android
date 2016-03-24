package com.youzu.android.framework.json.parser.deserializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.youzu.android.framework.json.JSONException;
import com.youzu.android.framework.json.parser.DefaultJSONParser;
import com.youzu.android.framework.json.parser.JSONToken;

public class DateFormatDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static DateFormatDeserializer instance = new DateFormatDeserializer();

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {

        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            return (T) new SimpleDateFormat(strVal);
        }

        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
