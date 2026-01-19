import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Book {
    private int id;
    private String title;
    private String author;
    private long price;

    public Book(int id, String title, String author, long price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void input() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap ma sach: ");
        this.id = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhap ten sach: ");
        this.title = scanner.nextLine();
        System.out.println("Nhap tac gia: ");
        this.author = scanner.nextLine();
        System.out.println("Nhap don gia: ");
        this.price = scanner.nextLong();
    }

    public void output() {
        String msg = """
                BOOK: id= %d, title=%s, author=%s, price=%d""".formatted(id, title, author, price);
        System.out.println(msg);
    }
}


