package anywheresoftware.b4a.sql;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.collections.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

@BA.Version(1.5f)
@BA.ShortName("SQL")
public class SQL implements BA.CheckForReinitialize {
    private SQLiteDatabase db;
    private volatile ArrayList<Object[]> nonQueryStatementsList = new ArrayList<>();

    private static SQL cloneMe(SQL sql) {
        SQL ret = new SQL();
        ret.db = sql.db;
        ret.nonQueryStatementsList = sql.nonQueryStatementsList;
        return ret;
    }

    public void Initialize(String Dir, String FileName, boolean CreateIfNecessary) {
        this.db = SQLiteDatabase.openDatabase(new File(Dir, FileName).toString(), null, (CreateIfNecessary ? 268435456 : 0) | 16);
    }

    public static void LIBRARY_DOC() {
    }

    private void checkNull() {
        if (this.db == null) {
            throw new RuntimeException("Object should first be initialized.");
        }
    }

    @Override // anywheresoftware.b4a.BA.CheckForReinitialize
    public boolean IsInitialized() {
        if (this.db == null) {
            return false;
        }
        return this.db.isOpen();
    }

    public void ExecNonQuery(String Statement) {
        checkNull();
        this.db.execSQL(Statement);
    }

    public void ExecNonQuery2(String Statement, List Args) {
        SQLiteStatement s = this.db.compileStatement(Statement);
        try {
            int numArgs = !Args.IsInitialized() ? 0 : Args.getSize();
            for (int i = 0; i < numArgs; i++) {
                DatabaseUtils.bindObjectToProgram(s, i + 1, Args.Get(i));
            }
            s.execute();
        } finally {
            s.close();
        }
    }

    public void AddNonQueryToBatch(String Statement, List Args) {
        this.nonQueryStatementsList.add(new Object[]{Statement, Args});
    }

    public Object ExecNonQueryBatch(final BA ba, final String EventName) {
        final ArrayList<Object[]> myList = this.nonQueryStatementsList;
        this.nonQueryStatementsList = new ArrayList<>();
        final SQL ret = cloneMe(this);
        BA.submitRunnable(new Runnable() {
            /* class anywheresoftware.b4a.sql.SQL.AnonymousClass1 */

            public void run() {
                synchronized (SQL.this.db) {
                    try {
                        SQL.this.BeginTransaction();
                        Iterator it = myList.iterator();
                        while (it.hasNext()) {
                            Object[] o = (Object[]) it.next();
                            SQL.this.ExecNonQuery2((String) o[0], (List) o[1]);
                        }
                        SQL.this.TransactionSuccessful();
                        SQL.this.EndTransaction();
                        ba.raiseEventFromDifferentThread(ret, SQL.this, 0, String.valueOf(EventName.toLowerCase(BA.cul)) + "_nonquerycomplete", true, new Object[]{true});
                    } catch (Exception e) {
                        SQL.this.EndTransaction();
                        e.printStackTrace();
                        ba.setLastException(e);
                        ba.raiseEventFromDifferentThread(ret, SQL.this, 0, String.valueOf(EventName.toLowerCase(BA.cul)) + "_nonquerycomplete", true, new Object[]{false});
                    }
                }
            }
        }, this, 1);
        return ret;
    }

    public Object ExecQueryAsync(final BA ba, final String EventName, final String Query, final List Args) {
        final SQL ret = cloneMe(this);
        BA.submitRunnable(new Runnable() {
            /* class anywheresoftware.b4a.sql.SQL.AnonymousClass2 */

            public void run() {
                synchronized (SQL.this.db) {
                    try {
                        String[] s = null;
                        if (Args != null && Args.IsInitialized()) {
                            s = new String[Args.getSize()];
                            for (int i = 0; i < s.length; i++) {
                                Object o = Args.Get(i);
                                s[i] = o == null ? null : String.valueOf(o);
                            }
                        }
                        ba.raiseEventFromDifferentThread(ret, SQL.this, 0, String.valueOf(EventName.toLowerCase(BA.cul)) + "_querycomplete", true, new Object[]{true, AbsObjectWrapper.ConvertToWrapper(new ResultSetWrapper(), SQL.this.ExecQuery2(Query, s))});
                    } catch (Exception e) {
                        e.printStackTrace();
                        ba.setLastException(e);
                        ba.raiseEventFromDifferentThread(ret, SQL.this, 0, String.valueOf(EventName.toLowerCase(BA.cul)) + "_querycomplete", true, new Object[]{false, AbsObjectWrapper.ConvertToWrapper(new ResultSetWrapper(), null)});
                    }
                }
            }
        }, this, 0);
        return ret;
    }

    public Cursor ExecQuery(String Query) {
        checkNull();
        return ExecQuery2(Query, null);
    }

    public Cursor ExecQuery2(String Query, String[] StringArgs) {
        checkNull();
        return this.db.rawQuery(Query, StringArgs);
    }

    public String ExecQuerySingleResult(String Query) {
        return ExecQuerySingleResult2(Query, null);
    }

    /* JADX INFO: finally extract failed */
    public String ExecQuerySingleResult2(String Query, String[] StringArgs) {
        checkNull();
        Cursor cursor = this.db.rawQuery(Query, StringArgs);
        try {
            if (cursor.moveToFirst() && cursor.getColumnCount() != 0) {
                String string = cursor.getString(0);
                cursor.close();
                return string;
            }
            cursor.close();
            return null;
        } catch (Throwable th) {
            cursor.close();
            throw th;
        }
    }

    public void BeginTransaction() {
        checkNull();
        this.db.beginTransaction();
    }

    public void TransactionSuccessful() {
        this.db.setTransactionSuccessful();
    }

    public void EndTransaction() {
        this.db.endTransaction();
    }

    public void Close() {
        if (this.db != null && this.db.isOpen()) {
            this.db.close();
        }
    }

    @BA.ShortName("Cursor")
    public static class CursorWrapper extends AbsObjectWrapper<Cursor> {
        public int getPosition() {
            return ((Cursor) getObject()).getPosition();
        }

        public void setPosition(int Value) {
            ((Cursor) getObject()).moveToPosition(Value);
        }

        public String GetColumnName(int Index) {
            return ((Cursor) getObject()).getColumnName(Index);
        }

        public int getRowCount() {
            return ((Cursor) getObject()).getCount();
        }

        public int getColumnCount() {
            return ((Cursor) getObject()).getColumnCount();
        }

        public int GetInt2(int Index) {
            return ((Cursor) getObject()).getInt(Index);
        }

        public int GetInt(String ColumnName) {
            return ((Cursor) getObject()).getInt(((Cursor) getObject()).getColumnIndexOrThrow(ColumnName));
        }

        public String GetString2(int Index) {
            return ((Cursor) getObject()).getString(Index);
        }

        public String GetString(String ColumnName) {
            return ((Cursor) getObject()).getString(((Cursor) getObject()).getColumnIndexOrThrow(ColumnName));
        }

        public Long GetLong2(int Index) {
            return Long.valueOf(((Cursor) getObject()).getLong(Index));
        }

        public Long GetLong(String ColumnName) {
            return Long.valueOf(((Cursor) getObject()).getLong(((Cursor) getObject()).getColumnIndexOrThrow(ColumnName)));
        }

        public Double GetDouble2(int Index) {
            return Double.valueOf(((Cursor) getObject()).getDouble(Index));
        }

        public Double GetDouble(String ColumnName) {
            return Double.valueOf(((Cursor) getObject()).getDouble(((Cursor) getObject()).getColumnIndexOrThrow(ColumnName)));
        }

        public byte[] GetBlob(String ColumnName) {
            return ((Cursor) getObject()).getBlob(((Cursor) getObject()).getColumnIndexOrThrow(ColumnName));
        }

        public byte[] GetBlob2(int Index) {
            return ((Cursor) getObject()).getBlob(Index);
        }

        public void Close() {
            ((Cursor) getObject()).close();
        }
    }

    @BA.ShortName("ResultSet")
    public static class ResultSetWrapper extends CursorWrapper {
        public boolean NextRow() {
            int position = getPosition() + 1;
            if (getRowCount() <= position) {
                return false;
            }
            setPosition(position);
            return true;
        }
    }
}
