import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.*;

public class MyUI extends JFrame {
    private String url = "jdbc:mysql://localhost:3306/travel_guide";
    private String username = "root";
    private String password = "JustThisOnce";
    private JComboBox<String> stateComboBox;
    private JComboBox<String> cityComboBox;
    private JButton selectCityButton; // Button to select city
    private JButton findAttractionButton; // Button to find attractions
    private JButton registerButton;
    private BufferedImage backgroundImage;
    private String selectedState;
    private String selectedCity;
    private JLabel resultLabel;
    private DatabaseConnector connector;

    public MyUI() {
        setTitle("Mera UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        ImageIcon icon = new ImageIcon("x.jpg");
        backgroundImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = backgroundImage.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        String[] indianStates = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep", "Delhi", "Puducherry"};
        stateComboBox = new JComboBox<>(indianStates);
        stateComboBox.setSelectedIndex(-1);
        cityComboBox = new JComboBox<>();
        cityComboBox.setVisible(false); // Hide city combo box by default
        selectCityButton = new JButton("Select City");
        findAttractionButton = new JButton("Find Attraction");
        registerButton = new JButton("Register");
        resultLabel = new JLabel();
        connector = new DatabaseConnector(url, username, password);
        selectCityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedState = (String) stateComboBox.getSelectedItem();
                cityComboBox.setVisible(true); // Show city combo box after selecting state
                populateCityComboBox(selectedState); // Populate city combo box based on selected state
            }
        });
        findAttractionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCity = (String) cityComboBox.getSelectedItem();
                showSelectedLocation(selectedState, selectedCity); // Method to display selected state and city
            }
        });
        JPanel travelGuidePanel = new JPanel();
        travelGuidePanel.setLayout(new BorderLayout());
        JLabel headingLabel = new JLabel("Travel Guide");
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        travelGuidePanel.add(headingLabel, BorderLayout.NORTH);
        JPanel boxPanel = new JPanel(new GridLayout(4, 1, 5, 5)); // Increased rows to accommodate buttons
        boxPanel.add(stateComboBox);
        boxPanel.add(cityComboBox);
        boxPanel.add(selectCityButton);
        boxPanel.add(findAttractionButton);
        boxPanel.setBackground(Color.BLUE);
        boxPanel.setOpaque(false);
        travelGuidePanel.add(boxPanel, BorderLayout.CENTER);
        Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
        travelGuidePanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(20, 50, 20, 50)));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        travelGuidePanel.add(resultPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        });
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(travelGuidePanel, new GridBagConstraints());
        setVisible(true);
    }

    private void showSelectedLocation(String stateName, String cityName) {
        JFrame selectedLocationFrame = new JFrame("Attractions in " + cityName);
        selectedLocationFrame.setSize(400, 300);
        selectedLocationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose frame on close

        // Create table to display results
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        tableModel.addColumn("City");
        tableModel.addColumn("Location");
        tableModel.addColumn("Rating");
        String sqlQuery = "SELECT city, location, rating FROM " + stateName + " WHERE city = ? ORDER BY rating DESC";
        // Execute SQL query and fetch data
 
        try {
            connector.connect();
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, cityName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                String location = resultSet.getString("location");
                String rating = resultSet.getString("rating");
                tableModel.addRow(new Object[]{city, location, rating});
            }
            resultSet.close();
            connector.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Add table to frame
        JScrollPane scrollPane = new JScrollPane(table);
        selectedLocationFrame.add(scrollPane);

        selectedLocationFrame.setVisible(true);
    }

    private void populateCityComboBox(String selectedState) {
        cityComboBox.removeAllItems();
        String sqlQuery = "SELECT DISTINCT city FROM " + selectedState;
        try {
            connector.connect();
            ResultSet resultSet = connector.executeQuery(sqlQuery);
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                cityComboBox.addItem(city);
            }
            resultSet.close();
            connector.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getSelectedState() {
        return selectedState;
    }

    public static void main(String[] args) {
        new MyUI();
    }
}
