package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class BookSearchUtil {

    /**
     * 根据书名查找书籍，并根据类别创建相应的书籍对象（多态）
     *
     * @param bookName 要查找的书名
     * @return 根据类别创建的书籍对象（Book、Computer_book、Literature_book、Law_book）
     */
    public static Book searchBookByName(String bookName) {
        String filePath = "./src/main/java/file/book.xlsx";
        Book foundBook = null;


        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                Cell titleCell = row.getCell(0);
                Cell authorCell = row.getCell(1);
                Cell publicationDateCell = row.getCell(2);
                Cell ibsnCell = row.getCell(3);
                Cell bookTypeCell = row.getCell(4);
                Cell otherSizeCell = row.getCell(5);

                // 检查每一列是否为空，防止空指针异常
                if (titleCell != null && authorCell != null && publicationDateCell != null &&
                        ibsnCell != null && bookTypeCell != null && otherSizeCell != null) {

                    String title = titleCell.getStringCellValue();
                    String author = authorCell.getStringCellValue();
                    String publicationDate = publicationDateCell.getStringCellValue();
                    String ibsn = ibsnCell.getStringCellValue();
                    String bookType = bookTypeCell.getStringCellValue();
                    String otherSize = otherSizeCell.getStringCellValue();


                    if (title.equalsIgnoreCase(bookName)) {

                        switch (bookType.toLowerCase()) {
                            case "计算机":
                                foundBook = new Computer_book(title, author, publicationDate, ibsn, bookType, otherSize);
                                break;
                            case "文学":
                                foundBook = new Literature_book(title, author, publicationDate, ibsn, bookType, otherSize);
                                break;
                            case "法学":
                                foundBook = new Law_book(title, author, publicationDate, ibsn, bookType, otherSize);
                                break;
                            default:
                                foundBook = new Book(title, author, publicationDate, ibsn, bookType, otherSize);
                        }
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果没有找到书籍，返回 null
        return foundBook;
    }

    /**
     * 打印书籍的详细信息
     *
     * @param book 要显示的书籍对象
     * @param info 要查看的具体信息类型：bookType, title, author, publicationDate, ibsn, othersize 或 all
     */
    public static void displayBookInfo(Book book, String info) {
        if (book == null) {
            System.out.println("未找到该书籍！");
            return;
        }

        switch (info.toLowerCase()) {
            case "title":
                System.out.println("书名: " + book.getTitle());
                break;
            case "author":
                System.out.println("作者: " + book.getAuthor());
                break;
            case "publicationdate":
                System.out.println("出版日期: " + book.getPublicationDate());
                break;
            case "ibsn":
                System.out.println("ISBN: " + book.getIbsn());
                break;
            case "booktype":
                System.out.println("书籍类型: " + book.getBookType());
                break;
            case "othersize":
                System.out.println("其他尺寸: " + book.getOtherSize());
                break;
            case "all":

                System.out.println("书名: " + book.getTitle());
                System.out.println("作者: " + book.getAuthor());
                System.out.println("出版日期: " + book.getPublicationDate());
                System.out.println("ISBN: " + book.getIbsn());
                System.out.println("书籍类型: " + book.getBookType());
                System.out.println("其他尺寸: " + book.getOtherSize());
                break;
            default:
                System.out.println("无效的查询选项！");
                break;
        }
    }


}


