import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

public class FileOpener implements ActionListener{

	String filename = "", directory="";
	JFrame app;
	String file;
	public JTextField text;
	
	public FileOpener(JFrame f, JMenuItem mi, JTextField field) {
		this.app = f;
		this.text = field;
		mi.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
				//Pop up a file dialog
				JFileChooser fc = new JFileChooser();
				int rVal = fc.showOpenDialog(app);
				
				if(rVal == JFileChooser.APPROVE_OPTION){
					filename = fc.getSelectedFile().getName();
					directory = fc.getCurrentDirectory().toString();
					file = directory + "/" + filename;
					text.setText(file);
				}
			
		
	}

}
