import java.awt.event.MouseEvent;
import javax.swing.JTextField;


public class MouseListener implements java.awt.event.MouseListener {

	private JTextField text;

	public MouseListener(JTextField field) {
		this.text =field;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		text.setText("");

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
