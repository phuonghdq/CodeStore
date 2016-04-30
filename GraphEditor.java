import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class GraphEditor implements FramesController {

	public final static String MENU_FILE = "File";
	public final static String MENU_ITEM_NEW = "New";
	public final static String MENU_ITEM_CLOSE = "Close";
	public final static String MENU_ITEM_QUIT = "Quit";

	public final static String DIALOG_QUIT_MSG = "Do you really want to quit ?";
	public final static String DIALOG_QUIT_TITLE = "Quit ?";

	public final static String TITLE = "Graph";

	private static final List<JFrame> frames = new ArrayList<JFrame>();

	public void quit() {
		int answer = JOptionPane.showConfirmDialog(null, DIALOG_QUIT_MSG, DIALOG_QUIT_TITLE, JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public JFrame createFrame() {
		JFrame frame = new GraphFrame(this);
		frame.setTitle(TITLE);
		int pos = 30 * (frames.size() % 5);
		frame.setLocation(pos, pos);
		frame.setPreferredSize(new Dimension(600, 600));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frames.add(frame);
		return frame;
	}

	public void deleteFrame(JFrame frame) {
		if (frames.size() > 1) {
			frames.remove(frame);
			frame.dispose();
		} else {
			quit();
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GraphEditor().createFrame();
			}
		});
	}
}