package anywheresoftware.b4a.objects;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.B4AApplication;
import anywheresoftware.b4a.keywords.Common;
import java.lang.reflect.Method;

@BA.ShortName("Notification")
public class NotificationWrapper extends AbsObjectWrapper<Object> {
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MIN = 1;
    private static Method methodSetLastEvent;
    private static int pendingId = 1;

    @BA.Hide
    public static class NotificationData {
        public int defaults;
        public int flags;
        public int icon;
        public int importanceLevel;
        public int number;
    }

    public void Initialize() {
        Initialize2(3);
    }

    public void Initialize2(int ImportanceLevel) {
        NotificationData nd = new NotificationData();
        nd.importanceLevel = ImportanceLevel;
        nd.defaults = -1;
        setObject(nd);
    }

    private NotificationData getND() {
        Object o = getObject();
        if (o instanceof NotificationData) {
            return (NotificationData) o;
        }
        throw new RuntimeException("Cannot change properties after call to SetInfo. Initialize the notification again.");
    }

    public void setVibrate(boolean v) {
        setValue(v, 2);
    }

    public void setSound(boolean v) {
        setValue(v, 1);
    }

    public void setLight(boolean v) {
        setValue(v, 4);
        setFlag(v, 1);
    }

    private void setValue(boolean v, int Default) {
        if (v) {
            getND().defaults |= Default;
            return;
        }
        getND().defaults &= Default ^ -1;
    }

    public void setAutoCancel(boolean v) {
        setFlag(v, 16);
    }

    public void setInsistent(boolean v) {
        setFlag(v, 4);
    }

    public void setOnGoingEvent(boolean v) {
        setFlag(v, 2);
    }

    public int getNumber() {
        return getND().number;
    }

    public void setNumber(int v) {
        getND().number = v;
    }

    private void setFlag(boolean v, int Flag) {
        if (v) {
            getND().flags |= Flag;
            return;
        }
        getND().flags &= Flag ^ -1;
    }

    public void setIcon(String s) {
        getND().icon = BA.applicationContext.getResources().getIdentifier(s, "drawable", BA.packageName);
    }

    @BA.DesignerName("SetInfo")
    public void SetInfoNew(BA ba, CharSequence Title, CharSequence Body, Object Activity) throws ClassNotFoundException {
        SetInfo2New(ba, Title, Body, null, Activity);
    }

    @BA.DesignerName("SetInfo2")
    public void SetInfo2New(BA ba, CharSequence Title, CharSequence Body, CharSequence Tag, Object Activity) throws ClassNotFoundException {
        int i;
        Notification n;
        Notification.Builder builder;
        Intent i2 = Common.getComponentIntent(ba, Activity);
        i2.addFlags(268435456);
        i2.addFlags(131072);
        if (Tag != null) {
            i2.putExtra("Notification_Tag", Tag);
        }
        Context context = ba.context;
        if (Tag == null) {
            i = 0;
        } else {
            i = pendingId;
            pendingId = i + 1;
        }
        PendingIntent pi = PendingIntent.getActivity(context, i, i2, 134217728);
        NotificationData nd = getND();
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 26) {
                try {
                    String channelId = "channel_" + nd.importanceLevel;
                    builder = new Notification.Builder(BA.applicationContext, channelId);
                    B4AApplication b4AApplication = Common.Application;
                    ((NotificationManager) BA.applicationContext.getSystemService("notification")).createNotificationChannel(new NotificationChannel(channelId, B4AApplication.getLabelName(), nd.importanceLevel));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                builder = new Notification.Builder(BA.applicationContext);
            }
            builder.setContentTitle(Title).setContentText(Body).setContentIntent(pi);
            n = builder.build();
            n.defaults = nd.defaults;
            n.flags = nd.flags;
            n.icon = nd.icon;
            n.when = System.currentTimeMillis();
            n.number = nd.number;
            n.extras.putBoolean(NotificationCompat.EXTRA_SHOW_WHEN, true);
        } else {
            n = new Notification();
            n.defaults = nd.defaults;
            n.flags = nd.flags;
            n.icon = nd.icon;
            n.when = System.currentTimeMillis();
            n.number = nd.number;
            try {
                if (methodSetLastEvent == null) {
                    methodSetLastEvent = Notification.class.getDeclaredMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                }
                methodSetLastEvent.invoke(n, ba.context, Title, Body, pi);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        setObject(n);
    }

    @BA.Hide
    public void SetInfo(BA ba, String Title, String Body, Object Activity) throws ClassNotFoundException {
        SetInfo2New(ba, Title, Body, null, Activity);
    }

    @BA.Hide
    public void SetInfo2(BA ba, String Title, String Body, String Tag, Object Activity) throws ClassNotFoundException {
        SetInfo2New(ba, Title, Body, Tag, Activity);
    }

    public void Notify(int Id) {
        NotificationManager nm = (NotificationManager) BA.applicationContext.getSystemService("notification");
        if (!(getObject() instanceof Notification)) {
            throw new RuntimeException("You must first call SetInfo or SetInfo2");
        }
        nm.notify(Id, (Notification) getObject());
    }

    public void Cancel(int Id) {
        ((NotificationManager) BA.applicationContext.getSystemService("notification")).cancel(Id);
    }
}
