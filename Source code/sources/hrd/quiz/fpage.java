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
import anywheresoftware.b4a.keywords.constants.KeyCodes;
import anywheresoftware.b4a.objects.ActivityWrapper;
import anywheresoftware.b4a.objects.ButtonWrapper;
import anywheresoftware.b4a.objects.EditTextWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.objects.ViewWrapper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class fpage extends Activity implements B4AActivity {
    public static int _tp = 0;
    static boolean afterFirstLayout;
    public static boolean dontPause;
    public static final boolean fullScreen = false;
    public static final boolean includeTitle = false;
    static boolean isFirst = true;
    public static fpage mostCurrent;
    public static WeakReference<Activity> previousOne;
    public static BA processBA;
    private static boolean processGlobalsRun = false;
    public Common __c = null;
    ActivityWrapper _activity;
    public ButtonWrapper _cmdabt = null;
    public ButtonWrapper _cmds = null;
    public finalp _finalp = null;
    public ImageViewWrapper _imageview1 = null;
    public LabelWrapper _lblscore = null;
    public main _main = null;
    public mainquiz _mainquiz = null;
    public PanelWrapper _panel1 = null;
    public PanelWrapper _panel2 = null;
    public PanelWrapper _panel3 = null;
    public starter _starter = null;
    public EditTextWrapper _txtname = null;
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
            processBA = new BA(getApplicationContext(), null, null, "hrd.quiz", "hrd.quiz.fpage");
            processBA.loadHtSubs(getClass());
            BALayout.setDeviceScale(getApplicationContext().getResources().getDisplayMetrics().density);
        } else if (!(previousOne == null || (activity = previousOne.get()) == null || activity == this)) {
            BA.LogInfo("Killing previous instance (fpage).");
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
            if (fpage.afterFirstLayout || fpage.mostCurrent == null) {
                return;
            }
            if (fpage.mostCurrent.layout.getWidth() == 0) {
                BA.handler.postDelayed(this, 5);
                return;
            }
            fpage.mostCurrent.layout.getLayoutParams().height = fpage.mostCurrent.layout.getHeight();
            fpage.mostCurrent.layout.getLayoutParams().width = fpage.mostCurrent.layout.getWidth();
            fpage.afterFirstLayout = true;
            fpage.mostCurrent.afterFirstLayout();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void afterFirstLayout() {
        if (this == mostCurrent) {
            this.activityBA = new BA(this, this.layout, processBA, "hrd.quiz", "hrd.quiz.fpage");
            processBA.sharedProcessBA.activityBA = new WeakReference<>(this.activityBA);
            ViewWrapper.lastId = 0;
            this._activity = new ActivityWrapper(this.activityBA, "activity");
            Msgbox.isDismissing = false;
            if (BA.isShellModeRuntimeCheck(processBA)) {
                if (isFirst) {
                    processBA.raiseEvent2(null, true, "SHELL", false, new Object[0]);
                }
                processBA.raiseEvent2(null, true, "CREATE", true, "hrd.quiz.fpage", processBA, this.activityBA, this._activity, Float.valueOf(Common.Density), mostCurrent);
                this._activity.reinitializeForShell(this.activityBA, "activity");
            }
            initializeProcessGlobals();
            initializeGlobals();
            BA.LogInfo("** Activity (fpage) Create, isFirst = " + isFirst + " **");
            processBA.raiseEvent2(null, true, "activity_create", false, Boolean.valueOf(isFirst));
            isFirst = false;
            if (this == mostCurrent) {
                processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (fpage) Resume **");
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
            fpage.processBA.raiseEventFromUI(menuItem.getTitle(), this.eventName + "_click", new Object[0]);
            return true;
        }
    }

    public static Class<?> getObject() {
        return fpage.class;
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
            Boolean bool = (Boolean) fpage.processBA.raiseEvent2(fpage.this._activity, false, "activity_keypress", false, Integer.valueOf(i));
            if (bool == null || bool.booleanValue()) {
                return true;
            }
            if (i != 4) {
                return false;
            }
            fpage.this.finish();
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
                BA.LogInfo("** Activity (fpage) Pause, UserClosed = " + this.activityBA.activity.isFinishing() + " **");
            } else {
                BA.LogInfo("** Activity (fpage) Pause event (activity is not paused). **");
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
            fpage fpage = fpage.mostCurrent;
            if (fpage != null && fpage == this.activity.get()) {
                fpage.processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (fpage) Resume **");
                if (fpage == fpage.mostCurrent) {
                    fpage.processBA.raiseEvent(fpage._activity, "activity_resume", null);
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
        mostCurrent._activity.LoadLayout("ls", mostCurrent.activityBA);
        mostCurrent._cmds.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._cmds.getWidth()) / 2.0d)));
        mostCurrent._cmdabt.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._cmdabt.getWidth()) / 2.0d)));
        mostCurrent._lblscore.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._lblscore.getWidth()) / 2.0d)));
        mostCurrent._panel2.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._panel2.getWidth()) / 2.0d)));
        main main = mostCurrent._main;
        if (main._kvs._getsimple("SC").equals("")) {
            mostCurrent._lblscore.setText(BA.ObjectToCharSequence("-/10"));
        } else {
            LabelWrapper labelWrapper = mostCurrent._lblscore;
            StringBuilder sb = new StringBuilder();
            main main2 = mostCurrent._main;
            labelWrapper.setText(BA.ObjectToCharSequence(sb.append(main._kvs._getsimple("SC")).append("/10").toString()));
        }
        main main3 = mostCurrent._main;
        if (main._kvs._getsimple("NM").equals("")) {
            return "";
        }
        EditTextWrapper editTextWrapper = mostCurrent._txtname;
        main main4 = mostCurrent._main;
        editTextWrapper.setText(BA.ObjectToCharSequence(main._kvs._getsimple("NM")));
        return "";
    }

    public static boolean _activity_keypress(int i) throws Exception {
        if (_tp == 1) {
            KeyCodes keyCodes = Common.KeyCodes;
            if (i == 4) {
                _tp = 0;
                mostCurrent._panel3.setVisible(false);
                mostCurrent._panel3.SendToBack();
            }
        }
        return false;
    }

    public static String _activity_pause(boolean z) throws Exception {
        return "";
    }

    public static String _activity_resume() throws Exception {
        main main = mostCurrent._main;
        if (main._kvs._getsimple("SC").equals("")) {
            mostCurrent._lblscore.setText(BA.ObjectToCharSequence("-/10"));
            return "";
        }
        LabelWrapper labelWrapper = mostCurrent._lblscore;
        StringBuilder sb = new StringBuilder();
        main main2 = mostCurrent._main;
        labelWrapper.setText(BA.ObjectToCharSequence(sb.append(main._kvs._getsimple("SC")).append("/10").toString()));
        return "";
    }

    public static String _activity_touch(int i, float f, float f2) throws Exception {
        if (_tp != 1) {
            return "";
        }
        _tp = 0;
        mostCurrent._panel3.setVisible(false);
        mostCurrent._panel3.SendToBack();
        return "";
    }

    public static String _cmdabt_click() throws Exception {
        _tp = 1;
        mostCurrent._panel3.setVisible(true);
        mostCurrent._panel3.BringToFront();
        return "";
    }

    public static void _cmds_click() throws Exception {
        new ResumableSub_CmdS_Click(null).resume(processBA, null);
    }

    public static class ResumableSub_CmdS_Click extends BA.ResumableSub {
        fpage parent;

        public ResumableSub_CmdS_Click(fpage fpage) {
            this.parent = fpage;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = -1;
                        Common.Sleep(fpage.mostCurrent.activityBA, this, 100);
                        this.state = 1;
                        return;
                    case 1:
                        this.state = -1;
                        fpage fpage = this.parent;
                        main main = fpage.mostCurrent._main;
                        keyvaluestore keyvaluestore = main._kvs;
                        fpage fpage2 = this.parent;
                        keyvaluestore._putsimple("NM", fpage.mostCurrent._txtname.getText());
                        BA ba2 = fpage.processBA;
                        fpage fpage3 = this.parent;
                        mainquiz mainquiz = fpage.mostCurrent._mainquiz;
                        Common.StartActivity(ba2, mainquiz.getObject());
                        break;
                }
            }
        }
    }

    public static String _globals() throws Exception {
        mostCurrent._panel1 = new PanelWrapper();
        mostCurrent._panel2 = new PanelWrapper();
        mostCurrent._lblscore = new LabelWrapper();
        mostCurrent._cmds = new ButtonWrapper();
        mostCurrent._cmdabt = new ButtonWrapper();
        mostCurrent._txtname = new EditTextWrapper();
        _tp = 0;
        mostCurrent._panel3 = new PanelWrapper();
        mostCurrent._imageview1 = new ImageViewWrapper();
        return "";
    }

    public static String _panel3_touch(int i, float f, float f2) throws Exception {
        if (_tp != 1) {
            return "";
        }
        _tp = 0;
        mostCurrent._panel3.setVisible(false);
        mostCurrent._panel3.SendToBack();
        return "";
    }

    public static String _process_globals() throws Exception {
        return "";
    }
}
