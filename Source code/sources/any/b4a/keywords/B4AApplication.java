package anywheresoftware.b4a.keywords;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;

public class B4AApplication {
    private static CanvasWrapper.BitmapWrapper loadedIcon;

    public static String getLabelName() throws PackageManager.NameNotFoundException {
        return String.valueOf(BA.applicationContext.getPackageManager().getApplicationLabel(BA.applicationContext.getPackageManager().getApplicationInfo(BA.packageName, 0)));
    }

    public static String getVersionName() throws PackageManager.NameNotFoundException {
        return BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionName;
    }

    public static int getVersionCode() throws PackageManager.NameNotFoundException {
        return BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionCode;
    }

    public static String getPackageName() {
        return BA.packageName;
    }

    public static CanvasWrapper.BitmapWrapper getIcon() throws PackageManager.NameNotFoundException {
        if (loadedIcon != null) {
            return loadedIcon;
        }
        loadedIcon = new CanvasWrapper.BitmapWrapper();
        Drawable d = BA.applicationContext.getPackageManager().getApplicationIcon(BA.packageName);
        if (d instanceof BitmapDrawable) {
            loadedIcon.setObject(((BitmapDrawable) d).getBitmap());
        } else {
            loadedIcon.InitializeMutable(Common.DipToCurrent(108), Common.DipToCurrent(108));
            CanvasWrapper cw = new CanvasWrapper();
            cw.Initialize2((Bitmap) loadedIcon.getObject());
            cw.DrawDrawable(d, new Rect(0, 0, loadedIcon.getWidth(), loadedIcon.getHeight()));
        }
        return loadedIcon;
    }
}
