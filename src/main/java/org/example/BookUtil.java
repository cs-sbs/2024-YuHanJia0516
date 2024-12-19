package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookUtil {


    public static <T extends Book> List<T> filterBooksByCategory(String category, Class<T> bookType) throws IOException {
        List<T> filteredBooks = new ArrayList<>();


        FileInputStream fis = new FileInputStream(new File("./src/main/java/file/book.xlsx"));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);  // 假设图书数据在第一个工作表


        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 跳过表头行


            String title = row.getCell(0).getStringCellValue();
            String author = row.getCell(1).getStringCellValue();
            String publicationDate = row.getCell(2).getStringCellValue();
            String ibsn = row.getCell(3).getStringCellValue();
            String bookCategory = row.getCell(4).getStringCellValue();
            String otherSize = row.getCell(5).getStringCellValue();


            if (bookCategory.equalsIgnoreCase(category)) {
                T book = createBook(bookCategory, title, author, publicationDate, ibsn, otherSize);
                filteredBooks.add(book);
            }
        }

        workbook.close();
        fis.close();
        return filteredBooks;
    }


    private static <T extends Book> T createBook(String category, String title, String author, String publicationDate, String ibsn, String otherSize) {// 根据类别创建不同的图书对象
        if (category.equalsIgnoreCase("Computer_book")) {
            return (T) new Computer_book(title, author, publicationDate, ibsn, category, otherSize);
        } else if (category.equalsIgnoreCase("Law_book")) {
            return (T) new Law_book(title, author, publicationDate, ibsn, category, otherSize);
        } else if (category.equalsIgnoreCase("Literature_book")) {
            return (T) new Literature_book(title, author, publicationDate, ibsn, category, otherSize);
        }
        return null;
    }
    public static void Classification(){
        try {
            // 筛选计算机类图书
            List<Computer_book> computerBooks = BookUtil.filterBooksByCategory("Computer_book", Computer_book.class);
            System.out.println("计算机类图书:");
            for (Computer_book book : computerBooks) {
                System.out.println("书名: " + book.getTitle() + ", 作者: " + book.getAuthor());
            }

            // 筛选法律类图书
            List<Law_book> lawBooks = BookUtil.filterBooksByCategory("Law_book", Law_book.class);
            System.out.println("\n法律类图书:");
            for (Law_book book : lawBooks) {
                System.out.println("书名: " + book.getTitle() + ", 作者: " + book.getAuthor());
            }

            // 筛选文学类图书
            List<Literature_book> literatureBooks = BookUtil.filterBooksByCategory("Literature_book", Literature_book.class);
            System.out.println("\n文学类图书:");
            for (Literature_book book : literatureBooks) {
                System.out.println("书名: " + book.getTitle() + ", 作者: " + book.getAuthor());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
