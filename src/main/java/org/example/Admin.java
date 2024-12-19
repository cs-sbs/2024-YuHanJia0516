package org.example;
import java.util.Scanner;

public class Admin extends User implements Isolation_Screen{
    public Admin() {
    }

    public Admin(String name, String password) {
        super(name, password);
    }

    @Override
    public void book_add_update() {
        System.out.println("你是admin用户，可以进行增加或者更新");
        BookStorageUtil.addBookFromUserInput();
    }

    @Override
    public void book_delete() {
        System.out.println("你是admin用户，可以进行删除操作");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("请输入要删除的书名: ");
        String bookTitle = scanner.nextLine();
        BookDeletionUtil.deleteBookByTitle(bookTitle);
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

            // 调用方法显示相应信息
            BookSearchUtil.displayBookInfo(book, info);
        } else {
            System.out.println("未找到名为 '" + searchTitle + "' 的书籍");


        }
    }

    @Override
    public void isolation_Screen() {
        System.out.println("VIP分类打印");
        BookUtil.Classification();
    }
}
