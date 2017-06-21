package DataServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import EventBus.MessageBus;
import RecServer.CenterStart;
import StromModel.LogMsg;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
*  注册中心界面
*/

public class frmStrom extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private final JLabel lblNewLabel = new JLabel("New label");
	private JTable table;
	JSplitPane splitPane;
	JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//SubstanceBusinessBlueSteelLookAndFeel
					//SubstanceEmeraldDuskLookAndFeel
					  UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceEmeraldDuskLookAndFeel");
					//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
					
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					  //UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
					frmStrom frame = new frmStrom();
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
	public frmStrom() {
		setTitle("\u6570\u636E\u4E2D\u5FC3");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				splitPane.setDividerLocation(0.5);
				//
				CenterStart  start=new CenterStart();
				start.start();
			    MessageBus.register("LogInfo", this);
			}
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 548, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u57FA\u7840\u529F\u80FD");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("\u8F6F\u4EF6\u81EA\u68C0");
		menu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("\u670D\u52A1\u68C0\u6D4B");
		menu.add(menuItem_1);
		
		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("\u5FC3\u8DF3\u8FD0\u884C");
		checkBoxMenuItem.setSelected(true);
		menu.add(checkBoxMenuItem);
		
		JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem("\u670D\u52A1\u8BF7\u6C42\u65E5\u5FD7\u663E\u793A");
		menu.add(checkBoxMenuItem_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("服务状态信息", null, panel, null);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		 splitPane = new JSplitPane();
		panel.add(splitPane);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5BFC\u822A\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		splitPane.setLeftComponent(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u670D\u52A1\u72B6\u6001\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(panel_4);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("日志信息", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("测试信息", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		 textArea = new JTextArea();
		 textArea.setLineWrap(true);        //激活自动换行功能 
		 textArea.setWrapStyleWord(true);  
		panel_2.add(textArea);
		contentPane.add(lblNewLabel, BorderLayout.SOUTH);
	}
    public void logShow(LogMsg msg)
    {
        if(msg.msg.isEmpty())
        {
            textArea.setText(msg.toString());
        }
        else if(msg.objMsg==null)
        {
            textArea.setText(msg.msg);
        }
            
   
    }
}
