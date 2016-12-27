package com.example.ajax.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbFloat;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;
import com.example.ajax.myapplication.utils.Constants;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper implements IDBOperation {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
    private static final String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s";

    public DBHelper(final Context context, final SQLiteDatabase.CursorFactory factory, final int version) {
        super(context, Constants.DATABASE, factory, version);

    }

    @Nullable
    private static String getTableName(final AnnotatedElement clazz) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            return table.name();
        } else {
            return null;
        }
    }

    @Nullable
    private static String getTableCreateQuery(final Class<?> clazz) {
        boolean delimAdded = false;
        final Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            try {
                final String name = table.name();

                final StringBuilder builder = new StringBuilder();
                final Field[] fields = clazz.getFields();

                for (int i = 0; i < fields.length; i++) {
                    final Field field = fields[i];

                    final Annotation[] annotations = field.getAnnotations();

                    String type = null;

                    for (final Annotation annotation : annotations) {
                        if (annotation instanceof dbLong) {
                            type = ((dbLong) annotation).value();
                        } else if (annotation instanceof dbString) {
                            type = ((dbString) annotation).value();
                        } else if (annotation instanceof dbFloat) {
                            type = ((dbFloat) annotation).value();
                        }
                    }

                    if (type == null) {
                        continue;
                    }

                    if (delimAdded) {
                        builder.append(",");
                    }

                    final String value = (String) field.get(null);

                    builder.append(String.format(Locale.US, SQL_TABLE_CREATE_FIELD_TEMPLATE, value, type));
                    delimAdded = true;

                }
                return String.format(Locale.US, SQL_TABLE_CREATE_TEMPLATE, name, builder);
            } catch (final Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        for (final Class<?> clazz : Contract.MODELS) {
            final String sql = getTableCreateQuery(clazz);

            if (sql != null) {
                db.execSQL(sql);
            }
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        for (final Class<?> clazz : Contract.MODELS) {
            final String sql = "DROP TABLE IF EXISTS " + getTableName(clazz);
            db.execSQL(sql);
        }
        onCreate(db);

    }

    @Override
    public Cursor query(final String sql, String... args) {
        final SQLiteDatabase database = getReadableDatabase();

        if (args[0].equals("")) {
            args = null;
        }
        return database.rawQuery(sql, args);
    }

    @Override
    public long insert(final Class<?> table, final ContentValues values) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            long id;

            try {
                database.beginTransaction();

                id = database.insert(name, null, values);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return id;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int bulkInsert(final Class<?> table, final List<ContentValues> values) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;

            try {
                database.beginTransaction();

                for (final ContentValues value : values) {
                    database.insert(name, null, value);

                    count++;
                }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int delete(final Class<?> table, final String sql, final String... args) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;

            try {
                database.beginTransaction();

                count = database.delete(name, sql, args);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }

    public void insert(final String query) {
        final SQLiteDatabase database = getWritableDatabase();
        database.execSQL(query);
    }
}
