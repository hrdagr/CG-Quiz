package anywheresoftware.b4a.randomaccessfile;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.streams.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.Adler32;

@BA.ShortName("AsyncStreams")
public class AsyncStreams {
    static final byte[] CLOSE_PILL = new byte[0];
    static final int STREAM_PREFIX = -2;
    public String StreamFolder;
    private AIN ain;
    private AOUT aout;
    private BA ba;
    private String eventName;
    volatile long streamReceived;
    volatile long streamTotal;
    private Thread tin;
    private Thread tout;

    public void Initialize(BA ba2, InputStream In, OutputStream Out, String EventName) throws IOException {
        shared(ba2, In, Out, EventName, false, false);
    }

    public void InitializePrefix(BA ba2, InputStream In, boolean BigEndian, OutputStream Out, String EventName) throws IOException {
        if (File.getExternalWritable()) {
            this.StreamFolder = File.getDirDefaultExternal();
        } else {
            this.StreamFolder = File.getDirInternalCache();
        }
        shared(ba2, In, Out, EventName, BigEndian, true);
    }

    public long getStreamTotal() {
        return this.streamTotal;
    }

    public long getStreamReceived() {
        return this.streamReceived;
    }

    private void shared(BA ba2, InputStream In, OutputStream Out, String EventName, boolean BigEndian, boolean Prefix) throws IOException {
        if (IsInitialized()) {
            Close();
        }
        this.ba = ba2;
        this.eventName = EventName.toLowerCase(BA.cul);
        if (In != null) {
            this.ain = new AIN(In, BigEndian, Prefix);
            this.tin = new Thread(this.ain);
            this.tin.setDaemon(true);
            this.tin.start();
        }
        if (Out != null) {
            this.aout = new AOUT(Out, BigEndian, Prefix);
            this.tout = new Thread(this.aout);
            this.tout.setDaemon(true);
            this.tout.start();
        }
    }

    public boolean IsInitialized() {
        return (this.ain == null && this.aout == null) ? false : true;
    }

    public boolean Write(byte[] Buffer) {
        return Write2(Buffer, 0, Buffer.length);
    }

    public boolean Write2(byte[] Buffer, int Start, int Length) {
        AOUT a = this.aout;
        if (a == null) {
            return false;
        }
        return a.put(Buffer, Start, Length);
    }

    public boolean SendAllAndClose() {
        return Write2(CLOSE_PILL, 0, 0);
    }

    public boolean WriteStream(InputStream In, Long Size) {
        AOUT a = this.aout;
        if (a == null) {
            return false;
        }
        return a.put(In, Size.longValue());
    }

    public int getOutputQueueSize() {
        if (this.aout == null) {
            return 0;
        }
        return this.aout.queue.size();
    }

    public synchronized void Close() throws IOException {
        if (!(this.tin == null || this.ain == null)) {
            this.ain.close();
            if (Thread.currentThread() != this.tin) {
                this.tin.interrupt();
            }
        }
        if (!(this.tout == null || this.aout == null)) {
            this.aout.close();
            if (Thread.currentThread() != this.tout) {
                this.tout.interrupt();
            }
        }
        this.ain = null;
        this.aout = null;
    }

    /* access modifiers changed from: private */
    public class AIN implements Runnable {
        private ByteBuffer bb;
        private byte[] buffer = new byte[8192];
        private String ev;
        private final InputStream in;
        private final boolean prefix;
        private final byte[] prefixBuffer = new byte[4];
        private volatile boolean working = true;

        public AIN(InputStream in2, boolean bigEndian, boolean prefix2) {
            this.ev = String.valueOf(AsyncStreams.this.eventName) + "_newdata";
            this.in = in2;
            this.prefix = prefix2;
            if (prefix2) {
                this.bb = ByteBuffer.wrap(new byte[8]);
                this.bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            }
        }

        /* JADX INFO: finally extract failed */
        public void run() {
            byte[] data;
            while (this.working) {
                try {
                    if (!this.prefix) {
                        int count = this.in.read(this.buffer);
                        if (count == 0) {
                            continue;
                        } else if (count < 0) {
                            closeUnexpected();
                            return;
                        } else if (this.working) {
                            data = new byte[count];
                            System.arraycopy(this.buffer, 0, data, 0, count);
                        } else {
                            return;
                        }
                    } else if (readNumberOfBytes(this.in, this.prefixBuffer, 4) && this.working) {
                        this.bb.clear();
                        this.bb.put(this.prefixBuffer, 0, 4);
                        int msgLength = this.bb.getInt(0);
                        if (msgLength > 100000000) {
                            throw new RuntimeException("Message size too large. Prefix mode can only work if both sides of the connection follow the 'prefix' protocol.");
                        } else if (msgLength != -2) {
                            if (msgLength > this.buffer.length) {
                                this.buffer = new byte[msgLength];
                            }
                            if (readNumberOfBytes(this.in, this.buffer, msgLength)) {
                                data = new byte[msgLength];
                                System.arraycopy(this.buffer, 0, data, 0, data.length);
                            } else {
                                return;
                            }
                        } else if (readNumberOfBytes(this.in, this.buffer, 8)) {
                            this.bb.clear();
                            this.bb.put(this.buffer, 0, 8);
                            AsyncStreams.this.streamTotal = this.bb.getLong(0);
                            AsyncStreams.this.streamReceived = 0;
                            int i = 1;
                            while (File.Exists(AsyncStreams.this.StreamFolder, String.valueOf(i))) {
                                i++;
                            }
                            OutputStream out = (OutputStream) File.OpenOutput(AsyncStreams.this.StreamFolder, String.valueOf(i), false).getObject();
                            try {
                                Adler32 adler = new Adler32();
                                while (AsyncStreams.this.streamReceived < AsyncStreams.this.streamTotal) {
                                    long remain = AsyncStreams.this.streamTotal - AsyncStreams.this.streamReceived;
                                    if (remain > ((long) this.buffer.length)) {
                                        remain = (long) this.buffer.length;
                                    }
                                    int len = (int) remain;
                                    if (!readNumberOfBytes(this.in, this.buffer, len)) {
                                        break;
                                    }
                                    adler.update(this.buffer, 0, len);
                                    out.write(this.buffer, 0, len);
                                    AsyncStreams.this.streamReceived += (long) len;
                                }
                                if (!readNumberOfBytes(this.in, this.buffer, 8)) {
                                    out.close();
                                    return;
                                }
                                this.bb.clear();
                                this.bb.put(this.buffer, 0, 8);
                                if (this.bb.getLong(0) != adler.getValue()) {
                                    throw new Exception("CRC value does not match.");
                                }
                                out.close();
                                AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, String.valueOf(AsyncStreams.this.eventName) + "_newstream", true, new Object[]{AsyncStreams.this.StreamFolder, String.valueOf(i)});
                            } catch (Throwable th) {
                                out.close();
                                throw th;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, this.ev, true, new Object[]{data});
                } catch (Exception e) {
                    if (this.working) {
                        e.printStackTrace();
                        AsyncStreams.this.ba.setLastException(e);
                        AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, String.valueOf(AsyncStreams.this.eventName) + "_error", false, null);
                        return;
                    }
                    return;
                }
            }
        }

        private boolean readNumberOfBytes(InputStream in2, byte[] buffer2, int len) throws IOException {
            int count = 0;
            while (count < len) {
                int c = in2.read(buffer2, count, len - count);
                if (c == -1) {
                    closeUnexpected();
                    return false;
                }
                count += c;
            }
            return true;
        }

        private void closeUnexpected() throws IOException {
            AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, String.valueOf(AsyncStreams.this.eventName) + "_terminated", false, null);
            AsyncStreams.this.Close();
        }

        public void close() {
            this.working = false;
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public class AOUT implements Runnable {
        private final ByteBuffer bb;
        private final OutputStream out;
        private final boolean prefix;
        private final ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(100);
        private byte[] streamBuffer;
        private volatile boolean working = true;

        public AOUT(OutputStream out2, boolean bigEndian, boolean prefix2) {
            this.out = out2;
            this.prefix = prefix2;
            if (prefix2) {
                this.bb = ByteBuffer.wrap(new byte[8]);
                this.bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return;
            }
            this.bb = null;
        }

        public void run() {
            while (this.working) {
                try {
                    Object b = this.queue.take();
                    if (!(b instanceof byte[])) {
                        StreamAndSize st = (StreamAndSize) b;
                        try {
                            Adler32 adler = new Adler32();
                            synchronized (this.bb) {
                                this.bb.putInt(0, -2);
                                this.out.write(this.bb.array(), 0, 4);
                                this.bb.putLong(0, st.size);
                                this.out.write(this.bb.array(), 0, 8);
                            }
                            if (this.streamBuffer == null) {
                                this.streamBuffer = new byte[8192];
                            }
                            while (true) {
                                int len = st.in.read(this.streamBuffer);
                                if (len <= 0) {
                                    break;
                                }
                                this.out.write(this.streamBuffer, 0, len);
                                adler.update(this.streamBuffer, 0, len);
                            }
                            synchronized (this.bb) {
                                this.bb.putLong(0, adler.getValue());
                                this.out.write(this.bb.array(), 0, 8);
                            }
                            try {
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        } finally {
                            try {
                                st.in.close();
                            } catch (Exception ee2) {
                                ee2.printStackTrace();
                            }
                        }
                    } else if (b == AsyncStreams.CLOSE_PILL) {
                        AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, String.valueOf(AsyncStreams.this.eventName) + "_terminated", false, null);
                        AsyncStreams.this.Close();
                        return;
                    } else {
                        this.out.write((byte[]) b);
                    }
                } catch (Exception e) {
                    if (this.working) {
                        e.printStackTrace();
                        AsyncStreams.this.ba.setLastException(e);
                        AsyncStreams.this.ba.raiseEventFromDifferentThread(AsyncStreams.this, null, 0, String.valueOf(AsyncStreams.this.eventName) + "_error", false, null);
                    }
                }
            }
        }

        public boolean put(InputStream in, long size) {
            if (!this.prefix) {
                throw new RuntimeException("WriteStream is only supported in prefix mode.");
            }
            try {
                StreamAndSize st = new StreamAndSize();
                st.in = in;
                st.size = size;
                return this.queue.offer(st, 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean put(byte[] buffer, int start, int len) {
            byte[] b;
            if (buffer == AsyncStreams.CLOSE_PILL) {
                b = AsyncStreams.CLOSE_PILL;
            } else if (!this.prefix) {
                b = new byte[len];
                System.arraycopy(buffer, start, b, 0, len);
            } else {
                b = new byte[(len + 4)];
                synchronized (this.bb) {
                    this.bb.putInt(0, len);
                    System.arraycopy(this.bb.array(), 0, b, 0, 4);
                }
                System.arraycopy(buffer, start, b, 4, len);
            }
            try {
                return this.queue.offer(b, 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() {
            this.working = false;
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static class StreamAndSize {
        InputStream in;
        long size;

        StreamAndSize() {
        }
    }
}
