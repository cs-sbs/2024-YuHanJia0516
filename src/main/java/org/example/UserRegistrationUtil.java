package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class UserRegistrationUtil {

    /**
     * 用户注册功能（Excel方式）
     * @param name 用户名
     * @param password 用户密码
     * @param userType 用户类型
     * @return 注册结果信息
     */
    private static String registerUserToExcel(String name, String password, String userType) {
        String filePath = "./src/main/java/file/user.xlsx";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean userExists = false;


            for (Row row : sheet) {

                Cell nameCell = row.getCell(0);
                if (nameCell != null && nameCell.getStringCellValue().equals(name)) {
                    userExists = true;
                    break;
                }
            }

            if (userExists) {
                return "用户已存在！";
            }

            Row newRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            newRow.createCell(0).setCellValue(name);
            newRow.createCell(1).setCellValue(password);
            newRow.createCell(2).setCellValue(userType);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                return "用户注册成功！（Excel方式）";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "发生错误，请检查文件路径！";
        }
    }

    /**
     * 用户注册功能（MySQL方式）
     * @param name 用户名
     * @param password 用户密码
     * @param userType 用户类型
     * @return 注册结果信息
     */
    private static String registerUserToMySQL(String name, String password, String userType) {
        // 连接到数据库
        try (Connection connection = MySQLConnection.getConnection()) {

            if (connection == null) {
                return "数据库连接失败！";
            }


            String checkQuery = "SELECT COUNT(*) FROM user WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "用户已存在！（MySQL方式）";
                    }
                }
            }


            String insertQuery = "INSERT INTO user (name, password, userType) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, password);
                insertStmt.setString(3, userType);
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    return "用户注册成功！（MySQL方式）";
                } else {
                    return "注册失败，请稍后再试！（MySQL方式）";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "发生错误，请检查数据库连接或SQL语句！";
        }
    }

    public static void RegisterUser() {

        Scanner scanner = new Scanner(System.in);


        System.out.print("请输入用户名: ");
        String name = scanner.nextLine();

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        System.out.print("请输入用户类型 (admin/user): ");
        String userType = scanner.nextLine();

        if (!Objects.equals(userType, "admin") && !Objects.equals(userType, "user")) {
            System.out.println("输入用户类型错误，请重新输入");
            RegisterUser();
            return;
        }


        String resultExcel = registerUserToExcel(name, password, userType);
        String resultMySQL = registerUserToMySQL(name, password, userType);


        System.out.println(resultExcel);
        System.out.println(resultMySQL);
    }

}



