import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
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
		
		JFrame frame = new JFrame("Stack Visualization Program");
		JTextField field = new JTextField("Enter Your Program Name Here");
		JTextArea area = new JTextArea(10, 50);
		JButton button = new JButton("Visualize Stack");
		
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		
		button.setSize(new Dimension (50, 50));
		button.setAlignmentX(0);
		button.setAlignmentY(0);
		button.setVisible(true);
		button.setBounds(65, 30, 375, 85);
		
		frame.getContentPane().add(area);
		frame.getContentPane().add(button);
		
		field.addMouseListener(new MouseListener(field));
		open.addActionListener(new FileOpener(frame, open, field));
		button.addActionListener(new RunProgram(field));
		
        field.setBounds(frame.getHeight(), frame.getWidth(), frame.getWidth(), 150);
		frame.add(area, BorderLayout.CENTER);
		frame.add(field,BorderLayout.SOUTH);

		frame.setBounds(0, 30, 400, 175);

		fileMenu.add(open);
		frame.setJMenuBar(bar);
		bar.add(fileMenu);

		frame.pack();
		frame.setVisible(true);

		
	}
	

}
