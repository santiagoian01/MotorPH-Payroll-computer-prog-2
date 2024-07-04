import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestUI {
    private static final List<String> leaveRecords = new ArrayList<>();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String LEAVE_FILE_PATH = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Leave_Records.csv";
    private static final String EMPLOYEE_DATABASE_FILE = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
    private static String employeeId = ""; // Placeholder for employee ID

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loadLeaveRecordsFromFile();
            showLeaveRequestDialog(null);
        });
    }

    public static void showLeaveRequestDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Leave Request", true);
        JPanel panel = new JPanel(new BorderLayout());
        dialog.add(panel);

        JPanel inputPanel = new JPanel();
        JLabel employeeIdLabel = new JLabel("Employee ID: ");
        JTextField employeeIdField = new JTextField(15); // Input field for employee ID
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD): ");
        JTextField startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD): ");
        JTextField endDateField = new JTextField(10);
        JLabel reasonLabel = new JLabel("Reason for Leave: ");
        JTextField reasonField = new JTextField(20);

        inputPanel.add(employeeIdLabel);
        inputPanel.add(employeeIdField);
        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(reasonLabel);
        inputPanel.add(reasonField);

        panel.add(inputPanel, BorderLayout.NORTH);

        JButton fileLeaveButton = new JButton("File Leave");
        fileLeaveButton.addActionListener(e -> {
            employeeId = employeeIdField.getText().trim(); // Get the entered employee ID
            if (isValidEmployeeId(employeeId)) {
                String startDateStr = startDateField.getText().trim();
                String endDateStr = endDateField.getText().trim();
                String reason = reasonField.getText().trim();

                if (isValidDate(startDateStr) && isValidDate(endDateStr)) {
                    LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
                    LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);

                    if (!startDate.isAfter(endDate)) {
                        fileLeaveRequest(startDate, endDate, reason);
                        JOptionPane.showMessageDialog(dialog, "Leave filed from " + startDateStr + " to " + endDateStr);
                        employeeIdField.setText(""); // Clear the employee ID field after filing leave
                        startDateField.setText(""); // Clear the start date field after filing leave
                        endDateField.setText(""); // Clear the end date field after filing leave
                        reasonField.setText(""); // Clear the reason field after filing leave
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Start date must be before end date!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Employee ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(fileLeaveButton, BorderLayout.CENTER);

        JButton viewLeaveButton = new JButton("View Leave Records");
        viewLeaveButton.addActionListener(e -> {
            String inputEmployeeId = JOptionPane.showInputDialog(dialog, "Enter Employee ID to view records:", "View Leave Records", JOptionPane.PLAIN_MESSAGE);
            if (inputEmployeeId != null && !inputEmployeeId.trim().isEmpty() && isValidEmployeeId(inputEmployeeId.trim())) {
                showLeaveRecords(dialog, inputEmployeeId.trim());
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Employee ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(viewLeaveButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static boolean isValidEmployeeId(String empId) {
        // check if eid exist in MS1 java gui\lib\Employee_Database - Employee_Database.csv
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(empId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void fileLeaveRequest(LocalDate startDate, LocalDate endDate, String reason) {
        String record = employeeId + "," + startDate.format(dateFormatter) + "," + endDate.format(dateFormatter) + "," + reason;
        leaveRecords.add(record);
        saveLeaveRecordToFile(record);
    }

    static void showLeaveRecords(Component parent, String inputEmployeeId) {
        List<String[]> filteredRecords = new ArrayList<>();
        for (String record : leaveRecords) {
            String[] parts = record.split(",");
            if (parts.length == 4 && parts[0].equals(inputEmployeeId)) {
                filteredRecords.add(parts);
            }
        }

        String[] columnNames = {"Employee ID", "Start Date", "End Date", "Reason"};
        String[][] data = new String[filteredRecords.size()][4];
        for (int i = 0; i < filteredRecords.size(); i++) {
            data[i] = filteredRecords.get(i);
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog recordsDialog = new JDialog((Frame) null, "Leave Records for Employee ID: " + inputEmployeeId, true);
        recordsDialog.add(scrollPane);
        recordsDialog.setSize(500, 300);
        recordsDialog.setLocationRelativeTo(parent);
        recordsDialog.setVisible(true);
    }

    private static void loadLeaveRecordsFromFile() {
        File file = new File(LEAVE_FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    leaveRecords.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveLeaveRecordToFile(String record) {
        File file = new File(LEAVE_FILE_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
