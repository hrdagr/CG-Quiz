package anywheresoftware.b4a.randomaccessfile;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.streams.File;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

@BA.Hide
public class CountingStreams {

    @BA.ShortName("CountingInputStream")
    public static class CountingInput extends File.InputStreamWrapper {
        public void Initialize(InputStream InputStream) {
            setObject(new MyInputStream(InputStream));
        }

        @Override // anywheresoftware.b4a.objects.streams.File.InputStreamWrapper
        @BA.Hide
        public void InitializeFromBytesArray(byte[] Buffer, int StartOffset, int MaxCount) {
        }

        public long getCount() {
            return ((MyInputStream) getObject()).counter.get();
        }

        public void setCount(long v) {
            ((MyInputStream) getObject()).counter.set(v);
        }
    }

    private static class MyInputStream extends FilterInputStream {
        AtomicLong counter = new AtomicLong(0);

        protected MyInputStream(InputStream in) {
            super(in);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int r = this.in.read();
            if (r >= 0) {
                this.counter.addAndGet(1);
            }
            return r;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buffer) throws IOException {
            return read(buffer, 0, buffer.length);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buffer, int offset, int count) throws IOException {
            int r = this.in.read(buffer, offset, count);
            if (r > 0) {
                this.counter.addAndGet((long) r);
            }
            return r;
        }
    }

    @BA.ShortName("CountingOutputStream")
    public static class CountingOutput extends File.OutputStreamWrapper {
        public void Initialize(OutputStream OutputStream) {
            setObject(new MyOutputStream(OutputStream));
        }

        @Override // anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper
        @BA.Hide
        public void InitializeToBytesArray(int StartSize) {
        }

        public long getCount() {
            return ((MyOutputStream) getObject()).counter.get();
        }

        public void setCount(long v) {
            ((MyOutputStream) getObject()).counter.set(v);
        }
    }

    private static class MyOutputStream extends FilterOutputStream {
        AtomicLong counter = new AtomicLong(0);

        public MyOutputStream(OutputStream out) {
            super(out);
        }

        @Override // java.io.OutputStream, java.io.FilterOutputStream
        public void write(byte[] buffer) throws IOException {
            write(buffer, 0, buffer.length);
        }

        @Override // java.io.OutputStream, java.io.FilterOutputStream
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.out.write(buffer, offset, count);
            this.counter.addAndGet((long) count);
        }

        @Override // java.io.OutputStream, java.io.FilterOutputStream
        public void write(int oneByte) throws IOException {
            this.out.write(oneByte);
            this.counter.addAndGet(1);
        }
    }
}
