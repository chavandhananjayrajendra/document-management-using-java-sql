
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;


public class DocumentManagement {
    private final Connection conn;
    private final Scanner scanner;


    public DocumentManagement() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/document_management", "dhanu", "dhanu");
        this.scanner = new Scanner(System.in);
    }

    public void createDocument(String documentType) throws SQLException {
        // Code for createDocument method
        System.out.println("Are they a resident of India? (yes/no)");
        String isIndianResident = scanner.nextLine();

        if (!isIndianResident.equalsIgnoreCase("yes")) {
            System.out.println("Only Indian residents are eligible to apply for documents in this system.");
            return;
        }

        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter address:");
        String address = scanner.nextLine();
        System.out.println("Enter email ID:");
        String emailId = scanner.nextLine();
        System.out.println("Enter gender:");
        String gender = scanner.nextLine();

        System.out.println("\nDetails entered:");
        System.out.println("Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Address: " + address);
        System.out.println("Email ID: " + emailId);
        System.out.println("Gender: " + gender);

        System.out.println("\nPlease verify if all details are correct. (yes/no)");
        String verifyDetails = scanner.nextLine();

        if (!verifyDetails.equalsIgnoreCase("yes")) {
            System.out.println("Please re-enter the details.");
            return;
        }

        System.out.println("Enter authority name:");
        String authorityName = scanner.nextLine();

        String documentNumber = generateDocumentNumber();
        String uniqueId = generateUniqueId();

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO documents (document_type, document_number, name, phone_number, address, email_id, gender, authority_name, unique_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, documentType);
            stmt.setString(2, documentNumber);
            stmt.setString(3, name);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, address);
            stmt.setString(6, emailId);
            stmt.setString(7, gender);
            stmt.setString(8, authorityName);
            stmt.setString(9, uniqueId);
            stmt.executeUpdate();
        }

        System.out.println("Document assigned successfully.");
        System.out.println("Unique ID: " + uniqueId);
    }

    private String generateDocumentNumber() {
        // Generate a random 10-digit document number
        Random random = new Random();
        int documentNumber = 1000000000 + random.nextInt(900000000);
        return String.valueOf(documentNumber);
    }

    private String generateUniqueId() {
        // Generate a random UUID (unique identifier)
        return UUID.randomUUID().toString();
    }
    public void updateDocument() throws SQLException {
        System.out.println("Enter unique ID of the document to update:");
        String uniqueId = scanner.nextLine();

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM documents WHERE unique_id = ?")) {
            stmt.setString(1, uniqueId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Select field to update:");
                System.out.println("1. Phone Number");
                System.out.println("2. Address");
                System.out.println("3. Email ID");
                System.out.print("Enter your choice: ");
                int fieldChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (fieldChoice) {
                    case 1:
                        System.out.println("Enter updated phone number:");
                        String phoneNumber = scanner.nextLine();
                        updateDocumentField(uniqueId, "phone_number", phoneNumber);
                        break;
                    case 2:
                        System.out.println("Enter updated address:");
                        String address = scanner.nextLine();
                        updateDocumentField(uniqueId, "address", address);
                        break;
                    case 3:
                        System.out.println("Enter updated email ID:");
                        String emailId = scanner.nextLine();
                        updateDocumentField(uniqueId, "email_id", emailId);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

                System.out.println("Document updated successfully.");
            } else {
                System.out.println("Document with unique ID " + uniqueId + " not found.");
            }
        }
    }

    private void updateDocumentField(String uniqueId, String fieldName, String value) throws SQLException {
        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE documents SET " + fieldName + " = ? WHERE unique_id = ?")) {
            updateStmt.setString(1, value);
            updateStmt.setString(2, uniqueId);
            updateStmt.executeUpdate();
        }
    }



    public void findDocument() throws SQLException {
        System.out.println("Select search option:");
        System.out.println("1. Find by Unique ID");
        System.out.println("2. Find by Mobile Number");
        System.out.println("3. Find by Email");
        System.out.println("4. Find by Address");
        System.out.print("Enter your choice: ");

        int searchOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (searchOption) {
            case 1:
                System.out.println("Enter unique ID of the document to find:");
                String uniqueId = scanner.nextLine();
                findByUniqueId(uniqueId);
                break;
            case 2:
                System.out.println("Enter mobile number of the document to find:");
                String mobileNumber = scanner.nextLine();
                findByMobileNumber(mobileNumber);
                break;
            case 3:
                System.out.println("Enter email of the document to find:");
                String email = scanner.nextLine();
                findByEmail(email);
                break;
            case 4:
                System.out.println("Enter address of the document to find:");
                String address = scanner.nextLine();
                findByAddress(address);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void findByUniqueId(String uniqueId) throws SQLException {
        // Code for findByUniqueId method
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM documents WHERE unique_id = ?")) {
            stmt.setString(1, uniqueId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                printDocumentDetails(rs);
            } else {
                System.out.println("Document with unique ID " + uniqueId + " not found.");
            }
        }
    }

    private void findByMobileNumber(String mobileNumber) throws SQLException {
        // Code for findByMobileNumber method
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM documents WHERE phone_number = ?")) {
            stmt.setString(1, mobileNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                printDocumentDetails(rs);
            } else {
                System.out.println("Document with mobile number " + mobileNumber + " not found.");
            }
        }
    }

    private void findByEmail(String email) throws SQLException {
        // Code for findByEmail method
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM documents WHERE email_id = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                printDocumentDetails(rs);
            } else {
                System.out.println("Document with email " + email + " not found.");
            }
        }
    }

    private void findByAddress(String address) throws SQLException {
        // Code for findByAddress method
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM documents WHERE address = ?")) {
            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                printDocumentDetails(rs);
            } else {
                System.out.println("Document with address " + address + " not found.");
            }
        }
    }


    private void printDocumentDetails(ResultSet rs) throws SQLException {
        // Code for printDocumentDetails method
        System.out.println("Document Type: " + rs.getString("document_type"));
        System.out.println("Document Number: " + rs.getString("document_number"));
        System.out.println("Name: " + rs.getString("name"));
        System.out.println("Phone Number: " + rs.getString("phone_number"));
        System.out.println("Address: " + rs.getString("address"));
        System.out.println("Email ID: " + rs.getString("email_id"));
        System.out.println("Gender: " + rs.getString("gender"));
        System.out.println("Authority Name: " + rs.getString("authority_name"));
        System.out.println("Unique ID:"+ rs.getString("unique_id"));
    }
    public void viewDocumentList() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM documents");
            while (rs.next()) {
                printDocumentDetails(rs);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            DocumentManagement documentManagement = new DocumentManagement();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Create Aadhar Card");
                System.out.println("2. Create PAN Card");
                System.out.println("3. Create Voter ID");
                System.out.println("4. Update Document");
                System.out.println("5. Find Document");
                System.out.println("6. View Document List");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        documentManagement.createDocument("Aadhar Card");
                        break;
                    case 2:
                        documentManagement.createDocument("PAN Card");
                        break;
                    case 3:
                        documentManagement.createDocument("Voter ID");
                        break;
                    case 4:
                        documentManagement.updateDocument();
                        break;
                    case 5:
                        documentManagement.findDocument();
                        break;
                    case 6:
                        documentManagement.viewDocumentList();
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error!");
            e.printStackTrace();
        }
    }}