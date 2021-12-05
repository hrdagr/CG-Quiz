package anywheresoftware.b4a.objects;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.B4XViewWrapper;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;

@BA.ShortName("B4XCanvas")
public class B4XCanvas {
    @BA.Hide
    public CanvasWrapper cvs;
    @BA.Hide
    public B4XViewWrapper target;
    private B4XRect targetRect;

    public void Initialize(B4XViewWrapper View) {
        this.cvs = new CanvasWrapper();
        this.target = View;
        this.cvs.Initialize(View.getViewObject());
        this.cvs.setAntiAlias(true);
        this.targetRect = new B4XRect();
        this.targetRect.Initialize(0.0f, 0.0f, (float) View.getWidth(), (float) View.getHeight());
    }

    public void Resize(float Width, float Height) {
        this.target.SetLayoutAnimated(0, this.target.getLeft(), this.target.getTop(), (int) (((double) Width) + 0.5d), (int) (((double) Height) + 0.5d));
        Initialize(this.target);
    }

    public B4XRect getTargetRect() {
        return this.targetRect;
    }

    public B4XViewWrapper getTargetView() {
        return this.target;
    }

    public void Invalidate() {
        this.target.getViewObject().invalidate();
    }

    public void DrawLine(float x1, float y1, float x2, float y2, int Color, float StrokeWidth) {
        this.cvs.DrawLine(x1, y1, x2, y2, Color, StrokeWidth);
    }

    public B4XViewWrapper.B4XBitmapWrapper CreateBitmap() {
        return (B4XViewWrapper.B4XBitmapWrapper) AbsObjectWrapper.ConvertToWrapper(new B4XViewWrapper.B4XBitmapWrapper(), this.cvs.getBitmap().getObject());
    }

    public void DrawRect(B4XRect Rect, int Color, boolean Filled, float StrokeWidth) {
        this.cvs.DrawRect(Rect.toRect(), Color, Filled, StrokeWidth);
    }

    public void DrawCircle(float x, float y, float Radius, int Color, boolean Filled, float StrokeWidth) {
        this.cvs.DrawCircle(x, y, Radius, Color, Filled, StrokeWidth);
    }

    public void DrawBitmap(Bitmap Bitmap, B4XRect Destination) {
        this.cvs.DrawBitmap(Bitmap, null, Destination.toRect());
    }

    public void DrawBitmapRotated(Bitmap Bitmap, B4XRect Destination, float Degrees) {
        this.cvs.DrawBitmapRotated(Bitmap, null, Destination.toRect(), Degrees);
    }

    public void ClipPath(B4XPath Path) throws Exception {
        this.cvs.ClipPath((Path) Path.getObject());
    }

    public void RemoveClip() {
        this.cvs.RemoveClip();
    }

    public void DrawPath(B4XPath Path, int Color, boolean Filled, float StrokeWidth) throws Exception {
        this.cvs.DrawPath((Path) Path.getObject(), Color, Filled, StrokeWidth);
    }

    public void DrawPathRotated(B4XPath Path, int Color, boolean Filled, float StrokeWidth, float Degrees, float CenterX, float CenterY) throws Exception {
        this.cvs.canvas.save();
        try {
            this.cvs.canvas.rotate(Degrees, CenterX, CenterY);
            this.cvs.DrawPath((Path) Path.getObject(), Color, Filled, StrokeWidth);
        } finally {
            this.cvs.canvas.restore();
        }
    }

    public void ClearRect(B4XRect Rect) {
        this.cvs.DrawRect(Rect.toRect(), 0, true, 0.0f);
    }

    public void DrawText(BA ba, String Text, float x, float y, B4XViewWrapper.B4XFont Font, int Color, Paint.Align Alignment) {
        this.cvs.DrawText(ba, Text, x, y, Font.typeface, Font.getSize(), Color, Alignment);
        this.cvs.setAntiAlias(true);
    }

    public void DrawTextRotated(BA ba, String Text, float x, float y, B4XViewWrapper.B4XFont Font, int Color, Paint.Align Alignment, float Degree) {
        this.cvs.DrawTextRotated(ba, Text, x, y, Font.typeface, Font.getSize(), Color, Alignment, Degree);
        this.cvs.setAntiAlias(true);
    }

    public void Release() {
    }

    public B4XRect MeasureText(String Text, B4XViewWrapper.B4XFont Font) {
        Paint paint = this.cvs.paint;
        paint.setTextSize(Font.getSize() * BA.applicationContext.getResources().getDisplayMetrics().scaledDensity);
        paint.setTypeface(Font.typeface);
        paint.setStrokeWidth(0.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.LEFT);
        Rect r = new Rect();
        if (Text.startsWith(" ")) {
            Text = "." + Text.substring(1);
        }
        if (Text.endsWith(" ")) {
            Text = String.valueOf(Text.substring(0, Text.length() - 1)) + ".";
        }
        paint.getTextBounds(Text, 0, Text.length(), r);
        B4XRect xr = new B4XRect();
        xr.Initialize((float) r.left, (float) r.top, (float) r.right, (float) r.bottom);
        return xr;
    }

    @BA.ShortName("B4XRect")
    public static class B4XRect {
        private RectF rf;
        private Rect ri;

        public void Initialize(@BA.Pixel float Left, @BA.Pixel float Top, @BA.Pixel float Right, @BA.Pixel float Bottom) {
            this.rf = new RectF(Left, Top, Right, Bottom);
            this.ri = new Rect();
        }

        public float getLeft() {
            return this.rf.left;
        }

        public void setLeft(float Left) {
            this.rf.left = Left;
        }

        public float getTop() {
            return this.rf.top;
        }

        public void setTop(float Top) {
            this.rf.top = Top;
        }

        public float getRight() {
            return this.rf.right;
        }

        public void setRight(float Right) {
            this.rf.right = Right;
        }

        public float getBottom() {
            return this.rf.bottom;
        }

        public void setBottom(float Bottom) {
            this.rf.bottom = Bottom;
        }

        public float getWidth() {
            return this.rf.right - this.rf.left;
        }

        public void setWidth(int w) {
            this.rf.right = this.rf.left + ((float) w);
        }

        public float getHeight() {
            return this.rf.bottom - this.rf.top;
        }

        public void setHeight(float h) {
            this.rf.bottom = this.rf.top + h;
        }

        public float getCenterX() {
            return this.rf.centerX();
        }

        public float getCenterY() {
            return this.rf.centerY();
        }

        @BA.Hide
        public Rect toRect() {
            this.rf.round(this.ri);
            return this.ri;
        }

        @BA.Hide
        public String toString() {
            if (this.rf != null) {
                return this.rf.toString();
            }
            return "Not initialized";
        }
    }

    @BA.ShortName("B4XPath")
    public static class B4XPath extends AbsObjectWrapper<Path> {
        public B4XPath Initialize(@BA.Pixel float x, @BA.Pixel float y) {
            Path path = new Path();
            path.moveTo(x, y);
            setObject(path);
            return this;
        }

        public B4XPath InitializeOval(B4XRect Rect) {
            Path path = new Path();
            path.addOval(Rect.rf, Path.Direction.CW);
            setObject(path);
            return this;
        }

        public B4XPath InitializeArc(@BA.Pixel float x, @BA.Pixel float y, @BA.Pixel float Radius, float StartingAngle, float SweepAngle) {
            Path path = new Path();
            path.moveTo(x, y);
            path.arcTo(new RectF(x - Radius, y - Radius, x + Radius, y + Radius), StartingAngle, SweepAngle);
            setObject(path);
            return this;
        }

        public B4XPath InitializeRoundedRect(B4XRect Rect, float CornersRadius) {
            Path path = new Path();
            path.addRoundRect(Rect.rf, CornersRadius, CornersRadius, Path.Direction.CW);
            setObject(path);
            return this;
        }

        public B4XPath LineTo(@BA.Pixel float x, @BA.Pixel float y) {
            ((Path) getObject()).lineTo(x, y);
            return this;
        }
    }
}
