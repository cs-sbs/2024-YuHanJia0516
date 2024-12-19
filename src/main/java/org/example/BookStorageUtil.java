package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

import java.util.Scanner;

public class BookStorageUtil {

    private static final String filePath = "./src/main/java/file/book.xlsx";  // 图书信息存储文件路径
    /**
     * 检查图书ISBN是否已经存在于Excel中
     * @param ibsn ISBN
     * @return 是否已存在
     */
    private static boolean isBookExistsInExcel(String ibsn) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell ibsnCell = row.getCell(3);  // 假设ISBN在第4列（索引3）
                if (ibsnCell != null && ibsnCell.getStringCellValue().equals(ibsn)) {
                    return true;  // 如果找到了相同的ISBN，表示图书已存在
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查图书ISBN是否已经存在于MySQL数据库中
     * @param ibsn ISBN
     * @return 是否已存在
     */
    private static boolean isBookExistsInDatabase(String ibsn) {
        String query = "SELECT COUNT(*) FROM book WHERE ibsn = ?";
        try (Connection conn = MySQLConnection.getConnection();  // 使用已创建的连接方法
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, ibsn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // 如果查询到记录，表示图书已存在
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新图书信息到Excel和MySQL数据库
     * @param book 图书对象
     * @return 操作结果：图书已更新或添加成功
     */
    private static String addOrUpdateBook(Book book) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean bookFound = false;


            for (Row row : sheet) {
                Cell ibsnCell = row.getCell(3);
                if (ibsnCell != null && ibsnCell.getStringCellValue().equals(book.getIbsn())) {

                    row.getCell(0).setCellValue(book.getTitle());
                    row.getCell(1).setCellValue(book.getAuthor());
                    row.getCell(2).setCellValue(book.getPublicationDate());
                    row.getCell(3).setCellValue(book.getIbsn());
                    row.getCell(4).setCellValue(book.getBookType());
                    row.getCell(5).setCellValue(book.getOtherSize());
                    bookFound = true;
                    break;
                }
            }


            if (!bookFound) {  // 如果未找到已有图书，新增一行
                Row newRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
                newRow.createCell(0).setCellValue(book.getTitle());
                newRow.createCell(1).setCellValue(book.getAuthor());
                newRow.createCell(2).setCellValue(book.getPublicationDate());
                newRow.createCell(3).setCellValue(book.getIbsn());
                newRow.createCell(4).setCellValue(book.getBookType());
                newRow.createCell(5).setCellValue(book.getOtherSize());
            }


            try (FileOutputStream fos = new FileOutputStream(filePath)) {// 保存更改到文件
                workbook.write(fos);
            }


            String result = updateDatabase(book);// 更新数据库
            return bookFound ? "图书信息已更新！" : "图书添加成功！" + " " + result;

        } catch (IOException e) {
            e.printStackTrace();
            return "操作失败，请检查文件路径！";
        }
    }

    /**
     * 更新图书信息到MySQL数据库
     * @param book 图书对象
     * @return 操作结果
     */
    private static String updateDatabase(Book book) {
        String result = "";
        try (Connection conn = MySQLConnection.getConnection()) {  // 使用已连接的数据库
            if (isBookExistsInDatabase(book.getIbsn())) {

                String updateQuery = "UPDATE book SET title = ?, author = ?, publicationDate = ?, bookType = ?, otherSize = ? WHERE ibsn = ?";// 更新数据库中的图书信息
                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, book.getTitle());
                    stmt.setString(2, book.getAuthor());
                    stmt.setInt(3, Integer.parseInt(book.getPublicationDate()));
                    stmt.setString(4, book.getBookType());
                    stmt.setString(5, book.getOtherSize());
                    stmt.setString(6, book.getIbsn());
                    int rowsUpdated = stmt.executeUpdate();
                    result = rowsUpdated > 0 ? "数据库图书信息已更新！" : "数据库更新失败！";
                }
            } else {

                String insertQuery = "INSERT INTO book (title, author, publicationDate, ibsn, bookType, otherSize) VALUES (?, ?, ?, ?, ?, ?)";// 插入新图书信息
                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setString(1, book.getTitle());
                    stmt.setString(2, book.getAuthor());
                    stmt.setInt(3, Integer.parseInt(book.getPublicationDate()));
                    stmt.setString(4, book.getIbsn());
                    stmt.setString(5, book.getBookType());
                    stmt.setString(6, book.getOtherSize());
                    stmt.executeUpdate();
                    result = "数据库图书添加成功！";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "数据库操作失败！";
        }
        return result;
    }

    /**
     * 从用户输入获取图书信息并添加或更新
     */
    public static void addBookFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入图书标题: ");
        String title = scanner.nextLine();

        System.out.print("请输入作者: ");
        String author = scanner.nextLine();

        System.out.print("请输入出版日期: ");
        String publicationDate = scanner.nextLine();

        System.out.print("请输入ISBN: ");
        String ibsn = scanner.nextLine();

        System.out.print("请输入图书类型: ");
        String bookType = scanner.nextLine();

        System.out.print("请输入其他信息: ");
        String otherSize = scanner.nextLine();


        Book book = BookFactory.createBook(title, author, publicationDate, ibsn, bookType, otherSize); // 创建图书对象


        if (isBookExistsInExcel(ibsn)) {
            System.out.println("图书已存在，正在更新...");
        } else {
            System.out.println("图书不存在，正在添加...");
        }


        String result = addOrUpdateBook(book);// 添加或更新图书信息
        System.out.println(result);
    }
}



