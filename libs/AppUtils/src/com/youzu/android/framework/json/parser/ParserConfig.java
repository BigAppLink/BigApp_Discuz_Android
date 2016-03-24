/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youzu.android.framework.json.parser;

import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import com.youzu.android.framework.json.JSONArray;
import com.youzu.android.framework.json.JSONObject;
import com.youzu.android.framework.json.parser.deserializer.ArrayDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ArrayListStringDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ArrayListStringFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ArrayListTypeDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.BigDecimalDeserializer;
import com.youzu.android.framework.json.parser.deserializer.BigIntegerDeserializer;
import com.youzu.android.framework.json.parser.deserializer.BooleanDeserializer;
import com.youzu.android.framework.json.parser.deserializer.BooleanFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.CalendarDeserializer;
import com.youzu.android.framework.json.parser.deserializer.CharArrayDeserializer;
import com.youzu.android.framework.json.parser.deserializer.CharacterDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ClassDerializer;
import com.youzu.android.framework.json.parser.deserializer.CollectionDeserializer;
import com.youzu.android.framework.json.parser.deserializer.DateDeserializer;
import com.youzu.android.framework.json.parser.deserializer.DateFormatDeserializer;
import com.youzu.android.framework.json.parser.deserializer.DefaultFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.DefaultObjectDeserializer;
import com.youzu.android.framework.json.parser.deserializer.EnumDeserializer;
import com.youzu.android.framework.json.parser.deserializer.FieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.FileDeserializer;
import com.youzu.android.framework.json.parser.deserializer.FloatDeserializer;
import com.youzu.android.framework.json.parser.deserializer.InetAddressDeserializer;
import com.youzu.android.framework.json.parser.deserializer.InetSocketAddressDeserializer;
import com.youzu.android.framework.json.parser.deserializer.IntegerDeserializer;
import com.youzu.android.framework.json.parser.deserializer.IntegerFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.JSONArrayDeserializer;
import com.youzu.android.framework.json.parser.deserializer.JSONObjectDeserializer;
import com.youzu.android.framework.json.parser.deserializer.JavaBeanDeserializer;
import com.youzu.android.framework.json.parser.deserializer.JavaObjectDeserializer;
import com.youzu.android.framework.json.parser.deserializer.LocaleDeserializer;
import com.youzu.android.framework.json.parser.deserializer.LongDeserializer;
import com.youzu.android.framework.json.parser.deserializer.LongFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.MapDeserializer;
import com.youzu.android.framework.json.parser.deserializer.NumberDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ObjectDeserializer;
import com.youzu.android.framework.json.parser.deserializer.SimpleTypeDeserializer;
import com.youzu.android.framework.json.parser.deserializer.SqlDateDeserializer;
import com.youzu.android.framework.json.parser.deserializer.StackTraceElementDeserializer;
import com.youzu.android.framework.json.parser.deserializer.StringDeserializer;
import com.youzu.android.framework.json.parser.deserializer.StringFieldDeserializer;
import com.youzu.android.framework.json.parser.deserializer.ThrowableDeserializer;
import com.youzu.android.framework.json.parser.deserializer.TimeDeserializer;
import com.youzu.android.framework.json.parser.deserializer.TimestampDeserializer;
import com.youzu.android.framework.json.util.FieldInfo;
import com.youzu.android.framework.json.util.IdentityHashMap;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class ParserConfig {

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    private final Set<Class<?>>                             primitiveClasses  = new HashSet<Class<?>>();

    private static ParserConfig                             global            = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> derializers       = new IdentityHashMap<Type, ObjectDeserializer>();

    private DefaultObjectDeserializer                       defaultSerializer = new DefaultObjectDeserializer();

    private boolean                                         asmEnable         = false;

    protected final SymbolTable                             symbolTable       = new SymbolTable();

    public DefaultObjectDeserializer getDefaultSerializer() {
        return defaultSerializer;
    }

    public ParserConfig(){
        primitiveClasses.add(boolean.class);
        primitiveClasses.add(Boolean.class);

        primitiveClasses.add(char.class);
        primitiveClasses.add(Character.class);

        primitiveClasses.add(byte.class);
        primitiveClasses.add(Byte.class);

        primitiveClasses.add(short.class);
        primitiveClasses.add(Short.class);

        primitiveClasses.add(int.class);
        primitiveClasses.add(Integer.class);

        primitiveClasses.add(long.class);
        primitiveClasses.add(Long.class);

        primitiveClasses.add(float.class);
        primitiveClasses.add(Float.class);

        primitiveClasses.add(double.class);
        primitiveClasses.add(Double.class);

        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);

        primitiveClasses.add(String.class);
        primitiveClasses.add(java.util.Date.class);
        primitiveClasses.add(java.sql.Date.class);
        primitiveClasses.add(java.sql.Time.class);
        primitiveClasses.add(java.sql.Timestamp.class);

        derializers.put(SimpleDateFormat.class, DateFormatDeserializer.instance);
        derializers.put(java.sql.Timestamp.class, TimestampDeserializer.instance);
        derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        derializers.put(java.sql.Time.class, TimeDeserializer.instance);
        derializers.put(java.util.Date.class, DateDeserializer.instance);
        derializers.put(Calendar.class, CalendarDeserializer.instance);

        derializers.put(JSONObject.class, JSONObjectDeserializer.instance);
        derializers.put(JSONArray.class, JSONArrayDeserializer.instance);

        derializers.put(Map.class, MapDeserializer.instance);
        derializers.put(HashMap.class, MapDeserializer.instance);
        derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        derializers.put(TreeMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

        derializers.put(Collection.class, CollectionDeserializer.instance);
        derializers.put(List.class, CollectionDeserializer.instance);
        derializers.put(ArrayList.class, CollectionDeserializer.instance);

        derializers.put(Object.class, JavaObjectDeserializer.instance);
        derializers.put(String.class, StringDeserializer.instance);
        derializers.put(char.class, CharacterDeserializer.instance);
        derializers.put(Character.class, CharacterDeserializer.instance);
        derializers.put(byte.class, NumberDeserializer.instance);
        derializers.put(Byte.class, NumberDeserializer.instance);
        derializers.put(short.class, NumberDeserializer.instance);
        derializers.put(Short.class, NumberDeserializer.instance);
        derializers.put(int.class, IntegerDeserializer.instance);
        derializers.put(Integer.class, IntegerDeserializer.instance);
        derializers.put(long.class, LongDeserializer.instance);
        derializers.put(Long.class, LongDeserializer.instance);
        derializers.put(BigInteger.class, BigIntegerDeserializer.instance);
        derializers.put(BigDecimal.class, BigDecimalDeserializer.instance);
        derializers.put(float.class, FloatDeserializer.instance);
        derializers.put(Float.class, FloatDeserializer.instance);
        derializers.put(double.class, NumberDeserializer.instance);
        derializers.put(Double.class, NumberDeserializer.instance);
        derializers.put(boolean.class, BooleanDeserializer.instance);
        derializers.put(Boolean.class, BooleanDeserializer.instance);
        derializers.put(Class.class, ClassDerializer.instance);
        derializers.put(char[].class, CharArrayDeserializer.instance);

        derializers.put(UUID.class, SimpleTypeDeserializer.instance);
        derializers.put(TimeZone.class, SimpleTypeDeserializer.instance);
        derializers.put(Locale.class, LocaleDeserializer.instance);
        derializers.put(InetAddress.class, InetAddressDeserializer.instance);
        derializers.put(Inet4Address.class, InetAddressDeserializer.instance);
        derializers.put(Inet6Address.class, InetAddressDeserializer.instance);
        derializers.put(InetSocketAddress.class, InetSocketAddressDeserializer.instance);
        derializers.put(File.class, FileDeserializer.instance);
        derializers.put(URI.class, SimpleTypeDeserializer.instance);
        derializers.put(URL.class, SimpleTypeDeserializer.instance);
        derializers.put(Pattern.class, SimpleTypeDeserializer.instance);
        derializers.put(Charset.class, SimpleTypeDeserializer.instance);
        derializers.put(Number.class, NumberDeserializer.instance);
        derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);

        derializers.put(Serializable.class, defaultSerializer);
        derializers.put(Cloneable.class, defaultSerializer);
        derializers.put(Comparable.class, defaultSerializer);
        derializers.put(Closeable.class, defaultSerializer);
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type instanceof Class<?>) {
            return getDeserializer((Class<?>) type, type);
        }

        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                return getDeserializer((Class<?>) rawType, type);
            } else {
                return getDeserializer(rawType);
            }
        }

        return this.defaultSerializer;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type == null) {
            type = clazz;
        }

        derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            derializer = derializers.get(clazz);
        }

        if (derializer != null) {
            return derializer;
        }

        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            return ArrayDeserializer.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            if (type instanceof ParameterizedType) {
                Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (itemType == String.class) {
                    derializer = ArrayListStringDeserializer.instance;
                } else {
                    derializer = new ArrayListTypeDeserializer(clazz, itemType);
                }
            } else {
                derializer = CollectionDeserializer.instance;
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionDeserializer.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, derializer);

        return derializer;
    }
    
    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz) {
        return createJavaBeanDeserializer(clazz, clazz);
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        if (clazz == Class.class) {
            return this.defaultSerializer;
        }

        return new JavaBeanDeserializer(this, clazz, type);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
    }

    public FieldDeserializer createFieldDeserializerWithoutASM(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.getFieldClass();

        if (fieldClass == boolean.class || fieldClass == Boolean.class) {
            return new BooleanFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == int.class || fieldClass == Integer.class) {
            return new IntegerFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == long.class || fieldClass == Long.class) {
            return new LongFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == String.class) {
            return new StringFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            Type fieldType = fieldInfo.getFieldType();
            if (fieldType instanceof ParameterizedType) {
                Type itemType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                if (itemType == String.class) {
                    return new ArrayListStringFieldDeserializer(mapping, clazz, fieldInfo);
                }
            }

            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        derializers.put(type, deserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.getFieldClass(), fieldInfo.getFieldType());
    }

    public boolean isPrimitive(Class<?> clazz) {
        return primitiveClasses.contains(clazz);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            return getField(clazz.getSuperclass(), fieldName);
        }

        return null;
    }

    public Map<String, FieldDeserializer> getFieldDeserializers(Class<?> clazz) {
        ObjectDeserializer deserizer = getDeserializer(clazz);

        if (deserizer instanceof JavaBeanDeserializer) {
            return ((JavaBeanDeserializer) deserizer).getFieldDeserializerMap();
        } else {
            return Collections.emptyMap();
        }
    }

}
