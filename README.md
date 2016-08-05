# android OrmLite 实际应用


---

相信看了上一篇 [ormLite 入门](http://blog.csdn.net/ziqiang1/article/details/52121643)，对ormLite框架也有了个基本的认识

今天我们来了解一下ormLite的实际应用

现在我们有3张表，分为是t_student，t_class,t_score，我们学生表中有班级和成绩两个外键的引用  
我们来看一下代码:
###1.首先是实体类
>学生表:   

```java
@DatabaseTable(tableName = Student.TABLE_NAME)
public class Student {
    public final static String TABLE_NAME = "t_student";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(defaultValue = "小明")
    private String name;

    @DatabaseField(foreign = true,columnName = "ofClss",foreignAutoRefresh = true)
    private Classz ofClass;

}

```

我们的外键引用的是一个Classz的类型 

>班级表:

```java
@DatabaseTable(tableName = Classz.TABLE_NAME)
public class Classz {
    public final static String TABLE_NAME = "t_class";

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(unique = true,canBeNull = false)
    private String name;

    @DatabaseField(defaultValue = "工科楼")
    private String address;
}
```


###2.helper类

```java
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
```

>helper类中，我们添加了一个updateColumn方法，用来修改表结构，而不在是不负责任的直接删除了

###3.我们的Dao类  
>因为有多个Dao类  所以我们写一个BaseDao类，集成了基本的CRUD操作

```java
public abstract class BaseDao<T,ID> {

    private DatabaseHelper helper;

    public BaseDao(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public DatabaseHelper getHelper() {
        return helper;
    }

    public abstract Dao<T, ID> getDao();

    public int add(T t){
        try {
            return getDao().create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int addList(List<T> list){
        try {
            return getDao().create(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<T> getAll(){
        List<T> list = new ArrayList<>();
        try {
            list = getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public T get(ID id){
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int update(T t){
        try {
            return getDao().update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int delete(ID id){
        try {
            return getDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
```

>接着是我们的StudentDao  

```java
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
```

>ClasszDao类  

```java
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
```


我们的实现类中重写getDao方法，返回有相应泛型约束的Dao.

###4.我们的MainActivity
```java
public class MainActivity extends AppCompatActivity {

    private StudentDao studentDao;
    private ClasszDao classzDao;
    private EditText edittxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentDao = new StudentDao(this);
        classzDao = new ClasszDao(this);
        edittxt = (EditText) findViewById(R.id.edittxt);
    }

    int i = 1;
    public void c(View view){

        Classz classz = new Classz();
        classz.setId(System.currentTimeMillis()+"");
        classz.setName("计算机科学与技术"+i+"班");
        Student student = new Student();
        student.setOfClass(classz);
        student.setName("小明"+i);
        i++;
        int add = studentDao.add(student);
        Log.d("MyOrm",add>-1?"student 插入成功: "+add:"student 插入失败");
        int add1 = classzDao.add(classz);
        Log.d("MyOrm",add1>-1?"classz 插入成功: "+add1:"classz 插入失败");
    }

    public void r(View view){
        String s = edittxt.getText().toString();
        try{
            int i = Integer.parseInt(s);
            Student student = studentDao.get(i);
            Log.d("MyOrm",student.toString());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    public void r_all(View view){
        List<Student> all = studentDao.getAll();
        List<Classz> all1 = classzDao.getAll();
        Log.d("MyOrm",all.toString()+"---classz: "+all1.toString());
    }
    public void u(View view){
        Student student = studentDao.get(3);
        //将学号为3的学生转到 1班
        Classz classz = classzDao.getClassz("计算机科学与技术1班");
        student.setOfClass(classz);
        int update = studentDao.update(student);
        Log.d("MyOrm",update>-1?"转班成功: "+update:"转班失败");
    }

    public void d(View view){
        String s = edittxt.getText().toString();
        try{
            int i = Integer.parseInt(s);
            int result = studentDao.delete(i);
            Log.d("MyOrm",result>-1?"删除成功: "+result:"删除失败");
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
}

```

###5.源码下载地址：
[点击这里](https://github.com/yingshirun/OrmLitExamples)
