import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

class CustomCellRenderer implements TableCellRenderer {
    public final DefaultTableCellRenderer   dtr  = new DefaultTableCellRenderer();
     
    //----+----2----+----3----+----4----+----5----+----6----+----7----+----8   
        public Component getTableCellRendererComponent(
            JTable  table,
            Object  value,
            boolean isSelected,
            boolean hasFocus,
            int     row,
            int     column
        ) {
            Component   renderer    = dtr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
            //Color LIGHTLIGHT = new Color(255, 250, 190);
            Color LIGHTLIGHT = new Color(200, 230, 255);
            Color bg;
             
            if( row % 2  == 0 ){
                bg  = Color.WHITE;
            } else {
                bg  = LIGHTLIGHT;                 
            }
            
            switch (column) {
	            case 0: ((JLabel) renderer).setHorizontalAlignment(JLabel.RIGHT);
	                    break;
	            case 1: ((JLabel) renderer).setHorizontalAlignment(JLabel.RIGHT);
	                    break;
	            case 2: ((JLabel) renderer).setHorizontalAlignment(JLabel.RIGHT);
	                    break;
	            case 3: ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
	                    break;
	            case 4: ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
	            		break;
	            case 6: ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
        				break;
	            case 7: ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
        				break;
	            default:((JLabel) renderer).setHorizontalAlignment(JLabel.LEFT);
	                    break;
            }
            
            renderer.setBackground(bg);
            return(renderer);
        }
    }
