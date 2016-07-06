package com.jiayusoft.mobile.utils.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by ASUS on 2014/7/21.
 */
public class FileUtil {
    public static void createFile(String fileName){
        File file = new File(fileName);
        if (!file.exists()){
            if (file.getParentFile().mkdirs()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
