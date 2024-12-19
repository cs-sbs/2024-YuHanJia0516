package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;

public class BookDeletionUtil {

    /**
     * 根据书名删除书籍
     *
     * @param bookTitle 书名
     */
    public static void deleteBookByTitle(String bookTitle) {
        String filePath = "./src/main/java/file/book.xlsx";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean bookFoundInExcel = false;

            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    Cell titleCell = row.getCell(0);

                    if (titleCell != null && titleCell.getCellType() == CellType.STRING) {
                        String title = titleCell.getStringCellValue();

                        if (title.equals(bookTitle)) {

                            sheet.removeRow(row);
                            bookFoundInExcel = true;
                            break;
                        }
                    }
                }
            }


            if (bookFoundInExcel) {

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                    System.out.println("书籍 '" + bookTitle + "' 已从Excel删除！");
                }
            } else {
                System.out.println("未找到书籍: " + bookTitle + "（Excel方式）");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("发生错误，请检查文件路径或文件是否打开！");
        }


        try (Connection connection = MySQLConnection.getConnection()) {

            if (connection == null) {
                System.out.println("数据库连接失败！");
                return;
            }


            String checkQuery = "SELECT COUNT(*) FROM book WHERE title = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, bookTitle);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("未找到书籍: " + bookTitle + "（MySQL方式）");
                        return;
                    }
                }
            }


            String deleteQuery = "DELETE FROM book WHERE title = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, bookTitle);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("书籍 '" + bookTitle + "' 已从MySQL数据库删除！");
                } else {
                    System.out.println("删除失败，请稍后再试！（MySQL方式）");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("发生错误，请检查数据库连接或SQL语句！");
        }
    }
}

