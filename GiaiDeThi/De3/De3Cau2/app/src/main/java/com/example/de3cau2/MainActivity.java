package com.example.de3cau2;

import static com.example.adapter.HelperAdapter.DB_NAME;
import static com.example.adapter.HelperAdapter.TBL_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.adapter.HelperAdapter;
import com.example.de3cau2.databinding.ActivityMainBinding;
import com.example.models.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String DB_PATH_SUFFIX = "/databases/";
    ArrayList<Student> students;
    Student selectedStudent = null;
    HelperAdapter dbhelper;
    public static SQLiteDatabase db;
    private ArrayAdapter<Student> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAdapter();
        CopyDBStudent();
        OpenDBStudent();
        students = new ArrayList<Student>();
        addEvents();
        registerForContextMenu(binding.lvStudent);
    }

    private void addEvents() {
        binding.lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStudent = students.get(i);
                Intent intent = new Intent(getApplicationContext(),SavedActivity.class);
                intent.putExtra("StudentInfo",selectedStudent);
                startActivity(intent);
                return false;
            }
        });
    }

    private void OpenDBStudent() {
        db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);

    }

    private void CopyDBStudent() {
        try{
            File dbFile = getDatabasePath(DB_NAME);
            if(!dbFile.exists()){
                if(processCopy()){
                    Toast.makeText(this, "Success!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.e("Error: ",e.toString());
        }
    }

    private boolean processCopy() {
        String dbPath = getApplicationInfo().dataDir+DB_PATH_SUFFIX+DB_NAME;
        try{
            InputStream inputStream = getAssets().open(DB_NAME);
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush(); outputStream.close(); inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initAdapter() {
        dbhelper = new HelperAdapter(MainActivity.this);
        dbhelper.CreateSampleData();
    }
    @Override
    protected void onResume() {
        loadDataFromDB();
        super.onResume();


    }

    private void loadDataFromDB() {
        students.clear();
        Student s;
        Cursor cursor = db.query(TBL_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String sId = cursor.getString(0);
            String sName = cursor.getString(1);
            String sClass = cursor.getString(2);
            s = new Student(sId,sName,sClass);
            students.add(s);
        }
        cursor.close();
        adapter = new ArrayAdapter<Student>(MainActivity.this, android.R.layout.simple_list_item_1,students);
        binding.lvStudent.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("===Activity Lifecycle===", "onStart");
        Toast.makeText(this, "Activity Lifecycle: onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("===Activity Lifecycle===", "onStop");
        Toast.makeText(this, "Activity Lifecycle: onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("===Activity Lifecycle===", "onDestroy");
        Toast.makeText(this, "Activity Lifecycle: onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("===Activity Lifecycle===", "onPause");
        Toast.makeText(this, "Activity Lifecycle: onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("===Activity Lifecycle===", "onRestart");
        Toast.makeText(this, "Activity Lifecycle: onRestart", Toast.LENGTH_SHORT).show();
    }
}