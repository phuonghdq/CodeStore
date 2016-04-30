package machine;

import java.awt.Graphics2D;

public interface Action<T> {
	public void execute(T arg);
}
