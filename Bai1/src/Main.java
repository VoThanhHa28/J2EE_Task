import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner x = new Scanner(System.in);
        String msg = """
                Chương trình quản lý sách
                    1. Thêm 1 cuốn sách
                    2. Xóa 1 cuốn sách
                    3. Thay đổi sách
                    4. Xuất thông tin
                    5. Tìm sách Lập trình
                    6. Lấy sách tối đa theo giá
                    7. Tìm kiếm theo tác giả
                    0. Thoát
                Chọn chức năng:""";

        int chon = 0;
        do {
            System.out.printf(msg);
            chon = x.nextInt();
            switch (chon) {
                case 1 -> {
                    Book newBook = new Book();
                    newBook.input();
                    listBook.add(newBook);
                }
                case 2 -> {
                    System.out.print("Nhập vào mã sách cần xóa:");
                    int bookid = x.nextInt();
                    // kiem tra ma sach
                    Book find = listBook.stream().filter(p -> p.getId() == bookid).findFirst().orElseThrow();
                    listBook.remove(find);
                    System.out.print("Đã xóa sách thành công");
                }
                case 3 -> {
                    System.out.print("Nhập vào mã sách cần điều chỉnh:");
                    int bookid = x.nextInt();
                    Book find = listBook.stream().filter(p -> p.getId() == bookid).findFirst().orElseThrow();
                }
                case 4 -> {
                    System.out.println("\n Xuất thông tin danh sách ");
                    listBook.forEach(p -> p.output());
                }

                case 5->{
                    List<Book> list5 = listBook.stream()
                            .filter(u-> u.getTitle().toLowerCase().contains("Lập trình")).toList();
                    list5.forEach(Book::output);
                }
                case 6-> {
                    System.out.println("Nhập số lượng sách cần lấy:");
                    int limit = x.nextInt();
                    System.out.println("Danh sách " + limit + " sách có giá cao nhất:");
                    listBook.stream()
                            .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                            .limit(limit)
                            .forEach(p-> p.output());
                }
                case 7-> {
                    System.out.println("Nhập tên tác giả cần tìm:");
                    x.nextLine();
                    String author = x.nextLine();

                    Set<String> authorSet = new HashSet<>();
                    authorSet.add(author);

                    System.out.println("Danh sách sách của tác giả " + author + ":");
                    listBook.stream()
                            .filter(p-> authorSet.contains(p.getAuthor()))
                            .forEach(p-> p.output());
                }

                case 8-> {
                    System.out.println("Thoát chương trình.  Tạm biệt!");
                    chon = 0;
                }
            }
        } while (chon != 0);
    }
}