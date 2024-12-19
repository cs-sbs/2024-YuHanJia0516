package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.Iterator;

public class UserLoginUtil {

    /**
     * 用户登录功能，判断用户是admin还是user
     * @param name 用户名
     * @param password 用户密码
     * @return 用户类型：admin、user 或 登录失败的标识
     */
    private static String loginUser(String name, String password) {
        String filePath = "./src/main/java/file/user.xlsx";


        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell nameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);
                Cell userTypeCell = row.getCell(2);

                if (nameCell != null && passwordCell != null && userTypeCell != null) {
                    String storedName = nameCell.getStringCellValue();
                    String storedPassword = passwordCell.getStringCellValue();

                    if (storedName.equals(name) && storedPassword.equals(password)) {
                        return userTypeCell.getStringCellValue();
                    }
                }
            }
            return "错误";

        } catch (IOException e) {
            e.printStackTrace();
            return "发生错误";
        }
    }
    /**
     * 处理用户登录，返回对应的登录结果
     * @return 登录结果：0 - 登录失败，1 - admin，2 - user
     */
    public static int LoginUser () {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("请输入用户名: ");
        String name = scanner.nextLine();

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        String result = loginUser(name, password);

        return switch (result) {
            case "admin" -> {
                System.out.println("登录成功，用户类型：admin");
                yield 1;
            }
            case "user" -> {
                System.out.println("登录成功，用户类型：user");
                yield 2;
            }
            case "错误" -> {
                System.out.println("用户名或密码错误！");
                yield 0;
            }
            case "发生错误" -> {
                System.out.println("发生错误，请检查文件路径！");
                yield 0;
            }
            default -> 0;
        };
    }
}
