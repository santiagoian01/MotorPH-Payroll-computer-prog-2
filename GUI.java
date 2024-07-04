import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class GUI implements ActionListener {

    private JFrame frame;
    private JLabel eidLabel;
    private JTextField eidTextField;
    private JLabel passLabel;
    private JPasswordField passTextField;
    private JButton logButton;
    private JButton backButton;
    private JLabel successLoginLabel;
    private boolean isHRLogin = false;

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.showInitialChoice();
    }

    public void showInitialChoice() {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame();
        frame.setTitle("MotorPH Portal");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton empChoiceButton = new JButton("Log in as Employee");
        empChoiceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        empChoiceButton.addActionListener(e -> showLoginScreen(false));
        panel.add(empChoiceButton);

        JButton hrChoiceButton = new JButton("Log in as HR");
        hrChoiceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        hrChoiceButton.addActionListener(e -> showLoginScreen(true));
        panel.add(hrChoiceButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void showLoginScreen(boolean isHR) {
        isHRLogin = isHR;
        frame.dispose();

        frame = new JFrame();
        frame.setTitle(isHR ? "HR Login" : "Employee Login");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        eidLabel = new JLabel("ID");
        eidLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(eidLabel);

        eidTextField = new JTextField(20);
        eidTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(eidTextField);

        passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passLabel);

        passTextField = new JPasswordField(20);
        passTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passTextField);

        logButton = new JButton("Log In");
        logButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logButton.addActionListener(this);
        panel.add(logButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> showInitialChoice());
        panel.add(backButton);

        successLoginLabel = new JLabel("");
        successLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        successLoginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(successLoginLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logButton) {
            String user = eidTextField.getText();
            String password = new String(passTextField.getPassword());

            boolean authenticated = authenticate(user, password, isHRLogin);
            if (authenticated) {
                frame.dispose();
                if (isHRLogin) {
                    successLoginLabel.setText("HR Log in Success");
                    HRDashboard.openHRDashboard(user);
                } else {
                    successLoginLabel.setText("Employee Log in Success");
                    Dashboard.openDashboard(user, this); // Pass the current instance
                }
            } else {
                successLoginLabel.setText("Invalid ID or password!");
            }
        }
    }

    private boolean authenticate(String username, String password, boolean isHR) {
        String csvFile = isHR ? "MS1 java gui\\lib\\HR Details.csv" : "MS1 java gui\\lib\\Employee_Database - Employee_Database.csv";
        String line;
        String cvsSplitBy = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                
                String[] userRecord = line.split(cvsSplitBy);
                
                if (userRecord.length < 2) {
                    continue; 
                }
                
                String storedUsername = userRecord[0].trim();
                String storedPassword = userRecord[1].trim();
    
                
                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return true; // Authentication successful
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false; // Invalid authentication
    }
    
}
