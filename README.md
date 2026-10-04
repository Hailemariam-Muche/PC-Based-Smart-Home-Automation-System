# PC-Based-Smart-Home-Automation-System
Z Blue:
PC-BASED SMART HOME AUTOMATION SYSTEM
=========================================

PROJECT OVERVIEW
----------------
The PC-Based Smart Home Automation System is an embedded and desktop
application project designed to provide centralized monitoring and control
of household appliances and devices from a personal computer.

The system integrates a Java-based desktop application with a PIC16F877A
microcontroller through serial UART communication. The PC application
provides a graphical user interface (GUI) for controlling lamps and a door
motor, monitoring temperature, displaying fan status, and receiving system
feedback from the embedded controller.

This project demonstrates the integration of embedded systems, desktop
software, serial communication, sensor monitoring, and automated appliance
control in a practical smart-home application.


SYSTEM ARCHITECTURE
--------------------
PC / Java Desktop Application
          |
          | RS-232 / UART Serial Communication
          |
          v
PIC16F877A Microcontroller
          |
          +---- Lamp 1
          +---- Lamp 2
          +---- Lamp 3
          +---- Fan
          +---- Door Motor
          +---- Temperature Monitoring


KEY FEATURES
------------
1. PC-based graphical user interface for smart-home control.
2. Serial communication between the PC and PIC16F877A.
3. Independent ON/OFF control of three lamps.
4. Door OPEN/CLOSE control using a motor driver.
5. Temperature monitoring and display.
6. Automatic fan control based on temperature.
7. Real-time device status feedback from the microcontroller.
8. COM-port selection and serial connection management.
9. System event and communication logging.
10. Proteus-based circuit simulation and embedded-system development.


HARDWARE COMPONENTS
-------------------
- PIC16F877A Microcontroller
- Three Lamps / Lamp Indicators
- Fan Motor
- Door Motor
- L293D Motor Driver
- Temperature Sensor
- RS-232 Serial Communication Interface
- PC


SOFTWARE AND DEVELOPMENT TOOLS
-------------------------------
Embedded System:
- MikroC PRO for PIC
- PIC16F877A firmware

PC Application:
- Java
- Java Swing
- NetBeans IDE
- jSerialComm library

Simulation and Design:
- Proteus Design Suite


SERIAL COMMUNICATION
--------------------
The PC application communicates with the PIC16F877A using serial UART
communication.

Communication parameters:
- Baud Rate: 9600 bps
- Data Bits: 8
- Stop Bits: 1
- Parity: None

The PC sends control commands to the microcontroller, while the
microcontroller sends device status and temperature information back to
the PC application.


CONTROL COMMANDS
----------------
Lamp 1:
    A  = Lamp 1 ON
    a  = Lamp 1 OFF

Lamp 2:
    B  = Lamp 2 ON
    b  = Lamp 2 OFF

Lamp 3:
    C  = Lamp 3 ON
    c  = Lamp 3 OFF

Door:
    O  = Door OPEN
    X  = Door CLOSE


STATUS AND FEEDBACK MESSAGES
----------------------------
The embedded system can return status information to the PC application,
including:

    TEMP:value
    L1_ON
    L1_OFF
    L2_ON
    L2_OFF
    L3_ON
    L3_OFF
    FAN_ON
    FAN_OFF
    DOOR_OPENING
    DOOR_OPENED
    DOOR_CLOSING
    DOOR_CLOSED

These messages allow the Java application to update the GUI and provide
real-time information about the connected devices.


TEMPERATURE-BASED FAN CONTROL
-----------------------------
The system includes temperature monitoring and automatic fan control.

The configured control threshold is:

    Temperature >= 30 degrees C  -> Fan ON
    Temperature <  30 degrees C  -> Fan OFF

The measured temperature is transmitted to the Java application and
displayed on the PC interface.


PC APPLICATION
--------------
The Java desktop application provides:

- COM-port selection
- Connect and disconnect functions
- Lamp control switches
- Door control
- Temperature display
- Fan status monitoring
- Serial communication monitoring
- System log
- Real-time device feedback


PROJECT DEVELOPMENT
-------------------
The project was developed through the following general stages:

1. Design of the smart-home control concept.
2. Design and simulation of the embedded circuit in Proteus.
3. Development of PIC16F877A firmware using MikroC.
4. Implementation of UART serial communication.
5. Development of the Java Swing desktop application.
6. Integration of the Java application with the serial interface.
7. Implementation of appliance control and status feedback.
8. Implementation of temperature monitoring and automatic fan control.
9. Testing and debugging of hardware, firmware, communication, and GUI.


PROJECT STRUCTURE
-----------------
A typical project repository may contain:

    README.txt
    SmartHomeApp.java
    MikroC/
        PIC16F877A firmware source files
    Proteus/
        Circuit and simulation files
    Java/
        PC application source files
    Documentation/
        Project description and technical documentation
    Hardware/
        Hardware design files, diagrams, and photographs
    Evidence/
        Simulation screenshots, prototype photographs, and demonstration
        videos or related project evidence


APPLICATIONS
------------
This project can be used as:

- An educational embedded-systems project
- A smart-home automation prototype
- A UART communication demonstration
- A PIC microcontroller control project
- A Java-to-microcontroller integration example
- A foundation for developing larger home-automation systems


PROJECT HIGHLIGHTS
------------------
This project demonstrates practical integration of:

    Embedded Systems
    Microcontroller Programming
    Java Desktop Application Development
    GUI Design
    UART / Serial Communication
    Motor Control
    Temperature Monitoring
    Automatic Control
    Proteus Simulation


AUTHOR
------
Hailemariam Muche
Electrical and Computer Engineering

Project: PC-Based Smart Home Automation System


REPOSITORY NOTE
---------------
This repository is intended to document the design, implementation,
simulation, source code, and supporting evidence of the PC-Based Smart
Home Automation System.

Hardware connections, firmware configuration, serial communication
settings, and software dependencies should be reviewed before attempting
to reproduce the system.

