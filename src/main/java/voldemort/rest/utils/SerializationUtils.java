package voldemort.rest.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SerializationUtils {

    @SuppressWarnings("unchecked")
    public static Object tightenNumericTypes(Object o) {
        if(o == null) {
            return null;
        } else if(o instanceof List) {
            List l = (List) o;
            for(int i = 0; i < l.size(); i++)
                l.set(i, tightenNumericTypes(l.get(i)));
            return l;
        } else if(o instanceof Map) {
            Map m = (Map) o;
            for(Map.Entry entry: (Set<Map.Entry>) m.entrySet())
                m.put(entry.getKey(), tightenNumericTypes(entry.getValue()));
            return m;
        } else if(o instanceof Number) {
            Number n = (Number) o;
            if(o instanceof Integer) {
                if(n.intValue() < Byte.MAX_VALUE)
                    return n.byteValue();
                else if(n.intValue() < Short.MAX_VALUE)
                    return n.shortValue();
                else
                    return n;
            } else if(o instanceof Double) {
                if(n.doubleValue() < Float.MAX_VALUE)
                    return n.floatValue();
                else
                    return n;
            } else {
                throw new RuntimeException("Unsupported numeric type: " + o.getClass());
            }
        } else {
            return o;
        }
    }
}
