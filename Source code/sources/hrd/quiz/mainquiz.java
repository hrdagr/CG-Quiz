package hrd.quiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.B4AMenuItem;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.Msgbox;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.keywords.constants.Colors;
import anywheresoftware.b4a.keywords.constants.KeyCodes;
import anywheresoftware.b4a.objects.ActivityWrapper;
import anywheresoftware.b4a.objects.ButtonWrapper;
import anywheresoftware.b4a.objects.ConcreteViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.drawable.ColorDrawable;
import anywheresoftware.b4a.sql.SQL;
import anywheresoftware.b4j.object.JavaObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class mainquiz extends Activity implements B4AActivity {
    public static String _curans = "";
    public static String _curdb = "";
    public static String _cursr = "";
    public static int _ix = 0;
    public static int[] _num = null;
    public static int _state = 0;
    static boolean afterFirstLayout;
    public static boolean dontPause;
    public static final boolean fullScreen = false;
    public static final boolean includeTitle = false;
    static boolean isFirst = true;
    public static mainquiz mostCurrent;
    public static WeakReference<Activity> previousOne;
    public static BA processBA;
    private static boolean processGlobalsRun = false;
    public Common __c = null;
    ActivityWrapper _activity;
    public ButtonWrapper _button3 = null;
    public ColorDrawable _cd1 = null;
    public ButtonWrapper _cmda = null;
    public ButtonWrapper _cmdb = null;
    public ButtonWrapper _cmdc = null;
    public ButtonWrapper _cmdd = null;
    public finalp _finalp = null;
    public fpage _fpage = null;
    public ImageViewWrapper _imageview1 = null;
    public LabelWrapper _lbllv = null;
    public LabelWrapper _lblque = null;
    public LabelWrapper _lblscore = null;
    public main _main = null;
    public PanelWrapper _panel1 = null;
    public PanelWrapper _panel2 = null;
    public PanelWrapper _panel3 = null;
    public PanelWrapper _panel4 = null;
    public PanelWrapper _panel5 = null;
    public PanelWrapper _panel6 = null;
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
            processBA = new BA(getApplicationContext(), null, null, "hrd.quiz", "hrd.quiz.mainquiz");
            processBA.loadHtSubs(getClass());
            BALayout.setDeviceScale(getApplicationContext().getResources().getDisplayMetrics().density);
        } else if (!(previousOne == null || (activity = previousOne.get()) == null || activity == this)) {
            BA.LogInfo("Killing previous instance (mainquiz).");
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
            if (mainquiz.afterFirstLayout || mainquiz.mostCurrent == null) {
                return;
            }
            if (mainquiz.mostCurrent.layout.getWidth() == 0) {
                BA.handler.postDelayed(this, 5);
                return;
            }
            mainquiz.mostCurrent.layout.getLayoutParams().height = mainquiz.mostCurrent.layout.getHeight();
            mainquiz.mostCurrent.layout.getLayoutParams().width = mainquiz.mostCurrent.layout.getWidth();
            mainquiz.afterFirstLayout = true;
            mainquiz.mostCurrent.afterFirstLayout();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void afterFirstLayout() {
        if (this == mostCurrent) {
            this.activityBA = new BA(this, this.layout, processBA, "hrd.quiz", "hrd.quiz.mainquiz");
            processBA.sharedProcessBA.activityBA = new WeakReference<>(this.activityBA);
            ViewWrapper.lastId = 0;
            this._activity = new ActivityWrapper(this.activityBA, "activity");
            Msgbox.isDismissing = false;
            if (BA.isShellModeRuntimeCheck(processBA)) {
                if (isFirst) {
                    processBA.raiseEvent2(null, true, "SHELL", false, new Object[0]);
                }
                processBA.raiseEvent2(null, true, "CREATE", true, "hrd.quiz.mainquiz", processBA, this.activityBA, this._activity, Float.valueOf(Common.Density), mostCurrent);
                this._activity.reinitializeForShell(this.activityBA, "activity");
            }
            initializeProcessGlobals();
            initializeGlobals();
            BA.LogInfo("** Activity (mainquiz) Create, isFirst = " + isFirst + " **");
            processBA.raiseEvent2(null, true, "activity_create", false, Boolean.valueOf(isFirst));
            isFirst = false;
            if (this == mostCurrent) {
                processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (mainquiz) Resume **");
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
            mainquiz.processBA.raiseEventFromUI(menuItem.getTitle(), this.eventName + "_click", new Object[0]);
            return true;
        }
    }

    public static Class<?> getObject() {
        return mainquiz.class;
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
            Boolean bool = (Boolean) mainquiz.processBA.raiseEvent2(mainquiz.this._activity, false, "activity_keypress", false, Integer.valueOf(i));
            if (bool == null || bool.booleanValue()) {
                return true;
            }
            if (i != 4) {
                return false;
            }
            mainquiz.this.finish();
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
                BA.LogInfo("** Activity (mainquiz) Pause, UserClosed = " + this.activityBA.activity.isFinishing() + " **");
            } else {
                BA.LogInfo("** Activity (mainquiz) Pause event (activity is not paused). **");
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
            mainquiz mainquiz = mainquiz.mostCurrent;
            if (mainquiz != null && mainquiz == this.activity.get()) {
                mainquiz.processBA.setActivityPaused(false);
                BA.LogInfo("** Activity (mainquiz) Resume **");
                if (mainquiz == mainquiz.mostCurrent) {
                    mainquiz.processBA.raiseEvent(mainquiz._activity, "activity_resume", null);
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
        mostCurrent._activity.LoadLayout("LMQ", mostCurrent.activityBA);
        mostCurrent._panel3.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._panel3.getWidth()) / 2.0d)));
        mostCurrent._panel4.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._panel4.getWidth()) / 2.0d)));
        mostCurrent._panel5.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._panel5.getWidth()) / 2.0d)));
        mostCurrent._panel6.setLeft((int) ((((double) Common.GetDeviceLayoutValues(mostCurrent.activityBA).Width) / 2.0d) - (((double) mostCurrent._panel6.getWidth()) / 2.0d)));
        _numgen();
        _cursr = BA.NumberToString(0);
        _qgen("EQ", _num[_ix]);
        return "";
    }

    public static String _activity_pause(boolean z) throws Exception {
        return "";
    }

    public static String _activity_resume() throws Exception {
        return "";
    }

    public static String _activity_touch(int i, float f, float f2) throws Exception {
        if (_state != 1) {
            return "";
        }
        _state = 0;
        _nextque(1);
        return "";
    }

    public static String _checkans(String str) throws Exception {
        _dissall();
        _state = 1;
        if (str.equals("A")) {
            if (mostCurrent._cmda.getText().equals(_curans)) {
                ButtonWrapper buttonWrapper = mostCurrent._cmda;
                Colors colors = Common.Colors;
                buttonWrapper.setColor(-16711936);
                Common.ToastMessageShow(BA.ObjectToCharSequence("Correct Answer"), false);
                _cursr = BA.NumberToString(Double.parseDouble(_cursr) + 1.0d);
                return "";
            }
            ButtonWrapper buttonWrapper2 = mostCurrent._cmda;
            Colors colors2 = Common.Colors;
            buttonWrapper2.setColor(-65536);
            _wrongans();
            return "";
        } else if (str.equals("B")) {
            if (mostCurrent._cmdb.getText().equals(_curans)) {
                ButtonWrapper buttonWrapper3 = mostCurrent._cmdb;
                Colors colors3 = Common.Colors;
                buttonWrapper3.setColor(-16711936);
                Common.ToastMessageShow(BA.ObjectToCharSequence("Correct Answer"), false);
                _cursr = BA.NumberToString(Double.parseDouble(_cursr) + 1.0d);
                return "";
            }
            ButtonWrapper buttonWrapper4 = mostCurrent._cmdb;
            Colors colors4 = Common.Colors;
            buttonWrapper4.setColor(-65536);
            _wrongans();
            return "";
        } else if (str.equals("C")) {
            if (mostCurrent._cmdc.getText().equals(_curans)) {
                ButtonWrapper buttonWrapper5 = mostCurrent._cmdc;
                Colors colors5 = Common.Colors;
                buttonWrapper5.setColor(-16711936);
                Common.ToastMessageShow(BA.ObjectToCharSequence("Correct Answer"), false);
                _cursr = BA.NumberToString(Double.parseDouble(_cursr) + 1.0d);
                return "";
            }
            ButtonWrapper buttonWrapper6 = mostCurrent._cmdc;
            Colors colors6 = Common.Colors;
            buttonWrapper6.setColor(-65536);
            _wrongans();
            return "";
        } else if (!str.equals("D")) {
            return "";
        } else {
            if (mostCurrent._cmdd.getText().equals(_curans)) {
                ButtonWrapper buttonWrapper7 = mostCurrent._cmdd;
                Colors colors7 = Common.Colors;
                buttonWrapper7.setColor(-16711936);
                Common.ToastMessageShow(BA.ObjectToCharSequence("Correct Answer"), false);
                _cursr = BA.NumberToString(Double.parseDouble(_cursr) + 1.0d);
                return "";
            }
            ButtonWrapper buttonWrapper8 = mostCurrent._cmdd;
            Colors colors8 = Common.Colors;
            buttonWrapper8.setColor(-65536);
            _wrongans();
            return "";
        }
    }

    public static void _clearall() throws Exception {
        new ResumableSub_ClearAll(null).resume(processBA, null);
    }

    public static class ResumableSub_ClearAll extends BA.ResumableSub {
        mainquiz parent;

        public ResumableSub_ClearAll(mainquiz mainquiz) {
            this.parent = mainquiz;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = -1;
                        mainquiz mainquiz = this.parent;
                        mainquiz.mostCurrent._cmda.setEnabled(true);
                        mainquiz mainquiz2 = this.parent;
                        mainquiz.mostCurrent._cmdb.setEnabled(true);
                        mainquiz mainquiz3 = this.parent;
                        mainquiz.mostCurrent._cmdc.setEnabled(true);
                        mainquiz mainquiz4 = this.parent;
                        mainquiz.mostCurrent._cmdd.setEnabled(true);
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 1;
                        return;
                    case 1:
                        this.state = -1;
                        mainquiz mainquiz5 = this.parent;
                        ButtonWrapper buttonWrapper = mainquiz.mostCurrent._cmda;
                        Colors colors = Common.Colors;
                        buttonWrapper.setColor(-1);
                        mainquiz mainquiz6 = this.parent;
                        ButtonWrapper buttonWrapper2 = mainquiz.mostCurrent._cmdb;
                        Colors colors2 = Common.Colors;
                        buttonWrapper2.setColor(-1);
                        mainquiz mainquiz7 = this.parent;
                        ButtonWrapper buttonWrapper3 = mainquiz.mostCurrent._cmdc;
                        Colors colors3 = Common.Colors;
                        buttonWrapper3.setColor(-1);
                        mainquiz mainquiz8 = this.parent;
                        ButtonWrapper buttonWrapper4 = mainquiz.mostCurrent._cmdd;
                        Colors colors4 = Common.Colors;
                        buttonWrapper4.setColor(-1);
                        break;
                }
            }
        }
    }

    public static String _cmda_click() throws Exception {
        _checkans("A");
        return "";
    }

    public static String _cmdb_click() throws Exception {
        _checkans("B");
        return "";
    }

    public static String _cmdc_click() throws Exception {
        _checkans("C");
        return "";
    }

    public static String _cmdd_click() throws Exception {
        _checkans("D");
        return "";
    }

    public static String _dissall() throws Exception {
        mostCurrent._cmda.setEnabled(false);
        mostCurrent._cmdb.setEnabled(false);
        mostCurrent._cmdc.setEnabled(false);
        mostCurrent._cmdd.setEnabled(false);
        return "";
    }

    public static String _globals() throws Exception {
        mostCurrent._panel2 = new PanelWrapper();
        mostCurrent._button3 = new ButtonWrapper();
        mostCurrent._cmdd = new ButtonWrapper();
        mostCurrent._cmdc = new ButtonWrapper();
        mostCurrent._cmdb = new ButtonWrapper();
        mostCurrent._cmda = new ButtonWrapper();
        mostCurrent._lblque = new LabelWrapper();
        mostCurrent._panel1 = new PanelWrapper();
        mostCurrent._lbllv = new LabelWrapper();
        mostCurrent._cd1 = new ColorDrawable();
        mostCurrent._panel6 = new PanelWrapper();
        mostCurrent._panel3 = new PanelWrapper();
        mostCurrent._panel5 = new PanelWrapper();
        mostCurrent._panel4 = new PanelWrapper();
        mostCurrent._lblscore = new LabelWrapper();
        mostCurrent._imageview1 = new ImageViewWrapper();
        return "";
    }

    public static String _imageview1_click() throws Exception {
        return "";
    }

    public static void _nextque(int i) throws Exception {
        new ResumableSub_NextQue(null, i).resume(processBA, null);
    }

    public static class ResumableSub_NextQue extends BA.ResumableSub {
        int _a;
        mainquiz parent;

        public ResumableSub_NextQue(mainquiz mainquiz, int i) {
            this.parent = mainquiz;
            this._a = i;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = 1;
                        mainquiz._clearall();
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 33;
                        return;
                    case 1:
                        this.state = 32;
                        mainquiz mainquiz = this.parent;
                        switch (BA.switchObjectToInt(mainquiz._curdb, "EQ", "MQ", "HQ")) {
                            case 0:
                                this.state = 3;
                                continue;
                            case 1:
                                this.state = 13;
                                continue;
                            case 2:
                                this.state = 23;
                                continue;
                        }
                    case 3:
                        this.state = 4;
                        break;
                    case 4:
                        this.state = 11;
                        mainquiz mainquiz2 = this.parent;
                        if (mainquiz._ix != 0) {
                            mainquiz mainquiz3 = this.parent;
                            if (mainquiz._ix != 1) {
                                mainquiz mainquiz4 = this.parent;
                                if (mainquiz._ix != 2) {
                                    break;
                                } else {
                                    this.state = 10;
                                    break;
                                }
                            } else {
                                this.state = 8;
                                break;
                            }
                        } else {
                            this.state = 6;
                            break;
                        }
                    case 6:
                        this.state = 11;
                        mainquiz mainquiz5 = this.parent;
                        mainquiz._ix = 1;
                        mainquiz mainquiz6 = this.parent;
                        int[] iArr = mainquiz._num;
                        mainquiz mainquiz7 = this.parent;
                        mainquiz._qgen("EQ", iArr[mainquiz._ix]);
                        break;
                    case 8:
                        this.state = 11;
                        mainquiz mainquiz8 = this.parent;
                        mainquiz._ix = 2;
                        mainquiz mainquiz9 = this.parent;
                        int[] iArr2 = mainquiz._num;
                        mainquiz mainquiz10 = this.parent;
                        mainquiz._qgen("EQ", iArr2[mainquiz._ix]);
                        break;
                    case 10:
                        this.state = 11;
                        mainquiz mainquiz11 = this.parent;
                        mainquiz._ix = 0;
                        mainquiz mainquiz12 = this.parent;
                        int[] iArr3 = mainquiz._num;
                        mainquiz mainquiz13 = this.parent;
                        mainquiz._qgen("MQ", iArr3[mainquiz._ix]);
                        break;
                    case 11:
                        this.state = 32;
                        break;
                    case 13:
                        this.state = 14;
                        break;
                    case 14:
                        this.state = 21;
                        mainquiz mainquiz14 = this.parent;
                        if (mainquiz._ix != 0) {
                            mainquiz mainquiz15 = this.parent;
                            if (mainquiz._ix != 1) {
                                mainquiz mainquiz16 = this.parent;
                                if (mainquiz._ix != 2) {
                                    break;
                                } else {
                                    this.state = 20;
                                    break;
                                }
                            } else {
                                this.state = 18;
                                break;
                            }
                        } else {
                            this.state = 16;
                            break;
                        }
                    case 16:
                        this.state = 21;
                        mainquiz mainquiz17 = this.parent;
                        mainquiz._ix = 1;
                        mainquiz mainquiz18 = this.parent;
                        int[] iArr4 = mainquiz._num;
                        mainquiz mainquiz19 = this.parent;
                        mainquiz._qgen("MQ", iArr4[mainquiz._ix]);
                        break;
                    case 18:
                        this.state = 21;
                        mainquiz mainquiz20 = this.parent;
                        mainquiz._ix = 2;
                        mainquiz mainquiz21 = this.parent;
                        int[] iArr5 = mainquiz._num;
                        mainquiz mainquiz22 = this.parent;
                        mainquiz._qgen("MQ", iArr5[mainquiz._ix]);
                        break;
                    case 20:
                        this.state = 21;
                        mainquiz mainquiz23 = this.parent;
                        mainquiz._ix = 0;
                        mainquiz mainquiz24 = this.parent;
                        int[] iArr6 = mainquiz._num;
                        mainquiz mainquiz25 = this.parent;
                        mainquiz._qgen("HQ", iArr6[mainquiz._ix]);
                        break;
                    case 21:
                        this.state = 32;
                        break;
                    case 23:
                        this.state = 24;
                        break;
                    case 24:
                        this.state = 31;
                        mainquiz mainquiz26 = this.parent;
                        if (mainquiz._ix != 0) {
                            mainquiz mainquiz27 = this.parent;
                            if (mainquiz._ix != 1) {
                                mainquiz mainquiz28 = this.parent;
                                if (mainquiz._ix != 2) {
                                    break;
                                } else {
                                    this.state = 30;
                                    break;
                                }
                            } else {
                                this.state = 28;
                                break;
                            }
                        } else {
                            this.state = 26;
                            break;
                        }
                    case 26:
                        this.state = 31;
                        mainquiz mainquiz29 = this.parent;
                        mainquiz._ix = 1;
                        mainquiz mainquiz30 = this.parent;
                        int[] iArr7 = mainquiz._num;
                        mainquiz mainquiz31 = this.parent;
                        mainquiz._qgen("HQ", iArr7[mainquiz._ix]);
                        break;
                    case 28:
                        this.state = 31;
                        mainquiz mainquiz32 = this.parent;
                        mainquiz._ix = 2;
                        mainquiz mainquiz33 = this.parent;
                        int[] iArr8 = mainquiz._num;
                        mainquiz mainquiz34 = this.parent;
                        mainquiz._qgen("HQ", iArr8[mainquiz._ix]);
                        break;
                    case KeyCodes.KEYCODE_B:
                        this.state = 31;
                        BA ba2 = mainquiz.processBA;
                        mainquiz mainquiz35 = this.parent;
                        finalp finalp = mainquiz.mostCurrent._finalp;
                        Common.StartActivity(ba2, finalp.getObject());
                        mainquiz mainquiz36 = this.parent;
                        mainquiz.mostCurrent._activity.Finish();
                        break;
                    case KeyCodes.KEYCODE_C:
                        this.state = 32;
                        break;
                    case 32:
                        this.state = -1;
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 34;
                        return;
                    case 33:
                        this.state = 1;
                        break;
                    case 34:
                        this.state = -1;
                        break;
                }
            }
        }
    }

    public static void _numgen() throws Exception {
        new ResumableSub_NumGen(null).resume(processBA, null);
    }

    public static class ResumableSub_NumGen extends BA.ResumableSub {
        mainquiz parent;

        public ResumableSub_NumGen(mainquiz mainquiz) {
            this.parent = mainquiz;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = -1;
                        mainquiz mainquiz = this.parent;
                        mainquiz._num[0] = Common.Rnd(0, 3);
                        mainquiz mainquiz2 = this.parent;
                        mainquiz._num[1] = Common.Rnd(4, 6);
                        mainquiz mainquiz3 = this.parent;
                        mainquiz._num[2] = Common.Rnd(6, 10);
                        StringBuilder sb = new StringBuilder();
                        mainquiz mainquiz4 = this.parent;
                        StringBuilder append = sb.append(BA.NumberToString(mainquiz._num[0])).append(" ");
                        mainquiz mainquiz5 = this.parent;
                        StringBuilder append2 = append.append(BA.NumberToString(mainquiz._num[1])).append(" ");
                        mainquiz mainquiz6 = this.parent;
                        Common.LogImpl("41572868", append2.append(BA.NumberToString(mainquiz._num[2])).toString(), 0);
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 1;
                        return;
                    case 1:
                        this.state = -1;
                        break;
                }
            }
        }
    }

    public static String _process_globals() throws Exception {
        _num = new int[3];
        _curans = "";
        _curdb = "";
        _cursr = "";
        _ix = 0;
        _state = 0;
        return "";
    }

    public static void _qgen(String str, int i) throws Exception {
        new ResumableSub_QGEN(null, str, i).resume(processBA, null);
    }

    public static class ResumableSub_QGEN extends BA.ResumableSub {
        SQL.CursorWrapper _c2 = null;
        String _datab;
        int _qnum;
        String _s1 = "";
        mainquiz parent;

        public ResumableSub_QGEN(mainquiz mainquiz, String str, int i) {
            this.parent = mainquiz;
            this._datab = str;
            this._qnum = i;
        }

        @Override // anywheresoftware.b4a.BA.ResumableSub
        public void resume(BA ba, Object[] objArr) throws Exception {
            while (true) {
                switch (this.state) {
                    case -1:
                        return;
                    case 0:
                        this.state = 1;
                        mainquiz._clearall();
                        Common.LogImpl("41507332", this._datab + " " + BA.NumberToString(this._qnum), 0);
                        this._c2 = new SQL.CursorWrapper();
                        this._c2 = (SQL.CursorWrapper) AbsObjectWrapper.ConvertToWrapper(new SQL.CursorWrapper(), (Cursor) Common.Null);
                        this._s1 = BA.ObjectToString(Common.Null);
                        this._s1 = "select * from " + this._datab + " where Sno = " + BA.NumberToString(this._qnum);
                        SQL.CursorWrapper cursorWrapper = new SQL.CursorWrapper();
                        mainquiz mainquiz = this.parent;
                        main main = mainquiz.mostCurrent._main;
                        this._c2 = (SQL.CursorWrapper) AbsObjectWrapper.ConvertToWrapper(cursorWrapper, main._sql.ExecQuery(this._s1));
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 9;
                        return;
                    case 1:
                        this.state = 8;
                        if (!this._datab.equals("MQ")) {
                            if (!this._datab.equals("HQ")) {
                                this.state = 7;
                                break;
                            } else {
                                this.state = 5;
                                break;
                            }
                        } else {
                            this.state = 3;
                            break;
                        }
                    case 3:
                        this.state = 8;
                        mainquiz mainquiz2 = this.parent;
                        mainquiz.mostCurrent._lbllv.setText(BA.ObjectToCharSequence("Mid"));
                        mainquiz mainquiz3 = this.parent;
                        LabelWrapper labelWrapper = mainquiz.mostCurrent._lbllv;
                        Colors colors = Common.Colors;
                        labelWrapper.setColor(-256);
                        break;
                    case 5:
                        this.state = 8;
                        mainquiz mainquiz4 = this.parent;
                        mainquiz.mostCurrent._lbllv.setText(BA.ObjectToCharSequence("Hard"));
                        mainquiz mainquiz5 = this.parent;
                        LabelWrapper labelWrapper2 = mainquiz.mostCurrent._lbllv;
                        Colors colors2 = Common.Colors;
                        labelWrapper2.setColor(-65536);
                        break;
                    case 7:
                        this.state = 8;
                        mainquiz mainquiz6 = this.parent;
                        mainquiz.mostCurrent._lbllv.setText(BA.ObjectToCharSequence("Easy"));
                        mainquiz mainquiz7 = this.parent;
                        LabelWrapper labelWrapper3 = mainquiz.mostCurrent._lbllv;
                        Colors colors3 = Common.Colors;
                        labelWrapper3.setColor(-16711936);
                        break;
                    case 8:
                        this.state = -1;
                        Common.Sleep(mainquiz.mostCurrent.activityBA, this, 100);
                        this.state = 10;
                        return;
                    case 9:
                        this.state = 1;
                        this._c2.setPosition(0);
                        mainquiz mainquiz8 = this.parent;
                        mainquiz.mostCurrent._lblque.setText(BA.ObjectToCharSequence(this._c2.GetString("Ques")));
                        mainquiz mainquiz9 = this.parent;
                        mainquiz.mostCurrent._cmda.setText(BA.ObjectToCharSequence(this._c2.GetString("OptA")));
                        mainquiz mainquiz10 = this.parent;
                        mainquiz.mostCurrent._cmdb.setText(BA.ObjectToCharSequence(this._c2.GetString("OptB")));
                        mainquiz mainquiz11 = this.parent;
                        mainquiz.mostCurrent._cmdc.setText(BA.ObjectToCharSequence(this._c2.GetString("OptC")));
                        mainquiz mainquiz12 = this.parent;
                        mainquiz.mostCurrent._cmdd.setText(BA.ObjectToCharSequence(this._c2.GetString("OptD")));
                        mainquiz mainquiz13 = this.parent;
                        mainquiz._curans = this._c2.GetString("Ans");
                        ConcreteViewWrapper concreteViewWrapper = new ConcreteViewWrapper();
                        mainquiz mainquiz14 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper, (View) mainquiz.mostCurrent._lblque.getObject()));
                        ConcreteViewWrapper concreteViewWrapper2 = new ConcreteViewWrapper();
                        mainquiz mainquiz15 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper2, (View) mainquiz.mostCurrent._cmda.getObject()));
                        ConcreteViewWrapper concreteViewWrapper3 = new ConcreteViewWrapper();
                        mainquiz mainquiz16 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper3, (View) mainquiz.mostCurrent._cmdb.getObject()));
                        ConcreteViewWrapper concreteViewWrapper4 = new ConcreteViewWrapper();
                        mainquiz mainquiz17 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper4, (View) mainquiz.mostCurrent._cmdc.getObject()));
                        ConcreteViewWrapper concreteViewWrapper5 = new ConcreteViewWrapper();
                        mainquiz mainquiz18 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper5, (View) mainquiz.mostCurrent._cmdd.getObject()));
                        ConcreteViewWrapper concreteViewWrapper6 = new ConcreteViewWrapper();
                        mainquiz mainquiz19 = this.parent;
                        mainquiz._setautosizebasedontext((ConcreteViewWrapper) AbsObjectWrapper.ConvertToWrapper(concreteViewWrapper6, (View) mainquiz.mostCurrent._lbllv.getObject()));
                        mainquiz mainquiz20 = this.parent;
                        mainquiz._curdb = this._datab;
                        break;
                    case 10:
                        this.state = -1;
                        break;
                }
            }
        }
    }

    public static String _setautosizebasedontext(ConcreteViewWrapper concreteViewWrapper) throws Exception {
        JavaObject javaObject = new JavaObject();
        javaObject.InitializeStatic("androidx.core.widget.TextViewCompat");
        javaObject.RunMethod("setAutoSizeTextTypeWithDefaults", new Object[]{concreteViewWrapper.getObject(), 1});
        return "";
    }

    public static String _wrongans() throws Exception {
        Common.LogImpl("41638401", _curans, 0);
        if (mostCurrent._cmda.getText().equals(_curans)) {
            Common.LogImpl("41638403", BA.NumberToString(11), 0);
            ButtonWrapper buttonWrapper = mostCurrent._cmda;
            Colors colors = Common.Colors;
            buttonWrapper.setColor(-16711936);
            return "";
        } else if (mostCurrent._cmdb.getText().equals(_curans)) {
            Common.LogImpl("41638406", BA.NumberToString(11), 0);
            ButtonWrapper buttonWrapper2 = mostCurrent._cmdb;
            Colors colors2 = Common.Colors;
            buttonWrapper2.setColor(-16711936);
            return "";
        } else if (mostCurrent._cmdc.getText().equals(_curans)) {
            Common.LogImpl("41638409", BA.NumberToString(11), 0);
            ButtonWrapper buttonWrapper3 = mostCurrent._cmdc;
            Colors colors3 = Common.Colors;
            buttonWrapper3.setColor(-16711936);
            return "";
        } else if (!mostCurrent._cmdd.getText().equals(_curans)) {
            return "";
        } else {
            Common.LogImpl("41638412", BA.NumberToString(11), 0);
            ButtonWrapper buttonWrapper4 = mostCurrent._cmdd;
            Colors colors4 = Common.Colors;
            buttonWrapper4.setColor(-16711936);
            return "";
        }
    }
}
