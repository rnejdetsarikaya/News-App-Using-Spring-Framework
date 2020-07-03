package com.yazlab.form;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.yazlab.dao.NewsDaoImpl;
import com.yazlab.model.News;
import com.yazlab.service.NewsServiceImpl;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class AdminPage extends JFrame{

	private JPanel contentPane;
	static AdminPage frame;
	NewsDaoImpl newsdao;
	/**
	 * @param newsService the newsService to set
	 */


	/**
	 * @param frame the frame to set
	 */
	
	String filename;
	private JTextField title;
	private JTextField type;
	private JTextField text;
	private JTextField url;
	private JTextField date;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AdminPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setFrame(AdminPage frame) {
		AdminPage.frame = frame;
	}

	
	/**
	 * Create the frame.
	 */
	public AdminPage() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\NECOO\\eclipse-workspace\\SpringRestWebService\\newspaper.png"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 484);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnNewButton = new JButton("Add Breaking News");
		btnNewButton.setBounds(441, 392, 148, 32);
		
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(12, 138, 420, 176);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("Choose \u0130mage");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame,"Choose a image", FileDialog.LOAD);
				fd.setFile("*.jpg;*.jpeg;*.png;*.bmp");
				fd.setVisible(true);
				filename = fd.getFile();
				if (filename == null)
				  System.out.println("You cancelled the choice");
				else {
					filename = fd.getDirectory()+fd.getFile();
					
					BufferedImage image;
					try {
						image = ImageIO.read(new File(filename));
						Image dimg = image.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(),
						        Image.SCALE_SMOOTH);//yüklenen fotoðrafý labele sýðdýr
						ImageIcon imageIcon = new ImageIcon(dimg);
						lblNewLabel.setIcon(imageIcon);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("You chose " + filename);
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
					Date d = f.parse(date.getText());
					System.out.println(f.format(new Date()));
					News news = new News(filename,title.getText(),text.getText(),type.getText(),f.parse(f.format(d)));
					newsdao = new NewsDaoImpl();
					newsdao.create(news);
					title.setText("");
					url.setText("");
					type.setText("");
					text.setText("");
					date.setText("");
					lblNewLabel.setIcon(null);
					lblNewLabel.revalidate();
					//newsdao.list();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(441, 24, 148, 32);
		contentPane.add(btnNewButton_1);
		
		title = new JTextField();
		title.setBounds(12, 24, 420, 32);
		contentPane.add(title);
		title.setColumns(10);
		
		Label label = new Label("News Title");
		label.setBounds(10, 0, 70, 24);
		contentPane.add(label);
		
		Label label_1 = new Label("News Type");
		label_1.setBounds(441, 70, 75, 24);
		contentPane.add(label_1);
		
		type = new JTextField();
		type.setBounds(441, 100, 148, 32);
		contentPane.add(type);
		type.setColumns(10);
		
		Label label_3 = new Label("News Text");
		label_3.setBounds(10, 320, 70, 24);
		contentPane.add(label_3);
		
		text = new JTextField();
		text.setBounds(12, 350, 420, 74);
		contentPane.add(text);
		text.setColumns(10);
		
		Label label_2 = new Label("Image URL");
		label_2.setBounds(12, 70, 70, 24);
		contentPane.add(label_2);
		
		JButton btnNewButton_2 = new JButton("Add");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			filename = url.getText();
			URL u;
			try {
				u = new URL(filename);
				Image image = ImageIO.read(u);
				Image dimg = image.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(),
				        Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(dimg);
				lblNewLabel.setIcon(icon);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(filename);
			
			}
		});
		btnNewButton_2.setBounds(365, 100, 67, 32);
		contentPane.add(btnNewButton_2);
		
		url = new JTextField();
		url.setColumns(10);
		url.setBounds(12, 100, 341, 32);
		contentPane.add(url);
		
		JLabel lblDate = new JLabel("Date (dd-MM-yyyy)");
		lblDate.setBounds(441, 138, 119, 24);
		contentPane.add(lblDate);
		
		date = new JTextField();
		date.setBounds(441, 167, 148, 32);
		contentPane.add(date);
		date.setColumns(10);
		
	}
}
