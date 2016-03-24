package com.youzu.android.framework.json.serializer;

import java.util.List;

public class FilterUtils {
    public static Object processValue(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<ValueFilter> valueFilters = serializer.getValueFiltersDirect();
        if (valueFilters != null) {
            for (ValueFilter valueFilter : valueFilters) {
                propertyValue = valueFilter.process(object, key, propertyValue);
            }
        }

        return propertyValue;
    }
    
    public static String processKey(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, byte intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Byte.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, short intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Short.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, int intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Integer.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, long intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Long.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, float intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Float.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, double intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Double.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, boolean intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Boolean.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }

    public static String processKey(JSONSerializer serializer, Object object, String key, char intValue) {
        List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
        if (nameFilters != null) {
            Object propertyValue = Character.valueOf(intValue);

            for (NameFilter nameFilter : nameFilters) {
                key = nameFilter.process(object, key, propertyValue);
            }
        }

        return key;
    }
    
    public static boolean apply(JSONSerializer serializer, Object object, String key, Object propertyValue) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, byte value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Byte.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, short value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Short.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, int value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Integer.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }
    
    public static boolean apply(JSONSerializer serializer, Object object, String key, char value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Character.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, long value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Long.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, float value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Float.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }

    public static boolean apply(JSONSerializer serializer, Object object, String key, double value) {
        List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
        
        if (propertyFilters != null) {
            boolean apply = true;

            Object propertyValue = Double.valueOf(value);
            for (PropertyFilter propertyFilter : propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }

            return apply;
        }

        return true;
    }
}
