package anywheresoftware.b4a.objects;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.keywords.constants.TypefaceWrapper;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;
import anywheresoftware.b4a.objects.drawable.ColorDrawable;
import anywheresoftware.b4a.objects.streams.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URLEncoder;

@BA.ActivityObject
@BA.Version(2.1f)
@BA.ShortName("B4XView")
public class B4XViewWrapper extends AbsObjectWrapper<Object> {
    public static final int TOUCH_ACTION_DOWN = 0;
    public static final int TOUCH_ACTION_MOVE = 2;
    public static final int TOUCH_ACTION_MOVE_NOTOUCH = 100;
    public static final int TOUCH_ACTION_UP = 1;
    private static Field solidColorField;
    private ConcreteViewWrapper nodeWrapper = new ConcreteViewWrapper();

    private ConcreteViewWrapper asViewWrapper() {
        this.nodeWrapper.setObject((View) getObject());
        return this.nodeWrapper;
    }

    @Override // anywheresoftware.b4a.AbsObjectWrapper, anywheresoftware.b4a.ObjectWrapper
    public void setObject(Object o) {
        if (o instanceof ObjectWrapper) {
            o = ((ObjectWrapper) o).getObjectOrNull();
        }
        super.setObject(o);
    }

    @BA.Hide
    public View getViewObject() {
        return (View) getObject();
    }

    private PanelWrapper asPanelWrapper() {
        View v = getViewObject();
        if (v instanceof ViewGroup) {
            return (PanelWrapper) AbsObjectWrapper.ConvertToWrapper(new PanelWrapper(), v);
        }
        throw typeDoesNotMatch();
    }

    public boolean getVisible() {
        return asViewWrapper().getVisible();
    }

    public void setVisible(boolean b) {
        asViewWrapper().setVisible(b);
    }

    public boolean getEnabled() {
        return asViewWrapper().getEnabled();
    }

    public void setEnabled(boolean b) {
        asViewWrapper().setEnabled(b);
    }

    public void setLeft(@BA.Pixel int d) {
        asViewWrapper().setLeft(d);
    }

    public void setTop(@BA.Pixel int d) {
        asViewWrapper().setTop(d);
    }

    public void setWidth(@BA.Pixel int d) {
        asViewWrapper().setWidth(d);
    }

    public void setHeight(@BA.Pixel int d) {
        asViewWrapper().setHeight(d);
    }

    public int getLeft() {
        return asViewWrapper().getLeft();
    }

    public int getTop() {
        return asViewWrapper().getTop();
    }

    public int getWidth() {
        return asViewWrapper().getWidth();
    }

    public int getHeight() {
        return asViewWrapper().getHeight();
    }

    public void SetLayoutAnimated(int Duration, @BA.Pixel int Left, @BA.Pixel int Top, @BA.Pixel int Width, @BA.Pixel int Height) {
        View target = getViewObject();
        BALayout.LayoutParams lp = (BALayout.LayoutParams) target.getLayoutParams();
        if (lp == null) {
            asViewWrapper().SetLayout(Left, Top, Width, Height);
            return;
        }
        int pLeft = lp.left;
        int pTop = lp.top;
        int pWidth = lp.width;
        int pHeight = lp.height;
        asViewWrapper().SetLayout(Left, Top, Width, Height);
        BALayout.LayoutParams lp2 = (BALayout.LayoutParams) target.getLayoutParams();
        if (Build.VERSION.SDK_INT >= 11 && Duration > 0 && pWidth >= 0 && pHeight >= 0) {
            target.setPivotX(0.0f);
            target.setPivotY(0.0f);
            WeakReference<AnimatorSet> wr = (WeakReference) AbsObjectWrapper.getExtraTags(target).get("prevSet");
            AnimatorSet prevSet = wr != null ? wr.get() : null;
            if (prevSet != null && prevSet.isRunning()) {
                prevSet.end();
            }
            AnimatorSet set = new AnimatorSet();
            AbsObjectWrapper.getExtraTags(target).put("prevSet", new WeakReference(set));
            set.playTogether(ObjectAnimator.ofFloat(target, "translationX", (float) (pLeft - lp2.left), 0.0f), ObjectAnimator.ofFloat(target, "translationY", (float) (pTop - lp2.top), 0.0f), ObjectAnimator.ofFloat(target, "scaleX", ((float) pWidth) / ((float) lp2.width), 1.0f), ObjectAnimator.ofFloat(target, "scaleY", ((float) pHeight) / ((float) lp2.height), 1.0f));
            set.setDuration((long) Duration);
            set.setInterpolator(new LinearInterpolator());
            set.start();
        }
    }

    public B4XViewWrapper getParent() {
        return (B4XViewWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XViewWrapper(), asViewWrapper().getParent());
    }

    public void RemoveViewFromParent() {
        asViewWrapper().RemoveView();
    }

    public boolean RequestFocus() {
        return asViewWrapper().RequestFocus();
    }

    public void SetVisibleAnimated(int Duration, boolean Visible) {
        asViewWrapper().SetVisibleAnimated(Duration, Visible);
    }

    public void setProgress(int i) {
        ((ProgressBar) getObject()).setProgress(i);
    }

    public int getProgress() {
        return ((ProgressBar) getObject()).getProgress();
    }

    private LabelWrapper asLabelWrapper() {
        if (getObject() instanceof TextView) {
            return (LabelWrapper) AbsObjectWrapper.ConvertToWrapper(new LabelWrapper(), getObject());
        }
        throw typeDoesNotMatch();
    }

    public String getEditTextHint() {
        EditText e = (EditText) getObject();
        return String.valueOf(e.getHint() == null ? "" : e.getHint());
    }

    public void setEditTextHint(CharSequence s) {
        ((EditText) getObject()).setHint(s);
    }

    public void setText(CharSequence s) {
        asLabelWrapper().setText(s);
    }

    public String getText() {
        return asLabelWrapper().getText();
    }

    public void setTextColor(int c) {
        asLabelWrapper().setTextColor(c);
    }

    public int getTextColor() {
        return asLabelWrapper().getTextColor();
    }

    public void SetTextSizeAnimated(int Duration, float TextSize) {
        asLabelWrapper().SetTextSizeAnimated(Duration, TextSize);
    }

    public float getTextSize() {
        return asLabelWrapper().getTextSize();
    }

    public void setTextSize(float d) {
        asLabelWrapper().setTextSize(d);
    }

    public B4XFont getFont() {
        LabelWrapper lw = asLabelWrapper();
        return XUI.CreateFont(lw.getTypeface(), lw.getTextSize());
    }

    public void setFont(B4XFont f) {
        LabelWrapper lw = asLabelWrapper();
        lw.setTextSize(f.textSize);
        lw.setTypeface(f.typeface);
    }

    public void SetTextAlignment(String Vertical, String Horizontal) {
        int v;
        int h;
        if (Vertical.equals("TOP")) {
            v = 48;
        } else if (Vertical.equals("CENTER")) {
            v = 16;
        } else {
            v = 80;
        }
        if (Horizontal.equals("LEFT")) {
            h = 3;
        } else if (Horizontal.equals("CENTER")) {
            h = 1;
        } else {
            h = 5;
        }
        asLabelWrapper().setGravity(v | h);
    }

    public void setChecked(boolean b) {
        ((CompoundButton) getObject()).setChecked(b);
    }

    public boolean getChecked() {
        return ((CompoundButton) getObject()).isChecked();
    }

    public BA.IterableList GetAllViewsRecursive() {
        return asPanelWrapper().GetAllViewsRecursive();
    }

    @BA.RaisesSynchronousEvents
    public void LoadLayout(String LayoutFile, BA ba) throws Exception {
        asPanelWrapper().LoadLayout(LayoutFile, ba);
    }

    public B4XViewWrapper GetView(int Index) {
        return (B4XViewWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XViewWrapper(), asPanelWrapper().GetView(Index).getObject());
    }

    public void AddView(View View, @BA.Pixel int Left, @BA.Pixel int Top, @BA.Pixel int Width, @BA.Pixel int Height) {
        asPanelWrapper().AddView(View, Left, Top, Width, Height);
    }

    public void RemoveAllViews() {
        asPanelWrapper().RemoveAllViews();
    }

    public int getNumberOfViews() {
        return asPanelWrapper().getNumberOfViews();
    }

    public B4XBitmapWrapper Snapshot() {
        int currentLeft;
        int currentTop;
        ViewGroup vg;
        int i;
        CanvasWrapper.BitmapWrapper bw = new CanvasWrapper.BitmapWrapper();
        ConcreteViewWrapper vw = asViewWrapper();
        bw.InitializeMutable(vw.getWidth(), vw.getHeight());
        CanvasWrapper cw = new CanvasWrapper();
        cw.Initialize2((Bitmap) bw.getObject());
        View v = getViewObject();
        if (v.getLayoutParams() instanceof BALayout.LayoutParams) {
            currentLeft = getLeft();
            currentTop = getTop();
        } else {
            currentLeft = 0;
            currentTop = 0;
        }
        v.measure(View.MeasureSpec.makeMeasureSpec(vw.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(vw.getHeight(), 1073741824));
        v.layout(0, 0, vw.getWidth(), vw.getHeight());
        ((View) vw.getObject()).draw(cw.canvas);
        v.layout(currentLeft, currentTop, vw.getWidth(), vw.getHeight());
        if ((v.getParent() instanceof ViewGroup) && (i = (vg = (ViewGroup) v.getParent()).indexOfChild(v)) > -1) {
            vg.removeViewAt(i);
            vg.addView(v, i);
        }
        return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), bw.getObject());
    }

    public void SetBitmap(Bitmap Bitmap) {
        asViewWrapper().SetBackgroundImageNew(Bitmap).setGravity(17);
    }

    public B4XBitmapWrapper GetBitmap() {
        B4XBitmapWrapper res = new B4XBitmapWrapper();
        Drawable d = getViewObject().getBackground();
        if (d instanceof BitmapDrawable) {
            res.setObject(((BitmapDrawable) d).getBitmap());
        }
        return res;
    }

    private RuntimeException typeDoesNotMatch() {
        return new RuntimeException("Type does not match (" + getObject().getClass() + ")");
    }

    public void setColor(int Color) {
        asViewWrapper().setColor(Color);
    }

    public int getColor() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Drawable d = getViewObject().getBackground();
        if (d instanceof ColorDrawable) {
            return ((ColorDrawable) d).getColor();
        }
        if (d instanceof ColorDrawable.GradientDrawableWithCorners) {
            return ((ColorDrawable.GradientDrawableWithCorners) d).color;
        }
        if ((d instanceof GradientDrawable) && (Build.VERSION.SDK_INT <= 28 || BA.applicationContext.getApplicationInfo().targetSdkVersion <= 28)) {
            Drawable.ConstantState cs = d.getConstantState();
            Class<?> c = cs.getClass();
            if (c.getName().equals("android.graphics.drawable.GradientDrawable$GradientState")) {
                if (solidColorField == null) {
                    try {
                        solidColorField = c.getDeclaredField("mSolidColor");
                    } catch (NoSuchFieldException e) {
                        try {
                            solidColorField = c.getDeclaredField("mSolidColors");
                        } catch (NoSuchFieldException e2) {
                            solidColorField = c.getDeclaredField("mColorStateList");
                        }
                    }
                    solidColorField.setAccessible(true);
                }
                Object value = solidColorField.get(cs);
                if (solidColorField.getName().equals("mSolidColor")) {
                    return ((Integer) value).intValue();
                }
                ColorStateList list = (ColorStateList) value;
                if (list != null) {
                    return list.getDefaultColor();
                }
            }
        }
        return 0;
    }

    public void SetColorAnimated(int Duration, int FromColor, final int ToColor) {
        final ColorDrawable.GradientDrawableWithCorners gdc;
        if (Build.VERSION.SDK_INT < 11 || Duration <= 0) {
            asViewWrapper().setColor(ToColor);
            return;
        }
        final View target = getViewObject();
        if (target.getBackground() instanceof ColorDrawable.GradientDrawableWithCorners) {
            gdc = (ColorDrawable.GradientDrawableWithCorners) target.getBackground();
        } else {
            gdc = new ColorDrawable.GradientDrawableWithCorners();
        }
        gdc.setColor(FromColor);
        target.setBackgroundDrawable(gdc);
        ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
        final int fa = Color.alpha(FromColor);
        final int fr = Color.red(FromColor);
        final int fg = Color.green(FromColor);
        final int fb = Color.blue(FromColor);
        final int ta = Color.alpha(ToColor);
        final int tr = Color.red(ToColor);
        final int tg = Color.green(ToColor);
        final int tb = Color.blue(ToColor);
        anim.setDuration((long) Duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class anywheresoftware.b4a.objects.B4XViewWrapper.AnonymousClass1 */

            public void onAnimationUpdate(ValueAnimator animation) {
                float p = animation.getAnimatedFraction();
                gdc.setColor(Color.argb((int) (((float) fa) + (((float) (ta - fa)) * p)), (int) (((float) fr) + (((float) (tr - fr)) * p)), (int) (((float) fg) + (((float) (tg - fg)) * p)), (int) (((float) fb) + (((float) (tb - fb)) * p))));
                target.invalidate();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            /* class anywheresoftware.b4a.objects.B4XViewWrapper.AnonymousClass2 */

            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                gdc.setColor(ToColor);
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        anim.start();
    }

    public void SetColorAndBorder(int BackgroundColor, @BA.Pixel int BorderWidth, int BorderColor, @BA.Pixel int BorderCornerRadius) {
        anywheresoftware.b4a.objects.drawable.ColorDrawable cd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
        cd.Initialize2(BackgroundColor, BorderCornerRadius, BorderWidth, BorderColor);
        getViewObject().setBackgroundDrawable((Drawable) cd.getObject());
    }

    private ScrollViewWrapper asVScrollViewWrapper() {
        return (ScrollViewWrapper) AbsObjectWrapper.ConvertToWrapper(new ScrollViewWrapper(), getObject());
    }

    private HorizontalScrollViewWrapper asHScrollViewWrapper() {
        return (HorizontalScrollViewWrapper) AbsObjectWrapper.ConvertToWrapper(new HorizontalScrollViewWrapper(), getObject());
    }

    public int getScrollViewOffsetY() {
        if (getObject() instanceof ScrollView) {
            return asVScrollViewWrapper().getScrollPosition();
        }
        return 0;
    }

    public void setScrollViewOffsetY(int d) {
        if (getObject() instanceof ScrollView) {
            asVScrollViewWrapper().ScrollToNow(d);
        }
    }

    public int getScrollViewOffsetX() {
        if (getObject() instanceof HorizontalScrollView) {
            return asHScrollViewWrapper().getScrollPosition();
        }
        return 0;
    }

    public void setScrollViewOffsetX(int d) {
        if (getObject() instanceof HorizontalScrollView) {
            asHScrollViewWrapper().ScrollToNow(d);
        }
    }

    private PanelWrapper getScrollViewPanel() {
        if (getObject() instanceof HorizontalScrollView) {
            return asHScrollViewWrapper().getPanel();
        }
        if (getObject() instanceof ScrollView) {
            return asVScrollViewWrapper().getPanel();
        }
        throw typeDoesNotMatch();
    }

    public B4XViewWrapper getScrollViewInnerPanel() {
        return (B4XViewWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XViewWrapper(), getScrollViewPanel().getObject());
    }

    public int getScrollViewContentHeight() {
        return getScrollViewInnerPanel().getHeight();
    }

    public void setScrollViewContentHeight(int d) {
        getScrollViewInnerPanel().setHeight(d);
    }

    public int getScrollViewContentWidth() {
        return getScrollViewInnerPanel().getWidth();
    }

    public void setScrollViewContentWidth(int d) {
        getScrollViewInnerPanel().setWidth(d);
    }

    public Object getTag() {
        return asViewWrapper().getTag();
    }

    public void setTag(Object o) {
        asViewWrapper().setTag(o);
    }

    public void SendToBack() {
        asViewWrapper().SendToBack();
    }

    public void BringToFront() {
        asViewWrapper().BringToFront();
    }

    public void SetRotationAnimated(int Duration, float Degree) {
        float current = getRotation();
        View v = getViewObject();
        v.setRotation(Degree);
        v.setPivotX((float) (getWidth() / 2));
        v.setPivotY((float) (getHeight() / 2));
        Animator a = ObjectAnimator.ofFloat(v, "rotation", current, Degree).setDuration((long) Duration);
        a.setInterpolator(new LinearInterpolator());
        a.start();
    }

    public float getRotation() {
        return getViewObject().getRotation();
    }

    public void setRotation(float f) {
        View v = getViewObject();
        v.setRotation(f);
        v.setPivotX((float) (getWidth() / 2));
        v.setPivotY((float) (getHeight() / 2));
    }

    public void setSelectionStart(int s) {
        SetSelection(s, 0);
    }

    public int getSelectionStart() {
        return Selection.getSelectionStart(((EditText) getObject()).getText());
    }

    public int getSelectionLength() {
        return Math.max(0, Selection.getSelectionEnd(((EditText) getObject()).getText()) - getSelectionStart());
    }

    public void SetSelection(int Start, int Length) {
        Selection.setSelection(((EditText) getObject()).getText(), Start, Start + Length);
    }

    public void SelectAll() {
        Selection.selectAll(((EditText) getObject()).getText());
    }

    @BA.ShortName("B4XBitmap")
    public static class B4XBitmapWrapper extends AbsObjectWrapper<Bitmap> {
        public double getWidth() {
            return (double) ((Bitmap) getObject()).getWidth();
        }

        public double getHeight() {
            return (double) ((Bitmap) getObject()).getHeight();
        }

        private CanvasWrapper.BitmapWrapper asBitmapWrapper() {
            return (CanvasWrapper.BitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new CanvasWrapper.BitmapWrapper(), getObject());
        }

        public void WriteToStream(OutputStream Out, int Quality, Bitmap.CompressFormat Format) {
            asBitmapWrapper().WriteToStream(Out, Quality, Format);
        }

        public B4XBitmapWrapper Resize(int Width, int Height, boolean KeepAspectRatio) {
            return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), asBitmapWrapper().Resize((float) Width, (float) Height, KeepAspectRatio).getObject());
        }

        public B4XBitmapWrapper Rotate(int Degrees) {
            return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), asBitmapWrapper().Rotate((float) Degrees).getObject());
        }

        public B4XBitmapWrapper Crop(int Left, int Top, int Width, int Height) {
            return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), asBitmapWrapper().Crop(Left, Top, Width, Height).getObject());
        }

        public float getScale() {
            return ((float) ((Bitmap) getObject()).getDensity()) / 160.0f;
        }
    }

    @BA.ShortName("B4XFont")
    public static class B4XFont {
        private float textSize;
        @BA.Hide
        public Typeface typeface;

        public float getSize() {
            return this.textSize;
        }

        public boolean getIsInitialized() {
            return this.typeface != null;
        }

        public TypefaceWrapper ToNativeFont() {
            return (TypefaceWrapper) AbsObjectWrapper.ConvertToWrapper(new TypefaceWrapper(), this.typeface);
        }
    }

    @BA.ShortName("XUI")
    public static class XUI {
        public static final int Color_Black = -16777216;
        public static final int Color_Blue = -16776961;
        public static final int Color_Cyan = -16711681;
        public static final int Color_DarkGray = -12303292;
        public static final int Color_Gray = -7829368;
        public static final int Color_Green = -16711936;
        public static final int Color_LightGray = -3355444;
        public static final int Color_Magenta = -65281;
        public static final int Color_Red = -65536;
        public static final int Color_Transparent = 0;
        public static final int Color_White = -1;
        public static final int Color_Yellow = -256;
        public static final int DialogResponse_Cancel = -3;
        public static final int DialogResponse_Negative = -2;
        public static final int DialogResponse_Positive = -1;

        public static B4XBitmapWrapper LoadBitmap(String Dir, String FileName) throws IOException {
            return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), Common.LoadBitmap(Dir, FileName).getObject());
        }

        public static B4XBitmapWrapper LoadBitmapResize(String Dir, String FileName, int Width, int Height, boolean KeepAspectRatio) throws IOException {
            return (B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XBitmapWrapper(), Common.LoadBitmapResize(Dir, FileName, Width, Height, KeepAspectRatio).getObject());
        }

        public static String getDefaultFolder() {
            return File.getDirInternal();
        }

        public static void SetDataFolder(String AppName) {
        }

        public static boolean SubExists(BA ba, Object Component, String Sub, int NotUsed) throws IllegalArgumentException, SecurityException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
            return Common.SubExists(ba, Component, Sub);
        }

        public static int PaintOrColorToColor(Object Color) {
            return ((Integer) Color).intValue();
        }

        public static B4XFont CreateFont(Typeface Typeface, float Size) {
            B4XFont f = new B4XFont();
            f.typeface = Typeface;
            f.textSize = Size;
            return f;
        }

        public static B4XFont CreateFont2(B4XFont B4XFont, float Size) {
            return CreateFont(B4XFont.typeface, Size);
        }

        public static B4XFont CreateDefaultFont(float Size) {
            return CreateFont(TypefaceWrapper.DEFAULT, Size);
        }

        public static B4XFont CreateDefaultBoldFont(float Size) {
            return CreateFont(TypefaceWrapper.DEFAULT_BOLD, Size);
        }

        public static B4XFont CreateFontAwesome(float Size) {
            return CreateFont(TypefaceWrapper.getFONTAWESOME(), Size);
        }

        public static B4XFont CreateMaterialIcons(float Size) {
            return CreateFont(TypefaceWrapper.getMATERIALICONS(), Size);
        }

        public static boolean getIsB4A() {
            return true;
        }

        public static boolean getIsB4i() {
            return false;
        }

        public static boolean getIsB4J() {
            return false;
        }

        public static float getScale() {
            return Common.Density;
        }

        public static B4XViewWrapper CreatePanel(BA ba, String EventName) {
            PanelWrapper p = new PanelWrapper();
            if (ba.eventsTarget == null || EventName.length() <= 0) {
                p.Initialize(ba.sharedProcessBA.activityBA.get(), EventName);
            } else if (ba.activity == null) {
                throw new RuntimeException("Class must have an Activity context.");
            } else {
                p.Initialize(ba, EventName);
            }
            return (B4XViewWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XViewWrapper(), p.getObject());
        }

        public static int Color_RGB(int R, int G, int B) {
            return Color_ARGB(255, R, G, B);
        }

        public static int Color_ARGB(int Alpha, int R, int G, int B) {
            return (Alpha << 24) | (R << 16) | (G << 8) | B;
        }

        public static Object MsgboxAsync(BA ba, CharSequence Message, CharSequence Title) throws Exception {
            return Common.Msgbox2Async(Message, Title, "OK", "", "", null, ba, true);
        }

        public static Object Msgbox2Async(BA ba, CharSequence Message, CharSequence Title, String Positive, String Cancel, String Negative, CanvasWrapper.BitmapWrapper Icon) {
            return Common.Msgbox2Async(Message, Title, Positive, Cancel, Negative, Icon, ba, false);
        }

        public static String FileUri(String Dir, String FileName) throws IOException {
            if (!Dir.equals(File.getDirAssets())) {
                return "file://" + File.Combine(Dir, urlencode(FileName));
            }
            if (File.virtualAssetsFolder == null) {
                return "file:///android_asset/" + urlencode(FileName.toLowerCase(BA.cul));
            }
            return "file://" + File.Combine(File.virtualAssetsFolder, urlencode(File.getUnpackedVirtualAssetFile(FileName)));
        }

        private static String urlencode(String s) throws UnsupportedEncodingException {
            return URLEncoder.encode(s, "utf8").replace("+", "%20");
        }
    }
}
