package com.shirun.ormliteexamples.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.shirun.ormliteexamples.bean.Student;

import java.sql.SQLException;

/**
 * ==========================================
 * <p>
 * 作    者 : ying
 * <p>
 * 创建时间 ： 2016/8/5.
 * <p>
 * 用   途 :
 * <p>
 * <p>
 * ==========================================
 */
public class StudentDao extends BaseDao<Student,Integer> {

    public StudentDao(Context context) {
        super(context);
    }

    @Override
    public Dao<Student,Integer> getDao() {
        try {
            return getHelper().getDao(Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
