package anywheresoftware.b4a.remotelogger;

import android.util.Log;
import anywheresoftware.b4a.ConnectorConsumer;
import anywheresoftware.b4a.ConnectorUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Connector implements Runnable, ConnectorConsumer {
    public static final int ALTERNATE_PORT_DELTA = 517;
    public static final int PING = 1;
    private MessageHandler handler;
    private AtomicReference<Long> lastReadtime = new AtomicReference<>(0L);
    private final int port;
    public volatile boolean working = true;
    private volatile Writer writer;
    BlockingQueue<byte[]> writerQ = new ArrayBlockingQueue(20);

    public interface MessageHandler {
        void handleIncomingData(int i, InputStream inputStream);
    }

    public Connector(MessageHandler handler2, int port2) {
        this.handler = handler2;
        this.port = port2;
    }

    public synchronized void UpdateMessageHandler(MessageHandler mh) {
        this.handler = mh;
    }

    @Override // anywheresoftware.b4a.ConnectorConsumer
    public boolean shouldAddPrefix() {
        return true;
    }

    @Override // anywheresoftware.b4a.ConnectorConsumer
    public void putTask(byte[] data) {
        if (this.writerQ != null && !this.writerQ.offer(data)) {
            Log.w("", "clearing writerQ   ");
            this.writerQ.clear();
            this.writerQ.add(data);
        }
    }

    public void run() {
        mainLoop();
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x017f  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0001 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0001 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00e8 A[SYNTHETIC, Splitter:B:45:0x00e8] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ed A[Catch:{ Exception -> 0x010d }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0126 A[SYNTHETIC, Splitter:B:67:0x0126] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x012b A[Catch:{ Exception -> 0x014b }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0144  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0155  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0161 A[SYNTHETIC, Splitter:B:89:0x0161] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0166 A[Catch:{ Exception -> 0x0185 }] */
    private void mainLoop() {
        ServerSocket server;
        Throwable th;
        Exception e;
        int waitingFor = 0;
        while (this.working) {
            Socket socket = null;
            Thread writerT = null;
            try {
                System.out.println("Starting remote logger. Port: " + this.port);
                server = new ServerSocket(this.port, 1);
            } catch (BindException e2) {
                System.out.println("switching to alternate port: " + (this.port + ALTERNATE_PORT_DELTA));
                server = new ServerSocket(this.port + ALTERNATE_PORT_DELTA, 1);
            }
            try {
                server.setSoTimeout(60000);
                socket = server.accept();
                OutputStream out = socket.getOutputStream();
                socket.setSoTimeout(10000);
                InputStream in = socket.getInputStream();
                System.out.println("After accept");
                waitingFor = 0;
                this.writer = new Writer(out);
                this.writerQ.clear();
                Thread writerT2 = new Thread(this.writer);
                try {
                    writerT2.setDaemon(true);
                    writerT2.start();
                    readData(in);
                    if (this.writer != null) {
                        this.writer.writerWorking = false;
                        if (writerT2 != null) {
                            writerT2.interrupt();
                        }
                    }
                    if (server != null) {
                        try {
                            server.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (socket != null) {
                        if (!socket.isInputShutdown()) {
                            socket.shutdownInput();
                        }
                        if (!socket.isOutputShutdown()) {
                            socket.shutdownOutput();
                        }
                        socket.close();
                    }
                    if (this.working) {
                        sleep(500);
                    }
                } catch (SocketTimeoutException e4) {
                    writerT = writerT2;
                    waitingFor++;
                    try {
                        Log.w("", "Remote logger timeout: " + waitingFor);
                        if (this.writer != null) {
                        }
                        if (server != null) {
                        }
                        if (socket != null) {
                        }
                        if (this.working) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (this.writer != null) {
                            this.writer.writerWorking = false;
                            if (writerT != null) {
                                writerT.interrupt();
                            }
                        }
                        if (server != null) {
                            try {
                                server.close();
                            } catch (Exception e5) {
                                e5.printStackTrace();
                                if (this.working) {
                                }
                                throw th;
                            }
                        }
                        if (socket != null) {
                            if (!socket.isInputShutdown()) {
                                socket.shutdownInput();
                            }
                            if (!socket.isOutputShutdown()) {
                                socket.shutdownOutput();
                            }
                            socket.close();
                        }
                        if (this.working) {
                            sleep(500);
                        }
                        throw th;
                    }
                } catch (Exception e6) {
                    e = e6;
                    writerT = writerT2;
                    e.printStackTrace();
                    if (this.writer != null) {
                    }
                    if (server != null) {
                    }
                    if (socket != null) {
                    }
                    if (this.working) {
                    }
                } catch (Throwable th3) {
                    th = th3;
                    writerT = writerT2;
                    if (this.writer != null) {
                    }
                    if (server != null) {
                    }
                    if (socket != null) {
                    }
                    if (this.working) {
                    }
                    throw th;
                }
            } catch (SocketTimeoutException e7) {
                waitingFor++;
                Log.w("", "Remote logger timeout: " + waitingFor);
                if (this.writer != null) {
                    this.writer.writerWorking = false;
                    if (writerT != null) {
                        writerT.interrupt();
                    }
                }
                if (server != null) {
                    try {
                        server.close();
                    } catch (Exception e8) {
                        e8.printStackTrace();
                        if (this.working) {
                            sleep(500);
                        }
                    }
                }
                if (socket != null) {
                    if (!socket.isInputShutdown()) {
                        socket.shutdownInput();
                    }
                    if (!socket.isOutputShutdown()) {
                        socket.shutdownOutput();
                    }
                    socket.close();
                }
                if (this.working) {
                }
            } catch (Exception e9) {
                e = e9;
                e.printStackTrace();
                if (this.writer != null) {
                    this.writer.writerWorking = false;
                    if (writerT != null) {
                        writerT.interrupt();
                    }
                }
                if (server != null) {
                    try {
                        server.close();
                    } catch (Exception e10) {
                        e10.printStackTrace();
                        if (this.working) {
                            sleep(500);
                        }
                    }
                }
                if (socket != null) {
                    if (!socket.isInputShutdown()) {
                        socket.shutdownInput();
                    }
                    if (!socket.isOutputShutdown()) {
                        socket.shutdownOutput();
                    }
                    socket.close();
                }
                if (this.working) {
                }
            }
        }
    }

    private void readData(InputStream in) throws IOException {
        while (this.working) {
            int b = in.read();
            this.lastReadtime.set(Long.valueOf(System.currentTimeMillis()));
            if (b == 1) {
                ConnectorUtils.startMessage((byte) 1);
                ConnectorUtils.sendMessage(this);
            }
            if (b == -1) {
                Log.w("", "-1 received");
                return;
            }
            if (b > 0) {
                this.handler.handleIncomingData(b, in);
            }
            if (this.writer == null) {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void sleep(int millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public class Writer implements Runnable {
        private OutputStream out;
        public volatile boolean writerWorking = true;

        public Writer(OutputStream out2) {
            this.out = out2;
        }

        public void run() {
            while (Connector.this.working && this.writerWorking) {
                try {
                    this.out.write(Connector.this.writerQ.take());
                } catch (Exception e) {
                    System.err.println("writer error");
                    e.printStackTrace();
                }
            }
            Connector.this.writer = null;
        }
    }
}
