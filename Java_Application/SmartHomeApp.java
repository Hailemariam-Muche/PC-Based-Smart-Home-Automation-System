import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.swing.JFrame;

/**
• PC Based Smart Home Automation System
• Communication:
• PC Java Application
• |
• | Serial / UART
• v
• Proteus
• |
• v
• PIC16F877A
• Commands:
• A = Lamp 1 ON
• a = Lamp 1 OFF
• B = Lamp 2 ON
• b = Lamp 2 OFF
• C = Lamp 3 ON
• c = Lamp 3 OFF
• O = Open Front Door
• X = Close Front Door
*/
public class SmartHomeApp extends JFrame {

// =========================================================
// SERIAL CONNECTION FIELDS
// =========================================================

private SerialPort chosenPort;
private OutputStream outputStream;
private BufferedReader reader;

// =========================================================
// USER INTERFACE ELEMENTS
// =========================================================

private JLabel tempLabel;
private JTextArea logArea;

private JButton connectButton;
private JComboBox<String> portBox;

// =========================================================
// COLORS
// =========================================================

private static final Color TEAL =
new Color(45, 170, 170);

private static final Color SWITCH_GRAY =
new Color(190, 190, 190);

// =========================================================
// CONSTRUCTOR
// =========================================================

public SmartHomeApp() {

// -----------------------------------------------------
// MAIN WINDOW
// -----------------------------------------------------

setTitle("PC Based Smart Home Control Panel v3.0");

setSize(500, 620);

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

setLayout(new BorderLayout(10, 10));

// -----------------------------------------------------
// SECTION 1:
// SERIAL CONNECTION PANEL
// -----------------------------------------------------

JPanel topPanel = new JPanel(
new FlowLayout(
FlowLayout.CENTER,
10,
10
)
);

topPanel.setBorder(
BorderFactory.createTitledBorder(
"Hardware Connection"
)
);

portBox = new JComboBox<>();

// Detect available COM ports
loadSerialPorts();

connectButton =
new JButton("Connect Hardware");

topPanel.add(
new JLabel("COM Port:")
);

topPanel.add(portBox);

topPanel.add(connectButton);

add(
topPanel,
BorderLayout.NORTH
);

// -----------------------------------------------------
// SECTION 2:
// CENTER SMART HOME CONTROL PANEL
// -----------------------------------------------------

JPanel centerPanel = new JPanel(
new GridLayout(
6,
1,
10,
10
)
);

centerPanel.setBorder(
BorderFactory.createEmptyBorder(
15,
20,
15,
20
)
);

// -----------------------------------------------------
// TEMPERATURE
// -----------------------------------------------------

tempLabel =
new JLabel(
"Temperature: -- °C",
SwingConstants.CENTER
);

tempLabel.setFont(
new Font(
"Arial",
Font.BOLD,
20
)
);

centerPanel.add(tempLabel);

// -----------------------------------------------------
// LAMP 1
// -----------------------------------------------------

JPanel lamp1Panel =
createDeviceToggle(
"Lamp 1",
'A',
'a'
);

centerPanel.add(lamp1Panel);

// -----------------------------------------------------
// LAMP 2
// -----------------------------------------------------

JPanel lamp2Panel =
createDeviceToggle(
"Lamp 2",
'B',
'b'
);

centerPanel.add(lamp2Panel);

// -----------------------------------------------------
// LAMP 3
// -----------------------------------------------------

JPanel lamp3Panel =
createDeviceToggle(
"Lamp 3",
'C',
'c'
);

centerPanel.add(lamp3Panel);

// -----------------------------------------------------
// FRONT DOOR OPEN
// -----------------------------------------------------

JButton doorOpen =
new JButton(
"🚪 Open Front Door"
);

doorOpen.setFont(
new Font(
"Arial",
Font.BOLD,
14
)
);

centerPanel.add(doorOpen);

// -----------------------------------------------------
// FRONT DOOR CLOSE
// -----------------------------------------------------

JButton doorClose =
new JButton(
"🔒 Close Front Door"
);

doorClose.setFont(
new Font(
"Arial",
Font.BOLD,
14
)
);

centerPanel.add(doorClose);

add(
centerPanel,
BorderLayout.CENTER
);

// -----------------------------------------------------
// SECTION 3:
// SYSTEM LOG
// -----------------------------------------------------

logArea =
new JTextArea(
7,
35
);

logArea.setEditable(false);

logArea.setFont(
new Font(
"Monospaced",
Font.PLAIN,
12
)
);

logArea.setBorder(
BorderFactory.createTitledBorder(
"System Communication Log"
)
);

JScrollPane scrollPane =
new JScrollPane(logArea);

add(
scrollPane,
BorderLayout.SOUTH
);

// -----------------------------------------------------
// BUTTON EVENTS
// -----------------------------------------------------

connectButton.addActionListener(
e -> toggleConnection()
);

doorOpen.addActionListener(
e -> {

sendCommand('O');

logArea.append(
"PC -> Open Front Door\n"
);
}
);

doorClose.addActionListener(
e -> {

sendCommand('X');

logArea.append(
"PC -> Close Front Door\n"
);
}
);

// -----------------------------------------------------
// WINDOW POSITION
// -----------------------------------------------------

setLocationRelativeTo(null);
}

// =========================================================
// LOAD AVAILABLE SERIAL PORTS
// =========================================================

private void loadSerialPorts() {

portBox.removeAllItems();

SerialPort[] ports =
SerialPort.getCommPorts();

if (ports.length == 0) {

portBox.addItem(
"No COM ports detected"
);

return;
}

for (SerialPort port : ports) {

portBox.addItem(
port.getSystemPortName()
);
}
}

// =========================================================
// CREATE DEVICE TOGGLE
// =========================================================

private JPanel createDeviceToggle(
String deviceName,
char onChar,
char offChar) {

JPanel panel =
new JPanel(
new FlowLayout(
FlowLayout.CENTER,
20,
5
)
);

JLabel deviceLabel =
new JLabel(
deviceName
);

deviceLabel.setFont(
new Font(
"Arial",
Font.BOLD,
16
)
);

JLabel stateLabel =
new JLabel(
"OFF"
);

stateLabel.setFont(
new Font(
"Arial",
Font.PLAIN,
14
)
);

ToggleSwitch toggle =
new ToggleSwitch();

panel.add(deviceLabel);

panel.add(toggle);

panel.add(stateLabel);

// -----------------------------------------------------
// TOGGLE ACTION
// -----------------------------------------------------

toggle.addActionListener(
e -> {

if (toggle.isSelected()) {

stateLabel.setText(
"ON"
);

stateLabel.setForeground(
TEAL
);

sendCommand(onChar);

logArea.append(
"PC -> "
+ deviceName
+ " ON\n"
);

} else {

stateLabel.setText(
"OFF"
);

stateLabel.setForeground(
Color.BLACK
);

sendCommand(offChar);

logArea.append(
"PC -> "
+ deviceName
+ " OFF\n"
);
}
}
);

return panel;
}

// =========================================================
// CUSTOM TOGGLE SWITCH
// =========================================================

private static class ToggleSwitch
extends JToggleButton {

private static final int WIDTH = 70;
private static final int HEIGHT = 30;

public ToggleSwitch() {

setPreferredSize(
new Dimension(
WIDTH,
HEIGHT
)
);

setMinimumSize(
new Dimension(
WIDTH,
HEIGHT
)
);

setMaximumSize(
new Dimension(
WIDTH,
HEIGHT
)
);

setFocusPainted(false);

setBorderPainted(false);

setContentAreaFilled(false);

setOpaque(false);

setCursor(
new Cursor(
Cursor.HAND_CURSOR
)
);
}

@Override
protected void paintComponent(
Graphics g) {

Graphics2D g2 =
(Graphics2D) g.create();

g2.setRenderingHint(
RenderingHints.KEY_ANTIALIASING,
RenderingHints.VALUE_ANTIALIAS_ON
);

int w = getWidth();
int h = getHeight();

// -------------------------------------------------
// DISABLED SWITCH
// -------------------------------------------------

if (!isEnabled()) {

g2.setColor(
new Color(
210,
210,
210
)
);

g2.fillRoundRect(
2,
5,
w - 4,
h - 10,
h - 10,
h - 10
);

g2.setColor(
new Color(
170,
170,
170
)
);

int knobSize =
h - 12;

g2.fillRoundRect(
5,
6,
knobSize,
knobSize,
5,
5
);

g2.dispose();

return;
}

// -------------------------------------------------
// SWITCH TRACK
// -------------------------------------------------

if (isSelected()) {

// ON
g2.setColor(
TEAL
);

} else {

// OFF
g2.setColor(
SWITCH_GRAY
);
}

g2.fillRoundRect(
2,
5,
w - 4,
h - 10,
h - 10,
h - 10
);

// -------------------------------------------------
// BORDER
// -------------------------------------------------

g2.setColor(
new Color(
150,
150,
150
)
);

g2.drawRoundRect(
2,
5,
w - 4,
h - 10,
h - 10,
h - 10
);

// -------------------------------------------------
// MOVING KNOB
// -------------------------------------------------

int knobSize =
h - 12;

int knobX;

if (isSelected()) {

// ON -> right
knobX =
w
- knobSize
- 5;

} else {

// OFF -> left
knobX = 5;
}

// Black knob
g2.setColor(
Color.BLACK
);

g2.fillRoundRect(
knobX,
6,
knobSize,
knobSize,
5,
5
);

g2.dispose();
}
}

// =========================================================
// LOGIN DIALOG
// =========================================================

private static boolean showLoginDialog(
JFrame parent) {

JDialog loginDialog =
new JDialog(
parent,
"System Authentication",
true
);

loginDialog.setLayout(
new GridLayout(
3,
2,
10,
10
)
);

loginDialog.setSize(
350,
180
);

loginDialog.setLocationRelativeTo(
parent
);

// -----------------------------------------------------
// USERNAME
// -----------------------------------------------------

JTextField userField =
new JTextField();

// -----------------------------------------------------
// PASSWORD
// -----------------------------------------------------

JPasswordField passField =
new JPasswordField();

// -----------------------------------------------------
// BUTTONS
// -----------------------------------------------------

JButton loginBtn =
new JButton(
"Login"
);

JButton cancelBtn =
new JButton(
"Cancel"
);

// -----------------------------------------------------
// ADD COMPONENTS
// -----------------------------------------------------

loginDialog.add(
new JLabel(
" Username:"
)
);

loginDialog.add(
userField
);

loginDialog.add(
new JLabel(
" Password:"
)
);

loginDialog.add(
passField
);

loginDialog.add(
loginBtn
);

loginDialog.add(
cancelBtn
);

// -----------------------------------------------------
// AUTHENTICATION STATUS
// -----------------------------------------------------

final boolean[] isAuthenticated =
{false};

// -----------------------------------------------------
// LOGIN ACTION
// -----------------------------------------------------

loginBtn.addActionListener(
e -> {

String username =
userField.getText();

String password =
new String(
passField.getPassword()
);

// -------------------------------------------------
// CURRENT LOGIN CREDENTIALS
// -------------------------------------------------

if (username.equals("admin")
&& password.equals("password123")) {

isAuthenticated[0] =
true;

loginDialog.dispose();

} else {

JOptionPane.showMessageDialog(
loginDialog,
"Invalid Credentials! Access Denied.",
"Login Error",
JOptionPane.ERROR_MESSAGE
);
}
}
);

// -----------------------------------------------------
// CANCEL
// -----------------------------------------------------

cancelBtn.addActionListener(
e -> System.exit(0)
);

// -----------------------------------------------------
// PREVENT WINDOW BYPASS
// -----------------------------------------------------

loginDialog.setDefaultCloseOperation(
JDialog.DO_NOTHING_ON_CLOSE
);

loginDialog.addWindowListener(
new WindowAdapter() {

@Override
public void windowClosing(
WindowEvent e) {

System.exit(0);
}
}
);

// -----------------------------------------------------
// DISPLAY LOGIN
// -----------------------------------------------------

loginDialog.setVisible(true);

return isAuthenticated[0];
}

// =========================================================
// SERIAL CONNECTION
// =========================================================

private void toggleConnection() {

// -----------------------------------------------------
// CONNECT
// -----------------------------------------------------

if (connectButton
.getText()
.equals("Connect Hardware")) {

String portName =
(String) portBox.getSelectedItem();

if (portName == null
|| portName.equals(
"No COM ports detected")) {

JOptionPane.showMessageDialog(
this,
"No COM Ports detected.",
"Serial Connection",
JOptionPane.WARNING_MESSAGE
);

return;
}

// -------------------------------------------------
// GET SELECTED COM PORT
// -------------------------------------------------

chosenPort =
SerialPort.getCommPort(
portName
);

// -------------------------------------------------
// SERIAL PARAMETERS
// -------------------------------------------------

chosenPort.setComPortParameters(
9600,
8,
SerialPort.ONE_STOP_BIT,
SerialPort.NO_PARITY
);

chosenPort.setComPortTimeouts(
SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
100,
0
);

// -------------------------------------------------
// OPEN PORT
// -------------------------------------------------

if (chosenPort.openPort()) {

connectButton.setText(
"Disconnect"
);

outputStream =
chosenPort.getOutputStream();

reader =
new BufferedReader(
new InputStreamReader(
chosenPort.getInputStream()
)
);

logArea.append(
"Serial connection opened on "
+ portName
+ "\n"
);

startDataListener();

} else {

JOptionPane.showMessageDialog(
this,
"Could not open connection to Proteus.",
"Connection Error",
JOptionPane.ERROR_MESSAGE
);
}

}

// -----------------------------------------------------
// DISCONNECT
// -----------------------------------------------------

else {

disconnectHardware();
}
}

// =========================================================
// DISCONNECT HARDWARE
// =========================================================

private void disconnectHardware() {

try {

if (chosenPort != null
&& chosenPort.isOpen()) {

chosenPort.closePort();
}

} catch (Exception e) {

logArea.append(
"Disconnect error: "
+ e.getMessage()
+ "\n"
);
}

outputStream = null;

reader = null;

connectButton.setText(
"Connect Hardware"
);

logArea.append(
"Connection closed safely.\n"
);
}

// =========================================================
// SEND SERIAL COMMAND
// =========================================================

private void sendCommand(
char command) {

try {

if (outputStream != null
&& chosenPort != null
&& chosenPort.isOpen()) {

outputStream.write(
(byte) command
);

outputStream.flush();

} else {

logArea.append(
"Warning: Hardware not connected. "
+ "Command "
+ command
+ " not sent.\n"
);
}

} catch (Exception e) {

logArea.append(
"Tx Failure: "
+ e.getMessage()
+ "\n"
);
}
}

// =========================================================
// RECEIVE DATA FROM PIC / PROTEUS
// =========================================================

private void startDataListener() {

Thread listenerThread =
new Thread(
() -> {

try {

String line;

while (
chosenPort != null
&& chosenPort.isOpen()
&& reader != null
&& (line =
reader.readLine())
!= null
) {

final String rawText =
line.trim();

SwingUtilities.invokeLater(
() -> {

// ----------------------------------
// TEMPERATURE DATA
// ----------------------------------

if (rawText.startsWith(
"TEMP:"
)) {

String temperature =
rawText.substring(
5
).trim();

tempLabel.setText(
"Temperature: "
+ temperature
+ " °C"
);

}

// ----------------------------------
// OTHER DATA
// ----------------------------------

else if (!rawText.isEmpty()) {

logArea.append(
"Proteus -> "
+ rawText
+ "\n"
);
}
}
);
}

} catch (Exception e) {

// Avoid displaying an error when the
// serial port was intentionally closed.
}
}
);

listenerThread.setDaemon(true);

listenerThread.start();
}

// =========================================================
// MAIN METHOD
// =========================================================

public static void main(
String[] args) {

SwingUtilities.invokeLater(() -> {

SmartHomeApp appFrame =
new SmartHomeApp();

// ---------------------------------------------
// SHOW LOGIN BEFORE MAIN APPLICATION
// ---------------------------------------------

if (showLoginDialog(appFrame)) {

appFrame.setVisible(true);

appFrame.logArea.append(
"Authentication successful.\n"
);

appFrame.logArea.append(
"Smart Home Control Panel ready.\n"
);
}
}
);
}
}