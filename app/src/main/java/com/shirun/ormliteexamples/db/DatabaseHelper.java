package com.shirun.ormliteexamples.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.shirun.ormliteexamples.bean.Classz;
import com.shirun.ormliteexamples.bean.Student;

import java.sql.SQLException;

/**
 * ==========================================
 * <p>
 * 作    者 : ying
 * <p>
 * 创建时间 ： 2016/8/2.
 * <p>
 * 用   途 :
 * <p>
 * <p>
 * ==========================================
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "my_orm_db";
    private static final int VERSION = 1;

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context mContext) {
        mContext = mContext.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(mContext);
                }
            }
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.d("myorm", "表开始新建");
        try {
            TableUtils.createTable(connectionSource, Student.class);
            TableUtils.createTable(connectionSource, Classz.class);
            Log.d("MyOrm", "表创建成功");
        } catch (SQLException e) {
            Log.d("MyOrm", "表创建失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldV, int newV) {
        Log.d("MyOrm", "更新表: old:" + oldV + " newV" + newV);
        //如果我们在Student类中在添加一个age字段可以这样改，只要将数据库版本+1就行
//        updateColumn(sqLiteDatabase, Student.TABLE_NAME, "age", "varchar", "18");
    }

    /**
     *
     * @param db
     * @param tableName     要修改的表
     * @param columnName    要添加的字段
     * @param columnType    字段类型
     * @param defaultField  默认值
     */
    public synchronized void updateColumn(SQLiteDatabase db, String tableName,
                                          String columnName, String columnType, Object defaultField) {
        try {
            if (db != null) {
                Cursor c = db.rawQuery("SELECT * from " + tableName
                        + " limit 1 ", null);
                boolean flag = false;

                if (c != null) {
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        if (columnName.equalsIgnoreCase(c.getColumnName(i))) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        String sql = "alter table " + tableName + " add "
                                + columnName + " " + columnType + " default "
                                + defaultField;
                        db.execSQL(sql);
                    }
                    c.close();
                }
            }
        } catch (Exception e) {
            Log.d("MyOrm", "更新失败");
            e.printStackTrace();
        }
    }

}
