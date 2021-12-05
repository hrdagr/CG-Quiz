package hrd.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.B4AMenuItem;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.Msgbox;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.objects.ActivityWrapper;
import anywheresoftware.b4a.objects.B4XViewWrapper;
import anywheresoftware.b4a.objects.MediaPlayerWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.streams.File;
import anywheresoftware.b4a.sql.SQL;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class main extends Activity implements B4AActivity {
    public static int _curscore = 0;
    public static keyvaluestore _kvs = null;
    public static MediaPlayerWrapper _mediaplayer1 = null;
    public static int _score = 0;
    public static SQL _sql = null;
    public static B4XViewWrapper.XUI _xui = null;
    static boolean afterFirstLayout = false;
    public static boolean dontPause = false;
    public static final boolean fullScreen = true;
    public static final boolean includeTitle = false;
    static boolean isFirst = true;
    public static main mostCurrent;
    public static WeakReference<Activity> previousOne;
    public static BA processBA;
    private static boolean processGlobalsRun = false;
    public Common __c = null;
    ActivityWrapper _activity;
    public finalp _finalp = null;
    public fpage _fpage = null;
    public mainquiz _mainquiz = null;
    public PanelWrapper _panel1 = null;
    public starter _starter = null;
    BA activityBA;
    BALayout layout;
    ArrayList<B4AMenuItem> menuItems;
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;

    public void onCreate(Bundle bundle) {
        Activity activity;
        super.onCreate(bundle);
        mostCurrent = this;
        if (processBA == null) {
            processBA = new BA(getApplicationContext(), null, null, "hrd.quiz", "hrd.quiz.main");
            processBA.loadHtSubs(getClass());
            BALayout.setDeviceScale(getApplicationContext().getResources().getDisplayMetrics().density);
        } else if (!(previousOne == null || (activity = previousOne.get()) == null || activity == this)) {
            BA.LogInfo("Killing previous instance (main).");
            activity.finish();
        }
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
        getWindow().requestFeature(1);
        getWindow().setFlags(1024, 1024);
        processBA.sharedProcessBA.activityBA = null;
        this.layout = new BALayout(this);
        setContentView(this.layout);
        afterFirstLayout = false;
        WaitForLayout waitForLayout = new WaitForLayout();
        if (ServiceHelper.StarterHelper.startFromActivity(this, processBA, waitForLayout, false)) {
            BA.handler.postDelayed(waitForLayout, 5);
        }
    }

    static class WaitForLayout implements Runnable {
        WaitForLayout() {
        }

        public void run() {
            if (main.afterFirstLayout || main.mostCurrent == null) {
                return;
            }
            if (main.mostCurrent.layout.getWidth() == 0) {
                BA.handler.postDelayed(this, 5);
                return;
            }
            main.mostCurrent.layout.getLayoutParams().height = main.mostCurrent.layout.getHeight();
            main.mostCurrent.layout.getLayoutParams().width = main.mostCurrent.layout.getWidth();
            main.afterFirstLayout = true;
            main.mostCurrent.afterFirstLayout();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void afterFirstLayout() {
        if (this == mostCurrent) {
            this.activityBA = new BA(this, this.layout, processBA, "hrd.quiz", "hrd.quiz.main");
            processBA.sharedProcessBA.activityBA = new WeakReference<>(this.activityBA);
            ViewWrapper.lastId = 0;
            this._activity = new ActivityWrapper(this.activityBA, "activity");
            Msgbox.isDismissing = false;
            if (BA.isShellModeRuntimeCheck(processBA)) {
                if (isFirst) {
                    processBA.raiseEvent2(null, true, "SHELL", false, new Object[0]);
                }
                processBA.raiseEvent2(null, true, "CREATE", true, "hrd.quiz.main", processBA, this.activityBA, this._activity, Float.valueOf(Common.Density), mostCurrent);
                this._activity.reinitializeForShell(this.activityBA, "activity");
            }
            initializeProcessGlobals();
            initializeGlobals();
            BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
            processBA.raiseEvent2(null, true, "activity_create", false, Boolean.valueOf(isFirst));
            isFirst = false;
            if (this == mostCurrent) {
                processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (main) Resume **");
                processBA.raiseEvent(null, "activity_resume", new Object[0]);
                if (Build.VERSION.SDK_INT >= 11) {
                    try {
                        Activity.class.getMethod("invalidateOptionsMenu", new Class[0]).invoke(this, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override // anywheresoftware.b4a.B4AActivity
    public void addMenuItem(B4AMenuItem b4AMenuItem) {
        if (this.menuItems == null) {
            this.menuItems = new ArrayList<>();
        }
        this.menuItems.add(b4AMenuItem);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", Boolean.TYPE).invoke(getClass().getMethod("getActionBar", new Class[0]).invoke(this, new Object[0]), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[]{menu})) {
            return true;
        }
        if (this.menuItems == null) {
            return false;
        }
        Iterator<B4AMenuItem> it = this.menuItems.iterator();
        while (it.hasNext()) {
            B4AMenuItem next = it.next();
            MenuItem add = menu.add(next.title);
            if (next.drawable != null) {
                add.setIcon(next.drawable);
            }
            if (Build.VERSION.SDK_INT >= 11) {
                try {
                    if (next.addToBar) {
                        MenuItem.class.getMethod("setShowAsAction", Integer.TYPE).invoke(add, 1);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            add.setOnMenuItemClickListener(new B4AMenuItemsClickListener(next.eventName.toLowerCase(BA.cul)));
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        processBA.raiseEvent(null, "activity_actionbarhomeclick", new Object[0]);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        processBA.runHook("onprepareoptionsmenu", this, new Object[]{menu});
        return true;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        processBA.runHook("onstart", this, null);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        processBA.runHook("onstop", this, null);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (processBA.subExists("activity_windowfocuschanged")) {
            processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, Boolean.valueOf(z));
        }
    }

    private class B4AMenuItemsClickListener implements MenuItem.OnMenuItemClickListener {
        private final String eventName;

        public B4AMenuItemsClickListener(String str) {
            this.eventName = str;
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            main.processBA.raiseEventFromUI(menuItem.getTitle(), this.eventName + "_click", new Object[0]);
            return true;
        }
    }

    public static Class<?> getObject() {
        return main.class;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (processBA.runHook("onkeydown", this, new Object[]{Integer.valueOf(i), keyEvent})) {
            return true;
        }
        if (this.onKeySubExist == null) {
            this.onKeySubExist = Boolean.valueOf(processBA.subExists("activity_keypress"));
        }
        if (this.onKeySubExist.booleanValue()) {
            if (i == 4 && Build.VERSION.SDK_INT >= 18) {
                HandleKeyDelayed handleKeyDelayed = new HandleKeyDelayed();
                handleKeyDelayed.kc = i;
                BA.handler.post(handleKeyDelayed);
                return true;
            } else if (new HandleKeyDelayed().runDirectly(i)) {
                return true;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private class HandleKeyDelayed implements Runnable {
        int kc;

        private HandleKeyDelayed() {
        }

        public void run() {
            runDirectly(this.kc);
        }

        public boolean runDirectly(int i) {
            Boolean bool = (Boolean) main.processBA.raiseEvent2(main.this._activity, false, "activity_keypress", false, Integer.valueOf(i));
            if (bool == null || bool.booleanValue()) {
                return true;
            }
            if (i != 4) {
                return false;
            }
            main.this.finish();
            return true;
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (processBA.runHook("onkeyup", this, new Object[]{Integer.valueOf(i), keyEvent})) {
            return true;
        }
        if (this.onKeyUpSubExist == null) {
            this.onKeyUpSubExist = Boolean.valueOf(processBA.subExists("activity_keyup"));
        }
        if (this.onKeyUpSubExist.booleanValue()) {
            Boolean bool = (Boolean) processBA.raiseEvent2(this._activity, false, "activity_keyup", false, Integer.valueOf(i));
            if (bool == null || bool.booleanValue()) {
                return true;
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[]{intent});
    }

    public void onPause() {
        super.onPause();
        if (this._activity != null && this == mostCurrent) {
            Msgbox.dismiss(true);
            if (!dontPause) {
                BA.LogInfo("** Activity (main) Pause, UserClosed = " + this.activityBA.activity.isFinishing() + " **");
            } else {
                BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
            }
            if (mostCurrent != null) {
                processBA.raiseEvent2(this._activity, true, "activity_pause", false, Boolean.valueOf(this.activityBA.activity.isFinishing()));
            }
            if (!dontPause) {
                processBA.setActivityPaused(true);
                mostCurrent = null;
            }
            if (!this.activityBA.activity.isFinishing()) {
                previousOne = new WeakReference<>(this);
            }
            Msgbox.isDismissing = false;
            processBA.runHook("onpause", this, null);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        previousOne = null;
        processBA.runHook("ondestroy", this, null);
    }

    public void onResume() {
        super.onResume();
        mostCurrent = this;
        Msgbox.isDismissing = false;
        if (this.activityBA != null) {
            BA.handler.post(new ResumeMessage(mostCurrent));
        }
        processBA.runHook("onresume", this, null);
    }

    private static class ResumeMessage implements Runnable {
        private final WeakReference<Activity> activity;

        public ResumeMessage(Activity activity2) {
            this.activity = new WeakReference<>(activity2);
        }

        public void run() {
            main main = main.mostCurrent;
            if (main != null && main == this.activity.get()) {
                main.processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (main) Resume **");
                if (main == main.mostCurrent) {
                    main.processBA.raiseEvent(main._activity, "activity_resume", null);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        processBA.onActivityResult(i, i2, intent);
        processBA.runHook("onactivityresult", this, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }

    private static void initializeGlobals() {
        processBA.raiseEvent2(null, true, "globals", false, null);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Object[] objArr = new Object[2];
            objArr[0] = strArr[i2];
            objArr[1] = Boolean.valueOf(iArr[i2] == 0);
            processBA.raiseEventFromDifferentThread(null, null, 0, "activity_permissionresult", true, objArr);
        }
    }

    public static boolean isAnyActivityVisible() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if (mostCurrent != null) {
            z = true;
        } else {
            z = false;
        }
        boolean z5 = false | z;
        if (fpage.mostCurrent != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        boolean z6 = z5 | z2;
        if (mainquiz.mostCurrent != null) {
            z3 = true;
        } else {
            z3 = false;
        }
        boolean z7 = z3 | z6;
        if (finalp.mostCurrent == null) {
            z4 = false;
        }
        return z7 | z4;
    }

    public static void _activity_create(boolean z) throws Exception {
        new ResumableSub_Activity_Create(null, z).resume(processBA, null);
    }

    public static class ResumableSub_Activity_Create extends BA.ResumableSub {
        boolean _firsttime;
        main parent;

        public ResumableSub_Activity_Create(main main, boolean z) {
            this.parent = main;
            this._firsttime = z;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = 1;
                        main main = this.parent;
                        main.mostCurrent._activity.LoadLayout("LM1", main.mostCurrent.activityBA);
                        break;
                    case 1:
                        this.state = 4;
                        if (!this._firsttime) {
                            break;
                        } else {
                            this.state = 3;
                            break;
                        }
                    case 3:
                        this.state = 4;
                        main main2 = this.parent;
                        keyvaluestore keyvaluestore = main._kvs;
                        BA ba2 = main.processBA;
                        File file = Common.File;
                        keyvaluestore._initialize(ba2, File.getDirInternal(), "CQ");
                        break;
                    case 4:
                        this.state = 7;
                        File file2 = Common.File;
                        File file3 = Common.File;
                        if (File.Exists(File.getDirInternal(), "Que.db")) {
                            break;
                        } else {
                            this.state = 6;
                            break;
                        }
                    case 6:
                        this.state = 7;
                        File file4 = Common.File;
                        File file5 = Common.File;
                        String dirAssets = File.getDirAssets();
                        File file6 = Common.File;
                        File.Copy(dirAssets, "Que.db", File.getDirInternal(), "Que.db");
                        break;
                    case 7:
                        this.state = 10;
                        main main3 = this.parent;
                        if (main._sql.IsInitialized()) {
                            break;
                        } else {
                            this.state = 9;
                            break;
                        }
                    case 9:
                        this.state = 10;
                        main main4 = this.parent;
                        SQL sql = main._sql;
                        File file7 = Common.File;
                        sql.Initialize(File.getDirInternal(), "Que.db", true);
                        break;
                    case 10:
                        this.state = 13;
                        if (!this._firsttime) {
                            break;
                        } else {
                            this.state = 12;
                            break;
                        }
                    case 12:
                        this.state = 13;
                        main main5 = this.parent;
                        main._mediaplayer1.Initialize();
                        main main6 = this.parent;
                        MediaPlayerWrapper mediaPlayerWrapper = main._mediaplayer1;
                        File file8 = Common.File;
                        mediaPlayerWrapper.Load(File.getDirAssets(), "arpa.mp3");
                        break;
                    case 13:
                        this.state = -1;
                        main main7 = this.parent;
                        main._mediaplayer1.Play();
                        Common.Sleep(main.mostCurrent.activityBA, this, 5000);
                        this.state = 14;
                        return;
                    case 14:
                        this.state = -1;
                        BA ba3 = main.processBA;
                        main main8 = this.parent;
                        fpage fpage = main.mostCurrent._fpage;
                        Common.StartActivity(ba3, fpage.getObject());
                        main main9 = this.parent;
                        main.mostCurrent._activity.Finish();
                        break;
                }
            }
        }
    }

    public static String _activity_pause(boolean z) throws Exception {
        return "";
    }

    public static String _activity_resume() throws Exception {
        return "";
    }

    public static String _activity_touch(int i, float f, float f2) throws Exception {
        BA ba = processBA;
        fpage fpage = mostCurrent._fpage;
        Common.StartActivity(ba, fpage.getObject());
        return "";
    }

    public static String _button1_click() throws Exception {
        return "";
    }

    public static String _globals() throws Exception {
        mostCurrent._panel1 = new PanelWrapper();
        return "";
    }

    public static void initializeProcessGlobals() {
        if (!processGlobalsRun) {
            processGlobalsRun = true;
            try {
                _process_globals();
                fpage._process_globals();
                mainquiz._process_globals();
                finalp._process_globals();
                starter._process_globals();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String _process_globals() throws Exception {
        _xui = new B4XViewWrapper.XUI();
        _mediaplayer1 = new MediaPlayerWrapper();
        _score = 0;
        _curscore = 0;
        _kvs = new keyvaluestore();
        _sql = new SQL();
        return "";
    }
}
