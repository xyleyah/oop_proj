import javax.swing.*;
import java.awt.*;
import javax.swing.text.JTextComponent;

public class CreateEventDashboardPanel extends JPanel {
    private MainFrame mainFrame;

    public CreateEventDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(new Color(135, 206, 235));
        setLayout(new GridBagLayout());
        setupPanel();
    }

    private void setupPanel() {
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            // Back Button at the very top
            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.setBackground(Color.YELLOW);
            backButton.setForeground(Color.BLACK);
            backButton.setOpaque(true);
            backButton.addActionListener(e -> mainFrame.switchToPanel("StudentAdminDashboard"));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.fill = GridBagConstraints.NONE;
            add(backButton, gbc);

            // Dashboard Label below back button
            JLabel createEventLabel = new JLabel("CREATE EVENT ");
            createEventLabel.setFont(new Font("Arial", Font.BOLD, 35));
            createEventLabel.setForeground(Color.BLACK);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.PAGE_START;
            add(createEventLabel, gbc);

            // Reset constraints for form fields
            gbc.gridy = 2;
            gbc.weighty = 1.0;
            setupFormFields(gbc);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in setting up Create Event Dashboard: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupFormFields(GridBagConstraints gbc) {
        // Faculty Field
        JLabel facultyLabel = new JLabel("Faculty:");
        facultyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JTextField facultyField = new JTextField();
        facultyField.setFont(new Font("Arial", Font.PLAIN, 25));
        facultyField.setPreferredSize(new Dimension(600, 30));
    
        // Event Name Field
        JLabel eventNameLabel = new JLabel("Event Name:");
        eventNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JTextField eventNameField = new JTextField();
        eventNameField.setFont(new Font("Arial", Font.PLAIN, 25));
        eventNameField.setPreferredSize(new Dimension(600, 30));
    
        // Event Date Field
        JLabel eventDateLabel = new JLabel("Event Date (MM-DD-YYYY):");
        eventDateLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JTextField eventDateField = new JTextField();
        eventDateField.setFont(new Font("Arial", Font.PLAIN, 25));
        eventDateField.setPreferredSize(new Dimension(600, 30));
    
     // Event Details Field
        JLabel eventDetailsLabel = new JLabel("Event Details:");
eventDetailsLabel.setFont(new Font("Arial", Font.BOLD, 24));
JTextArea eventDetailsField = new JTextArea(5, 50); // 5 rows and 50 columns
eventDetailsField.setFont(new Font("Arial", Font.PLAIN, 25));
eventDetailsField.setLineWrap(true); // Enable text wrapping
    eventDetailsField.setWrapStyleWord(true); // Wrap at word boundaries
eventDetailsField.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add a border

JScrollPane eventDetailsScrollPane = new JScrollPane(eventDetailsField); // Add a scroll pane
eventDetailsScrollPane.setPreferredSize(new Dimension(600, 100)); // Set preferred size for scrolling

JLabel eventStatusLabel = new JLabel("Event Status:");
eventStatusLabel.setFont(new Font("Arial", Font.BOLD, 24));
JComboBox<String> eventStatusComboBox = new JComboBox<>(new String[]{"PENDING"});
JTextField statusField = new JTextField();
statusField.setFont(new Font("Arial", Font.PLAIN, 25));
statusField.setPreferredSize(new Dimension(600, 30));
eventStatusComboBox.setFont(new Font("Arial", Font.PLAIN, 25));
eventStatusComboBox.setPreferredSize(new Dimension(600, 30));
eventStatusComboBox.setBackground(Color.WHITE);

        // Submit Button
        JButton submitButton = new JButton("SUBMIT");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(Color.YELLOW);
        submitButton.setForeground(Color.BLACK);
        submitButton.setOpaque(true);
    
        // Add labels and fields to panel
        gbc.gridwidth = 1;
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(facultyLabel, gbc);
    
        gbc.gridx = 1;
        add(facultyField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(eventNameLabel, gbc);
    
        gbc.gridx = 1;
        add(eventNameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(eventDateLabel, gbc);
    
        gbc.gridx = 1;
        add(eventDateField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(eventDetailsLabel, gbc);
        
        gbc.gridx = 1;
        add(eventDetailsScrollPane, gbc); // Use the scroll pane to contain the JTextArea
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(eventStatusLabel, gbc);
        
        gbc.gridx = 1;
        add(eventStatusComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);
    
        // Add Waiting for Approval Button (initially invisible)
        JButton waitingButton = new JButton("Waiting to be Approved");
        waitingButton.setFont(new Font("Arial", Font.ITALIC, 16));
        waitingButton.setBackground(new Color(200, 200, 200)); // Light gray
        waitingButton.setEnabled(false); // Disabled state
        waitingButton.setVisible(false); // Initially invisible
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(waitingButton, gbc);

        // Submit Button Action
        submitButton.addActionListener(e -> {
            String faculty = facultyField.getText().trim();
            String eventName = eventNameField.getText().trim();
            String eventDate = eventDateField.getText().trim();
            String eventDetails = eventDetailsField.getText().trim();
            String eventStatus = (String) eventStatusComboBox.getSelectedItem();

            if (validateInputs(faculty, eventName, eventDate, eventDetails, eventStatus)) {
                DatabaseUtil.insertEvent(faculty, eventName, eventDate, eventDetails, eventStatus);
                
                // Show success message
                JOptionPane.showMessageDialog(this,
                    "EVENT CREATED!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                // Clear all fields
                clearFields(facultyField, eventNameField, eventDateField, eventDetailsField,statusField);
                
                // Reset the panel state (make submit button visible again if it was hidden)
                submitButton.setVisible(true);
                if (waitingButton != null) {
                    waitingButton.setVisible(false);
                }
                
                // Refresh the panel
                revalidate();
                repaint();
                
                // Clear the combo box selection
                eventStatusComboBox.setSelectedIndex(5);
            }
        });
    }
    
    

    private boolean validateInputs(String faculty, String eventName, String eventDate, String eventDetails, String eventStatus) {
        if (!faculty.matches("^[a-zA-Z0-9 ]+$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid Faculty Name! Please enter only letters and numbers.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (eventName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Event Name cannot be empty!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!eventDate.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid Event Date! Please enter the date in MM-DD-YYYY format.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (eventDetails.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Event Details cannot be empty!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (eventStatus.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Event Status cannot be empty!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields(JTextComponent... components) {
        for (JTextComponent component : components) {
            component.setText("");
        }
    }
}