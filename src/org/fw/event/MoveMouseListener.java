package org.fw.event;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

//import org.apache.log4j.Logger;
//import org.fw.FramePanel;
/**
 * 拖动组件事件
 * @author Administrator
 *
 */
public class MoveMouseListener implements MouseListener, MouseMotionListener {

//	private static Logger log = Logger.getLogger(FramePanel.class);
	
	private int RANGE = 4;//边缘检测的误差范围
	private int MIN_WIDTH = 256;//最小宽度
	private int MIN_HEIGHT = 256;//最小高度
	
	JComponent target;// 操作的目标组件
	Point start_drag;// 开始操作目标组件是的鼠标起始位置
	Point now_loc;//目标组件的现在位置
	
	int width;//窗口宽度
	int height;//窗口高度
	int x_left;//最左边x坐标值
	int x_right;//最右边x坐标值
	int y_up;//最上边y坐标值
	int y_down;//最下边y坐标值
	int x;//鼠标x坐标
	int y;//鼠标y坐标
	
	Rectangle nw,ne,sw,se;//四个角的正方形区域
	
	boolean canResize;//可以改变大小
	boolean canMove;//可以移动
	boolean Resizing;//处于改变大小的状况
	Boolean Moving;//
	

	public MoveMouseListener(JComponent target) {
		this.target = target;
		canResize = true;
		canMove = true;
		Resizing = false;
		Moving = true;
	}

	public static JFrame getFrame(Container target) {
		if (target instanceof JFrame) {
			return (JFrame) target;
		}
		return getFrame(target.getParent());
	}
	
	/**
	 * 获取当前状态下鼠标在屏幕上的准确位置
	 * @param e
	 * @return 鼠标的坐标
	 */
	Point getScreenLocation(MouseEvent e) {
		Point cursor = e.getPoint();
		Point target_location = this.target.getLocationOnScreen();
		return new Point((int) (target_location.getX() + cursor.getX()),
				(int) (target_location.getY() + cursor.getY()));
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		
		setParameterValues(e);
		
		setCurrentCursorType();
		
	}
	
	@SuppressWarnings("static-access")
	private void setCurrentCursorType() {
		JFrame frame = this.getFrame(target);
		Cursor cursorType = frame.getCursor();
		
		if(this.isCanResize()){
			
			if(nw.contains(x, y)){//左上角
				frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			}else if(this.x_left != x && this.x_right != x && isBetween(y,this.y_up,RANGE)){//上
				frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			}else if(ne.contains(x, y)){//右上角
				frame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
			}else if(isBetween(x,this.x_right,RANGE) && this.y_up != y && this.y_down != y){//右
				frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			}else if(se.contains(x, y)){//右下角
				frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			}else if(this.x_left != x && this.x_right != x && isBetween(y,this.y_down,RANGE)){//下
				frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			}else if(sw.contains(x, y)){//左下角
				frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
			}else if(isBetween(x,this.x_left,RANGE) && this.y_up != y && this.y_down != y){//左
				frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		if(Resizing){
			frame.setCursor(cursorType);
		}
	}

	@SuppressWarnings("static-access")
	private void setParameterValues(MouseEvent e) {
		//获取鼠标位置
		this.start_drag = this.getScreenLocation(e);
		//获取窗口位置
		this.now_loc = this.getFrame(this.target).getLocation();
		
		if(this.isCanResize()){
			this.width = this.getFrame(target).getWidth();
			this.height = this.getFrame(target).getHeight();
			this.x_left = (int)now_loc.getX();
			this.x_right = x_left+width;
			this.y_up = (int)now_loc.getY();
			this.y_down = y_up+height;
			nw = new Rectangle(now_loc.x,now_loc.y,RANGE*2,RANGE*2);
			ne = new Rectangle(now_loc.x+width-RANGE*2,now_loc.y,RANGE*2,RANGE*2);
			sw = new Rectangle(now_loc.x,now_loc.y+height-RANGE*2,RANGE*2,RANGE*2);
			se = new Rectangle(now_loc.x+width-RANGE*2,now_loc.y+height-RANGE*2,RANGE*2,RANGE*2);
			x = (int)this.getScreenLocation(e).getX();
			y = (int)this.getScreenLocation(e).getY();
		}
	}

	/**
	 * x是否处于y的左右范围内
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isBetween(int x, int y,int range) {
		if(x>y-range&&x<y+range){
			return true;
		}else{
			return false;
		}
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		setParameterValues(e);
	}

	public void mouseReleased(MouseEvent e) {
		Resizing = false;
		Moving = true;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	public void mouseDragged(MouseEvent e) {
		Point current = this.getScreenLocation(e);
		Point offset = new Point(
				(int) current.getX() - (int) start_drag.getX(), (int) current
						.getY()
						- (int) start_drag.getY());
		JFrame frame = this.getFrame(target);
		//判断当前状态
		if(this.getFrame(target).getCursor().getType() == Cursor.DEFAULT_CURSOR){
			Moving = true;
			Resizing = false;
		}else{
			Moving = false;
			Resizing = true;
		}
		
		//如果可以移动
		if(this.isCanMove() && Moving && !Resizing ){
			Point new_location = new Point((int) (this.now_loc.getX() + offset
					.getX()), (int) (this.now_loc.getY() + offset.getY()));
			frame.setLocation(new_location);
		}
		//如果可以改变大小
		if(this.isCanResize() && !Moving && Resizing){
			JFrame jFrame = this.getFrame(target);
		
			if(jFrame.getCursorType() == Cursor.NW_RESIZE_CURSOR){
				int px = now_loc.x + width;
				int py = now_loc.y + height;
				jFrame.setSize(checkMin(px-e.getXOnScreen(),MIN_WIDTH),checkMin(py-e.getYOnScreen(),MIN_HEIGHT));
				jFrame.setLocation(e.getXOnScreen(),e.getYOnScreen());
			}else if(jFrame.getCursorType() == Cursor.N_RESIZE_CURSOR){
				int py = now_loc.y+height;
				jFrame.setSize(checkMin(this.width,MIN_WIDTH),checkMin(py-e.getYOnScreen(),MIN_HEIGHT));
				jFrame.setLocation(this.now_loc.x,e.getYOnScreen());
			}else if(jFrame.getCursorType() == Cursor.NE_RESIZE_CURSOR){
				int py = now_loc.y+height;
				this.width = e.getXOnScreen() - now_loc.x;
				jFrame.setSize(checkMin(this.width,MIN_WIDTH),checkMin(py-e.getYOnScreen(),MIN_HEIGHT));
				jFrame.setLocation(this.now_loc.x,e.getYOnScreen());
			}else if(jFrame.getCursorType() == Cursor.E_RESIZE_CURSOR){
				this.width = e.getXOnScreen() - now_loc.x;
				jFrame.setSize(checkMin(this.width,MIN_WIDTH),checkMin(this.height,MIN_HEIGHT));
			}else if(jFrame.getCursorType() == Cursor.SE_RESIZE_CURSOR){
				this.width = e.getXOnScreen()- now_loc.x;
				this.height =e.getYOnScreen()- now_loc.y;
				jFrame.setSize(checkMin(this.width,MIN_WIDTH),checkMin(this.height,MIN_HEIGHT));
			}else if(jFrame.getCursorType() == Cursor.S_RESIZE_CURSOR){
				this.height = e.getYOnScreen() - now_loc.y;
				jFrame.setSize(checkMin(this.width,MIN_WIDTH),checkMin(this.height,MIN_HEIGHT));
			}else if(jFrame.getCursorType() == Cursor.SW_RESIZE_CURSOR){
				int px = now_loc.x + width;
				this.height = e.getYOnScreen() - now_loc.y;
				jFrame.setSize(checkMin(px-e.getXOnScreen(),MIN_WIDTH),checkMin(this.height,MIN_HEIGHT));
				jFrame.setLocation(e.getXOnScreen(),this.now_loc.y);
			}else if(jFrame.getCursorType() == Cursor.W_RESIZE_CURSOR){
				int px = now_loc.x + width;
				jFrame.setSize(checkMin(px-e.getXOnScreen(),MIN_WIDTH),checkMin(this.height,MIN_HEIGHT));
				jFrame.setLocation(e.getXOnScreen(),this.now_loc.y);
			}
			
		}
	}

	/**
	 * 保证值大于最小值
	 * @param value
	 * @param min
	 * @return
	 */
	private int checkMin(int value, int min) {
		if(value<min){
			return min;
		}else{
			return value;
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(Moving){
			setParameterValues(e);
		}
		setCurrentCursorType();
	}

	public boolean isCanResize() {
		return canResize;
	}

	public void setCanResize(boolean canResize) {
		this.canResize = canResize;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
}
