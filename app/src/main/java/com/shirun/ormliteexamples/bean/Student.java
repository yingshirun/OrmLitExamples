package com.shirun.ormliteexamples.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
@DatabaseTable(tableName = Student.TABLE_NAME)
public class Student {
    public final static String TABLE_NAME = "t_student";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(defaultValue = "小明")
    private String name;

    @DatabaseField(foreign = true,columnName = "ofClss",foreignAutoRefresh = true)
    private Classz ofClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Classz getOfClass() {
        return ofClass;
    }

    public void setOfClass(Classz ofClass) {
        this.ofClass = ofClass;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ofClass=" + ofClass +
                '}';
    }
}
