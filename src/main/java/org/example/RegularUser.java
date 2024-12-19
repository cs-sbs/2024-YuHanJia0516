package org.example;

import java.util.Scanner;

public class RegularUser extends User {
    public RegularUser() {
    }

    public RegularUser(String name, String password) {
        super(name, password);
    }

    @Override
    public void book_add_update() {
        System.out.println("没有权限");
    }

    @Override
    public void book_delete() {
        System.out.println("没有权限");
    }


    @Override
    public void book_search() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你想查看的书籍名字");
        String searchTitle = scanner.nextLine();
        Book book = BookSearchUtil.searchBookByName(searchTitle);

        if (book != null) {
            // 让用户选择要查看的书籍信息
            System.out.println("请输入你想查看的书籍信息 (title, author, publicationDate, ibsn, bookType, othersize, all): ");
            String info = scanner.nextLine();


            BookSearchUtil.displayBookInfo(book, info);
        } else {
            System.out.println("未找到名为 '" + searchTitle + "' 的书籍");


        }
    }
}
