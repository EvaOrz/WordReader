package com.sunvote.myapplication;

import android.os.Environment;

import java.io.File;

public class FileUtil {

    private static final String PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/word_test/";


    public static String createFile(String kuozhan, String name) {
        File file = new File(PATH + name + kuozhan);
        if (!file.exists()) file.mkdir();
        return file.getAbsolutePath();
    }

    public static synchronized void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }

    public static synchronized void deleteFile(File file, String ignoFile) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            if (!file.getName().contains(ignoFile)) {
                file.delete();
            }
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                if (!file.getName().contains(ignoFile)) {
                    file.delete();
                }
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            if (!file.getName().contains(ignoFile)) {
                file.delete();
            }
        }
    }

    public static void deleteFileInDir(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {

                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFileInDir(childFiles[i]);
            }

        }
    }
}
