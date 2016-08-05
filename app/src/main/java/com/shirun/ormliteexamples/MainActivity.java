package com.shirun.ormliteexamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.shirun.ormliteexamples.bean.Classz;
import com.shirun.ormliteexamples.bean.Student;
import com.shirun.ormliteexamples.db.ClasszDao;
import com.shirun.ormliteexamples.db.StudentDao;

import java.util.List;

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
