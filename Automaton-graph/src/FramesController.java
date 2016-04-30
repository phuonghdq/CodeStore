import javax.swing.JFrame;

public interface FramesController {

	public void quit();

	public JFrame createFrame();

	public void deleteFrame(JFrame frame);
}
