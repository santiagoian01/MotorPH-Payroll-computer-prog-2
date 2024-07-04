import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalaryCalculatorUI {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String ATTENDANCE_FILE_PATH = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Attendance_records.csv";
    private static final String EMPLOYEE_DETAILS_FILE = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";

    private static JFrame salaryFrame;
    private static JComboBox<String> monthComboBox;
    private static JTextField employeeIdField;
    private static JTextArea resultTextArea;

    public static void openSalaryCalculator(JFrame parentFrame) {
        salaryFrame = new JFrame("Salary Calculator");
        salaryFrame.setSize(1000, 600);
        salaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel employeeIdLabel = new JLabel("Employee ID: ");
        employeeIdField = new JTextField(10);
        inputPanel.add(employeeIdLabel);
        inputPanel.add(employeeIdField);

        JLabel monthLabel = new JLabel("Select Month: ");
        monthComboBox = new JComboBox<>(getMonthNames());
        inputPanel.add(monthLabel);
        inputPanel.add(monthComboBox);

        JButton computeButton = new JButton("Compute");
        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empId = employeeIdField.getText().trim();
                if (isValidEmployeeId(empId)) {
                    String selectedMonth = (String) monthComboBox.getSelectedItem();
                    calculateAndDisplaySalary(empId, selectedMonth);
                } else {
                    JOptionPane.showMessageDialog(salaryFrame, "Employee ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(computeButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        resultTextArea = new JTextArea(20, 80);
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        salaryFrame.add(mainPanel);
        salaryFrame.pack(); // Adjust frame size
        salaryFrame.setLocationRelativeTo(parentFrame);
        salaryFrame.setVisible(true);
    }

    private static String[] getMonthNames() {
        String[] monthNames = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames;
    }

    private static boolean isValidEmployeeId(String empId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void calculateAndDisplaySalary(String empId, String selectedMonth) {
        List<LocalDate> attendanceDates = loadAttendanceRecords(empId, selectedMonth);
        double dailySalary = getDailySalary(empId);
        int workingDays = attendanceDates.size();

        double riceSubsidy = getRiceSubsidy(empId);
        double phoneAllowance = getPhoneAllowance(empId);
        double clothingAllowance = getClothingAllowance(empId);

        double grossSalary = dailySalary * workingDays;
        double netSalary = grossSalary + riceSubsidy + phoneAllowance + clothingAllowance;

        String employeeDetails = getEmployeeDetails(empId);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder();
        sb.append("Employee ID: ").append(empId).append("\n");
        sb.append(employeeDetails).append("\n\n");
        sb.append("Days Worked: ").append(workingDays).append("\n");
        sb.append("Daily Salary: Php ").append(decimalFormat.format(dailySalary)).append("\n");
        sb.append("Rice Subsidy: Php ").append(decimalFormat.format(riceSubsidy)).append("\n");
        sb.append("Phone Allowance: Php ").append(decimalFormat.format(phoneAllowance)).append("\n");
        sb.append("Clothing Allowance: Php ").append(decimalFormat.format(clothingAllowance)).append("\n");
        sb.append("------------------------------------\n");
        sb.append("Gross Salary: Php ").append(decimalFormat.format(grossSalary)).append("\n");
        sb.append("Net Salary: Php ").append(decimalFormat.format(netSalary)).append("\n");

        resultTextArea.setText(sb.toString());
    }

    private static List<LocalDate> loadAttendanceRecords(String empId, String selectedMonth) {
        List<LocalDate> attendanceDates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].trim().equals(empId)) {
                    LocalDate date = LocalDate.parse(data[1].trim(), dateFormatter);
                    if (date.getMonth().toString().equalsIgnoreCase(selectedMonth.toUpperCase())) {
                        attendanceDates.add(date);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendanceDates;
    }

    private static double getDailySalary(String empId) {
        double dailySalary = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    dailySalary = Double.parseDouble(data[19].trim().replace(",", "")); // Assuming daily salary is in column 20
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return dailySalary;
    }

    private static double getRiceSubsidy(String empId) {
        double riceSubsidy = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    riceSubsidy = Double.parseDouble(data[15].trim().replace(",", "")); // Assuming rice subsidy is in column 16
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return riceSubsidy;
    }

    private static double getPhoneAllowance(String empId) {
        double phoneAllowance = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    phoneAllowance = Double.parseDouble(data[16].trim().replace(",", "")); // Assuming phone allowance is in column 17
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return phoneAllowance;
    }

    private static double getClothingAllowance(String empId) {
        double clothingAllowance = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    clothingAllowance = Double.parseDouble(data[17].trim().replace(",", "")); // Assuming clothing allowance is in column 18
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return clothingAllowance;
    }

    private static String getEmployeeDetails(String empId) {
        StringBuilder details = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(empId)) {
                    String lastName = data[2].trim();
                    String firstName = data[3].trim();

                    // Append Last Name, First Name
                    details.append("Last Name: ").append(lastName).append("\n");
                    details.append("First Name: ").append(firstName).append("\n");

                    // Append other details
                    details.append("SSS No.: ").append(data[7].trim()).append("\n");
                    details.append("Philhealth No.: ").append(data[8].trim()).append("\n");
                    details.append("TIN: ").append(data[9].trim()).append("\n");
                    details.append("Pagibig No.: ").append(data[10].trim()).append("\n");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> openSalaryCalculator(null));
    }
}
