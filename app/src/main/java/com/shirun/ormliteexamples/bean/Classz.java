package com.shirun.ormliteexamples.bean;

/**
 * ==========================================
 * <p/>
 * 作    者 : ying
 * <p/>
 * 创建时间 ： 2016/8/5.
 * <p/>
 * 用   途 :
 * <p/>
 * <p/>
 * ==========================================
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Classz.TABLE_NAME)
public class Classz {
    public final static String TABLE_NAME = "t_class";

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(unique = true,canBeNull = false)
    private String name;

    @DatabaseField(defaultValue = "工科楼")
    private String address;

    @Override
    public String toString() {
        return "Classz{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
