package hrd.quiz;

import android.graphics.Bitmap;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;
import anywheresoftware.b4a.objects.streams.File;
import anywheresoftware.b4a.randomaccessfile.RandomAccessFile;
import anywheresoftware.b4a.sql.SQL;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

public class keyvaluestore extends B4AClass.ImplB4AClass implements BA.SubDelegator {
    private static HashMap<String, Method> htSubs;
    public Common __c = null;
    public finalp _finalp = null;
    public fpage _fpage = null;
    public main _main = null;
    public mainquiz _mainquiz = null;
    public SQL _sql1 = null;
    public starter _starter = null;
    public String _tempfile = "";
    public String _tempfolder = "";

    private void innerInitialize(BA ba) throws Exception {
        if (this.ba == null) {
            this.ba = new BA(ba, this, htSubs, "hrd.quiz.keyvaluestore");
            if (htSubs == null) {
                this.ba.loadHtSubs(getClass());
                htSubs = this.ba.htSubs;
            }
        }
        if (BA.isShellModeRuntimeCheck(this.ba)) {
            getClass().getMethod("_class_globals", keyvaluestore.class).invoke(this, null);
            return;
        }
        this.ba.raiseEvent2(null, true, "class_globals", false, new Object[0]);
    }

    public String _class_globals() throws Exception {
        this._sql1 = new SQL();
        Common common = this.__c;
        File file = Common.File;
        this._tempfolder = File.getDirInternalCache();
        this._tempfile = "key_value_temp.dat";
        return "";
    }

    public String _close() throws Exception {
        this._sql1.Close();
        return "";
    }

    public boolean _complete(boolean z) throws Exception {
        if (z) {
            this._sql1.TransactionSuccessful();
        } else {
            Common common = this.__c;
            StringBuilder append = new StringBuilder().append("Error saving object: ");
            Common common2 = this.__c;
            Common.LogImpl("43801092", append.append(BA.ObjectToString(Common.LastException(getActivityBA()))).toString(), 0);
        }
        this._sql1.EndTransaction();
        return z;
    }

    public boolean _containskey(String str) throws Exception {
        return Double.parseDouble(this._sql1.ExecQuerySingleResult2("SELECT count(key) FROM main WHERE key = ?", new String[]{str})) > 0.0d;
    }

    public String _createtable() throws Exception {
        this._sql1.ExecNonQuery("CREATE TABLE IF NOT EXISTS main(key TEXT PRIMARY KEY, value NONE)");
        return "";
    }

    public String _deleteall() throws Exception {
        this._sql1.ExecNonQuery("DROP TABLE main");
        _createtable();
        return "";
    }

    public CanvasWrapper.BitmapWrapper _getbitmap(String str) throws Exception {
        new SQL.CursorWrapper();
        SQL.CursorWrapper _getcursor = _getcursor(str);
        CanvasWrapper.BitmapWrapper bitmapWrapper = new CanvasWrapper.BitmapWrapper();
        if (_getcursor.getRowCount() == 0) {
            _getcursor.Close();
            return bitmapWrapper;
        }
        _getcursor.setPosition(0);
        byte[] GetBlob2 = _getcursor.GetBlob2(0);
        File.InputStreamWrapper inputStreamWrapper = new File.InputStreamWrapper();
        inputStreamWrapper.InitializeFromBytesArray(GetBlob2, 0, GetBlob2.length);
        bitmapWrapper.Initialize2((InputStream) inputStreamWrapper.getObject());
        inputStreamWrapper.Close();
        _getcursor.Close();
        return bitmapWrapper;
    }

    public SQL.CursorWrapper _getcursor(String str) throws Exception {
        return (SQL.CursorWrapper) AbsObjectWrapper.ConvertToWrapper(new SQL.CursorWrapper(), this._sql1.ExecQuery2("SELECT value FROM main WHERE key = ?", new String[]{str}));
    }

    public Object _getencryptedobject(String str, String str2) throws Exception {
        Common common = this.__c;
        return _getobjectinternal(str, true, str2);
    }

    public File.InputStreamWrapper _getinputstream(String str) throws Exception {
        new SQL.CursorWrapper();
        SQL.CursorWrapper _getcursor = _getcursor(str);
        if (_getcursor.getRowCount() == 0) {
            _getcursor.Close();
            File.InputStreamWrapper inputStreamWrapper = new File.InputStreamWrapper();
            Common common = this.__c;
            return (File.InputStreamWrapper) AbsObjectWrapper.ConvertToWrapper(inputStreamWrapper, (InputStream) Common.Null);
        }
        _getcursor.setPosition(0);
        byte[] GetBlob2 = _getcursor.GetBlob2(0);
        File.InputStreamWrapper inputStreamWrapper2 = new File.InputStreamWrapper();
        inputStreamWrapper2.InitializeFromBytesArray(GetBlob2, 0, GetBlob2.length);
        _getcursor.Close();
        return inputStreamWrapper2;
    }

    public Object _getobject(String str) throws Exception {
        Common common = this.__c;
        return _getobjectinternal(str, false, "");
    }

    public Object _getobjectinternal(String str, boolean z, String str2) throws Exception {
        Object ReadObject;
        new SQL.CursorWrapper();
        SQL.CursorWrapper _getcursor = _getcursor(str);
        if (_getcursor.getRowCount() == 0) {
            _getcursor.Close();
            Common common = this.__c;
            return Common.Null;
        }
        _getcursor.setPosition(0);
        byte[] GetBlob2 = _getcursor.GetBlob2(0);
        RandomAccessFile randomAccessFile = new RandomAccessFile();
        Common common2 = this.__c;
        randomAccessFile.Initialize3(GetBlob2, false);
        new Object();
        if (z) {
            ReadObject = randomAccessFile.ReadEncryptedObject(str2, randomAccessFile.CurrentPosition);
        } else {
            ReadObject = randomAccessFile.ReadObject(randomAccessFile.CurrentPosition);
        }
        randomAccessFile.Close();
        _getcursor.Close();
        return ReadObject;
    }

    public String _getsimple(String str) throws Exception {
        new SQL.CursorWrapper();
        SQL.CursorWrapper _getcursor = _getcursor(str);
        if (_getcursor.getRowCount() == 0) {
            _getcursor.Close();
            return "";
        }
        _getcursor.setPosition(0);
        String GetString2 = _getcursor.GetString2(0);
        _getcursor.Close();
        return GetString2;
    }

    public String _initialize(BA ba, String str, String str2) throws Exception {
        innerInitialize(ba);
        if (this._sql1.IsInitialized()) {
            this._sql1.Close();
        }
        SQL sql = this._sql1;
        Common common = this.__c;
        sql.Initialize(str, str2, true);
        _createtable();
        return "";
    }

    public String _insertquery(String str, Object obj) throws Exception {
        this._sql1.ExecNonQuery2("INSERT INTO main VALUES(?, ?)", Common.ArrayToList(new Object[]{str, obj}));
        return "";
    }

    public List _listkeys() throws Exception {
        new SQL.CursorWrapper();
        SQL.CursorWrapper cursorWrapper = (SQL.CursorWrapper) AbsObjectWrapper.ConvertToWrapper(new SQL.CursorWrapper(), this._sql1.ExecQuery("SELECT key FROM main"));
        List list = new List();
        list.Initialize();
        int rowCount = cursorWrapper.getRowCount() - 1;
        for (int i = 0; i <= rowCount; i++) {
            cursorWrapper.setPosition(i);
            list.Add(cursorWrapper.GetString2(0));
        }
        return list;
    }

    public boolean _putbitmap(String str, CanvasWrapper.BitmapWrapper bitmapWrapper) throws Exception {
        try {
            _start(str);
            File.OutputStreamWrapper outputStreamWrapper = new File.OutputStreamWrapper();
            outputStreamWrapper.InitializeToBytesArray(100);
            new CanvasWrapper.BitmapWrapper();
            bitmapWrapper.WriteToStream((OutputStream) outputStreamWrapper.getObject(), 100, (Bitmap.CompressFormat) BA.getEnumFromString(Bitmap.CompressFormat.class, "PNG"));
            _insertquery(str, outputStreamWrapper.ToBytesArray());
            outputStreamWrapper.Close();
            Common common = this.__c;
            return _complete(true);
        } catch (Exception e) {
            this.ba.setLastException(e);
            Common common2 = this.__c;
            return _complete(false);
        }
    }

    public boolean _putencyptedobject(String str, Object obj, String str2) throws Exception {
        Common common = this.__c;
        return _putobjectinternal(str, obj, true, str2);
    }

    public boolean _putinputstream(String str, File.InputStreamWrapper inputStreamWrapper) throws Exception {
        try {
            _start(str);
            File.OutputStreamWrapper outputStreamWrapper = new File.OutputStreamWrapper();
            outputStreamWrapper.InitializeToBytesArray(100);
            Common common = this.__c;
            File file = Common.File;
            File.Copy2((InputStream) inputStreamWrapper.getObject(), (OutputStream) outputStreamWrapper.getObject());
            inputStreamWrapper.Close();
            _insertquery(str, outputStreamWrapper.ToBytesArray());
            outputStreamWrapper.Close();
            Common common2 = this.__c;
            return _complete(true);
        } catch (Exception e) {
            this.ba.setLastException(e);
            Common common3 = this.__c;
            return _complete(false);
        }
    }

    public boolean _putobject(String str, Object obj) throws Exception {
        Common common = this.__c;
        return _putobjectinternal(str, obj, false, "");
    }

    public boolean _putobjectinternal(String str, Object obj, boolean z, String str2) throws Exception {
        try {
            _start(str);
            RandomAccessFile randomAccessFile = new RandomAccessFile();
            Common common = this.__c;
            File file = Common.File;
            File.Delete(this._tempfolder, this._tempfile);
            String str3 = this._tempfolder;
            String str4 = this._tempfile;
            Common common2 = this.__c;
            randomAccessFile.Initialize(str3, str4, false);
            if (z) {
                randomAccessFile.WriteEncryptedObject(obj, str2, randomAccessFile.CurrentPosition);
            } else {
                Common common3 = this.__c;
                randomAccessFile.WriteObject(obj, true, randomAccessFile.CurrentPosition);
            }
            randomAccessFile.Flush();
            byte[] bArr = new byte[((int) randomAccessFile.CurrentPosition)];
            randomAccessFile.ReadBytes(bArr, 0, bArr.length, 0);
            randomAccessFile.Close();
            _insertquery(str, bArr);
            Common common4 = this.__c;
            return _complete(true);
        } catch (Exception e) {
            this.ba.setLastException(e);
            Common common5 = this.__c;
            return _complete(false);
        }
    }

    public boolean _putsimple(String str, Object obj) throws Exception {
        try {
            _start(str);
            _insertquery(str, obj);
            Common common = this.__c;
            return _complete(true);
        } catch (Exception e) {
            this.ba.setLastException(e);
            Common common2 = this.__c;
            return _complete(false);
        }
    }

    public String _remove(String str) throws Exception {
        this._sql1.ExecNonQuery2("DELETE FROM main WHERE key = ?", Common.ArrayToList(new Object[]{str}));
        return "";
    }

    public String _start(String str) throws Exception {
        this._sql1.BeginTransaction();
        this._sql1.ExecNonQuery2("DELETE FROM main WHERE key = ?", Common.ArrayToList(new Object[]{str}));
        return "";
    }

    @Override // anywheresoftware.b4a.BA.SubDelegator
    public Object callSub(String str, Object obj, Object[] objArr) throws Exception {
        BA.senderHolder.set(obj);
        return BA.SubDelegator.SubNotFound;
    }
}
