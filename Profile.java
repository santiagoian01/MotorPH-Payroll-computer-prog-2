import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Profile {
    public static void openProfile(String eid) {
        JFrame profileFrame = new JFrame();
        profileFrame.setTitle("Employee Profile");
        profileFrame.setSize(450, 600); 
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pang close
        profileFrame.setVisible(true);
    
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(null);
    
        JLabel profileLabel = new JLabel("EMPLOYEE ID: " + eid);
        profileLabel.setBounds(10, 15, 200, 25);
        profilePanel.add(profileLabel);
    
        // CSV Reader
        String[] profileInfo = getEmployeeProfile(eid);
        displayEmployeeInfo(profileInfo, profilePanel);
    
        profileFrame.add(profilePanel);
    }

    private static void displayEmployeeInfo(String[] profileInfo, JPanel profilePanel) {
        int yCoordinate = 45;
    
        String[] labels = {"Last Name", "First Name", "Birthday", "Address", "Phone Number",
                            "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position",
                            "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                            "Clothing Allowance", "Gross Semi-monthly Rate", "Daily Rate", "Hourly Rate"};
        int[] indices = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
    
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i] + ": " + profileInfo[indices[i]]);
            label.setBounds(10, yCoordinate, 400, 50);
            profilePanel.add(label);
            yCoordinate += 30; // pang baba sa Y
        }
    }

    private static String[] getEmployeeProfile(String eid) {
         String[] profileInfo = new String[21]; 
        String csvFile = "F:\\MotorPH-Payroll-computer-prog-2-main\\MS1-GUI-Code-main\\MS1 java gui\\lib\\Employee_Database - Employee_Database.csv"; 
        String line;
        String cvsSplitBy = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] profileRecord = line.split(cvsSplitBy);
                if (profileRecord[0].equals(eid)) {
                    System.arraycopy(profileRecord, 1, profileInfo, 0, Math.min(profileRecord.length - 1, profileInfo.length));
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return profileInfo;
    }
}
    
