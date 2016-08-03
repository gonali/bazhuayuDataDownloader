package com.gonali.data.downloader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FilesOperations {

    public static List<File> getFileListByDirectory(String dirPath) {

        File file = new File(dirPath);
        List<File> fileList = new ArrayList<>();

        for (File f : file.listFiles()) {
            fileList.add(f);
        }

        return fileList;

    }


    public static String readFileByLine(String fileName) {

        File file = new File(fileName);
        BufferedReader reader = null;
        String lineString = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.trim() != null) {
                    lineString = lineString + tempString;
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return lineString;
    }


    public static String[] readFileLineByLine(String fileName) {

        List<String> list = new ArrayList<>();

        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString;

            while ((tempString = reader.readLine()) != null) {

                list.add(tempString);
            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }


    public static void writeToFile(String fileName, byte[] data) {

        try {
            FileOutputStream fs = new FileOutputStream(fileName);
            fs.write(data, 0, data.length);
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
