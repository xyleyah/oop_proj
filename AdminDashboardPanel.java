import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private DefaultListModel<String> pendingListModel;
    private DefaultListModel<String> approvedListModel;
    private JList<String> pendingList;
    private JList<String> approvedList;

    public AdminDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.pendingListModel = new DefaultListModel<>();
        this.approvedListModel = new DefaultListModel<>();
        setLayout(new BorderLayout());
        setupPanel();

        refreshLists();
    }

    public void refreshLists() {
     
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                loadPendingEvents();
                loadApprovedEvents();
                return null;
            }
        };
        worker.execute();
    }

    private void loadPendingEvents() {
        ResultSet rs = null;
        try {
            SwingUtilities.invokeLater(() -> pendingListModel.clear());
            rs = DatabaseUtil.getPendingEvents();
            final ResultSet finalRs = rs;
            while (finalRs != null && finalRs.next()) {
                String eventDetails = String.format("<html>ID: %d<br>Event: %s<br>Faculty: %s<br>Date: %s</html>", 
                    finalRs.getInt("EventID"),
                    finalRs.getString("EventName"),
                    finalRs.getString("Faculty"),
                    finalRs.getString("EventDate")
                );
                SwingUtilities.invokeLater(() -> pendingListModel.addElement(eventDetails));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading pending events", e);
        } finally {
            closeResultSet(rs);
        }
    }

    private void loadApprovedEvents() {
        ResultSet rs = null;
        try {
            SwingUtilities.invokeLater(() -> approvedListModel.clear());
            rs = DatabaseUtil.getAllEvents();
            final ResultSet finalRs = rs;
            while (finalRs != null && finalRs.next()) {
                if ("APPROVED".equals(finalRs.getString("Status"))) {
                    String eventDetails = String.format("<html>ID: %d<br>Event: %s<br>Faculty: %s<br>Date: %s<br>Details: %s</html>", 
                        finalRs.getInt("EventID"),
                        finalRs.getString("EventName"),
                        finalRs.getString("Faculty"),
                        finalRs.getString("EventDate"),
                        finalRs.getString("EventDetails")
                    );
                    SwingUtilities.invokeLater(() -> approvedListModel.addElement(eventDetails));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading approved events", e);
        } finally {
            closeResultSet(rs);
        }
    }

    private void moveItem(DefaultListModel<String> sourceModel, DefaultListModel<String> destinationModel, JList<String> sourceList) {
        int selectedIndex = sourceList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "No activity selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    String item = sourceModel.get(selectedIndex);
                    int eventId = Integer.parseInt(item.substring(item.indexOf("ID: ") + 4, item.indexOf(" -")));
                    DatabaseUtil.updateEventStatus(eventId, "APPROVED");
                    SwingUtilities.invokeLater(() -> refreshLists());
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error while moving item", e);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void declineItem(DefaultListModel<String> pendingListModel, JList<String> pendingList) {
        int selectedIndex = pendingList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to decline.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    String item = pendingListModel.get(selectedIndex);
                    int eventId = Integer.parseInt(item.substring(item.indexOf("ID: ") + 4, item.indexOf(" -")));
                    DatabaseUtil.updateEventStatus(eventId, "DECLINED");
                    SwingUtilities.invokeLater(() -> refreshLists());
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error during decline action", e);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void setupPanel() {
        try {
           
            JButton backButton = new JButton("LOG OUT");
            backButton.setFont(new Font("Monospace", Font.BOLD, 14));
            backButton.addActionListener(e -> mainFrame.switchToPanel("Initial"));
            backButton.setBackground(Color.YELLOW);
            backButton.setForeground(Color.BLACK);
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(backButton, BorderLayout.WEST);

            
            JLabel adminDashboardLabel = new JLabel(" ACTIVITY MANAGEMENT");
            adminDashboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
            adminDashboardLabel.setFont(new Font("Arial", Font.BOLD, 28));
            adminDashboardLabel.setOpaque(true);
            adminDashboardLabel. setBackground(new Color(135, 206, 235));
            adminDashboardLabel.setForeground(Color.BLACK);
            adminDashboardLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            topPanel.add(adminDashboardLabel, BorderLayout.CENTER);

           
            JPanel activityPanel = new JPanel(new GridLayout(1, 2));

           
            JPanel pendingPanel = createPendingPanel();
          
            JPanel approvedPanel = createApprovedPanel();

            activityPanel.add(pendingPanel);
            activityPanel.add(approvedPanel);

            add(topPanel, BorderLayout.NORTH);
            add(activityPanel, BorderLayout.CENTER);

           
            JButton refreshButton = new JButton("Refresh Lists");
            refreshButton.addActionListener(e -> refreshLists());
            add(refreshButton, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error setting up Admin Dashboard", e);
        }
    }

    private JPanel createPendingPanel() {
        JPanel pendingPanel = new JPanel(new BorderLayout());
        JLabel pendingLabel = new JLabel("Pending Activities");
        pendingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        pendingList = new JList<>(pendingListModel);
        pendingList.setFont(new Font("Arial", Font.PLAIN, 14));
        pendingList.setFixedCellHeight(100);
        pendingList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        JScrollPane pendingScroll = new JScrollPane(pendingList);
        pendingScroll.setPreferredSize(new Dimension(200, 300));

     
        JButton approveButton = new JButton("Approve");
        approveButton.setFont(new Font("Arial", Font.BOLD, 16));
        approveButton.setBackground(Color.YELLOW);
        approveButton.addActionListener(e -> moveItem(pendingListModel, approvedListModel, pendingList));

        JButton declineButton = new JButton("Decline");
        declineButton.setFont(new Font("Arial", Font.BOLD, 16));
        declineButton.setBackground(Color.YELLOW);
        declineButton.addActionListener(e -> declineItem(pendingListModel, pendingList));

 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
      
        buttonPanel.add(approveButton);
        buttonPanel.add(declineButton);

        pendingPanel.add(pendingLabel, BorderLayout.NORTH);
        pendingPanel.add(pendingScroll, BorderLayout.CENTER);
        pendingPanel.add(buttonPanel, BorderLayout.SOUTH);

        return pendingPanel;
    }

    private JPanel createApprovedPanel() {
        JPanel approvedPanel = new JPanel(new BorderLayout());
        JLabel approvedLabel = new JLabel("Approved Activities");
        approvedLabel.setFont(new Font("Arial", Font.BOLD, 20));
        

        approvedList = new JList<>(approvedListModel);
        approvedList.setFont(new Font("Arial", Font.PLAIN, 16));
        approvedList.setFixedCellHeight(30);

        JScrollPane approvedScroll = new JScrollPane(approvedList);
        approvedScroll.setPreferredSize(new Dimension(200, 300));

     

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
   
        approvedPanel.add(buttonPanel, BorderLayout.SOUTH);

        approvedPanel.add(approvedLabel, BorderLayout.NORTH);
        approvedPanel.add(approvedScroll, BorderLayout.CENTER);

        return approvedPanel;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showError(String message, Exception e) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(this, 
                message + ": " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE)
        );
    }
}
