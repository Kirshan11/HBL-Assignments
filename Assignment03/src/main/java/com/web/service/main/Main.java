package com.web.service.main;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {
	private JTable table;
	private JTextField txtName;
	private DefaultTableModel model;
	private JLabel lblFile;
	private String customerNo = "";

	Main() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Kirshan Lal\\AS400 Project\\HBL-Logo.png");
		setIconImage(icon);
		setBounds(200, 200, 700, 500);
		setTitle("Habib Bank Limited");
		setBackground(getBackground().DARK_GRAY);
		getContentPane().setLayout(null);
		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(120, 120, 320, 100);
		getContentPane().add(scrollPane);

		// Table
		table = new JTable();
		scrollPane.setViewportView(table);

		JTextField textField = new JTextField();
		textField.setBounds(150, 60, 200, 20);
		getContentPane().add(textField);

		JLabel lable = new JLabel("Enter Customer No:");
		lable.setBounds(18, 55, 200, 30);
		lable.setBackground(getBackground().darker());
		getContentPane().add(lable);

		JLabel heading = new JLabel("CUNSUME WEB SERVICE TO GET CUSTOMER DATA");
		heading.setBounds(180, 10, 200, 30);
		getContentPane().add(heading);
		// Button Save
		JButton btnSave = new JButton("Send");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customerNo = textField.getText();
				String str = null;
				try {
					str = PopulateData();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (str.equals("SUCCESS")) {
					JOptionPane.showMessageDialog(null, "Customer Data Fetched");

				} else {
					JOptionPane.showMessageDialog(null, "Customer Number Not Found");
				}
			}
		});

		btnSave.setBounds(390, 56, 80, 30);
		getContentPane().add(btnSave);

	}

	public String PopulateData() throws IOException {

		table.setSize(800, 500);
		table.setModel(new DefaultTableModel());
		table.setBackground(getBackground().CYAN);
		// Model for Table
		model = (DefaultTableModel) table.getModel();
		model.addColumn("Customer Full Name");
		URL url;
		url = new URL("http://10.200.65.227:10094/web/services/CustomerName");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		String input = "{\r\n" + "  \"INPUTPARM\": {\r\n" + "    \"CUSTOMERNO\": \"" + customerNo + "\"\r\n" + "  }\r\n"
				+ "}";

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			String dequoted = output.substring(66, output.length() - 3);
			System.out.println(dequoted);
			System.out.println(output);
			model.addRow(new Object[0]);
			model.setValueAt(dequoted, 0, 0);

		}
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		conn.disconnect();
		return "SUCCESS";

	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main frame = new Main();
				frame.setVisible(true);
			}
		});

	}

}
