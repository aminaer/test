import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.proteanit.sql.DbUtils;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Work extends JFrame {

	Connection myConn = null ;
	Statement myStmt = null ;
	ResultSet myRs = null ;

	InputStream input = null;
	FileOutputStream output = null;
	
	private JPanel contentPane;
	private JTextField textField;
	String s;
	private JTextField textField_1;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Work frame = new Work();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private void DisplayTable() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "select id,nom,description from file ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public Work() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 637, 382);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				   JFileChooser fileChooser = new JFileChooser();
			         fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			         FileNameExtensionFilter filter = new FileNameExtensionFilter("Pdf file(.pdf)", "pdf");
			         fileChooser.addChoosableFileFilter(filter);
			         int result = fileChooser.showSaveDialog(null);
			         if(result == JFileChooser.APPROVE_OPTION){
			             File selectedFile = fileChooser.getSelectedFile();
			             String path = selectedFile.getAbsolutePath();
			             
			             s = path;
			              }
			         else if(result == JFileChooser.CANCEL_OPTION){
			             System.out.println("No Data");
			         }
			}
		});
		
		btnBrowse.setBounds(309, 310, 89, 23);
		contentPane.add(btnBrowse);
		
		JLabel lblNom = new JLabel("Nom : ");
		lblNom.setBounds(40, 235, 46, 14);
		contentPane.add(lblNom);
		
		JTextField textNom = new JTextField();
		textNom.setBounds(132, 232, 86, 20);
		contentPane.add(textNom);
		textNom.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setBounds(40, 269, 67, 14);
		contentPane.add(lblDescription);
		
		JTextArea textDes = new JTextArea();
		textDes.setBounds(132, 263, 149, 70);
		contentPane.add(textDes);
		
		
		JScrollPane scrollPane = new JScrollPane(textDes);
		scrollPane.setBounds(132, 263, 149, 70);
		contentPane.add(scrollPane);
		
		
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  try{
					  Class.forName("com.mysql.cj.jdbc.Driver");
		               Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
		               PreparedStatement ps = con.prepareStatement("insert into file(Nom,description,file) values(?,?,?)");
		               InputStream is = new FileInputStream(new File(s));
		               ps.setString(1, textNom.getText());
		               ps.setString(2, textDes.getText());
		               ps.setBlob(3,is);
		               ps.executeUpdate();
		               JOptionPane.showMessageDialog(null, "Data Inserted");
		               DisplayTable();
		           }catch(Exception ex){
		               ex.printStackTrace();
		           }
			}
		});
		btnAdd.setBounds(303, 265, 95, 23);
		contentPane.add(btnAdd);
		
		JTextField search = new JTextField();
		search.setBounds(64, 25, 211, 20);
		contentPane.add(search);
		search.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				try
				{
					myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
				    myStmt = myConn.createStatement();
					String sql = "select file from file where nom = '"+search.getText()+"';";
					myRs = myStmt.executeQuery(sql);
					File theFile = new File(search.getText()+".pdf");
					output = new FileOutputStream(theFile);
					
					if (myRs.next()) {
						
						input = myRs.getBinaryStream("file");
						System.out.println("Reading file from database ...");
						System.out.println(sql);
						
						byte[] buffer = new byte[1024];
						while (input.read(buffer) > 0) {
							output.write(buffer);
						}
					System.out.println("\nSaved to file : "+theFile.getAbsolutePath());
					System.out.println("\nCompleted Succesfully");
					
					Desktop.getDesktop().open(new java.io.File(theFile.getAbsolutePath()));
					
					}
							
				
				}catch (Exception exc) {
					exc.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		btnSearch.setBounds(400, 24, 89, 23);
		contentPane.add(btnSearch);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(64, 79, 477, 126);
		contentPane.add(scrollPane_1);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				  // Get The Index Of The Slected Row 
		        int i = table.getSelectedRow();

		        TableModel model = table.getModel();
		        String nom = model.getValueAt(i,1).toString();
		        try
				{
					myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
				    myStmt = myConn.createStatement();
					String sql = "select file from file where nom = '"+nom+"';";
					myRs = myStmt.executeQuery(sql);
					File theFile = new File(nom+".pdf");
					output = new FileOutputStream(theFile);
					
					if (myRs.next()) {
						
						input = myRs.getBinaryStream("file");
						System.out.println("Reading file from database ...");
						System.out.println(sql);
						
						byte[] buffer = new byte[1024];
						while (input.read(buffer) > 0) {
							output.write(buffer);
						}
					System.out.println("\nSaved to file : "+theFile.getAbsolutePath());
					System.out.println("\nCompleted Succesfully");
					
					Desktop.getDesktop().open(new java.io.File(theFile.getAbsolutePath()));
					
					}
							
				
				}catch (Exception exc) {
					exc.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"id", "nom", "description"},
			},
			new String[] {
				"New column", "New column", "New column"
			}
		));
		table.setBounds(64, 79, 477, 126);
		scrollPane_1.add(table);
		scrollPane_1.setViewportView(table);
		
		DisplayTable();

	}
}
