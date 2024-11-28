import java.util.*;

public class LibrarySystem {

    private static final Library library = new Library();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcomeMessage();
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            processUserChoice(choice);
        }
    }

    // Display welcome message
    private static void displayWelcomeMessage() {
        System.out.println("Welcome to the Library System.");
    }

    // Display menu options to the user
    private static void displayMenu() {
        System.out.println("\n======== Library Menu ========");
        System.out.println("1. Add Books");
        System.out.println("2. Borrow Books");
        System.out.println("3. Return Books");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    // Capture and validate the user choice input
    private static int getUserChoice() {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) {
                    return choice;
                }
            } else {
                scanner.next(); // Consume invalid input
            }
            System.out.print("Invalid input. Please choose a number between 1 and 4: ");
        }
    }

    // Process the user's choice by delegating to respective method
    private static void processUserChoice(int choice) {
        switch (choice) {
            case 1 -> addBook();
            case 2 -> borrowBook();
            case 3 -> returnBook();
            case 4 -> exit();
            default -> System.out.println("Unexpected error. Please try again.");
        }
    }

    // Add books to the library
    private static void addBook() {
        scanner.nextLine(); // Consume the newline
        System.out.println("\n=== Add a New Book ===");
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        int quantity = getValidPositiveInt("Enter quantity: ");

        library.addOrUpdateBook(new Book(title, author, quantity));
    }

    // Borrow books from the library
    private static void borrowBook() {
        scanner.nextLine(); // Consume the newline
        System.out.println("\n=== Borrow a Book ===");
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        int quantity = getValidPositiveInt("Enter number of books to borrow: ");

        library.borrowBook(title, quantity);
    }

    // Return books to the library
    private static void returnBook() {
        scanner.nextLine(); // Consume the newline
        System.out.println("\n=== Return a Book ===");
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        int quantity = getValidPositiveInt("Enter number of books to return: ");

        library.returnBook(title, quantity);
    }

    // Exit the program
    private static void exit() {
        System.out.println("Exiting Library System. Goodbye!");
        scanner.close();
        System.exit(0);
    }

    // Method to get a valid positive integer
    private static int getValidPositiveInt(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) {
                    return value;
                }
            } else {
                scanner.next(); // Consume invalid input
            }
            System.out.print("Invalid input. Please enter a positive integer: ");
        }
    }
}

// Class to handle library operations (Add, Borrow, Return)
class Library {
    private final Map<String, Book> books = new HashMap<>();

    // Add or update book in the library
    public void addOrUpdateBook(Book book) {
        books.merge(book.getTitle(), book, (existingBook, newBook) -> {
            existingBook.increaseQuantity(newBook.getQuantity());
            return existingBook;
        });
        System.out.println("Successfully added/updated book: " + book);
    }

    // Borrow books from the library
    public void borrowBook(String title, int quantity) {
        Book book = books.get(title);
        if (book == null) {
            System.out.println("Error: Book not found in library.");
        } else if (book.getQuantity() < quantity) {
            System.out.println("Error: Not enough copies available to borrow.");
        } else {
            book.decreaseQuantity(quantity);
            System.out.println("Successfully borrowed " + quantity + " copy(s) of \"" + title + "\".");
        }
    }

    // Return books to the library
    public void returnBook(String title, int quantity) {
        Book book = books.get(title);
        if (book == null) {
            System.out.println("Error: Book not found in library.");
        } else {
            book.increaseQuantity(quantity);
            System.out.println("Successfully returned " + quantity + " copy(s) of \"" + title + "\".");
        }
    }
}

// Book class encapsulating the book details
class Book {
    private final String title;
    private final String author;
    private int quantity;

    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        }
    }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + author + " (Quantity: " + quantity + ")";
    }
}
