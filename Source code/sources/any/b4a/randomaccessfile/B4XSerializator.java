package anywheresoftware.b4a.randomaccessfile;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.collections.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@BA.ShortName("B4XSerializator")
public class B4XSerializator {
    private static final byte T_BOOLEAN = 7;
    private static final byte T_BYTE = 10;
    private static final byte T_CHAR = 14;
    private static final byte T_DOUBLE = 6;
    private static final byte T_FLOAT = 5;
    private static final byte T_INT = 3;
    private static final byte T_LIST = 21;
    private static final byte T_LONG = 4;
    private static final byte T_MAP = 20;
    private static final byte T_NSARRAY = 22;
    private static final byte T_NSDATA = 23;
    private static final byte T_NULL = 0;
    private static final byte T_SHORT = 2;
    private static final byte T_STRING = 1;
    private static final byte T_TYPE = 24;
    private ByteBuffer bb = ByteBuffer.wrap(new byte[8]);
    private DataInputStream in;
    private OutputStream out;
    private Object tag;

    public B4XSerializator() {
        this.bb.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void setTag(Object tag2) {
        this.tag = tag2;
    }

    public Object getTag() {
        return this.tag;
    }

    public byte[] ConvertObjectToBytes(Object Object) throws IOException {
        return WriteObject(Object);
    }

    public void ConvertObjectToBytesAsync(BA ba, final Object Object, String EventName) {
        BA.runAsync(ba, this, String.valueOf(EventName.toLowerCase(BA.cul)) + "_objecttobytes", new Object[]{false, new byte[0]}, new Callable<Object[]>() {
            /* class anywheresoftware.b4a.randomaccessfile.B4XSerializator.AnonymousClass1 */

            @Override // java.util.concurrent.Callable
            public Object[] call() throws Exception {
                return new Object[]{true, B4XSerializator.this.ConvertObjectToBytes(Object)};
            }
        });
    }

    public Object ConvertBytesToObject(byte[] Bytes) throws IOException {
        return ReadObject(Bytes);
    }

    public void ConvertBytesToObjectAsync(BA ba, final byte[] Bytes, String EventName) {
        Object[] objArr = new Object[2];
        objArr[0] = false;
        BA.runAsync(ba, this, String.valueOf(EventName.toLowerCase(BA.cul)) + "_bytestoobject", objArr, new Callable<Object[]>() {
            /* class anywheresoftware.b4a.randomaccessfile.B4XSerializator.AnonymousClass2 */

            @Override // java.util.concurrent.Callable
            public Object[] call() throws Exception {
                return new Object[]{true, B4XSerializator.this.ConvertBytesToObject(Bytes)};
            }
        });
    }

    @BA.Hide
    public byte[] WriteObject(Object Object) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        this.out = new DeflaterOutputStream(bout);
        writeObject(Object);
        this.out.close();
        return bout.toByteArray();
    }

    @BA.Hide
    public Object ReadObject(byte[] arr) throws IOException {
        this.in = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(arr)));
        Object ret = readObject();
        this.in.close();
        return ret;
    }

    private void writeInt(int i) throws IOException {
        this.bb.putInt(0, i);
        this.out.write(this.bb.array(), 0, 4);
    }

    private void writeShort(short s) throws IOException {
        this.bb.putShort(0, s);
        this.out.write(this.bb.array(), 0, 2);
    }

    private int readInt() throws IOException {
        this.in.readFully(this.bb.array(), 0, 4);
        return this.bb.getInt(0);
    }

    private short readShort() throws IOException {
        this.in.readFully(this.bb.array(), 0, 2);
        return this.bb.getShort(0);
    }

    private byte readByte() throws IOException {
        return this.in.readByte();
    }

    private void writeByte(byte b) throws IOException {
        this.out.write(b);
    }

    private void writeObject(Object o) throws IOException {
        int i = 1;
        if (o instanceof ObjectWrapper) {
            o = ((ObjectWrapper) o).getObject();
        }
        if (o == null) {
            writeByte(T_NULL);
        } else if (o instanceof Number) {
            if (o instanceof Integer) {
                writeByte((byte) 3);
                writeInt(((Integer) o).intValue());
            } else if (o instanceof Double) {
                writeByte((byte) 6);
                this.bb.putDouble(0, ((Double) o).doubleValue());
                this.out.write(this.bb.array(), 0, 8);
            } else if (o instanceof Float) {
                writeByte((byte) 5);
                this.bb.putFloat(0, ((Float) o).floatValue());
                this.out.write(this.bb.array(), 0, 4);
            } else if (o instanceof Long) {
                writeByte((byte) 4);
                this.bb.putLong(0, ((Long) o).longValue());
                this.out.write(this.bb.array(), 0, 8);
            } else if (o instanceof Byte) {
                writeByte(T_BYTE);
                writeByte(((Byte) o).byteValue());
            } else if (o instanceof Short) {
                writeByte((byte) 2);
                writeShort(((Short) o).shortValue());
            }
        } else if (o instanceof Character) {
            writeByte(T_CHAR);
            this.bb.putChar(0, ((Character) o).charValue());
            this.out.write(this.bb.array(), 0, 2);
        } else if (o instanceof Boolean) {
            writeByte((byte) 7);
            if (!((Boolean) o).booleanValue()) {
                i = 0;
            }
            writeByte((byte) i);
        } else if (o instanceof String) {
            byte[] temp = ((String) o).getBytes("UTF8");
            writeByte((byte) 1);
            writeInt(temp.length);
            this.out.write(temp);
        } else if (o instanceof List) {
            writeByte(T_LIST);
            writeList((List) o);
        } else if (o instanceof Map) {
            writeByte(T_MAP);
            writeMap((Map) o);
        } else if (!o.getClass().isArray()) {
            writeByte(T_TYPE);
            writeType(o);
        } else if (o.getClass().getComponentType() == Byte.TYPE) {
            writeByte(T_NSDATA);
            byte[] b = (byte[]) o;
            writeInt(b.length);
            this.out.write(b);
        } else if (o.getClass().getComponentType().isPrimitive()) {
            throw new RuntimeException("This method does not support arrays of primitives.");
        } else {
            writeByte(T_NSARRAY);
            writeList(Arrays.asList((Object[]) o));
        }
    }

    private Map<?, ?> readMap() throws IOException {
        int len = readInt();
        Map.MyMap mm = new Map.MyMap();
        for (int i = 0; i < len; i++) {
            mm.put(readObject(), readObject());
        }
        return mm;
    }

    private void writeMap(java.util.Map<?, ?> m) throws IOException {
        writeInt(m.size());
        for (Map.Entry<?, ?> e : m.entrySet()) {
            writeObject(e.getKey());
            writeObject(e.getValue());
        }
    }

    private ArrayList<?> readList() throws IOException {
        int len = readInt();
        ArrayList<Object> arr = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            arr.add(readObject());
        }
        return arr;
    }

    private void writeList(List<?> list) throws IOException {
        writeInt(list.size());
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            writeObject(it.next());
        }
    }

    private void writeType(Object target) throws IOException {
        Field[] fields = RandomAccessFile.isB4XType(target);
        if (fields == null) {
            throw new RuntimeException("Cannot serialize object: " + String.valueOf(target));
        }
        try {
            writeObject(target.getClass().getName());
            Map.MyMap map = new Map.MyMap();
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(target));
            }
            writeMap(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object readType() {
        try {
            Class<?> c = RandomAccessFile.readTypeClass((String) readObject());
            Map.MyMap map = (Map.MyMap) readMap();
            Object o = c.newInstance();
            Field[] declaredFields = c.getDeclaredFields();
            for (Field f : declaredFields) {
                Object val = map.get(f.getName());
                if (val == null) {
                    val = map.get("_" + f.getName());
                }
                if (val != null) {
                    f.setAccessible(true);
                    if (f.getType() == anywheresoftware.b4a.objects.collections.List.class) {
                        val = AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), val);
                    } else if (f.getType() == anywheresoftware.b4a.objects.collections.Map.class) {
                        val = AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), val);
                    } else if (f.getType() == Boolean.TYPE && !(val instanceof Boolean)) {
                        val = Boolean.valueOf(((Number) val).intValue() == 1);
                    }
                    f.set(o, val);
                }
            }
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object readObject() throws IOException {
        byte t = readByte();
        switch (t) {
            case 0:
                return null;
            case 1:
                byte[] b = new byte[readInt()];
                this.in.readFully(b);
                return new String(b, "UTF8");
            case 2:
                return Short.valueOf(readShort());
            case 3:
                return Integer.valueOf(readInt());
            case 4:
                this.in.readFully(this.bb.array(), 0, 8);
                return Long.valueOf(this.bb.getLong(0));
            case 5:
                this.in.readFully(this.bb.array(), 0, 4);
                return Float.valueOf(this.bb.getFloat(0));
            case 6:
                this.in.readFully(this.bb.array(), 0, 8);
                return Double.valueOf(this.bb.getDouble(0));
            case 7:
                return readByte() == 1;
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                throw new RuntimeException("Unsupported type: " + ((int) t));
            case 10:
                return Byte.valueOf(readByte());
            case 14:
                this.in.readFully(this.bb.array(), 0, 2);
                return Character.valueOf(this.bb.getChar(0));
            case 20:
                return readMap();
            case 21:
                return readList();
            case 22:
                return readList().toArray();
            case 23:
                byte[] b2 = new byte[readInt()];
                this.in.readFully(b2);
                return b2;
            case 24:
                return readType();
        }
    }
}
