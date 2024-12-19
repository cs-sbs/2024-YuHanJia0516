package org.example;

import java.io.*;
import java.nio.file.*;

public class FileBackupRestore {


    public static void backupFile() { // 功能一：备份文件
        try {

            Path sourcePath = Paths.get("./src/main/java/file/book.xlsx");
            Path backupPath = Paths.get("./src/main/java/file/book_backup.xlsx");


            Files.copy(sourcePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件备份成功");
        } catch (IOException e) {
            System.out.println("备份文件时发生错误: " + e.getMessage());
        }
    }


    public static void restoreFile() {// 功能二：恢复文件
        try {

            Path backupPath = Paths.get("./src/main/java/file/book.xlsx");
            Path sourcePath = Paths.get("./src/main/java/file/book_backup.xlsx");


            Files.copy(backupPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件恢复成功 ");
        } catch (IOException e) {
            System.out.println("恢复文件时发生错误: " + e.getMessage());
        }
    }
}