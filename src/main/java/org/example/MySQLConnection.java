package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    // 修改方法返回类型为 Connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Book_storage", "root", "111111");

            if (connection != null) {
                System.out.println("成功连接到数据库！");
            } else {
                System.out.println("数据库连接失败！");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("连接数据库时出现错误！");
        }
        return connection;
    }
}

