package com.shirun.ormliteexamples.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.shirun.ormliteexamples.bean.Classz;

import java.sql.SQLException;
import java.util.List;

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
public class ClasszDao extends BaseDao<Classz, String> {

    public ClasszDao(Context context) {
        super(context);
    }

    @Override
    public Dao<Classz, String> getDao() {
        try {
            return getHelper().getDao(Classz.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据班级名称获取班级信息
     * @param className
     * @return
     */
    public Classz getClassz(String className){
        try {
            List<Classz> name = getDao().queryBuilder()
                    .where().eq("name", className)
                    .query();
            if(name!=null && name.size() >0){
                return name.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
