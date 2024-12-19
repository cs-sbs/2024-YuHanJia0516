package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.Iterator;

public class UserLogoutUtil {

    /**
     * 用户注销功能（Excel方式），删除对应用户
     * @param name 用户名
     * @param password 用户密码
     * @return 操作结果：注销成功、用户不存在、或发生错误
     */
    private static String logoutUserFromExcel(String name, String password) {
        String filePath = "./src/main/java/file/user.xlsx";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean userFound = false;

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell nameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);

                if (nameCell != null && passwordCell != null) {
                    String storedName = nameCell.getStringCellValue();
                    String storedPassword = passwordCell.getStringCellValue();

                    if (storedName.equals(name) && storedPassword.equals(password)) {

                        sheet.removeRow(row);
                        userFound = true;
                        break;
                    }
                }
            }

            if (!userFound) {
                return "用户名或密码错误，未找到用户！";
            }


            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                return "用户注销成功！（Excel方式）";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "发生错误，请检查文件路径！";
        }
    }

    /**
     * 用户注销功能（MySQL方式），删除对应用户
     * @param name 用户名
     * @param password 用户密码
     * @return 操作结果：注销成功、用户不存在、或发生错误
     */
    private static String logoutUserFromMySQL(String name, String password) {

        try (Connection connection = MySQLConnection.getConnection()) {

            if (connection == null) {
                return "数据库连接失败！";
            }


            String checkQuery = "SELECT COUNT(*) FROM user WHERE name = ? AND password = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, name);
                checkStmt.setString(2, password);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return "用户名或密码错误，未找到用户！（MySQL方式）";
                    }
                }
            }


            String deleteQuery = "DELETE FROM user WHERE name = ? AND password = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, name);
                deleteStmt.setString(2, password);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    return "用户注销成功！（MySQL方式）";
                } else {
                    return "注销失败，请稍后再试！（MySQL方式）";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "发生错误，请检查数据库连接或SQL语句！";
        }
    }

    /**
     * 处理用户注销操作，执行Excel和MySQL方式
     * @return 操作结果：0 - 注销失败，1 - 注销成功
     */
    public static int UserLogout() {

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("请输入用户名: ");
        String name = scanner.nextLine();

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        String resultExcel = logoutUserFromExcel(name, password);
        String resultMySQL = logoutUserFromMySQL(name, password);


        System.out.println(resultExcel);
        System.out.println(resultMySQL);

        if (resultExcel.equals("用户名或密码错误，未找到用户！") && resultMySQL.equals("用户名或密码错误，未找到用户！（MySQL方式）")) {
            return 0;  // 注销失败
        } else if (resultExcel.contains("发生错误") || resultMySQL.contains("发生错误")) {
            return 0;  // 发生错误
        } else {
            return 1;  // 注销成功
        }
    }

}


