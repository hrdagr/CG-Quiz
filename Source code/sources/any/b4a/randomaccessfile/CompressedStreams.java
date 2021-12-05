package anywheresoftware.b4a.randomaccessfile;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.streams.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

@BA.ShortName("CompressedStreams")
public class CompressedStreams {
    public File.InputStreamWrapper WrapInputStream(InputStream In, String CompressMethod) throws IOException {
        InputStream cin = getInputStream(CompressMethod, In);
        File.InputStreamWrapper i = new File.InputStreamWrapper();
        i.setObject(cin);
        return i;
    }

    public File.OutputStreamWrapper WrapOutputStream(OutputStream Out, String CompressMethod) throws IOException {
        OutputStream cout = getOutputStream(CompressMethod, Out);
        File.OutputStreamWrapper i = new File.OutputStreamWrapper();
        i.setObject(cout);
        return i;
    }

    public byte[] CompressBytes(byte[] Data, String CompressMethod) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream(Data.length);
        OutputStream outputStream = getOutputStream(CompressMethod, b);
        outputStream.write(Data);
        outputStream.close();
        return b.toByteArray();
    }

    public byte[] DecompressBytes(byte[] CompressedData, String CompressMethod) throws IOException {
        InputStream in = getInputStream(CompressMethod, new ByteArrayInputStream(CompressedData));
        ByteArrayOutputStream out = new ByteArrayOutputStream(CompressedData.length);
        File.Copy2(in, out);
        return out.toByteArray();
    }

    private InputStream getInputStream(String CompressMethod, InputStream In) throws IOException {
        String m = CompressMethod.toLowerCase(BA.cul);
        if (m.equals("gzip")) {
            return new GZIPInputStream(In);
        }
        if (m.equals("zlib")) {
            return new InflaterInputStream(In);
        }
        throw new RuntimeException("Unknown compression method: " + CompressMethod);
    }

    private OutputStream getOutputStream(String CompressMethod, OutputStream out) throws IOException {
        String m = CompressMethod.toLowerCase(BA.cul);
        if (m.equals("gzip")) {
            return new GZIPOutputStream(out);
        }
        if (m.equals("zlib")) {
            return new DeflaterOutputStream(out);
        }
        throw new RuntimeException("Unknown compression method: " + CompressMethod);
    }
}
