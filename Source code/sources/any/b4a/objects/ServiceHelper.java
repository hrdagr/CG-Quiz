package anywheresoftware.b4a.objects;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.B4AApplication;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.keywords.DateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.Thread;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@BA.ActivityObject
public class ServiceHelper {
    public static final int AUTOMATIC_FOREGROUND_ALWAYS = 3;
    public static final int AUTOMATIC_FOREGROUND_NEVER = 1;
    public static final int AUTOMATIC_FOREGROUND_WHEN_NEEDED = 2;
    @BA.Hide
    public static final String AUTO_WAKE_ID = "b4a_wakelock";
    @BA.Hide
    public static final String FOREGROUND_KEY = "b4a_foreground";
    public int AutomaticForegroundMode = 2;
    public Notification AutomaticForegroundNotification;
    @BA.Hide
    public int autoNotificationId;
    NotificationManager mNM;
    private Service service;

    @BA.Hide
    public static void init() {
    }

    public ServiceHelper(Service service2) {
        this.service = service2;
        this.mNM = (NotificationManager) BA.applicationContext.getSystemService("notification");
    }

    public void StartForeground(int Id, Notification Notification) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.service.startForeground(Id, Notification);
    }

    public void StopForeground(int Id) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.service.stopForeground(true);
    }

    public void StopAutomaticForeground() {
        if (this.autoNotificationId > 0) {
            this.service.stopForeground(true);
            this.autoNotificationId = 0;
        }
    }

    @BA.Hide
    public static class StarterHelper {
        private static boolean alreadyRun;
        private static boolean insideHandler;
        private static BA serviceProcessBA;
        private static Runnable waitForLayouts;
        private static int wakeLockId;
        private static final HashMap<Integer, PowerManager.WakeLock> wakeLocks = new HashMap<>();

        public static void startServiceFromReceiver(Context context, Intent intent, boolean starterService, Class<?> baClass) {
            if (starterService) {
                BA.LogError("The Starter service should never be started from a receiver.");
            }
            if (!baClass.getName().equals("anywheresoftware.b4a.ShellBA") || BA.applicationContext != null) {
                boolean foreground = BA.isAnyActivityVisible();
                if (!foreground && context.getPackageManager().checkPermission("android.permission.WAKE_LOCK", BA.packageName) == 0) {
                    wakeLockId++;
                    PowerManager.WakeLock wl = ((PowerManager) context.getSystemService("power")).newWakeLock(1, String.valueOf(intent.getComponent()));
                    wl.setReferenceCounted(false);
                    wl.acquire(DateTime.TicksPerMinute);
                    wakeLocks.put(Integer.valueOf(wakeLockId), wl);
                    intent.putExtra(ServiceHelper.AUTO_WAKE_ID, wakeLockId);
                }
                if (Build.VERSION.SDK_INT < 26 || foreground) {
                    try {
                        context.startService(intent);
                    } catch (IllegalStateException i) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            intent.putExtra(ServiceHelper.FOREGROUND_KEY, true);
                            context.startForegroundService(intent);
                            return;
                        }
                        throw new RuntimeException(i);
                    }
                } else {
                    intent.putExtra(ServiceHelper.FOREGROUND_KEY, true);
                    context.startForegroundService(intent);
                }
            } else {
                BA.LogError("Cannot start from a receiver in debug mode.");
            }
        }

        public static IntentWrapper handleStartIntent(Intent intent, ServiceHelper sh, BA ba) {
            IntentWrapper iw = new IntentWrapper();
            boolean startForegroundServiceCalled = false;
            if (intent != null) {
                if (intent.getBooleanExtra(ServiceHelper.FOREGROUND_KEY, false)) {
                    startForegroundServiceCalled = true;
                }
                int wakeLockId2 = intent.getIntExtra(ServiceHelper.AUTO_WAKE_ID, 0);
                if (wakeLockId2 > 0) {
                    wakeLocks.remove(Integer.valueOf(wakeLockId2)).release();
                }
                if (intent.hasExtra("b4a_internal_intent")) {
                    iw.setObject((Intent) intent.getParcelableExtra("b4a_internal_intent"));
                } else {
                    iw.setObject(intent);
                }
            }
            if (startForegroundServiceCalled) {
                BA.LogInfo("Service started in foreground mode.");
            }
            if (sh.AutomaticForegroundMode != 1 && (sh.AutomaticForegroundMode == 3 || (sh.AutomaticForegroundMode == 2 && startForegroundServiceCalled))) {
                if (sh.AutomaticForegroundNotification == null) {
                    sh.AutomaticForegroundNotification = createAutoNotification(sh, ba);
                }
                sh.autoNotificationId = 51042;
                try {
                    sh.StartForeground(sh.autoNotificationId, sh.AutomaticForegroundNotification);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return iw;
        }

        private static Notification createAutoNotification(ServiceHelper sh, BA ba) {
            NotificationWrapper nw = new NotificationWrapper();
            nw.Initialize2(2);
            nw.setIcon("icon");
            try {
                B4AApplication b4AApplication = Common.Application;
                String labelName = B4AApplication.getLabelName();
                B4AApplication b4AApplication2 = Common.Application;
                nw.SetInfoNew(ba, labelName, B4AApplication.getLabelName(), Class.forName(String.valueOf(BA.packageName) + ".main"));
                return (Notification) nw.getObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean startFromActivity(Activity act, BA ba, Runnable waitForLayout, boolean noStarter) {
            if (alreadyRun || noStarter) {
                return true;
            }
            alreadyRun = true;
            addWaitForLayout(waitForLayout);
            try {
                Common.StartService(ba, "starter");
                return false;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean startFromServiceCreate(BA ba, boolean noStarter) {
            if (alreadyRun || noStarter) {
                return true;
            }
            alreadyRun = true;
            serviceProcessBA = ba;
            return false;
        }

        public static void runWaitForLayouts() {
            if (waitForLayouts != null) {
                BA.handler.post(waitForLayouts);
            }
        }

        public static void addWaitForLayout(Runnable r) {
            waitForLayouts = r;
        }

        public static void removeWaitForLayout() {
            waitForLayouts = null;
        }

        public static boolean onStartCommand(BA ba, Runnable handleStart) {
            if (ba == null || ba != serviceProcessBA) {
                if (!ba.isActivityPaused() || waitForLayouts == null) {
                    handleStart.run();
                } else {
                    BA.handler.postDelayed(handleStart, 500);
                }
                return true;
            }
            try {
                Common.StartService(ba, "starter");
                serviceProcessBA = null;
                return false;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        /* JADX INFO: finally extract failed */
        public static boolean handleUncaughtException(Throwable t, BA ba) throws Exception {
            if (insideHandler) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), t);
                return true;
            }
            try {
                insideHandler = true;
                if (!alreadyRun) {
                    insideHandler = false;
                    return false;
                } else if (!Common.SubExists(ba, "starter", "application_error")) {
                    insideHandler = false;
                    return false;
                } else if (Common.IsPaused(ba, "starter")) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), t);
                    insideHandler = false;
                    return true;
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintWriter pw = new PrintWriter(out);
                    t.printStackTrace(pw);
                    pw.close();
                    byte[] b = out.toByteArray();
                    B4AException exc = new B4AException();
                    if (t instanceof Exception) {
                        exc.setObject((Exception) t);
                    } else {
                        exc.setObject(new Exception(t));
                    }
                    if (Boolean.TRUE.equals((Boolean) Common.CallSubNew3(ba, "starter", "application_error", exc, Common.BytesToString(b, 0, b.length, "UTF8")))) {
                        Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
                        if (handler instanceof BA.B4AExceptionHandler) {
                            ((BA.B4AExceptionHandler) handler).original.uncaughtException(Thread.currentThread(), t);
                        } else {
                            handler.uncaughtException(Thread.currentThread(), t);
                        }
                    }
                    insideHandler = false;
                    return true;
                }
            } catch (Throwable th) {
                insideHandler = false;
                throw th;
            }
        }

        public static void callOSExceptionHandler(B4AException e) {
            Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
            if (handler instanceof BA.B4AExceptionHandler) {
                ((BA.B4AExceptionHandler) handler).original.uncaughtException(Thread.currentThread(), (Throwable) e.getObject());
            } else {
                handler.uncaughtException(Thread.currentThread(), (Throwable) e.getObject());
            }
        }
    }
}
