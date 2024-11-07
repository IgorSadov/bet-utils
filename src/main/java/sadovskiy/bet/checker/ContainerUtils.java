package sadovskiy.bet.checker;

import java.util.Collection;
import java.util.Map;

public class ContainerUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean stringIsNotEmpty(String string){
        return string!=null&&!string.trim().isEmpty();
    }

}
