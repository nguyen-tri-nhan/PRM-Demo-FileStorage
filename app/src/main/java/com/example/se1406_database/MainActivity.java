package com.example.se1406_database;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.se1406_database.dao.StudentAdapter;
import com.example.se1406_database.dao.StudentDAO;
import com.example.se1406_database.dao.Utils;
import com.example.se1406_database.dto.StudentDTO;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1406_database.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ListView listViewStudent;
    private TextView txtTitle;
    private StudentAdapter adapter;

    private static final int R_CREATE = 1234;
    public static final int R_UPDATE = 1235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void clickToLoadFromRaw(MenuItem item) {
        listViewStudent = findViewById(R.id.txtListViewStudent);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("List student from raw");
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.database);
            StudentDAO studentDAO = new StudentDAO();
            List<StudentDTO> lstStudent = studentDAO.loadFromRaw(inputStream);
            adapter = new StudentAdapter();
            adapter.setListStudentDTOS(lstStudent);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDTO student = (StudentDTO) lstStudent.get(position);
                    Intent intent = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intent.putExtra("student", student);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToSaveData(MenuItem item) {
        try {
            StudentDAO dao = new StudentDAO();
            InputStream inputStream = getResources().openRawResource(R.raw.database);
            List<StudentDTO> list = dao.loadFromRaw(inputStream);
            FileOutputStream fileOutputStream = openFileOutput("nhan.txt", MODE_PRIVATE);
            dao.saveToInternal(fileOutputStream, list);
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToLoadFromInternal(MenuItem item) {
        try {
            listViewStudent = findViewById(R.id.txtListViewStudent);
            txtTitle = findViewById(R.id.txtTitle);
            adapter = new StudentAdapter();
            txtTitle.setText("Load from internal");
            FileInputStream fileInputStream = openFileInput("nhan.txt");
            StudentDAO dao = new StudentDAO();
            List<StudentDTO> listStudent = dao.loadFromInternal(fileInputStream);
            adapter.setListStudentDTOS(listStudent);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDTO student = (StudentDTO) listStudent.get(position);
                    Intent intentDetail = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intentDetail.putExtra("student", student);
                    intentDetail.putExtra("action", "update");
                    Toast.makeText(MainActivity.this, student.toString(), Toast.LENGTH_SHORT).show();
//                    startActivity(intentDetail);
                    startActivityForResult(intentDetail, R_UPDATE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToNew(View view) {
        Intent intent = new Intent (this, StudentDetailActivity.class) ;
        intent.putExtra("action", "new");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        try {
            listViewStudent = findViewById(R.id.txtListViewStudent);
            txtTitle = findViewById(R.id.txtTitle);
            adapter = new StudentAdapter();
            StudentDAO dao = new StudentDAO();
            FileInputStream fis = openFileInput("nhan.txt");
            List<StudentDTO> lst = dao.loadFromInternal(fis);
            adapter.setListStudentDTOS(lst);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDTO student = (StudentDTO) lst.get(position);
                    Intent intentDetail = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intentDetail.putExtra("action", "update");
                    intentDetail.putExtra("student", student);
//                    startActivity(intentDetail);
                    startActivityForResult(intentDetail, R_UPDATE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToSaveToExternal(MenuItem item) {
        try {
            FileInputStream fis = openFileInput("nhan.txt");
            StudentDAO dao = new StudentDAO();
            List<StudentDTO> lst = dao.loadFromInternal(fis);
            dao.saveToExternal(lst);
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToLoadFromExternal(MenuItem item) {
        try {
            listViewStudent = findViewById(R.id.txtListViewStudent);
            txtTitle = findViewById(R.id.txtTitle);
            adapter = new StudentAdapter();
            txtTitle.setText("Load from SD Card");
            StudentDAO dao = new StudentDAO();
            List<StudentDTO> lst = dao.loadFromExternal();
            adapter.setListStudentDTOS(lst);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDTO student = (StudentDTO) lst.get(position);
                    Intent intentDetail = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intentDetail.putExtra("student", student);
                    intentDetail.putExtra("action", "update");
                    Toast.makeText(MainActivity.this, student.toString(), Toast.LENGTH_SHORT).show();
//                    startActivity(intentDetail);
                    startActivityForResult(intentDetail, R_UPDATE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToBackupToExternal(MenuItem item) {

        try {

            File sdCard = Environment.getExternalStorageDirectory();
            String realPath = sdCard.getAbsolutePath();
            String desDir = realPath + "/nhan";
            File dir = new File(desDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String dataPath = "/data/data" + this.getPackageName() + "/files";
            File dataDir = new File(dataPath);
            File[] lstFile = dataDir.listFiles();
            if (lstFile != null) {
                for (int i = 0; i < lstFile.length; i++) {
                    File f = lstFile[i];
                    Utils utils = new Utils();
                    utils.copyFile(f.getAbsolutePath(), desDir + "/" + f.getName());
                    Toast.makeText(this, "BackupSuccess", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clickToRestoreFromExternal(MenuItem item) {
        try {

            File sdCard = Environment.getExternalStorageDirectory();
            String dataPathDes = "/data/data" + this.getPackageName() + "/files";
            String realPath = sdCard.getAbsolutePath();
            String srcDir = realPath + "/nhan";
            File dataDir = new File(srcDir);
            File[] lstFile = dataDir.listFiles();
            if (lstFile != null) {
                for (int i = 0; i < lstFile.length; i++) {
                    File f = lstFile[i];
                    Utils utils = new Utils();
                    utils.copyFile(f.getAbsolutePath(), dataPathDes + "/" + f.getName());
                    Toast.makeText(this, "BackupSuccess", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}