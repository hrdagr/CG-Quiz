package anywheresoftware.b4j.object;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@BA.Version(2.06f)
@BA.ShortName("JavaObject")
public class JavaObject extends AbsObjectWrapper<Object> {
    private static Field context;
    private static final FieldCache fieldCache = new FieldCache();
    private static final MethodCache methodCache = new MethodCache();
    private static final HashMap<Class<?>, Class<?>> primitiveToBoxed = new HashMap<>();
    private static final HashMap<String, Class<?>> primitives = new HashMap<>();

    static {
        primitiveToBoxed.put(Byte.TYPE, Byte.class);
        primitiveToBoxed.put(Character.TYPE, Character.class);
        primitiveToBoxed.put(Short.TYPE, Short.class);
        primitiveToBoxed.put(Integer.TYPE, Integer.class);
        primitiveToBoxed.put(Long.TYPE, Long.class);
        primitiveToBoxed.put(Float.TYPE, Float.class);
        primitiveToBoxed.put(Double.TYPE, Double.class);
        primitiveToBoxed.put(Boolean.TYPE, Boolean.class);
        primitives.put("byte", Byte.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put("short", Short.TYPE);
        primitives.put("int", Integer.TYPE);
        primitives.put("long", Long.TYPE);
        primitives.put("float", Float.TYPE);
        primitives.put("double", Double.TYPE);
        primitives.put("boolean", Boolean.TYPE);
    }

    public JavaObject InitializeContext(BA ba) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        BA aba;
        Object shared = BA.class.getDeclaredField("sharedProcessBA").get(ba);
        WeakReference<BA> activityBA = (WeakReference) shared.getClass().getDeclaredField("activityBA").get(shared);
        if (!(activityBA == null || (aba = activityBA.get()) == null)) {
            ba = aba;
        }
        if (context == null) {
            context = BA.class.getDeclaredField("context");
        }
        setObject(context.get(ba));
        return this;
    }

    public JavaObject InitializeStatic(String ClassName) throws ClassNotFoundException {
        setObject(getCorrectClassName(ClassName));
        return this;
    }

    public JavaObject InitializeNewInstance(String ClassName, Object[] Params) throws Exception {
        Class<?> cls = getCorrectClassName(ClassName);
        if (Params == null || Params.length == 0) {
            setObject(cls.newInstance());
        } else {
            Constructor<?>[] constructors = cls.getConstructors();
            for (Constructor<?> c : constructors) {
                if (arrangeAndCheckMatch(c.getParameterTypes(), Params)) {
                    setObject(c.newInstance(Params));
                }
            }
            throw new RuntimeException("Constructor not found.");
        }
        return this;
    }

    public JavaObject InitializeArray(String ClassName, Object[] Values) throws ClassNotFoundException {
        Class<?> c = primitives.get(ClassName);
        if (!(c != null)) {
            c = getCorrectClassName(ClassName);
        }
        Object arr = Array.newInstance(c, Values.length);
        for (int i = 0; i < Values.length; i++) {
            Array.set(arr, i, Values[i]);
        }
        setObject(arr);
        return this;
    }

    public Object RunMethod(String MethodName, Object[] Params) throws Exception {
        Method method = null;
        Iterator<Method> it = methodCache.getMethod(getCurrentClass().getName(), MethodName, Params).iterator();
        while (true) {
            if (it.hasNext()) {
                Method m = it.next();
                if (arrangeAndCheckMatch(m.getParameterTypes(), Params)) {
                    method = m;
                    break;
                }
            } else {
                break;
            }
        }
        if (method != null) {
            return method.invoke(getObject(), Params);
        }
        throw new RuntimeException("Method: " + MethodName + " not matched.");
    }

    public JavaObject RunMethodJO(String MethodName, Object[] Params) throws Exception {
        return (JavaObject) AbsObjectWrapper.ConvertToWrapper(new JavaObject(), RunMethod(MethodName, Params));
    }

    private boolean arrangeAndCheckMatch(Class<?>[] clsArr, Object[] Params) {
        if (Params == null) {
            return clsArr.length == 0;
        }
        if (Params.length != clsArr.length) {
            return false;
        }
        int i = 0;
        while (i < Params.length) {
            if (Params[i] != null) {
                Class<?> p = Params[i].getClass();
                if (clsArr[i].isPrimitive()) {
                    clsArr[i] = primitiveToBoxed.get(clsArr[i]);
                }
                if (clsArr[i].isEnum() && p == String.class) {
                    Params[i] = Enum.valueOf(clsArr[i], (String) Params[i]);
                } else if (!clsArr[i].isAssignableFrom(p)) {
                    break;
                }
            }
            i++;
        }
        return i == Params.length;
    }

    public void SetField(String FieldName, Object Value) throws Exception {
        fieldCache.getField(getCurrentClass().getName(), FieldName).set(getObject(), Value);
    }

    public Object GetField(String Field) throws Exception {
        return fieldCache.getField(getCurrentClass().getName(), Field).get(getObject());
    }

    public JavaObject GetFieldJO(String Field) throws Exception {
        return (JavaObject) AbsObjectWrapper.ConvertToWrapper(new JavaObject(), GetField(Field));
    }

    public Object CreateEvent(BA ba, String Interface, String EventName, Object DefaultReturnValue) throws Exception {
        return createEvent(ba, Interface, EventName, false, DefaultReturnValue);
    }

    public Object CreateEventFromUI(BA ba, String Interface, String EventName, Object ReturnValue) throws Exception {
        return createEvent(ba, Interface, EventName, true, ReturnValue);
    }

    private Object createEvent(BA ba, String Interface, String EventName, boolean fromUi, Object returnValue) throws Exception {
        InvocationHandler handler = new InvocationHandler(EventName, fromUi, ba, getObject(), returnValue) {
            /* class anywheresoftware.b4j.object.JavaObject.AnonymousClass1 */
            String eventName;
            Thread t = Thread.currentThread();
            private final /* synthetic */ BA val$ba;
            private final /* synthetic */ boolean val$fromUi;
            private final /* synthetic */ Object val$obj;
            private final /* synthetic */ Object val$returnValue;

            {
                this.val$fromUi = r5;
                this.val$ba = r6;
                this.val$obj = r7;
                this.val$returnValue = r8;
                this.eventName = String.valueOf(r4.toLowerCase(BA.cul)) + "_event";
            }

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
                Object[] params = {arg1.getName(), arg2};
                if (Thread.currentThread() != this.t) {
                    this.val$ba.raiseEventFromDifferentThread(this.val$obj, null, 0, this.eventName, false, params);
                    return this.val$returnValue;
                } else if (!this.val$fromUi) {
                    Object ret = this.val$ba.raiseEvent(this.val$obj, this.eventName, params);
                    if (ret == null) {
                        return this.val$returnValue;
                    }
                    return ret;
                } else {
                    this.val$ba.raiseEventFromUI(this.val$obj, this.eventName, params);
                    return this.val$returnValue;
                }
            }
        };
        Class<?> inter = getCorrectClassName(Interface);
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{inter}, handler);
    }

    private Class<?> getCurrentClass() {
        if (getObject() instanceof Class) {
            return (Class) getObject();
        }
        return getObject().getClass();
    }

    private static Class<?> getCorrectClassName(String className) throws ClassNotFoundException {
        if (className.equals("Object")) {
            return Object.class;
        }
        if (className.equals("String")) {
            return String.class;
        }
        for (int i = 0; i < 3; i++) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException c) {
                int dot = className.lastIndexOf(".");
                if (dot != -1) {
                    className = String.valueOf(className.substring(0, dot)) + "$" + className.substring(dot + 1);
                } else if (i == 0) {
                    className = "java.lang." + className;
                } else {
                    throw c;
                }
            }
        }
        throw new ClassNotFoundException(className);
    }

    /* access modifiers changed from: package-private */
    public static class FieldCache {
        private ConcurrentHashMap<String, HashMap<String, Field>> cache = new ConcurrentHashMap<>();

        FieldCache() {
        }

        public Field getField(String className, String fieldName) throws Exception {
            HashMap<String, Field> classFields = this.cache.get(className);
            if (classFields == null) {
                classFields = new HashMap<>();
                Field[] fields = Class.forName(className).getFields();
                for (Field m : fields) {
                    classFields.put(m.getName(), m);
                }
                this.cache.put(className, classFields);
            }
            Field m2 = classFields.get(fieldName);
            if (m2 != null) {
                return m2;
            }
            throw new RuntimeException("Field: " + fieldName + " not found in: " + className);
        }
    }

    /* access modifiers changed from: package-private */
    public static class MethodCache {
        private static final HashMap<String, ArrayList<Method>> cantGetAllMethods = new HashMap<>();
        private ConcurrentHashMap<String, HashMap<String, ArrayList<Method>>> cache = new ConcurrentHashMap<>();

        MethodCache() {
        }

        public List<Method> getMethod(String className, String methodName, Object[] params) throws Exception {
            ArrayList<Method> arrayList;
            HashMap<String, ArrayList<Method>> classMethods = this.cache.get(className);
            if (classMethods == null) {
                classMethods = new HashMap<>();
                Class<?> cls = Class.forName(className);
                if ((cls.getModifiers() & 1) == 0) {
                    fillNonPublicB4JMethods(className, classMethods, cls);
                } else {
                    Method[] methods = null;
                    try {
                        methods = cls.getMethods();
                    } catch (Throwable th) {
                        BA.LogError("Cannot get methods of class: " + className + ", disabling cache.");
                        classMethods = cantGetAllMethods;
                    }
                    fillMethods(methods, classMethods);
                }
                this.cache.put(className, classMethods);
            }
            if (classMethods == cantGetAllMethods) {
                Class<?> cls2 = Class.forName(className);
                Class<?>[] paramTypes = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramTypes[i] = params[i] == null ? Object.class : params[i].getClass();
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= params.length) {
                        break;
                    }
                    try {
                        arrayList = Arrays.asList(cls2.getMethod(methodName, paramTypes));
                        break;
                    } catch (NoSuchMethodException e) {
                        Class<?> orig = paramTypes[i2];
                        Class<?> parent = paramTypes[i2].getSuperclass();
                        if (parent != null) {
                            paramTypes[i2] = parent;
                            try {
                                arrayList = Arrays.asList(cls2.getMethod(methodName, paramTypes));
                                break;
                            } catch (NoSuchMethodException e2) {
                                paramTypes[i2] = orig;
                            }
                        } else {
                            i2++;
                        }
                    }
                }
                return arrayList;
            }
            arrayList = classMethods.get(methodName);
            if (arrayList == null) {
                throw new RuntimeException("Method: " + methodName + " not found in: " + className);
            }
            return arrayList;
        }

        private void fillMethods(Method[] methods, HashMap<String, ArrayList<Method>> classMethods) {
            if (methods != null) {
                for (Method m : methods) {
                    ArrayList<Method> overloaded = classMethods.get(m.getName());
                    if (overloaded == null) {
                        overloaded = new ArrayList<>();
                        classMethods.put(m.getName(), overloaded);
                    }
                    overloaded.add(m);
                }
            }
        }

        private void fillNonPublicB4JMethods(String className, HashMap<String, ArrayList<Method>> classMethods, Class<?> cls) {
            for (Class<?> interf : cls.getInterfaces()) {
                fillMethods(interf.getMethods(), classMethods);
            }
            for (Class<?> cls2 = cls.getSuperclass(); cls2 != null; cls2 = cls2.getSuperclass()) {
                if ((cls2.getModifiers() & 1) != 0) {
                    fillMethods(cls2.getMethods(), classMethods);
                    return;
                }
            }
        }
    }
}
