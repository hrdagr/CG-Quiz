package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import anywheresoftware.b4a.keywords.constants.KeyCodes;
import java.io.IOException;
import java.io.InputStream;

@RequiresApi(KeyCodes.KEYCODE_A)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    /* access modifiers changed from: protected */
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fonts, int style) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* access modifiers changed from: protected */
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromInputStream(Context context, InputStream is) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    @Nullable
    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontsContractCompat.FontInfo[] fonts, int style) {
        int i;
        int i2;
        FontFamily.Builder familyBuilder;
        int i3;
        ContentResolver resolver = context.getContentResolver();
        int length = fonts.length;
        int i4 = 0;
        FontFamily.Builder familyBuilder2 = null;
        while (i4 < length) {
            FontsContractCompat.FontInfo font = fonts[i4];
            try {
                ParcelFileDescriptor pfd = resolver.openFileDescriptor(font.getUri(), "r", cancellationSignal);
                if (pfd == null) {
                    if (pfd != null) {
                        pfd.close();
                    }
                    familyBuilder = familyBuilder2;
                } else {
                    try {
                        Font.Builder weight = new Font.Builder(pfd).setWeight(font.getWeight());
                        if (font.isItalic()) {
                            i3 = 1;
                        } else {
                            i3 = 0;
                        }
                        Font platformFont = weight.setSlant(i3).setTtcIndex(font.getTtcIndex()).build();
                        if (familyBuilder2 == null) {
                            familyBuilder = new FontFamily.Builder(platformFont);
                        } else {
                            familyBuilder2.addFont(platformFont);
                            familyBuilder = familyBuilder2;
                        }
                        if (pfd != null) {
                            try {
                                pfd.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (Throwable th) {
                        th.addSuppressed(th);
                    }
                }
            } catch (IOException e2) {
                familyBuilder = familyBuilder2;
            }
            i4++;
            familyBuilder2 = familyBuilder;
        }
        if (familyBuilder2 == null) {
            return null;
        }
        if ((style & 1) != 0) {
            i = 700;
        } else {
            i = 400;
        }
        if ((style & 2) != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        return new Typeface.CustomFallbackBuilder(familyBuilder2.build()).setStyle(new FontStyle(i, i2)).build();
        throw th;
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry familyEntry, Resources resources, int style) {
        int i;
        int i2;
        FontFamily.Builder familyBuilder;
        int i3;
        FontResourcesParserCompat.FontFileResourceEntry[] entries = familyEntry.getEntries();
        int length = entries.length;
        int i4 = 0;
        FontFamily.Builder familyBuilder2 = null;
        while (i4 < length) {
            FontResourcesParserCompat.FontFileResourceEntry entry = entries[i4];
            try {
                Font.Builder weight = new Font.Builder(resources, entry.getResourceId()).setWeight(entry.getWeight());
                if (entry.isItalic()) {
                    i3 = 1;
                } else {
                    i3 = 0;
                }
                Font platformFont = weight.setSlant(i3).setTtcIndex(entry.getTtcIndex()).setFontVariationSettings(entry.getVariationSettings()).build();
                if (familyBuilder2 == null) {
                    familyBuilder = new FontFamily.Builder(platformFont);
                } else {
                    familyBuilder2.addFont(platformFont);
                    familyBuilder = familyBuilder2;
                }
            } catch (IOException e) {
                familyBuilder = familyBuilder2;
            }
            i4++;
            familyBuilder2 = familyBuilder;
        }
        if (familyBuilder2 == null) {
            return null;
        }
        if ((style & 1) != 0) {
            i = 700;
        } else {
            i = 400;
        }
        if ((style & 2) != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        return new Typeface.CustomFallbackBuilder(familyBuilder2.build()).setStyle(new FontStyle(i, i2)).build();
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        try {
            Font font = new Font.Builder(resources, id).build();
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(font).build()).setStyle(font.getStyle()).build();
        } catch (IOException e) {
            return null;
        }
    }
}
