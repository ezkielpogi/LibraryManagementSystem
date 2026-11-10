
package libmanagementsystem;

public class Transaction {
    int id;
    int bookId;
    int memberId;
    String type;
    String date;
    boolean returned;

    public Transaction(int id, int bookId, int memberId,
                       String type, String date) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.type = type;
        this.date = date;
        this.returned = false;
    }
}
