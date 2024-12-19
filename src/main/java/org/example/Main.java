package org.example;

import java.util.Scanner;

public class Main {

    // 标记是否登录以及是否为管理员
    private static boolean isLoggedIn = false;
    private static boolean isAdmin = false;

    public static void main(String[] args) {

        // 启动备份线程
        Thread backupThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BookBackupUtil.startAutoBackup();  // 启动自动备份任务
            }
        });
        backupThread.start();

        // 启动用户交互线程
        Thread userInteractionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                User us1 = new Admin();
                User us2 = new RegularUser();

                while (true) {
                    System.out.println("\n欢迎使用图书管理系统");
                    System.out.println("1. 注册");
                    System.out.println("2. 登录");
                    System.out.println("3. 搜索查看书籍");
                    System.out.println("4. 添加更新书籍（仅限管理员）");
                    System.out.println("5. 删除书籍（仅限管理员）");
                    System.out.println("6. 分类打印（需要管理员权限）");
                    System.out.println("7. 注销用户（需要管理员权限）");
                    System.out.println("8. 备份图书(需要管理员权限)");
                    System.out.println("9. 恢复图书(需要管理员权限)");
                    System.out.println("10. 退出");
                    System.out.print("请输入您的选择: ");
                    int choice = scanner.nextInt(); // 用户选择
                    scanner.nextLine(); // 消耗换行符

                    switch (choice) {
                        case 1:
                            UserRegistrationUtil.RegisterUser();   // 注册
                            break;
                        case 2:
                            int a = UserLoginUtil.LoginUser();
                            System.out.println(11);
                            if(a == 1){
                                isLoggedIn = true;
                                isAdmin = true;
                            }
                            if(a == 2){
                                isLoggedIn = true;
                                isAdmin = false;
                            }
                            if(a == 0){
                                isLoggedIn = false;
                                isAdmin = false;
                            }
                            // 登录
                            break;
                        case 3:
                            if (isLoggedIn) {
                                us2.book_search();
                                // 搜索查看书籍
                            } else {
                                System.out.println("请先登录查看书籍。");
                            }
                            break;
                        case 4:
                            if (isAdmin && isLoggedIn) {
                                us1.book_add_update();
                                // 添加或更新书籍（仅限管理员）
                            } else {
                                System.out.println("需要管理员权限。");
                            }
                            break;
                        case 5:
                            if (isAdmin && isLoggedIn) {
                                us1.book_delete();
                                // 删除书籍（仅限管理员）
                            } else {
                                System.out.println("需要管理员权限。");
                            }
                            break;
                        case 6:
                            if (isAdmin && isLoggedIn) {
                                Admin admin = new Admin();
                                admin.book_search();
                                // 分类打印书籍（仅限管理员）
                            } else {
                                System.out.println("需要管理员权限。");
                            }
                            break;
                        case 7:
                            if (isLoggedIn){
                                int b;
                                b = UserLogoutUtil.UserLogout();
                                if(b == 1){
                                    return; // 注销
                                }
                            }
                            System.out.println("请先登录");
                            break;
                        case 8:
                            if (isAdmin && isLoggedIn) {
                                FileBackupRestore.backupFile();
                            } else {
                                System.out.println("需要管理员权限。");
                            }
                            break;
                        case 9:
                            if (isAdmin && isLoggedIn) {
                                FileBackupRestore.restoreFile();
                            } else {
                                System.out.println("需要管理员权限。");
                            }
                            break;
                        case 10:
                            System.out.println("正在退出系统。");
                            return; // 退出系统
                        default:
                            System.out.println("无效选择。请输入1到8之间的数字。");
                    }
                }
            }
        });
        userInteractionThread.start();
    }
}
