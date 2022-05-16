package shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import shapes.TAnchors.EAnchors;

abstract public class TShape implements Serializable {
	// attributes
	private static final long serialVersionUID = 1L;
	// components
	protected Shape shape;
	private TAnchors anchors;
	// working
	private int px, py;
	private AffineTransform affineTransform;
	
	private boolean bSelected;
	
	// setters and getters
	public EAnchors getSelectedAnchor() {
		return this.anchors.getESelectedAnchor();
	}
	public void setSelectedAnchor(EAnchors eSelectedAnchor) {
		this.anchors.setESelectedAnchor(eSelectedAnchor);
	}
	public Point getResizeAnchorPosition() {
		return this.anchors.getResizerAnchorPosition();
	}
	public boolean isSelected() {
		return bSelected;
	}
	public void setSelected(boolean bSelected) {
		this.bSelected = bSelected;
	}
	public Rectangle getBounds() {
		return this.shape.getBounds();
	}

	// constructors
	public TShape() {
		this.bSelected = false;
		this.affineTransform = new AffineTransform();
		this.affineTransform.setToIdentity();
		
		this.anchors = new TAnchors();
	}
	public abstract TShape clone();	
	
	// methods
	public abstract void prepareDrawing(int x, int y);
	public abstract void keepDrawing(int x, int y);
	public void addPoint(int x, int y) {}

	public boolean contains(int x, int y) {
		if (this.bSelected) {
			if (this.anchors.contains(x, y)) {
				return true;
			}
		}
		
		Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
		if(transformedShape.contains(x, y)) {
			this.anchors.setESelectedAnchor(EAnchors.eMove);
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D graphics2D) {
		Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
		graphics2D.draw(transformedShape);
		if (this.bSelected) {
			this.anchors.draw(graphics2D, transformedShape.getBounds());			
		}
	}
	public void prepareMoving(int x, int y) {
		this.px = x;
		this.py = y;		
	}
	public void keepMoving(int x, int y) {
		this.affineTransform.translate(x-this.px, y-this.py);
		this.px = x;
		this.py = y;			
	}
	
	public void resize(int cx, int cy, double xFactor, double yFactor) {
		this.affineTransform.translate(cx, cy);
		this.affineTransform.scale(xFactor, yFactor);
		this.affineTransform.translate(-cx, -cy);
	}
}

