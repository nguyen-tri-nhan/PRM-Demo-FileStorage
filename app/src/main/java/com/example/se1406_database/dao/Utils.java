package com.example.se1406_database.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Utils {
    public Utils() {
    }

    public void copyFile(String src, String des) throws IOException {
        File srcFile = new File(src);
        File desFile = new File(des);
        FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
        FileChannel desChannel = new FileInputStream(desFile).getChannel();
        desChannel.transferFrom(srcChannel, 0, srcChannel.size());
        desChannel.close();
        srcChannel.close();
    }
}
