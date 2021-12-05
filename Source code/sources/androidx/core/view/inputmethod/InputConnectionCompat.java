package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    static boolean handlePerformPrivateCommand(@Nullable String action, @NonNull Bundle data, @NonNull OnCommitContentListener onCommitContentListener) {
        boolean interop;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        int i;
        int i2 = 1;
        if (data == null) {
            return false;
        }
        if (TextUtils.equals(COMMIT_CONTENT_ACTION, action)) {
            interop = false;
        } else if (!TextUtils.equals(COMMIT_CONTENT_INTEROP_ACTION, action)) {
            return false;
        } else {
            interop = true;
        }
        ResultReceiver resultReceiver = null;
        boolean result = false;
        if (interop) {
            str = COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY;
        } else {
            str = COMMIT_CONTENT_RESULT_RECEIVER_KEY;
        }
        try {
            ResultReceiver resultReceiver2 = (ResultReceiver) data.getParcelable(str);
            if (interop) {
                str2 = COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY;
            } else {
                str2 = COMMIT_CONTENT_CONTENT_URI_KEY;
            }
            Uri contentUri = (Uri) data.getParcelable(str2);
            if (interop) {
                str3 = COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY;
            } else {
                str3 = COMMIT_CONTENT_DESCRIPTION_KEY;
            }
            ClipDescription description = (ClipDescription) data.getParcelable(str3);
            if (interop) {
                str4 = COMMIT_CONTENT_LINK_URI_INTEROP_KEY;
            } else {
                str4 = COMMIT_CONTENT_LINK_URI_KEY;
            }
            Uri linkUri = (Uri) data.getParcelable(str4);
            if (interop) {
                str5 = COMMIT_CONTENT_FLAGS_INTEROP_KEY;
            } else {
                str5 = COMMIT_CONTENT_FLAGS_KEY;
            }
            int flags = data.getInt(str5);
            if (interop) {
                str6 = COMMIT_CONTENT_OPTS_INTEROP_KEY;
            } else {
                str6 = COMMIT_CONTENT_OPTS_KEY;
            }
            Bundle opts = (Bundle) data.getParcelable(str6);
            if (!(contentUri == null || description == null)) {
                result = onCommitContentListener.onCommitContent(new InputContentInfoCompat(contentUri, description, linkUri), flags, opts);
            }
            if (resultReceiver2 != null) {
                if (result) {
                    i = 1;
                } else {
                    i = 0;
                }
                resultReceiver2.send(i, null);
            }
            return result;
        } catch (Throwable th) {
            if (0 != 0) {
                if (0 == 0) {
                    i2 = 0;
                }
                resultReceiver.send(i2, null);
            }
            throw th;
        }
    }

    public static boolean commitContent(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull InputContentInfoCompat inputContentInfo, int flags, @Nullable Bundle opts) {
        boolean interop;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        ClipDescription description = inputContentInfo.getDescription();
        boolean supported = false;
        String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        int length = contentMimeTypes.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (description.hasMimeType(contentMimeTypes[i])) {
                supported = true;
                break;
            } else {
                i++;
            }
        }
        if (!supported) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo) inputContentInfo.unwrap(), flags, opts);
        }
        switch (EditorInfoCompat.getProtocol(editorInfo)) {
            case 2:
                interop = true;
                break;
            case 3:
            case 4:
                interop = false;
                break;
            default:
                return false;
        }
        Bundle params = new Bundle();
        if (interop) {
            str = COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY;
        } else {
            str = COMMIT_CONTENT_CONTENT_URI_KEY;
        }
        params.putParcelable(str, inputContentInfo.getContentUri());
        if (interop) {
            str2 = COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY;
        } else {
            str2 = COMMIT_CONTENT_DESCRIPTION_KEY;
        }
        params.putParcelable(str2, inputContentInfo.getDescription());
        if (interop) {
            str3 = COMMIT_CONTENT_LINK_URI_INTEROP_KEY;
        } else {
            str3 = COMMIT_CONTENT_LINK_URI_KEY;
        }
        params.putParcelable(str3, inputContentInfo.getLinkUri());
        if (interop) {
            str4 = COMMIT_CONTENT_FLAGS_INTEROP_KEY;
        } else {
            str4 = COMMIT_CONTENT_FLAGS_KEY;
        }
        params.putInt(str4, flags);
        if (interop) {
            str5 = COMMIT_CONTENT_OPTS_INTEROP_KEY;
        } else {
            str5 = COMMIT_CONTENT_OPTS_KEY;
        }
        params.putParcelable(str5, opts);
        if (interop) {
            str6 = COMMIT_CONTENT_INTEROP_ACTION;
        } else {
            str6 = COMMIT_CONTENT_ACTION;
        }
        return inputConnection.performPrivateCommand(str6, params);
    }

    @NonNull
    public static InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (Build.VERSION.SDK_INT >= 25) {
            return new InputConnectionWrapper(inputConnection, false) {
                /* class androidx.core.view.inputmethod.InputConnectionCompat.AnonymousClass1 */

                public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                    if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, flags, opts);
                }
            };
        } else {
            return EditorInfoCompat.getContentMimeTypes(editorInfo).length != 0 ? new InputConnectionWrapper(inputConnection, false) {
                /* class androidx.core.view.inputmethod.InputConnectionCompat.AnonymousClass2 */

                public boolean performPrivateCommand(String action, Bundle data) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(action, data, onCommitContentListener)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            } : inputConnection;
        }
    }
}
