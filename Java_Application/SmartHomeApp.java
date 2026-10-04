import com.fazecast.jSerialComm.SerialPort;
import javax.swing.*; // Fixed: Added missing asterisk for wildcard import
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SmartHomeApp extends JFrame {
    // Serial Connection Fields
    private SerialPort chosenPort;
    private OutputStream outputStream;
    private BufferedReader reader;

    // UI Dashboard Elements
    private JLabel tempLabel;
    private JTextArea logArea;
    private JButton connectButton;
    private JComboBox<String> portBox;

    public SmartHomeApp() {
        // Initialize the Main Dashboard Layout hidden at first
        setTitle("PC Smart Home Control Panel v3.0");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SECTION 1: TOP SERIAL CONNECTION PANEL ---
        JPanel topPanel = new JPanel();
        portBox = new JComboBox<>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            portBox.addItem(port.getSystemPortName());
        }
        connectButton = new JButton("Connect Hardware");
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portBox);
        topPanel.add(connectButton);
        add(topPanel, BorderLayout.NORTH);

        // --- SECTION 2: CENTER CONTROL METRIC DASHBOARD ---
        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        tempLabel = new JLabel("Temperature: -- °C", SwingConstants.CENTER);
        tempLabel.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(tempLabel);

        // State Toggles using JToggleButton
        JToggleButton lamp1Toggle = createDeviceToggle("Lamp 1", 'A', 'a');
        JToggleButton lamp2Toggle = createDeviceToggle("Lamp 2", 'B', 'b');
        JToggleButton lamp3Toggle = createDeviceToggle("Lamp 3", 'C', 'c');

        centerPanel.add(lamp1Toggle);
        centerPanel.add(lamp2Toggle);
        centerPanel.add(lamp3Toggle);

        // Directional Door Motors
        JButton doorOpen = new JButton("Open Front Door");
        JButton doorClose = new JButton("Close Front Door");
        centerPanel.add(doorOpen);
        centerPanel.add(doorClose);

        add(centerPanel, BorderLayout.CENTER);

        // --- SECTION 3: SYSTEM LOG AT BOTTOM ---
        logArea = new JTextArea(6, 30);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Bind Control Actions
        connectButton.addActionListener(e -> toggleConnection());
        doorOpen.addActionListener(e -> sendCommand('O'));
        doorClose.addActionListener(e -> sendCommand('X'));

        // Center window placement on desktop
        setLocationRelativeTo(null);
    }

    // Builder function for uniform toggle buttons
    private JToggleButton createDeviceToggle(String deviceName, char onChar, char offChar) {
        JToggleButton toggle = new JToggleButton(deviceName + " is OFF");
        toggle.setFont(new Font("Arial", Font.PLAIN, 14));
        toggle.addActionListener(e -> {
            if (toggle.isSelected()) {
                toggle.setText(deviceName + " is ON");
                toggle.setBackground(Color.GREEN);
                sendCommand(onChar);
            } else {
                toggle.setText(deviceName + " is OFF");
                toggle.setBackground(null);
                sendCommand(offChar);
            }
        });
        return toggle;
    }

    // --- CREDENTIAL VERIFICATION SYSTEM (THE LOGIN DIALOG) ---
    private static boolean showLoginDialog(JFrame parent) {
        JDialog loginDialog = new JDialog(parent, "System Authentication", true);
        loginDialog.setLayout(new GridLayout(3, 2, 10, 10));
        loginDialog.setSize(320, 160);
        loginDialog.setLocationRelativeTo(parent);
JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        loginDialog.add(new JLabel(" Username:"));
        loginDialog.add(userField);
        loginDialog.add(new JLabel(" Password:"));
        loginDialog.add(passField);
        loginDialog.add(loginBtn);
        loginDialog.add(cancelBtn);

        // Verification status transfer array
        final boolean[] isAuthenticated = {false};

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            // Evaluation logic
            if (username.equals("admin") && password.equals("password123")) {
                isAuthenticated[0] = true;
                loginDialog.dispose(); 
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid Credentials! Access Denied.",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> System.exit(0)); 

        // Prevent window bypassing
        loginDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        loginDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        loginDialog.setVisible(true);
        return isAuthenticated[0];
    }

    // --- HARDWARE COMMUNICATIONS LOGIC ---
    private void toggleConnection() {
        if (connectButton.getText().equals("Connect Hardware")) {
            String portName = (String) portBox.getSelectedItem();
            if(portName == null) {
                JOptionPane.showMessageDialog(this, "No COM Ports detected.");
                return;
            }
            chosenPort = SerialPort.getCommPort(portName);
            chosenPort.setComPortParameters(9600, 8, 1, 0);
            chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

            if (chosenPort.openPort()) {
                connectButton.setText("Disconnect");
                outputStream = chosenPort.getOutputStream();
                reader = new BufferedReader(new InputStreamReader(chosenPort.getInputStream()));
                startDataListener();
                logArea.append("Authenticated serial tunnel opened on " + portName + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Could not open connection to Proteus.");
            }
        } else {
            if (chosenPort != null) chosenPort.closePort();
            connectButton.setText("Connect Hardware");
            logArea.append("Connection severed safely.\n");
        }
    }

    private void sendCommand(char command) {
        try {
            if (outputStream != null) {
                outputStream.write((byte) command);
                outputStream.flush();
            } else {
                logArea.append("Tx Aborted: Hardware not connected.\n");
            }
        } catch (Exception e) {
            logArea.append("Tx Failure: " + e.getMessage() + "\n");
        }
    }

    private void startDataListener() {
        new Thread(() -> {
            try {
                String line;
                while (chosenPort.isOpen() && (line = reader.readLine()) != null) {
                    final String rawText = line.trim();
                    SwingUtilities.invokeLater(() -> {
                        if (rawText.startsWith("TEMP:")) {
                            tempLabel.setText("Temperature: " + rawText.substring(5) + " °C");
                        } else {
                            logArea.append("Proteus -> " + rawText + "\n");
                        }
                    });
                }
            } catch (Exception e) { 
                // Catching exception when port closes gracefully
            }
        }).start();
    }
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartHomeApp appFrame = new SmartHomeApp();
            if (showLoginDialog(appFrame)) {
                appFrame.setVisible(true);
            }
        });
    }
}