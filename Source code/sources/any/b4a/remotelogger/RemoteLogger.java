package anywheresoftware.b4a.remotelogger;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.ConnectorUtils;
import anywheresoftware.b4a.remotelogger.Connector;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class RemoteLogger implements Connector.MessageHandler {
    private static final byte LOGCAT_DATA = 8;
    private static final byte START_LOGCAT = 6;
    private static final byte STOP_LOGCAT = 7;
    private Connector connector = new Connector(this, calcPort());
    private Process lc;
    private Thread logcatReader;
    private volatile InputStream logcatStream;
    private volatile boolean logcatWorking;

    public RemoteLogger() {
        if (BA.bridgeLog == null) {
            BA.bridgeLog = new BA.IBridgeLog() {
                /* class anywheresoftware.b4a.remotelogger.RemoteLogger.AnonymousClass1 */

                @Override // anywheresoftware.b4a.BA.IBridgeLog
                public void offer(String msg) {
                }
            };
        }
    }

    public void Start() {
        Thread t = new Thread(this.connector);
        t.setDaemon(true);
        t.start();
    }

    private int calcPort() {
        int hash = 7;
        try {
            for (byte b : BA.packageName.getBytes("UTF8")) {
                hash = (hash * 31) + (b & 255);
            }
            return (Math.abs(hash) % 63500) + 1500;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // anywheresoftware.b4a.remotelogger.Connector.MessageHandler
    public void handleIncomingData(int firstByte, InputStream in) {
        DataInputStream din = new DataInputStream(in);
        if (firstByte == 6) {
            try {
                LogCatStart(ConnectorUtils.readString(din).split(","));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (firstByte == 7) {
            LogCatStop();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void logcatData(byte[] buffer, int length) {
        ConnectorUtils.startMessage((byte) 8).put(buffer, 0, length);
        ConnectorUtils.sendMessage(this.connector);
    }

    private void LogCatStart(String[] Args) throws InterruptedException, IOException {
        LogCatStop();
        if (this.logcatReader != null) {
            int wait = 10;
            while (this.logcatReader.isAlive()) {
                int wait2 = wait - 1;
                if (wait <= 0) {
                    break;
                }
                Thread.sleep(50);
                this.logcatReader.interrupt();
                wait = wait2;
            }
        }
        final String[] a = new String[(Args.length + 1)];
        a[0] = "/system/bin/logcat";
        System.arraycopy(Args, 0, a, 1, Args.length);
        this.logcatReader = new Thread(new Runnable() {
            /* class anywheresoftware.b4a.remotelogger.RemoteLogger.AnonymousClass2 */

            public void run() {
                try {
                    RemoteLogger.this.lc = Runtime.getRuntime().exec(a);
                    RemoteLogger.this.logcatStream = RemoteLogger.this.lc.getInputStream();
                    InputStream in = RemoteLogger.this.logcatStream;
                    RemoteLogger.this.logcatWorking = true;
                    byte[] buffer = new byte[8192];
                    while (true) {
                        if (!RemoteLogger.this.logcatWorking) {
                            break;
                        }
                        int count = in.read(buffer);
                        Thread.sleep(20);
                        while (count > 0 && in.available() > 0 && count < buffer.length) {
                            count += in.read(buffer, count, buffer.length - count);
                            Thread.sleep(20);
                        }
                        if (count == -1) {
                            RemoteLogger.this.logcatWorking = false;
                            break;
                        }
                        RemoteLogger.this.logcatData(buffer, count);
                    }
                    RemoteLogger.this.lc.destroy();
                } catch (Exception e) {
                    System.out.println("Log reader error: " + e.toString());
                    if (RemoteLogger.this.lc != null) {
                        RemoteLogger.this.lc.destroy();
                    }
                }
            }
        });
        this.logcatReader.setDaemon(true);
        this.logcatReader.start();
    }

    public void LogCatStop() throws IOException {
        this.logcatWorking = false;
        if (this.logcatStream != null) {
            this.logcatStream.close();
        }
        if (this.lc != null) {
            this.lc.destroy();
        }
    }
}
