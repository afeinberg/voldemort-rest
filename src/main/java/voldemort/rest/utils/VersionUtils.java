package voldemort.rest.utils;

import com.google.inject.internal.Lists;
import voldemort.versioning.ClockEntry;
import voldemort.versioning.VectorClock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionUtils {

    public static Map<Object, Object> clockToMap(VectorClock clock) {
        Map<Object, Object> retVal = new HashMap<Object, Object>(2);
        retVal.put("timestamp", clock.getTimestamp());
        List<Map<Short, Long>> clockEntries = Lists.newArrayList();
        for(ClockEntry clockEntry: clock.getEntries()) {
            Map<Short, Long> aMap = new HashMap<Short, Long>(2);
            aMap.put(clockEntry.getNodeId(), clockEntry.getVersion());
            clockEntries.add(aMap);
        }
        retVal.put("entries", clockEntries);

        return retVal;
    }
}
