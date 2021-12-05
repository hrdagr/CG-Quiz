package androidx.core.os;

import androidx.annotation.Nullable;

public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OperationCanceledException(@Nullable String message) {
        super(message == null ? "The operation has been canceled." : message);
    }
}
