import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class HRDashboard {
    private static final String CSV_FILE_PATH = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";

    public static void openHRDashboard(String eid) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame dashboardFrame = new JFrame("HR Dashboard");
        dashboardFrame.setSize(800, 600);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null);

        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome " + eid + " to HR Dashboard!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        JButton viewEmployeesButton = new JButton("View All Employees");
        viewEmployeesButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewEmployeesButton.addActionListener(e -> displayAllEmployees());
        buttonPanel.add(viewEmployeesButton);

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addEmployeeButton.addActionListener(e -> addEmployee());
        buttonPanel.add(addEmployeeButton);

        JButton deleteEmployeeButton = new JButton("Delete Employee");
        deleteEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteEmployeeButton.addActionListener(e -> deleteEmployee());
        buttonPanel.add(deleteEmployeeButton);

        JButton updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        updateEmployeeButton.addActionListener(e -> updateEmployee());
        buttonPanel.add(updateEmployeeButton);

        dashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> {
            dashboardFrame.dispose();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            // Optionally, navigate to login screen or exit the application
        });
        dashboardPanel.add(logoutButton, BorderLayout.SOUTH);

        dashboardFrame.add(dashboardPanel);
        dashboardFrame.setVisible(true);
    }

    private static void displayAllEmployees() {
        SwingUtilities.invokeLater(() -> {
            JFrame employeesFrame = new JFrame("All Employees");
            employeesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            String[] columnNames = {
                "Employee ID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                "SSS#", "Philhealth#", "TIN#", "PagIbig#", "Status", "Position", "Immediate Supervisor",
                "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
                "Gross Semi-Monthly Rate", "Daily Rate","Hourly Rate"
            };
    
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(model);
    
            try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] employeeData = line.split(",");
                    // Skip the second column (password)
                    Object[] rowData = new Object[employeeData.length - 1];
                    for (int i = 0, j = 0; i < employeeData.length; i++) {
                        if (i != 1) { // Skip the second column
                            rowData[j++] = employeeData[i];
                        }
                    }
                    model.addRow(rowData);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
    
            JScrollPane scrollPane = new JScrollPane(table);
            employeesFrame.add(scrollPane);
            employeesFrame.setSize(1000, 600); // Adjusted size for better table visibility
            employeesFrame.setLocationRelativeTo(null); // Center the frame on the screen
            employeesFrame.setVisible(true);
        });
    }
    

    private static void addEmployee() {
        JFrame addEmployeeFrame = new JFrame("Add Employee");
        addEmployeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel addEmployeePanel = new JPanel(new GridLayout(0, 2));

        // Labels and fields for adding employee
        JLabel[] labels = {
                new JLabel("Employee ID:"),
                new JLabel("Password:"),
                new JLabel("Last Name:"),
                new JLabel("First Name:"),
                new JLabel("Birthday:"),
                new JLabel("Address:"),
                new JLabel("Phone Number:"),
                new JLabel("SSS#:"),
                new JLabel("Philhealth#:"),
                new JLabel("TIN#:"),
                new JLabel("PagIbig#:"),
                new JLabel("Status:"),
                new JLabel("Position:"),
                new JLabel("Immediate Supervisor:"),
                new JLabel("Basic Salary:"),
                new JLabel("Rice Subsidy:"),
                new JLabel("Phone Allowance:"),
                new JLabel("Clothing Allowance:"),
                new JLabel("Gross Semi-Monthly Rate:"),
                new JLabel("Daily Rate"),
                new JLabel("Hourly Rate:")
        };

        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField();
            addEmployeePanel.add(labels[i]);
            addEmployeePanel.add(fields[i]);
        }

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] employeeData = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    employeeData[i] = fields[i].getText();
                }
                saveEmployeeRecordToCSV(employeeData);
                addEmployeeFrame.dispose();
            }
        });

        addEmployeePanel.add(addButton);

        addEmployeeFrame.getContentPane().add(addEmployeePanel);
        addEmployeeFrame.setSize(400, 600);
        addEmployeeFrame.setVisible(true);
    }

    private static void saveEmployeeRecordToCSV(String... employeeData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH, true))) {
            StringBuilder sb = new StringBuilder();
            for (String data : employeeData) {
                sb.append(data).append(",");
            }
            sb.deleteCharAt(sb.length() - 1).append("\n");
            writer.write(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteEmployee() {
        JFrame deleteEmployeeFrame = new JFrame("Delete Employee");
        deleteEmployeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel deleteEmployeePanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Delete Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        deleteEmployeePanel.add(titleLabel, constraints);

        constraints.gridy++;
        JLabel employeeIDLabel = new JLabel("Employee ID:");
        deleteEmployeePanel.add(employeeIDLabel, constraints);

        constraints.gridx++;
        JTextField employeeIDField = new JTextField(15);
        deleteEmployeePanel.add(employeeIDField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeID = employeeIDField.getText();
                if (!employeeID.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(deleteEmployeeFrame, "Are you sure you want to delete this employee?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteEmployeeRecord(employeeID);
                        JOptionPane.showMessageDialog(deleteEmployeeFrame, "Employee record deleted successfully!");
                        deleteEmployeeFrame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(deleteEmployeeFrame, "Please enter the Employee ID!");
                }
            }
        });
        deleteEmployeePanel.add(deleteButton, constraints);

        deleteEmployeeFrame.getContentPane().add(deleteEmployeePanel);
        deleteEmployeeFrame.pack();
        deleteEmployeeFrame.setLocationRelativeTo(null); // Center the frame on the screen from chatgpt
        deleteEmployeeFrame.setVisible(true);
    }

    private static void deleteEmployeeRecord(String employeeID) {
        String csvFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
        String tempFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\temp.csv";
        File oldFile = new File(csvFile);
        File newFile = new File(tempFile);
        String line;
        String cvsSplitBy = ",";
    
        boolean found = false;
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
    
            while ((line = br.readLine()) != null) {
                String[] employeeData = line.split(cvsSplitBy);
                if (!employeeData[0].equals(employeeID)) {
                    pw.println(line);
                    pw.flush();
                } else {
                    found = true;
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Replace the original file with the temporary file if employee was found
        if (found) {
            if (!oldFile.delete() || !newFile.renameTo(new File(csvFile))) {
                System.out.println("Failed to delete or rename the file");
            }
        }
    }
    
    private static void updateEmployee() {
        JFrame updateEmployeeFrame = new JFrame("Update Employee");
        updateEmployeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel updateEmployeePanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
    
        JLabel titleLabel = new JLabel("Update Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridwidth = 2;
        updateEmployeePanel.add(titleLabel, constraints);
    
        constraints.gridwidth = 1;
        constraints.gridy++;
        JLabel employeeIDLabel = new JLabel("Employee ID:");
        updateEmployeePanel.add(employeeIDLabel, constraints);
    
        constraints.gridx++;
        JTextField employeeIDField = new JTextField(15);
        updateEmployeePanel.add(employeeIDField, constraints);
    
        constraints.gridx = 0;
        constraints.gridy++;
        JButton searchButton = new JButton("Search");
        updateEmployeePanel.add(searchButton, constraints);
    
        constraints.gridx++;
        constraints.gridwidth = 2;
        JPanel employeeDetailsPanel = new JPanel(new GridBagLayout());
        updateEmployeePanel.add(employeeDetailsPanel, constraints);
    
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeID = employeeIDField.getText();
                if (!employeeID.isEmpty()) {
                    String[] employeeData = findEmployeeByID(employeeID);
                    if (employeeData != null) {
                        employeeDetailsPanel.removeAll();
    
                        GridBagConstraints detailsConstraints = new GridBagConstraints();
                        detailsConstraints.insets = new Insets(5, 5, 5, 5);
                        detailsConstraints.anchor = GridBagConstraints.WEST;
    
                        JLabel[] labels = {
                                new JLabel("Last Name:"),
                                new JLabel("First Name:"),
                                new JLabel("Birthday:"),
                                new JLabel("Address:"),
                                new JLabel("Phone Number:"),
                                new JLabel("SSS#:"),
                                new JLabel("Philhealth#:"),
                                new JLabel("TIN#:"),
                                new JLabel("PagIbig#:"),
                                new JLabel("Status:"),
                                new JLabel("Position:"),
                                new JLabel("Immediate Supervisor:"),
                                new JLabel("Basic Salary:"),
                                new JLabel("Rice Subsidy:"),
                                new JLabel("Phone Allowance:"),
                                new JLabel("Clothing Allowance:"),
                                new JLabel("Gross Semi-Monthly Rate:"),
                                new JLabel("Daily Rate:"),
                                new JLabel("Hourly Rate:")
                        };
    
                        JTextField[] fields = new JTextField[labels.length];
    
                        for (int i = 0, j = 2; i < labels.length; i++, j++) { // Start from 2 to skip Employee ID and Password
                            detailsConstraints.gridx = 0;
                            detailsConstraints.gridy = i;
                            employeeDetailsPanel.add(labels[i], detailsConstraints);
    
                            detailsConstraints.gridx = 1;
                            fields[i] = new JTextField(employeeData[j], 15);
                            employeeDetailsPanel.add(fields[i], detailsConstraints);
                        }
    
                        detailsConstraints.gridx = 0;
                        detailsConstraints.gridy = labels.length;
                        detailsConstraints.gridwidth = 2;
                        detailsConstraints.anchor = GridBagConstraints.CENTER;
                        JButton updateButton = new JButton("Update");
                        updateButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String[] updatedData = new String[fields.length];
                                for (int i = 0; i < fields.length; i++) {
                                    updatedData[i] = fields[i].getText();
                                }
                                updateEmployeeRecord(employeeID, updatedData);
                                JOptionPane.showMessageDialog(updateEmployeeFrame, "Employee record updated successfully!");
                                updateEmployeeFrame.dispose();
                            }
                        });
    
                        employeeDetailsPanel.add(updateButton, detailsConstraints);
                        updateEmployeeFrame.revalidate();
                        updateEmployeeFrame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(updateEmployeeFrame, "Employee not found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(updateEmployeeFrame, "Please enter the Employee ID!");
                }
            }
        });
    
        updateEmployeeFrame.getContentPane().add(updateEmployeePanel);
        updateEmployeeFrame.pack();
        updateEmployeeFrame.setLocationRelativeTo(null);
        updateEmployeeFrame.setVisible(true);
    }
    
    private static String[] findEmployeeByID(String employeeID) {
        String csvFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] employeeData = line.split(",");
                if (employeeData[0].equals(employeeID)) {
                    return employeeData;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void updateEmployeeRecord(String employeeID, String[] updatedData) {
        String csvFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
        String tempFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\temp.csv";
        File oldFile = new File(csvFile);
        File newFile = new File(tempFile);
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] employeeData = line.split(",");
                if (employeeData[0].equals(employeeID)) {
                    for (int i = 0; i < updatedData.length; i++) {
                        employeeData[i + 2] = updatedData[i]; // i + 2 because we skip Employee ID and Password
                    }
                    line = String.join(",", employeeData);
                }
                pw.println(line);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Replace the original file with the temporary file
        if (oldFile.delete()) {
            if (!newFile.renameTo(oldFile)) {
                System.out.println("Failed to rename temp file to original file");
            }
        } else {
            System.out.println("Failed to delete original file");
        }
    }
    
    
}
