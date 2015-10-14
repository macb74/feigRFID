import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.application.Application;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FeigRfid extends javax.swing.JFrame implements FeigGuiListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private static JPanel jPanelReadTabel;
	private JPanel jPanelConfigPlane;
	private JPanel jPanelButtons;
	private JPanel jPanelReadFields;
	private static JScrollPane jScrollPanelReadTable;
	private JLabel jLabelIp;
	private static JLabel jLabelFileName;
	private JComboBox jComboBoxSort;
	private JLabel jLabelConnected;
	private JButton jButtonClearReadTable;
	private JButton jButtonSetStartTime;
	private JTextArea textAreaProtocol;
	private JScrollPane jScrollPaneProtocoll;
	private JLabel jLabelMessage;
	private JLabel jLabelZeit;
	private JLabel jLabelWriteInfo;
	private JTextField jTextFieldWriteStnr;
	private static JPanel jPanelWriteArea;
	private static JCheckBox jCheckBoxUniqeId;
	private static JLabel jLabelUseUniqeId;
	private static JLabel jLabelLID;
	private static JLabel jLabelVID;
	private static JLabel jLabelDatabase;
	private JLabel jLabelAntennasValue;
	private JLabel jLabelSleepTime;
	private static JLabel jLabelTrValidTime;
	private static JLabel jLabelStartTime;
    private JLabel jLabelModeValueRead;
    private JLabel jLabelModeRead;
    private JLabel jLabelAntennasRead;
    private JLabel jLabelPower;
    private JButton jButtonSetMode;
	private JButton jButtonGetReaderConfig;
	private JButton jButtonSetReaderTime;
	private static JButton jButtonSetValTime;
	private static JButton jButtonSetPower;
	private JButton jButtonSetAnt;
	private JButton jButtonStart;
	private JButton jButtonSwitchMode;
	private JToggleButton jToggleBeep;
	private static JTextField jTextFieldLID;
	private static JTextField jTextFieldVID;
	private static JTextField jTextFieldIp;
	private static JTextField jTextFieldSleepTime;
	private static JTextField jTextFieldStartTime;
	private static JTextField jTextFieldTrValidTime;
	private static JTextField jTextFieldFileName;
	private static JTextField jTextFieldPower;
	private static JCheckBox databaseCheckBox;
	private JTable dataTable;


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {

		// Reader initialisieren
        try
        {
        	fedm = new de.feig.FedmIscReader();
        }
        catch (de.feig.FedmException e)
        {
            System.err.println(e.toString());
            System.gc();
            System.exit(e.getErrorcode());
        }
        catch (java.lang.Exception e)
        {
            System.err.println(e.toString());
            System.gc();
        }

        if(!ReadConfig.getConfig().getBoolean("HEADLESS")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					FeigRfid inst = new FeigRfid();
					inst.setLocationRelativeTo(null);
					inst.setVisible(true);
					setConfigValues();
					mode = "read";
					showReadFields(mode);
				}
			});
        } else {
        	new FeigRfidHeadless(fedm);
        }
	}
	
	public FeigRfid() {
		super();

		sDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		sTime = sDate + " 00:00:00";
		sort = 0;

		initGUI();
		UhrzeitThread uz = new UhrzeitThread();
		uz.start();
		
		// eine halbe Sekunde warten, bis das GUI fertig aufgebaut ist
		try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		onGetReaderSets(true);
	}

	private void initGUI() {

		try {
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        setTitle("open RFID Timing");
	        {
	        	jPanelReadTabel = new JPanel();
	        	getContentPane().add(jPanelReadTabel, new AnchorConstraint(0, 573, 977, 0, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
	        	AnchorLayout jPanelReadTabelLayout = new AnchorLayout();
	        	jPanelReadTabel.setLayout(jPanelReadTabelLayout);
	        	jPanelReadTabel.setPreferredSize(new java.awt.Dimension(535, 492));
	        	{
	        		jScrollPanelReadTable = new JScrollPane();
	        		jPanelReadTabel.add(jScrollPanelReadTable, new AnchorConstraint(29, 1000, 1000, 28, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
	        		jScrollPanelReadTable.setPreferredSize(new java.awt.Dimension(533, 486));
	        		{
	        			String[] columnNames = {"No", "Stnr", "Lap", "Time", "Finish Time", "ID"};
	        			
	        			dataTableModel = new DefaultTableModel(null, columnNames);
	        			dataTable = new JTable();
	        			dataTable.setModel(dataTableModel);
	        			
	        			dataTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
	        			dataTable.setDefaultRenderer(Number.class, new CustomCellRenderer()); 
	        				        			
	        			dataTable.getColumn("No").setPreferredWidth(5);
	        			dataTable.getColumn("Stnr").setPreferredWidth(10);
	        			dataTable.getColumn("Lap").setPreferredWidth(5);
	        			dataTable.getColumn("Time").setPreferredWidth(50);
	        			dataTable.getColumn("Finish Time").setPreferredWidth(50);
	        			dataTable.getColumn("ID").setPreferredWidth(180);
	        		    
	        			jScrollPanelReadTable.setViewportView(dataTable);
	        			dataTable.setFillsViewportHeight(true);
	        			dataTable.setName("dataTable");

	        		}
	        	}
	        }
	        {
	        	jPanelWriteArea = new JPanel();
	        	getContentPane().add(jPanelWriteArea, new AnchorConstraint(0, 573, 977, 0, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
	        	GridBagLayout jPanel1Layout = new GridBagLayout();
	        	jPanelWriteArea.setPreferredSize(new java.awt.Dimension(535, 500));
	        	jPanel1Layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.1};
	        	jPanel1Layout.rowHeights = new int[] {89, 157, 41, 7};
	        	jPanel1Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
	        	jPanel1Layout.columnWidths = new int[] {7, 7, 7};
	        	jPanelWriteArea.setLayout(jPanel1Layout);
	        	{
	        		jTextFieldWriteStnr = new JTextField();
	        		jPanelWriteArea.add(jTextFieldWriteStnr, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jTextFieldWriteStnr.setPreferredSize(new java.awt.Dimension(128, 67));
	        		jTextFieldWriteStnr.setName("jTextFieldWriteStnr");
	        		jTextFieldWriteStnr.setHorizontalAlignment(JTextField.CENTER);
	        	}
	        	{
	        		jLabelWriteInfo = new JLabel();
	        		jPanelWriteArea.add(jLabelWriteInfo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jLabelWriteInfo.setName("jLabelWriteInfo");
	        		jLabelWriteInfo.setHorizontalAlignment(JTextField.CENTER);
	        		jLabelWriteInfo.setPreferredSize(new java.awt.Dimension(179, 15));
	        	}
	        	{
	        		jScrollPaneProtocoll = new JScrollPane();
	        		jPanelWriteArea.add(jScrollPaneProtocoll, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jScrollPaneProtocoll.setPreferredSize(new java.awt.Dimension(527, 185));
	        		{
	        			textAreaProtocol = new JTextArea();
	        			jScrollPaneProtocoll.setViewportView(textAreaProtocol);
	        			textAreaProtocol.setName("textAreaProtocol");
	        		}
	        	}
	        }
	        {
	        	jPanelConfigPlane = new JPanel();
	        	getContentPane().add(jPanelConfigPlane, new AnchorConstraint(25, 997, 977, 581, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
	        	GridBagLayout jPanelConfigPlaneLayout = new GridBagLayout();
	        	jPanelConfigPlaneLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
	        	jPanelConfigPlaneLayout.rowHeights = new int[] {50, 25, 120, 30, 45};
	        	jPanelConfigPlaneLayout.columnWeights = new double[] {0.1};
	        	jPanelConfigPlaneLayout.columnWidths = new int[] {7};
	        	jPanelConfigPlane.setLayout(jPanelConfigPlaneLayout);
	        	jPanelConfigPlane.setPreferredSize(new java.awt.Dimension(388, 500));
	        	{
	        		jButtonStart = new JButton();
	        		jPanelConfigPlane.add(jButtonStart, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jButtonStart.setPreferredSize(new java.awt.Dimension(372, 37));
	        		jButtonStart.setText("Read");
	        	}
	        	{
	        		jPanelButtons = new JPanel();
	        		jPanelConfigPlane.add(jPanelButtons, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		GridBagLayout jPanelReadButtonsLayout = new GridBagLayout();
	        		jPanelButtons.setPreferredSize(new java.awt.Dimension(360, 112));
	        		jPanelReadButtonsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
	        		jPanelReadButtonsLayout.rowHeights = new int[] {7, 7, 7, 7};
	        		jPanelReadButtonsLayout.columnWeights = new double[] {0.1, 0.1};
	        		jPanelReadButtonsLayout.columnWidths = new int[] {20, 20};
	        		jPanelButtons.setLayout(jPanelReadButtonsLayout);
	        		{
	        			jButtonGetReaderConfig = new JButton();
	        			jPanelButtons.add(jButtonGetReaderConfig, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonGetReaderConfig.setText("get Reader Config");
	        			jButtonGetReaderConfig.setPreferredSize(new java.awt.Dimension(165, 21));
	        			jButtonGetReaderConfig.setName("jButtonGetReaderConfig");
	        		}
	        		{
	        			jButtonSetReaderTime = new JButton();
	        			jPanelButtons.add(jButtonSetReaderTime, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetReaderTime.setPreferredSize(new java.awt.Dimension(165, 21));
	        			jButtonSetReaderTime.setText("set Reader Time");
	        			jButtonSetReaderTime.setName("jButtonSetReaderTime");
	        		}
	        		{
	        			jButtonSetMode = new JButton();
	        			jPanelButtons.add(jButtonSetMode, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetMode.setPreferredSize(new java.awt.Dimension(165, 21));
	        			jButtonSetMode.setName("jButtonSetMode");
	        			jButtonSetMode.setText("enable BRM Mode");	        			
	        		}
	        		{
	        			jToggleBeep = new JToggleButton();
	        			jPanelButtons.add(jToggleBeep, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jToggleBeep.setName("jToggleBeep");
	        			jToggleBeep.setPreferredSize(new java.awt.Dimension(165, 21));
	        		}
	        		{
	        			jButtonSetAnt = new JButton();
	        			jPanelButtons.add(jButtonSetAnt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetAnt.setName("jButtonSetAnt");
	        			jButtonSetAnt.setPreferredSize(new java.awt.Dimension(165, 21));
	        		}
	        		{
	        			jButtonSwitchMode = new JButton();
	        			jPanelButtons.add(jButtonSwitchMode, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSwitchMode.setName("jButtonSwitchMode");
	        			jButtonSwitchMode.setText("write Mode");	        			
	        			jButtonSwitchMode.setPreferredSize(new java.awt.Dimension(165, 21));
	        		}

	        		{
	        			jButtonClearReadTable = new JButton();
	        			jPanelButtons.add(jButtonClearReadTable, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonClearReadTable.setName("jButtonClearReadTable");
	        			jButtonClearReadTable.setPreferredSize(new java.awt.Dimension(165, 21));
	        		}

	        		{
	        			ComboBoxModel jComboBoxSortModel = 
	        					new DefaultComboBoxModel(
	        							new String[] { "Event Log", "Result by last Event", "Race Result" });
	        			jComboBoxSort = new JComboBox();
	        			jPanelButtons.add(jComboBoxSort, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jComboBoxSort.setModel(jComboBoxSortModel);
	        			jComboBoxSort.setPreferredSize(new java.awt.Dimension(160, 21));
	        			jComboBoxSort.setFont(new Font("Dialog", Font.BOLD, 11));
	        		}
	        	}
	        	{
	        		jPanelReadFields = new JPanel();
	        		jPanelConfigPlane.add(jPanelReadFields, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		GridBagLayout jPanelReadFieldsLayout = new GridBagLayout();
	        		jPanelReadFieldsLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	        		jPanelReadFieldsLayout.rowHeights = new int[] {24, 24, 22, 24, 24, 24, 24, 24, 24, 24};
	        		jPanelReadFieldsLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1};
	        		jPanelReadFieldsLayout.columnWidths = new int[] {125, 55, 5, 40, 40, 40, 40};
	        		jPanelReadFields.setLayout(jPanelReadFieldsLayout);
	        		jPanelReadFields.setPreferredSize(new java.awt.Dimension(360, 240));
	        		{
	        			jButtonSetPower = new JButton();
	        			jPanelReadFields.add(jButtonSetPower, new GridBagConstraints(5, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetPower.setText("set");
	        			jButtonSetPower.setName("jButtonSetPower");
	        			jButtonSetPower.setPreferredSize(new java.awt.Dimension(69, 22));
	        		}
	        		{
	        			jTextFieldPower = new JTextField();
	        			jPanelReadFields.add(jTextFieldPower, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldPower.setName("jTextFieldPower");
	        			jTextFieldPower.setPreferredSize(new java.awt.Dimension(70, 19));
	        		}
	        		{
	        			jLabelPower = new JLabel();
	        			jPanelReadFields.add(jLabelPower, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelPower.setText("Power (mW):");
	        			jLabelPower.setPreferredSize(new java.awt.Dimension(116, 14));
	        			jLabelPower.setName("jLabelPower");
	        		}
	        		{
	        			jButtonSetValTime = new JButton();
	        			jPanelReadFields.add(jButtonSetValTime, new GridBagConstraints(5, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetValTime.setText("set");
	        			jButtonSetValTime.setName("jButtonSetValTime");
	        			jButtonSetValTime.setPreferredSize(new java.awt.Dimension(69, 22));
	        		}
	        		{
	        			jTextFieldTrValidTime = new JTextField();
	        			jPanelReadFields.add(jTextFieldTrValidTime, new GridBagConstraints(1, 4, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldTrValidTime.setName("jTextFieldTrValidTime");
	        			jTextFieldTrValidTime.setPreferredSize(new java.awt.Dimension(70, 19));
	        		}
	        		{
	        			jLabelTrValidTime = new JLabel();
	        			jPanelReadFields.add(jLabelTrValidTime, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelTrValidTime.setText("Tr. valid Time (s):");
	        			jLabelTrValidTime.setPreferredSize(new java.awt.Dimension(116, 14));
	        			jLabelTrValidTime.setName("jLabelTrValidTime");
	        		}
	        		{
	        			jLabelAntennasRead = new JLabel();
	        			jPanelReadFields.add(jLabelAntennasRead, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelAntennasRead.setText("Antennas:");
	        			jLabelAntennasRead.setPreferredSize(new java.awt.Dimension(70, 16));
	        			jLabelAntennasRead.setName("jLabelAntennas");
	        		}
	        		{
	        			jLabelAntennasValue = new JLabel("",SwingConstants.LEFT);
	        			jPanelReadFields.add(jLabelAntennasValue, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelAntennasValue.setText("-");
	        			jLabelAntennasValue.setName("jLabelAntennasValue");
	        			jLabelAntennasValue.setPreferredSize(new java.awt.Dimension(50, 13));
	        		}
	        		{
	        			jLabelIp = new JLabel();
	        			jPanelReadFields.add(jLabelIp, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelIp.setPreferredSize(new java.awt.Dimension(44, 13));
	        			jLabelIp.setText("IP:");
	        			jLabelIp.setName("jLabelIp");
	        		}
	        		{
	        			jTextFieldIp = new JTextField();
	        			jPanelReadFields.add(jTextFieldIp, new GridBagConstraints(1, 7, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldIp.setName("jTextFieldFileName");
	        			jTextFieldIp.setPreferredSize(new java.awt.Dimension(130, 19));
	        		}
	        		{
	        			jLabelFileName = new JLabel();
	        			jPanelReadFields.add(jLabelFileName, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelFileName.setPreferredSize(new java.awt.Dimension(73, 14));
	        			jLabelFileName.setText("File Name:");
	        			jLabelFileName.setName("jLabelFileName");
	        		}
	        		{
	        			jTextFieldFileName = new JTextField();
	        			jPanelReadFields.add(jTextFieldFileName, new GridBagConstraints(1, 8, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldFileName.setText("");
	        			jTextFieldFileName.setName("jTextFieldFileName");
	        			jTextFieldFileName.setPreferredSize(new java.awt.Dimension(130, 19));
	        		}
	        		{
	        			jLabelSleepTime = new JLabel();
	        			jPanelReadFields.add(jLabelSleepTime, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelSleepTime.setPreferredSize(new java.awt.Dimension(104, 15));
	        			jLabelSleepTime.setText("sleep Time (s):");
	        			jLabelSleepTime.setName("jLabelSleepTime");
	        		}
	        		{
	        			jLabelStartTime = new JLabel();
	        			jPanelReadFields.add(jLabelStartTime, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelStartTime.setPreferredSize(new java.awt.Dimension(77, 15));
	        			jLabelStartTime.setText("Start Time:");
	        			jLabelStartTime.setName("jLabelStartTime");
	        		}
	        		{
	        			jTextFieldStartTime = new JTextField();
	        			jPanelReadFields.add(jTextFieldStartTime, new GridBagConstraints(1, 5, 0, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldStartTime.setText(sTime);
	        			jTextFieldStartTime.setPreferredSize(new java.awt.Dimension(130, 19));
	        			jTextFieldStartTime.setName("jTextFieldStartTime");
	        		}
	        		{
	        			jLabelModeRead = new JLabel();
	        			jPanelReadFields.add(jLabelModeRead, new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelModeRead.setPreferredSize(new java.awt.Dimension(44, 14));
	        			jLabelModeRead.setText("Mode:");
	        			jLabelModeRead.setName("jLabelMode");
	        		}
	        		{
	        			jLabelModeValueRead = new JLabel("",SwingConstants.LEFT);
	        			jPanelReadFields.add(jLabelModeValueRead, new GridBagConstraints(4, 9, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelModeValueRead.setPreferredSize(new java.awt.Dimension(40, 14));
	        			jLabelModeValueRead.setText("-");
	        			jLabelModeValueRead.setName("jLabelModeValue");
	        		}
	        		{
	        			jTextFieldSleepTime = new JTextField();
	        			jPanelReadFields.add(jTextFieldSleepTime, new GridBagConstraints(1, 6, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldSleepTime.setPreferredSize(new java.awt.Dimension(70, 19));
	        			jTextFieldSleepTime.setText("");
	        			jTextFieldSleepTime.setName("jTextFieldSleepTime");
	        		}
	        		{
	        			jLabelDatabase = new JLabel();
	        			jPanelReadFields.add(jLabelDatabase, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelDatabase.setName("jLabelDatabase");
	        			jLabelDatabase.setPreferredSize(new java.awt.Dimension(123, 14));
	        		}
	        		{
	        			databaseCheckBox = new JCheckBox();
	        			jPanelReadFields.add(databaseCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			databaseCheckBox.setPreferredSize(new java.awt.Dimension(21, 21));
	        			databaseCheckBox.setName("databaseCheckBox");
	        		}
	        		{
	        			jTextFieldVID = new JTextField();
	        			jPanelReadFields.add(jTextFieldVID, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldVID.setText("0");
	        			jTextFieldVID.setPreferredSize(new java.awt.Dimension(30, 19));
	        		}
	        		{
	        			jTextFieldLID = new JTextField();
	        			jPanelReadFields.add(jTextFieldLID, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jTextFieldLID.setText("0");
	        			jTextFieldLID.setPreferredSize(new java.awt.Dimension(30, 19));
	        		}
	        		{
	        			jLabelVID = new JLabel();
	        			jPanelReadFields.add(jLabelVID, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelVID.setName("jLabelVID");	        		
	        		}
	        		{
	        			jLabelLID = new JLabel();
	        			jPanelReadFields.add(jLabelLID, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelLID.setName("jLabelLID");
	        		}
	        		{
	        			jLabelUseUniqeId = new JLabel();
	        			jPanelReadFields.add(jLabelUseUniqeId, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelUseUniqeId.setPreferredSize(new java.awt.Dimension(123,14));
	        			jLabelUseUniqeId.setName("jLabelUseUniqeId");
	        		}
	        		{
	        			jCheckBoxUniqeId = new JCheckBox();
	        			jPanelReadFields.add(jCheckBoxUniqeId, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jCheckBoxUniqeId.setPreferredSize(new java.awt.Dimension(21,21));
	        			jCheckBoxUniqeId.setName("jCheckBoxUniqeId");
	        		}
	        		{
	        			jButtonSetStartTime = new JButton();
	        			jPanelReadFields.add(jButtonSetStartTime, new GridBagConstraints(5, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jButtonSetStartTime.setName("jButtonSetStartTime");
	        			jButtonSetStartTime.setText("set");
	        			jButtonSetStartTime.setPreferredSize(new java.awt.Dimension(69, 22));
	        		}
	        		{
	        			jLabelConnected = new JLabel();
	        			jPanelReadFields.add(jLabelConnected, new GridBagConstraints(6, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        			jLabelConnected.setOpaque(true);
	        			jLabelConnected.setBackground(Color.LIGHT_GRAY);
	        			jLabelConnected.setPreferredSize(new java.awt.Dimension(10, 10));
	        		}
	        	}
	        	{
	        		jLabelZeit = new JLabel();
	        		jPanelConfigPlane.add(jLabelZeit, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jLabelZeit.setName("jLabelZeit");
	        		jLabelZeit.setHorizontalAlignment(JTextField.CENTER);
	        		jLabelZeit.setPreferredSize(new java.awt.Dimension(225, 40));
	        	}
	        	{
	        		jLabelMessage = new JLabel();
	        		jPanelConfigPlane.add(jLabelMessage, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	        		jLabelMessage.setName("jLabelErrorMessage");
	        		jLabelMessage.setHorizontalAlignment(JTextField.CENTER);
	        		
	        		jLabelMessage.setPreferredSize(new java.awt.Dimension(339, 20));
	        	}
	        }

	        // Write Mode


			pack();
			this.setSize(950, 550);
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				thisWindowClosing(evt);
			}
		});
		
		jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonStartActionPerformed(evt);
            }
        });
		
		jButtonSetAnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSetAntActionPerformed(evt);
            }
        });

		jButtonSetValTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSetValidTimeActionPerformed(evt);
            }
        });

		jButtonSetPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSetPowerActionPerformed(evt);
            }
        });

		jButtonSetStartTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSetStartTimeActionPerformed(evt);
            }
        });
		
		jButtonSetMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
        		if(evt.getActionCommand().equals("enable BRM Mode")) {
                	setMode("BRM");        			
        		} else {
                	setMode("ISO");        			        			
        		}
            }
        });
		
		jButtonSetReaderTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSetReaderTimeActionPerformed(evt);
            }
        });
		
		jButtonGetReaderConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonGetReaderConfigActionPerformed(evt);
            }
        });

		jButtonSwitchMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonSwitchModeActionPerformed(evt);
            }
        });
		
		jToggleBeep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jToggleBeepActionPerformed(evt);
            }
        });
		
		jComboBoxSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jComboBoxSortActionPerformed(evt);
            }

        });
		
		jButtonClearReadTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButtonClearReadTableActionPerformed(evt);
            }
        });	
		
		
		
	}
		
	private void jButtonStartActionPerformed(ActionEvent evt) {        
		if(mode.equals("read")) {
			read(evt);
		} else {
			write(evt);
		}
		
	}

	
	private void jComboBoxSortActionPerformed(ActionEvent evt) {

		switch (jComboBoxSort.getSelectedIndex()) {
			case 0: sort = 0;
					onGetReaderSets(true);
					break;
			case 1: sort = 1;
					onGetReaderSets(true);
					break;
			case 2: sort = 2;
					onGetReaderSets(true);
					break;
		}
	}

	
	private void read(ActionEvent evt) {
		if (evt.getActionCommand().equals("Read")) {
            // rename button
        	jButtonStart.setText("Stop");
        	enableAllFields(false);
        	jComboBoxSort.setEnabled(true);
        	
        	int[] id = new int[2];
        	id[0] = Integer.parseInt(jTextFieldVID.getText());
        	id[1] = Integer.parseInt(jTextFieldLID.getText());
        	
            // start thread
			brmReadThread = new BrmReadThread();
	        brmReadThread.setFedmIscReader(fedm);
	        brmReadThread.setHost(jTextFieldIp.getText());
	        brmReadThread.setSleepTime(Integer.parseInt(jTextFieldSleepTime.getText())*1000);
	        brmReadThread.setSets(ReadConfig.getConfig().getInt("SETS"));
			brmReadThread.setDB(databaseCheckBox.isSelected());
			brmReadThread.setId(id);
	        brmReadThread.setFeigGuiListener(this);
            runner = new Thread(brmReadThread);
            brmReadThread.setRunning(true);
            runner.start(); 
		} else {
        	jButtonStart.setText("Read");
			enableAllFields(true);
        	showReadFields(mode);

            // disable timer
            brmReadThread.setRunning(false);
            runner = null;
        }		
	}

	private void write(ActionEvent evt) {
		if (evt.getActionCommand().equals("Write")) {
        	jButtonStart.setText("Stop");
        	enableAllFields(false);
    			
			if (Integer.parseInt(jTextFieldWriteStnr.getText()) > 65535) {
				setMessage("Startnummer muss kleiner 65536 sein", 2000);
			}
    		
			try {
				intStNr = Integer.parseInt(jTextFieldWriteStnr.getText());
			} catch (Exception E){
				setMessage("Bitte die neue Nummer als Zahl angeben\n", 2000);
			}
			
			writeTag = new WriteTag();
			writeTag.setUniqeID(jCheckBoxUniqeId.isSelected());
			writeTag.setFedmIscReader(fedm);
			writeTag.setHost(jTextFieldIp.getText());
			writeTag.setNewSnr(intStNr);
			writeTag.setSleepTime(Integer.parseInt(jTextFieldSleepTime.getText())*1000);
			writeTag.setFeigGuiListener(this);
			writeTag.run();
            runner = new Thread(writeTag);
            writeTag.setRunning(true);
            runner.start(); 
		
		} else {
			writeTag.setRunning(false);
            runner = null;
        	jButtonStart.setText("Write");
        	enableAllFields(true);
        	showReadFields(mode);
		}
	}

	private void jButtonSwitchModeActionPerformed(ActionEvent evt) { 	
		if(evt.getActionCommand().equals("write Mode")) {
			jButtonSwitchMode.setText("read Mode");
			jButtonSetMode.setText("enable ISO Mode");
			jButtonStart.setText("Write");
			mode = "write";
			enableAllFields(true);
			showReadFields(mode);
		} else {
			jButtonSwitchMode.setText("write Mode");
			jButtonSetMode.setText("enable BRM Mode");
			jButtonStart.setText("Read");
			mode = "read";
			enableAllFields(true);
			showReadFields(mode);
		}
	}
	
	private void enableAllFields(boolean b) {
		Component[] allItems = jPanelReadFields.getComponents();
		for( Component c : allItems ) {
			c.setEnabled(b);
		}
		Component[] allButtons = jPanelButtons.getComponents();
		for( Component c : allButtons ) {
			c.setEnabled(b);
		}
	}
	
	private static void showReadFields(String mode) {
		if(mode.equals("read")) {
			jPanelReadTabel.setVisible(true);
			jPanelWriteArea.setVisible(false);
			jLabelTrValidTime.setEnabled(true);
			jTextFieldTrValidTime.setEnabled(true);
			jButtonSetValTime.setEnabled(true);
			jTextFieldStartTime.setEnabled(true);
			jLabelStartTime.setEnabled(true);
			jTextFieldVID.setEnabled(true);
			jLabelVID.setEnabled(true);
			jTextFieldLID.setEnabled(true);
			jLabelLID.setEnabled(true);
			jTextFieldFileName.setEnabled(true);
			jLabelFileName.setEnabled(true);
			jLabelDatabase.setEnabled(true);
			databaseCheckBox.setEnabled(true);
			jLabelUseUniqeId.setEnabled(false);
			jCheckBoxUniqeId.setEnabled(false);
		} else {
			jPanelReadTabel.setVisible(false);
			jPanelWriteArea.setVisible(true);
			jLabelTrValidTime.setEnabled(false);
			jTextFieldTrValidTime.setEnabled(false);
			jButtonSetValTime.setEnabled(false);
			jLabelStartTime.setEnabled(false);
			jTextFieldStartTime.setEnabled(false);
			jLabelStartTime.setEnabled(false);
			jTextFieldVID.setEnabled(false);
			jLabelVID.setEnabled(false);
			jTextFieldLID.setEnabled(false);
			jLabelLID.setEnabled(false);
			jTextFieldFileName.setEnabled(false);
			jLabelFileName.setEnabled(false);
			jLabelDatabase.setEnabled(false);
			databaseCheckBox.setEnabled(false);
			jLabelUseUniqeId.setEnabled(true);
			jCheckBoxUniqeId.setEnabled(true);
		}
	}
	
	private void jButtonSetValidTimeActionPerformed(ActionEvent evt) {        

		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();

    	if(con.isConnected()) {
	    	SetValidTime setValidTime = new SetValidTime();
			setValidTime.setFedmIscReader(fedm);
			setValidTime.setNewValidTime(jTextFieldTrValidTime.getText());
			setValidTime.run();
    		setMessage("Reset...", 2000);			
    	} else {
    		setMessage("Can not connect", 2000);
    	}
		con.fedmCloseConnection();
		
	}

	private void jButtonSetPowerActionPerformed(ActionEvent evt) {        

		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();

    	if(con.isConnected()) {
	    	SetPower setPower = new SetPower();
	    	setPower.setFedmIscReader(fedm);
	    	setPower.setNewPower(jTextFieldPower.getText());
	    	setPower.run();
    		setMessage("Reset...", 2000);
    	} else {
    		setMessage("Can not connect", 2000);
    	}
		con.fedmCloseConnection();
	}

	private void jButtonSetStartTimeActionPerformed(ActionEvent evt) {
		if((sTime.equals(sDate + " 00:00:00") && (jTextFieldStartTime.getText().equals(sDate + " 00:00:00")))|| jTextFieldStartTime.getText().equals("")) {
			String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
			sTime = sDate + " " + nowTime;
			jTextFieldStartTime.setText(sTime);
		} else {
			sTime = jTextFieldStartTime.getText();
		}
		onGetReaderSets(true);
	}
	
	private void jButtonClearReadTableActionPerformed(ActionEvent evt) {
		try {
			Connection cn = Derby.derbyConnect();
			Derby.derbyUpdate("TRUNCATE TABLE \"APP\".\"ZEIT\"", cn);
			Derby.derbyDisconnect(cn);
			onGetReaderSets(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void jButtonSetAntActionPerformed(ActionEvent evt) {        

		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();

    	if(con.isConnected()) {
	    	SetAntenna setAnt = new SetAntenna();
	    	setAnt.setFedmIscReader(fedm);
	    	setAnt.run();
    	} else {
    		setMessage("Can not connect", 2000);
    	}

    	try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		getConfig();
    	con.fedmCloseConnection();

	}
	
	private void setMode(String mode) {
				
		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();

    	if(con.isConnected()) {
	    	SetMode setMode = new SetMode();
			setMode.setFedmIscReader(fedm);
			setMode.setNewMode(mode);
			setMode.run();
    		setMessage("Reset...", 2000);
    	} else {
    		setMessage("Can not connect", 2000);
    	}
    	
    	getConfig();		
		con.fedmCloseConnection();
	}

	
	private void jButtonSetReaderTimeActionPerformed(ActionEvent evt) {        

    	String output;
		
		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();

    	if(con.isConnected()) {
	    	SetTime setTime = new SetTime();
			setTime.setFedmIscReader(fedm);
			output = setTime.run();
			setMessage(output, 2000);
    	} else {
    		setMessage("Can not connect", 2000);
    	}
		con.fedmCloseConnection();

	}
	
	private void jButtonGetReaderConfigActionPerformed(ActionEvent evt) {      

		FedmConnect con = new FedmConnect();
		con.setFedmIscReader(fedm);
		con.setHost(jTextFieldIp.getText());
		con.fedmOpenConnection();

		if(con.isConnected()) {
			getConfig();
		} else {
			setMessage("Can not connect", 2000);
		}
		con.fedmCloseConnection();
	}

	private void getConfig() {
		
		String[] readerInfo = new String[5];

    	Info info = new Info();
		info.setFedmIscReader(fedm);
		readerInfo = info.run();

		jTextFieldTrValidTime.setText(readerInfo[1]);
		jLabelAntennasValue.setText(readerInfo[3]);
		jLabelModeValueRead.setText(readerInfo[0]);
		jTextFieldPower.setText(readerInfo[4]);
		
		setMessage("Reader Time: " + readerInfo[2], 2000);
	}
	
	private void jToggleBeepActionPerformed(ActionEvent evt) {        
			
		if (evt.getActionCommand().equals("Beep on")) {
			jToggleBeep.setText("Beep off");
			setBeep("off");	    	
		} else {
			jToggleBeep.setText("Beep on");
			setBeep("on");
		}
		
	}
	
	
	private void setBeep(String mode) {
		FedmConnect con = new FedmConnect();
    	con.setFedmIscReader(fedm);
    	con.setHost(jTextFieldIp.getText());
    	con.fedmOpenConnection();
		
    	if(con.isConnected()) {
			Beep beep = new Beep();
			beep.setFedmIscReader(fedm);
			beep.setNewMode(mode);
			beep.run();
			con.fedmCloseConnection();
    	} else {
    		setMessage("Can not connect", 2000);
    	}
	}
	
	private static void setConfigValues() {
		jTextFieldIp.setText(ReadConfig.getConfig().getString("HOST"));
		jTextFieldSleepTime.setText(Integer.toString(ReadConfig.getConfig().getInt("SLEEPTIME")/1000));
		jTextFieldFileName.setText(ReadConfig.getConfig().getString("RESULT_FILENAME"));
		jTextFieldVID.setText(ReadConfig.getConfig().getString("vID"));
		jTextFieldLID.setText(ReadConfig.getConfig().getString("lID"));
	}
		
    public void onGetReaderSets(boolean b) {
    	if(b) {
			readDataTable = new ReadDataTableThread();
			readDataTable.setFeigGuiListener(this);
			readDataTable.setStartTime(sTime);
			readDataTable.setSort(sort);
			readDataTableRunner = new Thread(readDataTable);
            readDataTableRunner.start();
    	}
    }
    
    public void onGetWriteSets(String newNr, String info) {
    	jTextFieldWriteStnr.setText(newNr);
    	jLabelWriteInfo.setText(info);
    }

    public void onReaderConnect(boolean c) {
    	if(c) {
    		jLabelConnected.setBackground(Color.RED);
    	} else {
    		jLabelConnected.setBackground(Color.LIGHT_GRAY);
    	}
    }

    
    public void onGetReadSets(String[][] tableData) {
    	
    	int[] id = new int[2];
    	id[0] = Integer.parseInt(jTextFieldVID.getText());
    	id[1] = Integer.parseInt(jTextFieldLID.getText());
    	
		setMessage("Refresh...", 500);
        dataTable.setVisible(false);
        dataTableModel.setRowCount(0);
        
		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rd = tableData[i][1];
			String rt = tableData[i][2];
			String sn = tableData[i][3];
			String zs = tableData[i][4];			
			String lt;
			lt = CalculateTime.calcTime(sTime, rt);


			String[] rowData = {Integer.toString(i+1 ) + " ", st + " ", rd + " ", rt.substring(11), lt + "." + zs, " " + sn};
	        dataTableModel.addRow(rowData);
		}

        dataTable.setVisible(true);
        
        //Tabellenanzeige in csv Datei schreiben
		dataTableWriteToCsvThread = new DataTableWriteToCsvThread();
		dataTableWriteToCsvThread.setFileContent(tableData);
		dataTableWriteToCsvThread.setStartTime(sTime);
		dataTableWriteToCsvThread.setFileName(jTextFieldFileName.getText());
		dataTableWriteToCsvThread.setId(id);
		dataTableWriteToCsvThreadRunner = new Thread(dataTableWriteToCsvThread);
		dataTableWriteToCsvThreadRunner.start();
    }
        	
	public void setMessage(String message, int sleeptime) {
		jLabelMessage.setText(message);
		WaitThread wait = new WaitThread();
		wait.setSleepTime(sleeptime);
		wait.start();
	}
	
	public void setProtocoll(String protocoll) {
		textAreaProtocol.append(protocoll);
        textAreaProtocol.setSelectionStart(textAreaProtocol.getText().length());
	}
	
	private void thisWindowClosing(WindowEvent evt) {   
    	System.exit(0);
	}

	class WaitThread extends Thread{
		private int sleeptime;

		public void run(){
			try{Thread.sleep(sleeptime);}catch(Exception e){ e.printStackTrace(); }
			jLabelMessage.setText("");
		}

		public void setSleepTime(int sleeptime) {
			this.sleeptime = sleeptime;
		}
	}

	class UhrzeitThread extends Thread{
		
		public void run(){
			while(true){
				try{Thread.sleep(1000);}catch(Exception e){ e.printStackTrace(); }
				String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());				
				laufZeit = CalculateTime.calcTime(sTime, now);
				jLabelZeit.setText(laufZeit);
		    }
		}
	}

	private String laufZeit;
	private static de.feig.FedmIscReader fedm;
    private DefaultTableModel dataTableModel;
    private BrmReadThread brmReadThread;
    private ReadDataTableThread readDataTable;
    private DataTableWriteToCsvThread dataTableWriteToCsvThread;
    private WriteTag writeTag; 
    private Thread runner;
    private Thread readDataTableRunner;
    private Thread dataTableWriteToCsvThreadRunner;
    private static String mode;
    private int intStNr;
    private String sTime;
    private String sDate;
    private int sort;

}
