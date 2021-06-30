package com.example.se1406_database.dao;

import android.os.Environment;

import com.example.se1406_database.dto.StudentDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<StudentDTO> loadFromRaw(InputStream is) throws Exception {
        List<StudentDTO> lst = new ArrayList<>();
        StudentDTO student = null;
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        String s = null;
        try {
            inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);
            while ((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                student = new StudentDTO(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
                lst.add(student);
            }

        } finally {
            if (br != null) br.close();
            if (inputStreamReader != null) inputStreamReader.close();
        }
        return lst;
    }

    public List<StudentDTO> loadFromInternal(FileInputStream fileInputStream) throws Exception {
        List<StudentDTO> lst = new ArrayList<>();
        StudentDTO student = null;
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        String s = null;
        try {
            inputStreamReader = new InputStreamReader(fileInputStream);
            br = new BufferedReader(inputStreamReader);
            while ((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                student = new StudentDTO(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
                lst.add(student);
            }

        } finally {
            if (br != null) br.close();
            if (inputStreamReader != null) inputStreamReader.close();
        }
        return lst;
    }

    public void saveToInternal(FileOutputStream fos, List<StudentDTO> list) throws Exception {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
        String result = "";
        for (StudentDTO student: list) {
            result += student.toString() + "\n";
        }
        outputStreamWriter.write(result);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    public boolean saveToExternal(List<StudentDTO> lst) throws  Exception{
        boolean check = false;
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File dir = new File(realPath + "/nhan");
        dir.mkdir();
        File file = new File(dir, "nhan.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        String result = "";
        for (StudentDTO dto: lst) {
            result += dto.toString() + "\n";
        }
        outputStreamWriter.write(result);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        fileOutputStream.close();
        check = true;
        return check;
    }

    public List<StudentDTO> loadFromExternal() throws Exception {
        List<StudentDTO> lst = new ArrayList<>();
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File directory = new File(realPath + "/nhan");
        File file = new File(directory, "nhan.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String result = "";
        while ((result = bufferedReader.readLine()) != null ) {
            String[] tmp = result.split("-");
            StudentDTO student = new StudentDTO(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
            lst.add(student);
        }
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        return lst;
    }
}
