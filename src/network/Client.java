package network;

























//Just for ideas


import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame {
 
  // Datagram socket
  private DatagramSocket socket;

  // The byte array for sending and receiving datagram packets
  private byte[] buf = new byte[256];

  // Server InetAddress
  private InetAddress address;

  // The packet sent to the server
  private DatagramPacket sendPacket;

  // The packet received from the server
  private DatagramPacket receivePacket;

  public static void main(String[] args) {
    new Client();
  }

  public Client() {
    // Panel p to hold the label and text field
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new JLabel("Connected to server"), BorderLayout.WEST);
    JLabel jtf;
	p.add(jtf, BorderLayout.CENTER);
    jtf.setHorizontalAlignment(JTextField.RIGHT);

    setLayout(new BorderLayout());
    add(p, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);

    jtf.addActionListener(new ButtonListener()); // Register listener

    setTitle("DatagramClient");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // get a datagram socket
      socket = new DatagramSocket();
      address = InetAddress.getByName("localhost");
      sendPacket =
        new DatagramPacket(buf, buf.length, address, 8000);
      receivePacket = new DatagramPacket(buf, buf.length);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        // Initialize buffer for each iteration
        Arrays.fill(buf, (byte)0);

        // send radius to the server in a packet
        sendPacket.setData(jtf.getText().trim().getBytes());
        socket.send(sendPacket);

        // receive area from the server in a packet
        socket.receive(receivePacket);

      
       
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}