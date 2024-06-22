import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard {
    private static JFrame dashboardFrame;
    private static GUI previousScreen;

    public static void openDashboard(String eid, GUI gui) {
        previousScreen = gui;

        dashboardFrame = new JFrame();
        dashboardFrame.setTitle("Dashboard");
        dashboardFrame.setSize(800, 400);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dashboardFrame.add(mainPanel, BorderLayout.CENTER);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + getEmployeeName(eid) + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Date and Time Panel
        JPanel dateTimePanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.add(dateTimePanel, BorderLayout.CENTER);

        JLabel dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dateTimePanel.add(dateLabel);

        JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dateTimePanel.add(timeLabel);

        // Update date and time label every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();
                dateLabel.setText(dateFormat.format(now));
                timeLabel.setText(timeFormat.format(now));
            }
        });
        timer.start();

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Updated to 5 rows for additional Log Out button
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Profile Button
        JButton profileButton = new JButton("Profile");
        profileButton.setFont(new Font("Arial", Font.PLAIN, 16));
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Profile.openProfile(eid);
            }
        });
        buttonPanel.add(profileButton);

        // Attendance Tracker Button
        JButton attendanceTrackerButton = new JButton("Attendance");
        attendanceTrackerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        attendanceTrackerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttendanceTrackerUI.showAttendanceTrackerDialog(dashboardFrame);
            }
        });
        buttonPanel.add(attendanceTrackerButton);

        // Leave Request Button
        JButton leaveRequestButton = new JButton("Leave Request");
        leaveRequestButton.setFont(new Font("Arial", Font.PLAIN, 16));
        leaveRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LeaveRequestUI.showLeaveRequestDialog(dashboardFrame);
            }
        });
        buttonPanel.add(leaveRequestButton);

        // Salary Calculator Button
        JButton salaryCalculatorButton = new JButton("Salary Calculator");
        salaryCalculatorButton.setFont(new Font("Arial", Font.PLAIN, 16));
        salaryCalculatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SalaryCalculatorUI.openSalaryCalculator(dashboardFrame);
            }
        });
        buttonPanel.add(salaryCalculatorButton);

        // Log Out Button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                previousScreen.showInitialChoice();
                JOptionPane.showMessageDialog(null, "Logged out successfully!");
            }
        });
        buttonPanel.add(logoutButton);

        // Center the frame on the screen
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }

    private static String getEmployeeName(String eid) {
        String csvFile = "MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].equals(eid)) {
                    return data[3] + " " + data[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Unknown Employee";
    }
}
