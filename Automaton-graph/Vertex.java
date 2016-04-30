import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

public class Vertex {
	RectangularShape shape;
	String label;
	boolean indInitial;
	boolean indFinal;
	Color color;

	Vertex(RectangularShape rs, String label) {
		this.shape = rs;
		this.label = label;
	}

	Vertex(RectangularShape rs, String label, boolean init, boolean fin, Color cl) {
		this.shape = rs;
		this.label = label;
		this.indInitial = init;
		this.indFinal = fin;
		this.color = cl;
	}

	public RectangularShape getShape() {
		return shape;
	}

	public void setShape(RectangularShape shape) {
		this.shape = shape;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean contains(int x, int y) {
		return shape.contains(x, y);
	}

	void draw(Graphics2D g2) {
		g2.draw(shape);
		if (label != null)
			g2.drawString(label, (int) (shape.getCenterX()), (int) (shape.getCenterY()));
	}
}
