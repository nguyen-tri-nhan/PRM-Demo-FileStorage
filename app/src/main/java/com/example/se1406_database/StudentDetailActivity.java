package com.example.se1406_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.se1406_database.dao.StudentDAO;
import com.example.se1406_database.dto.StudentDTO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class StudentDetailActivity extends AppCompatActivity {

    private EditText edtId, edtName, edtMark;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        edtId = findViewById(R.id.edtId);
        edtMark = findViewById(R.id.edtMark);
        edtName = findViewById(R.id.edtName);

        Intent intent = this.getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("update")){
            StudentDTO student = (StudentDTO) intent.getSerializableExtra("student");
            edtId.setText(student.getId());
            edtName.setText(student.getName());
            edtMark.setText(student.getMark() + "");
        }
    }

    public void clickToSave(View view) {
        try {
            StudentDAO dao = new StudentDAO();
            String id = edtId.getText().toString();
            String name = edtName.getText().toString();
            float mark = Float.parseFloat(edtMark.getText().toString());
            StudentDTO student = new StudentDTO(id, name, mark);
            FileInputStream fis = openFileInput("nhan.txt");
            List<StudentDTO> lst = dao.loadFromInternal(fis);
            FileOutputStream fos = openFileOutput("nhan.txt", MODE_PRIVATE);
            if (action.equals("new")) {
                lst.add(student);
            } else if (action.equals("update")) {
                for (StudentDTO dto: lst) {
                    if (dto.getId().equals(student.getId())) {
                        student.setName(dto.getName());
                        student.setMark(dto.getMark());
                        break;
                    }
                }
            }
            dao.saveToInternal(fos, lst);

            Toast.makeText(this ,"Done", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}