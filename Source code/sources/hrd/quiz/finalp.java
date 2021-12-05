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
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.objects.ViewWrapper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class finalp extends Activity implements B4AActivity {
    static boolean afterFirstLayout;
    public static boolean dontPause;
    public static final boolean fullScreen = false;
    public static final boolean includeTitle = false;
    static boolean isFirst = true;
    public static finalp mostCurrent;
    public static WeakReference<Activity> previousOne;
    public static BA processBA;
    private static boolean processGlobalsRun = false;
    public Common __c = null;
    ActivityWrapper _activity;
    public fpage _fpage = null;
    public ImageViewWrapper _imageview1 = null;
    public LabelWrapper _lblscore = null;
    public main _main = null;
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
            processBA = new BA(getApplicationContext(), null, null, "hrd.quiz", "hrd.quiz.finalp");
            processBA.loadHtSubs(getClass());
            BALayout.setDeviceScale(getApplicationContext().getResources().getDisplayMetrics().density);
        } else if (!(previousOne == null || (activity = previousOne.get()) == null || activity == this)) {
            BA.LogInfo("Killing previous instance (finalp).");
            activity.finish();
        }
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
        getWindow().requestFeature(1);
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
            if (finalp.afterFirstLayout || finalp.mostCurrent == null) {
                return;
            }
            if (finalp.mostCurrent.layout.getWidth() == 0) {
                BA.handler.postDelayed(this, 5);
                return;
            }
            finalp.mostCurrent.layout.getLayoutParams().height = finalp.mostCurrent.layout.getHeight();
            finalp.mostCurrent.layout.getLayoutParams().width = finalp.mostCurrent.layout.getWidth();
            finalp.afterFirstLayout = true;
            finalp.mostCurrent.afterFirstLayout();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void afterFirstLayout() {
        if (this == mostCurrent) {
            this.activityBA = new BA(this, this.layout, processBA, "hrd.quiz", "hrd.quiz.finalp");
            processBA.sharedProcessBA.activityBA = new WeakReference<>(this.activityBA);
            ViewWrapper.lastId = 0;
            this._activity = new ActivityWrapper(this.activityBA, "activity");
            Msgbox.isDismissing = false;
            if (BA.isShellModeRuntimeCheck(processBA)) {
                if (isFirst) {
                    processBA.raiseEvent2(null, true, "SHELL", false, new Object[0]);
                }
                processBA.raiseEvent2(null, true, "CREATE", true, "hrd.quiz.finalp", processBA, this.activityBA, this._activity, Float.valueOf(Common.Density), mostCurrent);
                this._activity.reinitializeForShell(this.activityBA, "activity");
            }
            initializeProcessGlobals();
            initializeGlobals();
            BA.LogInfo("** Activity (finalp) Create, isFirst = " + isFirst + " **");
            processBA.raiseEvent2(null, true, "activity_create", false, Boolean.valueOf(isFirst));
            isFirst = false;
            if (this == mostCurrent) {
                processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (finalp) Resume **");
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
            finalp.processBA.raiseEventFromUI(menuItem.getTitle(), this.eventName + "_click", new Object[0]);
            return true;
        }
    }

    public static Class<?> getObject() {
        return finalp.class;
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
            Boolean bool = (Boolean) finalp.processBA.raiseEvent2(finalp.this._activity, false, "activity_keypress", false, Integer.valueOf(i));
            if (bool == null || bool.booleanValue()) {
                return true;
            }
            if (i != 4) {
                return false;
            }
            finalp.this.finish();
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
                BA.LogInfo("** Activity (finalp) Pause, UserClosed = " + this.activityBA.activity.isFinishing() + " **");
            } else {
                BA.LogInfo("** Activity (finalp) Pause event (activity is not paused). **");
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
            finalp finalp = finalp.mostCurrent;
            if (finalp != null && finalp == this.activity.get()) {
                finalp.processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (finalp) Resume **");
                if (finalp == finalp.mostCurrent) {
                    finalp.processBA.raiseEvent(finalp._activity, "activity_resume", null);
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

    public static void initializeProcessGlobals() {
        try {
            Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals", new Class[0]).invoke(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String _activity_create(boolean z) throws Exception {
        mostCurrent._activity.LoadLayout("LFP", mostCurrent.activityBA);
        LabelWrapper labelWrapper = mostCurrent._lblscore;
        StringBuilder sb = new StringBuilder();
        mainquiz mainquiz = mostCurrent._mainquiz;
        labelWrapper.setText(BA.ObjectToCharSequence(sb.append(mainquiz._cursr).append("/10").toString()));
        return "";
    }

    public static String _activity_pause(boolean z) throws Exception {
        return "";
    }

    public static String _activity_resume() throws Exception {
        return "";
    }

    public static String _activity_touch(int i, float f, float f2) throws Exception {
        main main = mostCurrent._main;
        keyvaluestore keyvaluestore = main._kvs;
        mainquiz mainquiz = mostCurrent._mainquiz;
        keyvaluestore._putsimple("SC", mainquiz._cursr);
        BA ba = processBA;
        fpage fpage = mostCurrent._fpage;
        Common.StartActivity(ba, fpage.getObject());
        mostCurrent._activity.Finish();
        return "";
    }

    public static String _globals() throws Exception {
        mostCurrent._imageview1 = new ImageViewWrapper();
        mostCurrent._lblscore = new LabelWrapper();
        mostCurrent._panel1 = new PanelWrapper();
        return "";
    }

    public static String _imageview1_click() throws Exception {
        main main = mostCurrent._main;
        keyvaluestore keyvaluestore = main._kvs;
        mainquiz mainquiz = mostCurrent._mainquiz;
        keyvaluestore._putsimple("SC", mainquiz._cursr);
        BA ba = processBA;
        fpage fpage = mostCurrent._fpage;
        Common.StartActivity(ba, fpage.getObject());
        mostCurrent._activity.Finish();
        return "";
    }

    public static String _process_globals() throws Exception {
        return "";
    }
}
