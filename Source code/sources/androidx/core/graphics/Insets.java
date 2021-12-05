package androidx.core.graphics;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import anywheresoftware.b4a.keywords.constants.KeyCodes;

public final class Insets {
    @NonNull
    public static final Insets NONE = new Insets(0, 0, 0, 0);
    public final int bottom;
    public final int left;
    public final int right;
    public final int top;

    private Insets(int left2, int top2, int right2, int bottom2) {
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
    }

    @NonNull
    public static Insets of(int left2, int top2, int right2, int bottom2) {
        if (left2 == 0 && top2 == 0 && right2 == 0 && bottom2 == 0) {
            return NONE;
        }
        return new Insets(left2, top2, right2, bottom2);
    }

    @NonNull
    public static Insets of(@NonNull Rect r) {
        return of(r.left, r.top, r.right, r.bottom);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Insets insets = (Insets) o;
        if (this.bottom != insets.bottom) {
            return false;
        }
        if (this.left != insets.left) {
            return false;
        }
        if (this.right != insets.right) {
            return false;
        }
        return this.top == insets.top;
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public String toString() {
        return "Insets{left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + '}';
    }

    @NonNull
    @Deprecated
    @RequiresApi(api = KeyCodes.KEYCODE_A)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
    public static Insets wrap(@NonNull android.graphics.Insets insets) {
        return toCompatInsets(insets);
    }

    @NonNull
    @RequiresApi(api = KeyCodes.KEYCODE_A)
    public static Insets toCompatInsets(@NonNull android.graphics.Insets insets) {
        return of(insets.left, insets.top, insets.right, insets.bottom);
    }

    @NonNull
    @RequiresApi(api = KeyCodes.KEYCODE_A)
    public android.graphics.Insets toPlatformInsets() {
        return android.graphics.Insets.of(this.left, this.top, this.right, this.bottom);
    }
}
