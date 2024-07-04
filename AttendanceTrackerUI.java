import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTrackerUI {
    private static final List<String> attendanceRecords = new ArrayList<>();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String ATTENDANCE_FILE_PATH = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Attendance_records.csv";
    private static final String EMPLOYEE_DATABASE_FILE = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
    private static String employeeId = ""; // Placeholder for employee ID
    protected static Object showLeaveRequestDialog;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loadAttendanceRecordsFromFile();
            showAttendanceTrackerDialog(null);
        });
    }

    public static void showAttendanceTrackerDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Attendance Tracker", true);
        JPanel panel = new JPanel(new BorderLayout());
        dialog.add(panel);

        JPanel inputPanel = new JPanel();
        JLabel employeeIdLabel = new JLabel("Employee ID: ");
        JTextField employeeIdField = new JTextField(15); // Input field for employee ID
        JLabel dayLabel = new JLabel("Day: ");
        JComboBox<Integer> dayComboBox = new JComboBox<>(getDays());
        JLabel monthLabel = new JLabel("Month: ");
        JComboBox<String> monthComboBox = new JComboBox<>(getMonths());
        JLabel yearLabel = new JLabel("Year: ");
        JComboBox<Integer> yearComboBox = new JComboBox<>(getYears());

        inputPanel.add(employeeIdLabel);
        inputPanel.add(employeeIdField);
        inputPanel.add(dayLabel);
        inputPanel.add(dayComboBox);
        inputPanel.add(monthLabel);
        inputPanel.add(monthComboBox);
        inputPanel.add(yearLabel);
        inputPanel.add(yearComboBox);

        panel.add(inputPanel, BorderLayout.NORTH);

        JButton markPresentButton = new JButton("Mark Present");
        markPresentButton.addActionListener(e -> {
            employeeId = employeeIdField.getText().trim(); // Get the entered employee ID
            if (isValidEmployeeId(employeeId)) {
                int day = (Integer) dayComboBox.getSelectedItem();
                int month = monthComboBox.getSelectedIndex() + 1;
                int year = (Integer) yearComboBox.getSelectedItem();

                LocalDate selectedDate = LocalDate.of(year, month, day);
                markAttendance(selectedDate);
                JOptionPane.showMessageDialog(dialog, "Attendance marked for " + selectedDate.format(dateFormatter));
                employeeIdField.setText(""); // Clear the employee ID field after marking attendance
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Employee ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(markPresentButton, BorderLayout.CENTER);

        JButton viewAttendanceButton = new JButton("View Attendance Records");
        viewAttendanceButton.addActionListener(e -> {
            String inputEmployeeId = JOptionPane.showInputDialog(dialog, "Enter Employee ID to view records:", "View Attendance Records", JOptionPane.PLAIN_MESSAGE);
            if (inputEmployeeId != null && !inputEmployeeId.trim().isEmpty() && isValidEmployeeId(inputEmployeeId.trim())) {
                showAttendanceRecords(dialog, inputEmployeeId.trim());
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Employee ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(viewAttendanceButton, BorderLayout.SOUTH);

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

    private static Integer[] getDays() {
        Integer[] days = new Integer[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = i;
        }
        return days;
    }

    private static String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    private static Integer[] getYears() {
        Integer[] years = new Integer[50];
        for (int i = 0; i < 50; i++) {
            years[i] = 2024 + i; // Start from year 2024
        }
        return years;
    }

    private static void markAttendance(LocalDate date) {
        String record = employeeId + "," + date.format(dateFormatter);
        attendanceRecords.add(record);
        saveAttendanceRecordToFile(record);
    }

    private static void showAttendanceRecords(Component parent, String inputEmployeeId) {
        List<String[]> filteredRecords = new ArrayList<>();
        for (String record : attendanceRecords) {
            String[] parts = record.split(",");
            if (parts.length == 2 && parts[0].equals(inputEmployeeId)) {
                filteredRecords.add(parts);
            }
        }

        String[] columnNames = {"Employee ID", "Date"};
        String[][] data = new String[filteredRecords.size()][2];
        for (int i = 0; i < filteredRecords.size(); i++) {
            data[i] = filteredRecords.get(i);
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog recordsDialog = new JDialog((Frame) null, "Attendance Records for Employee ID: " + inputEmployeeId, true);
        recordsDialog.add(scrollPane);
        recordsDialog.setSize(300, 200);
        recordsDialog.setLocationRelativeTo(parent);
        recordsDialog.setVisible(true);
    }

    private static void loadAttendanceRecordsFromFile() {
        File file = new File(ATTENDANCE_FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    attendanceRecords.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveAttendanceRecordToFile(String record) {
        File file = new File(ATTENDANCE_FILE_PATH);
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
