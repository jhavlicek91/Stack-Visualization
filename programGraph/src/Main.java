import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws ClassNotLoadedException 
	 * @throws IncompatibleThreadStateException 
	 */
	
	public static void main(String[] args)  {
		
		//Intialize parts of the interface
		JFrame frame = new JFrame("Stack Visualization Program");
		JTextField field = new JTextField("Enter Your Program Name Here");
		JButton button = new JButton("Visualize Stack");
		frame.setBounds(0, 30, 400, 175);
		
		//Add menu bar
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		
		//Set button defaults
		button.setSize(new Dimension (100, 100));
		button.setAlignmentX(0);
		button.setAlignmentY(0);
		button.setVisible(true);
		button.setBounds(65, 30, 375, 85);
		
		frame.getContentPane().add(button);
		
		//Add listeners to the text field, button, and menubar
		field.addMouseListener(new MouseListener(field));
		open.addActionListener(new FileOpener(frame, open, field));
		button.addActionListener(new RunProgram(field));
		
        field.setBounds(frame.getHeight(), frame.getWidth(), frame.getWidth(), 150);
		frame.add(field,BorderLayout.SOUTH);

		//Add menu to the frame
		fileMenu.add(open);
		frame.setJMenuBar(bar);
		bar.add(fileMenu);

		frame.setVisible(true);

		
	}
	

}
