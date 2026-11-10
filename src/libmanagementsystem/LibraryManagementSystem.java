
package libmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LibraryManagementSystem extends JFrame {

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Transaction> transactions = new ArrayList<>();

    // HashMaps for direct lookup by ID
    HashMap<Integer, Book> bookMap = new HashMap<>();
    HashMap<Integer, Member> memberMap = new HashMap<>();
    HashMap<Integer, Transaction> transactionMap = new HashMap<>();

    int nextBookId = 1;
    int nextMemberId = 1;
    int nextTransactionId = 1;

    DefaultTableModel bookModel, memberModel, transactionModel;
    JTable bookTable, memberTable, transactionTable;
    JLabel detailsLabel;

    static class Book {
        int id;
        String title, author;
        int quantity;

        Book(int id, String title, String author, int quantity) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.quantity = quantity;
        }
    }

    static class Member {
        int id;
        String name;

        Member(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Transaction {
        int id, bookId, memberId;
        String type, date;
        boolean returned;

        Transaction(int id, int bookId, int memberId,
                    String type, String date) {
            this.id = id;
            this.bookId = bookId;
            this.memberId = memberId;
            this.type = type;
            this.date = date;
            this.returned = false;
        }
    }

    public LibraryManagementSystem() {
        setTitle("Library Management System - TPS");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        // BOOK MANAGEMENT
        JPanel bookPanel = new JPanel(new BorderLayout(5, 5));

        bookModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "Available"}, 0
        );

        bookTable = new JTable(bookModel);
        bookTable.setRowHeight(24);

        JPanel bookButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );

        JButton addBook = new JButton("Add Book");
        JButton updateBook = new JButton("Update Book");
        JButton deleteBook = new JButton("Delete Book");

        bookButtons.add(addBook);
        bookButtons.add(updateBook);
        bookButtons.add(deleteBook);

        bookPanel.add(
            new JScrollPane(bookTable), BorderLayout.CENTER
        );
        bookPanel.add(bookButtons, BorderLayout.SOUTH);

        addBook.addActionListener(e -> addBook());
        updateBook.addActionListener(e -> updateBook());
        deleteBook.addActionListener(e -> deleteBook());

        // MEMBER MANAGEMENT
        JPanel memberPanel = new JPanel(new BorderLayout(5, 5));

        memberModel = new DefaultTableModel(
            new String[]{"Member ID", "Member Name"}, 0
        );

        memberTable = new JTable(memberModel);
        memberTable.setRowHeight(24);

        JButton addMember = new JButton("Register Member");
        JButton deleteMember = new JButton("Delete Member");

        JPanel memberButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );

        memberButtons.add(addMember);
        memberButtons.add(deleteMember);

        memberPanel.add(
            new JScrollPane(memberTable), BorderLayout.CENTER
        );
        memberPanel.add(memberButtons, BorderLayout.SOUTH);

        addMember.addActionListener(e -> addMember());
        deleteMember.addActionListener(e -> deleteMember());

        // TRANSACTION MANAGEMENT
        JPanel transactionPanel = new JPanel(
            new BorderLayout(5, 5)
        );

        transactionModel = new DefaultTableModel(
            new String[]{
                "Transaction ID", "Book ID", "Member ID",
                "Type", "Date", "Status"
            }, 0
        );

        transactionTable = new JTable(transactionModel);
        transactionTable.setRowHeight(24);

        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");

        JPanel transactionButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );

        transactionButtons.add(borrowButton);
        transactionButtons.add(returnButton);

        transactionPanel.add(
            new JScrollPane(transactionTable), BorderLayout.CENTER
        );
        transactionPanel.add(
            transactionButtons, BorderLayout.SOUTH
        );

        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());

        // CARD LAYOUT NAVIGATION
        contentPanel.add(bookPanel, "Books");
        contentPanel.add(memberPanel, "Members");
        contentPanel.add(transactionPanel, "Transactions");

        // HEADER
        JPanel header = new JPanel(new BorderLayout(12, 8));
        header.setBorder(
            BorderFactory.createEmptyBorder(12, 14, 8, 14)
        );

        JLabel heading = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        heading.setFont(new Font("Arial", Font.BOLD, 21));

        header.add(heading, BorderLayout.WEST);

        JPanel upperRight = new JPanel();
        upperRight.setLayout(
            new BoxLayout(upperRight, BoxLayout.Y_AXIS)
        );

        detailsLabel = new JLabel(
            "Books: " + books.size()
            + "   |   Members: " + members.size()
        );

        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel navigation = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 6, 4)
        );

        JButton booksTab = new JButton("Books");
        JButton membersTab = new JButton("Members");
        JButton transactionsTab = new JButton("Transactions");

        navigation.add(booksTab);
        navigation.add(membersTab);
        navigation.add(transactionsTab);

        navigation.setAlignmentX(Component.RIGHT_ALIGNMENT);

        upperRight.add(detailsLabel);
        upperRight.add(navigation);

        header.add(upperRight, BorderLayout.EAST);

        booksTab.addActionListener(
            e -> cardLayout.show(contentPanel, "Books")
        );

        membersTab.addActionListener(
            e -> cardLayout.show(contentPanel, "Members")
        );

        transactionsTab.addActionListener(
            e -> cardLayout.show(contentPanel, "Transactions")
        );

        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // SAMPLE DATA
        Book book1 = new Book(
            nextBookId++, "Java Programming", "John Smith", 5
        );

        Book book2 = new Book(
            nextBookId++, "Database Systems", "Maria Santos", 3
        );

        Member member1 = new Member(
            nextMemberId++, "Juan Dela Cruz"
        );

        books.add(book1);
        books.add(book2);
        members.add(member1);

        bookMap.put(book1.id, book1);
        bookMap.put(book2.id, book2);
        memberMap.put(member1.id, member1);

        refreshTables();
    }

    // ADD BOOK
    void addBook() {
        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField quantity = new JTextField();

        Object[] fields = {
            "Book Title:", title,
            "Author:", author,
            "Quantity:", quantity
        };

        if (JOptionPane.showConfirmDialog(
            this, fields, "Add Book",
            JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION) {

            try {
                String t = title.getText().trim();
                String a = author.getText().trim();
                int q = Integer.parseInt(
                    quantity.getText().trim()
                );

                if (t.isEmpty() || a.isEmpty() || q < 0) {
                    throw new IllegalArgumentException();
                }

                Book newBook = new Book(
                    nextBookId++, t, a, q
                );

                books.add(newBook);
                bookMap.put(newBook.id, newBook);

                refreshTables();
                message("Book added successfully.");

            } catch (Exception ex) {
                message(
                    "Enter a valid title, author, and nonnegative quantity."
                );
            }
        }
    }

    // UPDATE BOOK
    void updateBook() {
        int row = bookTable.getSelectedRow();

        if (row == -1) {
            message("Select a book first.");
            return;
        }

        int id = Integer.parseInt(
            bookModel.getValueAt(row, 0).toString()
        );

        Book b = findBook(id);

        if (b == null) {
            return;
        }

        JTextField title = new JTextField(b.title);
        JTextField author = new JTextField(b.author);
        JTextField quantity = new JTextField(
            String.valueOf(b.quantity)
        );

        Object[] fields = {
            "Book Title:", title,
            "Author:", author,
            "Total Quantity:", quantity
        };

        if (JOptionPane.showConfirmDialog(
            this, fields, "Update Book",
            JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION) {

            try {
                String t = title.getText().trim();
                String a = author.getText().trim();
                int q = Integer.parseInt(
                    quantity.getText().trim()
                );

                int borrowed = borrowedCount(b.id);

                if (t.isEmpty() || a.isEmpty() || q < borrowed) {
                    message(
                        "Quantity cannot be less than books currently borrowed."
                    );
                    return;
                }

                b.title = t;
                b.author = a;
                b.quantity = q;

                refreshTables();
                message("Book updated successfully.");

            } catch (Exception ex) {
                message("Enter valid book information.");
            }
        }
    }

    // DELETE BOOK
    void deleteBook() {
        int row = bookTable.getSelectedRow();

        if (row == -1) {
            message("Select a book first.");
            return;
        }

        int id = Integer.parseInt(
            bookModel.getValueAt(row, 0).toString()
        );

        if (borrowedCount(id) > 0) {
            message(
                "Cannot delete a book with active borrowings."
            );
            return;
        }

        if (JOptionPane.showConfirmDialog(
            this, "Delete this book?",
            "Confirm", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            Book bookToDelete = bookMap.remove(id);
            books.remove(bookToDelete);

            refreshTables();
            message("Book deleted.");
        }
    }

    // REGISTER MEMBER
    void addMember() {
        String name = JOptionPane.showInputDialog(
            this, "Enter member name:"
        );

        if (name != null && !name.trim().isEmpty()) {

            Member newMember = new Member(
                nextMemberId++, name.trim()
            );

            members.add(newMember);
            memberMap.put(newMember.id, newMember);

            refreshTables();
            message("Member registered successfully.");
        }
    }

    // DELETE MEMBER
    void deleteMember() {
        int row = memberTable.getSelectedRow();

        if (row == -1) {
            message("Select a member first.");
            return;
        }

        int id = Integer.parseInt(
            memberModel.getValueAt(row, 0).toString()
        );

        for (Transaction t : transactions) {
            if (t.memberId == id && !t.returned) {
                message(
                    "Cannot delete a member with an active borrowing."
                );
                return;
            }
        }

        if (JOptionPane.showConfirmDialog(
            this, "Delete this member?",
            "Confirm", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            Member memberToDelete = memberMap.remove(id);
            members.remove(memberToDelete);

            refreshTables();
            message("Member deleted.");
        }
    }

    // BORROW BOOK
    void borrowBook() {
        if (books.isEmpty() || members.isEmpty()) {
            message(
                "Add a book and register a member first."
            );
            return;
        }

        String bookInput = JOptionPane.showInputDialog(
            this, "Enter Book ID:"
        );

        if (bookInput == null) {
            return;
        }

        String memberInput = JOptionPane.showInputDialog(
            this, "Enter Member ID:"
        );

        if (memberInput == null) {
            return;
        }

        try {
            int bookId = Integer.parseInt(bookInput.trim());
            int memberId = Integer.parseInt(
                memberInput.trim()
            );

            Book b = findBook(bookId);
            Member m = findMember(memberId);

            if (b == null || m == null) {
                message("Invalid Book ID or Member ID.");
                return;
            }

            if (b.quantity - borrowedCount(b.id) <= 0) {
                message("No available copies of this book.");
                return;
            }

            Transaction newTransaction = new Transaction(
                nextTransactionId++,
                bookId,
                memberId,
                "BORROW",
                java.time.LocalDate.now().toString()
            );

            transactions.add(newTransaction);
            transactionMap.put(
                newTransaction.id, newTransaction
            );

            refreshTables();
            message("Book borrowed successfully.");

        } catch (NumberFormatException ex) {
            message("IDs must be valid numbers.");
        }
    }

    // RETURN BOOK
    void returnBook() {
        int row = transactionTable.getSelectedRow();

        if (row == -1) {
            message(
                "Select a borrowing transaction first."
            );
            return;
        }

        int id = Integer.parseInt(
            transactionModel.getValueAt(row, 0).toString()
        );

        // Direct lookup instead of linear search
        Transaction selected = transactionMap.get(id);

        if (selected == null
            || !selected.type.equals("BORROW")
            || selected.returned) {

            message(
                "Select an active borrowing transaction."
            );
            return;
        }

        if (JOptionPane.showConfirmDialog(
            this, "Confirm return of this book?",
            "Return Book", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            selected.returned = true;

            Transaction returnTransaction = new Transaction(
                nextTransactionId++,
                selected.bookId,
                selected.memberId,
                "RETURN",
                java.time.LocalDate.now().toString()
            );

            transactions.add(returnTransaction);
            transactionMap.put(
                returnTransaction.id, returnTransaction
            );

            refreshTables();
            message("Book returned successfully.");
        }
    }

    // COUNT ACTIVE BORROWINGS
    int borrowedCount(int bookId) {
        int count = 0;

        for (Transaction t : transactions) {
            if (t.type.equals("BORROW")
                && t.bookId == bookId
                && !t.returned) {
                count++;
            }
        }

        return count;
    }

    // HASHMAP LOOKUP: BOOK
    Book findBook(int id) {
        return bookMap.get(id);
    }

    // HASHMAP LOOKUP: MEMBER
    Member findMember(int id) {
        return memberMap.get(id);
    }

    // REFRESH TABLES
    void refreshTables() {
        bookModel.setRowCount(0);
        memberModel.setRowCount(0);
        transactionModel.setRowCount(0);

        for (Book b : books) {
            bookModel.addRow(new Object[]{
                b.id,
                b.title,
                b.author,
                b.quantity - borrowedCount(b.id)
            });
        }

        for (Member m : members) {
            memberModel.addRow(new Object[]{
                m.id,
                m.name
            });
        }

        for (Transaction t : transactions) {
            transactionModel.addRow(new Object[]{
                t.id,
                t.bookId,
                t.memberId,
                t.type,
                t.date,
                t.type.equals("RETURN")
                    ? "Completed"
                    : (t.returned ? "Returned" : "Borrowed")
            });
        }

        if (detailsLabel != null) {
            detailsLabel.setText(
                "Books: " + books.size()
                + "   |   Members: " + members.size()
                + "   |   Transactions: " + transactions.size()
            );
        }
    }

    // DISPLAY MESSAGE
    void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }
}

