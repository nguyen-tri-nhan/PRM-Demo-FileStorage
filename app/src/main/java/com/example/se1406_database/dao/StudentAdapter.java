package com.example.se1406_database.dao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.se1406_database.R;
import com.example.se1406_database.dto.StudentDTO;

import java.util.List;

public class StudentAdapter extends BaseAdapter {

    private List<StudentDTO> listStudentDTOS;

    public void setListStudentDTOS(List<StudentDTO> listStudentDTOS) {
        this.listStudentDTOS = listStudentDTOS;
    }

    @Override
    public int getCount() {
        return listStudentDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return listStudentDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        TextView txtId = convertView.findViewById(R.id.txtId);
        TextView txtName = convertView.findViewById(R.id.txtFullname);
        TextView txtMark = convertView.findViewById(R.id.txtMark);

        StudentDTO student = listStudentDTOS.get(position);
        txtId.setText(student.getId());
        txtName.setText(student.getName());
        txtMark.setText(student.getMark()+"");

        return convertView;
    }
}
