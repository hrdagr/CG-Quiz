package hrd.quiz;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.objects.B4AException;
import anywheresoftware.b4a.objects.IntentWrapper;
import anywheresoftware.b4a.objects.ServiceHelper;
import java.lang.reflect.Method;

public class starter extends Service {
    static starter mostCurrent;
    public static BA processBA;
    public Common __c = null;
    public finalp _finalp = null;
    public fpage _fpage = null;
    public main _main = null;
    public mainquiz _mainquiz = null;
    private ServiceHelper _service;

    public static class starter_BR extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
            Intent intent2 = new Intent(context, starter.class);
            if (intent != null) {
                intent2.putExtra("b4a_internal_intent", intent);
            }
            ServiceHelper.StarterHelper.startServiceFromReceiver(context, intent2, true, BA.class);
        }
    }

    public static Class<?> getObject() {
        return starter.class;
    }

    public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
            processBA = new BA(this, null, null, "hrd.quiz", "hrd.quiz.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false, new Object[0]);
            }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals", new Class[0]).invoke(null, null);
                processBA.loadHtSubs(getClass());
                ServiceHelper.init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this._service = new ServiceHelper(this);
        processBA.service = this;
        if (BA.isShellModeRuntimeCheck(processBA)) {
            processBA.raiseEvent2(null, true, "CREATE", true, "hrd.quiz.starter", processBA, this._service, Float.valueOf(Common.Density));
        }
        processBA.setActivityPaused(false);
        BA.LogInfo("*** Service (starter) Create ***");
        processBA.raiseEvent(null, "service_create", new Object[0]);
        processBA.runHook("oncreate", this, null);
        ServiceHelper.StarterHelper.runWaitForLayouts();
    }

    public void onStart(Intent intent, int i) {
        onStartCommand(intent, 0, 0);
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        if (!ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            /* class hrd.quiz.starter.AnonymousClass1 */

            public void run() {
                starter.this.handleStart(intent);
            }
        })) {
            ServiceHelper.StarterHelper.addWaitForLayout(new Runnable() {
                /* class hrd.quiz.starter.AnonymousClass2 */

                public void run() {
                    starter.processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    starter.processBA.raiseEvent(null, "service_create", new Object[0]);
                    starter.this.handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
                }
            });
        }
        processBA.runHook("onstartcommand", this, new Object[]{intent, Integer.valueOf(i), Integer.valueOf(i2)});
        return 2;
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        processBA.raiseEvent(null, "service_taskremoved", new Object[0]);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleStart(Intent intent) {
        BA.LogInfo("** Service (starter) Start **");
        Method method = processBA.htSubs.get("service_start");
        if (method == null) {
            return;
        }
        if (method.getParameterTypes().length > 0) {
            IntentWrapper handleStartIntent = ServiceHelper.StarterHelper.handleStartIntent(intent, this._service, processBA);
            processBA.raiseEvent(null, "service_start", handleStartIntent);
            return;
        }
        processBA.raiseEvent(null, "service_start", new Object[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        BA.LogInfo("** Service (starter) Destroy (ignored)**");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean _application_error(B4AException b4AException, String str) throws Exception {
        return true;
    }

    public static String _process_globals() throws Exception {
        return "";
    }

    public static String _service_create() throws Exception {
        return "";
    }

    public static String _service_destroy() throws Exception {
        return "";
    }

    public static String _service_start(IntentWrapper intentWrapper) throws Exception {
        mostCurrent._service.StopAutomaticForeground();
        return "";
    }

    public static String _service_taskremoved() throws Exception {
        return "";
    }
}
