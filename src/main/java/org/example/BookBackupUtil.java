package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class BookBackupUtil {
    private static final String BACKUP_FILE_PATH = "./src/main/java/file/book_backup.xlsx";  // 定义备份文件路径

    /**
     * 从数据库中备份书籍数据到Excel文件
     */
    public static void backupBooksToExcel() {
        try (Connection connection = MySQLConnection.getConnection()) {   // 使用 MySQLConnection 类来获取数据库连接

            if (connection == null) {
                System.out.println("数据库连接失败，无法进行备份！");
                return;
            }
            String query = "SELECT title, author, publicationDate, ibsn, bookType, quantity, otherSize FROM book";// 查询所有书籍数据
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                Workbook workbook = new XSSFWorkbook();// 创建Excel工作簿和工作表
                Sheet sheet = workbook.createSheet("Books");

                Row headerRow = sheet.createRow(0);
                String[] columnNames = {"书名", "作者", "出版日期", "ISBN", "书籍类别", "库存数量", "其他信息"};// 设置标题行
                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                }

                int rowNum = 1;  // 从第二行开始  填充数据
                while (rs.next()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rs.getString("title"));
                    row.createCell(1).setCellValue(rs.getString("author"));
                    row.createCell(2).setCellValue(rs.getInt("publicationDate"));
                    row.createCell(3).setCellValue(rs.getString("ibsn"));
                    row.createCell(4).setCellValue(rs.getString("bookType"));
                    row.createCell(5).setCellValue(rs.getInt("quantity"));
                    row.createCell(6).setCellValue(rs.getString("otherSize"));
                }


                try (FileOutputStream fos = new FileOutputStream(BACKUP_FILE_PATH)) {// 将数据写入Excel文件
                    workbook.write(fos);
                    System.out.println("数据库内容已成功备份到 " + BACKUP_FILE_PATH);
                }

            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("发生错误，备份失败！");
        }
    }

    /**
     * 定时备份：每隔一分钟自动备份一次
     */
    public static void startAutoBackup() {
        // 创建定时器任务，定时调用备份方法
        Timer timer = new Timer(true);
        TimerTask backupTask = new TimerTask() {
            @Override
            public void run() {
                backupBooksToExcel();
            }
        };

        // 每隔一分钟执行一次备份
        timer.scheduleAtFixedRate(backupTask, 0, 60000);  // 延迟0秒后，每隔60秒执行一次
    }
}
