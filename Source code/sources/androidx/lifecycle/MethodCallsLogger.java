package androidx.lifecycle;

import androidx.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods = new HashMap();

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
    public boolean approveCall(String name, int type) {
        int mask;
        boolean wasCalled;
        Integer nullableMask = this.mCalledMethods.get(name);
        if (nullableMask != null) {
            mask = nullableMask.intValue();
        } else {
            mask = 0;
        }
        if ((mask & type) != 0) {
            wasCalled = true;
        } else {
            wasCalled = false;
        }
        this.mCalledMethods.put(name, Integer.valueOf(mask | type));
        return !wasCalled;
    }
}
