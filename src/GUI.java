import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	boolean sol = false;
	int spacing = 2;
	int size = 80;
	boolean newGame = false,happy=true;
	public int mx,my,x,y;
	Random rand = new Random();
	boolean mines[][] = new boolean[9][16];
	int neighbours[][] = new int[9][16];
	boolean revealed[][] = new boolean[9][16];
	boolean flagged[][] = new boolean[9][16];
	boolean unFlag[][] = new boolean[9][16];
	ArrayList<Integer> X = new ArrayList<Integer>();
	ArrayList<Integer> Y = new ArrayList<Integer>();
	
	public GUI() {
		this.setTitle("Minesweeper");
		this.setSize(1286,829); //29 for top borders, 6 from each side
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setBackground(Color.DARK_GRAY);
		
		for(int i=0;i<9;i++) {
			for(int j=0;j<16;j++) {
				neighbours[i][j]=0;
				if(rand.nextInt(100)<20)
					mines[i][j]=true;
				else
					mines[i][j]=false;
				revealed[i][j] = false;
				flagged[i][j]=false;
				unFlag[i][j]=false;
			}
		}
		
		for(int i=0;i<9;i++) {
			for(int j=0;j<16;j++) {
				for(int k=i-1;k<=i+1;k++) {
					for(int l=j-1;l<=j+1;l++) {
						if(k>=0&&l>=0&&k<9&&l<16&&(k!=i||l!=j)&&mines[k][l]==true)
							neighbours[i][j]++;
					}
				}
			}
		}
		
		Board board = new Board();
		this.setContentPane(board);
		Move move = new Move();
		this.addMouseMotionListener(move);
		Click click = new Click();
		this.addMouseListener(click);
		
	}
	
	public class Board extends JPanel{
		private static final long serialVersionUID = 1L;
		public void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			for(int i=0;i<9;i++) {
				for(int j=0;j<16;j++) {
					g2D.setColor(Color.GRAY);
//					if(mines[i][j])
//						g2D.setColor(Color.YELLOW);
					if(revealed[i][j]) {
						g2D.setColor(Color.WHITE);
						if(mines[i][j]&&i==y&&j==x)
							g2D.setColor(Color.RED);
						if(mines[i][j]&&sol)
							g2D.setColor(Color.RED);
					}
					if(mx >= spacing+j*size+2 && mx <= (j+1)*size-spacing+3 && my >= spacing+(i+1)*size+26 && my <= 26+(i+2)*size-spacing-1 && !newGame && !revealed[i][j] )
						g2D.setColor(Color.LIGHT_GRAY);
					//main squares
					g2D.fill(new Rectangle2D.Double(spacing+j*size, spacing+(i+1)*size, size-2*spacing, size-2*spacing));
					//smiley
					g2D.setColor(Color.YELLOW);
					g2D.fill(new Ellipse2D.Double(660-size/2-10,20+size/2-size/2-10,size-20,size-20)); // center = (640,20+size/2) , radius = size/2 -10
					g2D.setColor(Color.BLACK);
					//g2D.fill(new Ellipse2D.Double(640-3,0+size/2-3,6,6));
					g2D.fill(new Ellipse2D.Double(640-3-10,20+size/2-3-30,6,6));
					g2D.fill(new Ellipse2D.Double(640-3+10,20+size/2-3-30,6,6));
					g2D.setStroke(new BasicStroke(3f));
					if(happy)
						g2D.draw(new Arc2D.Double(640-3+10-20-1.5,20+size/2-3-30+3, 30, 30, -15, -150, Arc2D.OPEN));
					else
						g2D.draw(new Arc2D.Double(640-3+10-20-1.5,20+size/2-3-15+3, 30, 30, 15, 150, Arc2D.OPEN));
					
					if(flagged[i][j]) {
						g2D.setColor(Color.BLACK);
						g2D.fill(new Rectangle2D.Double(4+spacing+j*size+size/2-6, spacing+(i+1)*size+10, 6, size-20));
						g2D.fill(new Rectangle2D.Double(4+spacing+j*size+20-3, spacing+(i+1)*size-25+size, size-40, 15));
						g2D.fill(new Rectangle2D.Double(spacing+j*size+20-6, spacing+(i+1)*size-30+size/2, size-50, 20));
						g2D.setColor(Color.RED);
						g2D.fill(new Rectangle2D.Double(3+spacing+j*size+20-6, 3+spacing+(i+1)*size-30+size/2, size-50-6-3, 20-6));
					}
					if(revealed[i][j]) {
						if(!mines[i][j] && neighbours[i][j]!=0) {
							if(neighbours[i][j]==1)
								g2D.setColor(Color.BLUE);
							else if(neighbours[i][j]==2)
								g2D.setColor(Color.GREEN);
							else if(neighbours[i][j]==3)
								g2D.setColor(Color.RED);
							else if(neighbours[i][j]==4)
								g2D.setColor(new Color(0,0,128));
							else if(neighbours[i][j]==5)
								g2D.setColor(new Color(178,34,34));
							else if(neighbours[i][j]==6)
								g2D.setColor(new Color(72,209,204));
							else if(neighbours[i][j]==7)
								g2D.setColor(Color.BLACK);
							else
								g2D.setColor(Color.DARK_GRAY);
							g2D.setFont(new Font("TimesRoman", Font.BOLD, 40));
							g2D.drawString(Integer.toString(neighbours[i][j]),spacing+j*size+size/2-15, spacing+(i+1)*size+size/2+10);
						}
						else if(mines[i][j]){
							g2D.setColor(Color.BLACK);
							g2D.fill(new Ellipse2D.Double(spacing+j*size+size/4, spacing+(i+1)*size+size/4, size/2, size/2));
							g2D.setStroke(new BasicStroke(6f));
							g2D.draw(new Line2D.Double(j*size+spacing+size/2-20, (i+1)*size+spacing+size/2-20, j*size+spacing+size/2+20, (i+1)*size+spacing+size/2+20));
							g2D.draw(new Line2D.Double(j*size+spacing+size/2+20, (i+1)*size+spacing+size/2-20, j*size+spacing+size/2-20, (i+1)*size+spacing+size/2+20));
							g2D.fill(new Rectangle2D.Double(spacing+j*size+size/2-3, spacing+(i+1)*size+10, 6, size-20));
							g2D.fill(new Rectangle2D.Double(spacing+j*size+size/2-30, spacing+(i+1)*size+size/2-3,size-20,6));
						}
					}
				}
			}
		}
	}
	
	public class Move implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}
	}
	
	public class Click implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
//			if(inBoxX()!=-1 && inBoxY()!=-1)
//				System.out.println("In Box(i,j): (" + inBoxY() + ", " + inBoxX() + ") value = "+ neighbours[inBoxY()][inBoxX()]);
//			else
//				System.out.println("Not in any Box");
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if(inSmiley())
				reset();
			if(!newGame) {
				if(inBoxY()!=-1&&inBoxX()!=-1&&e.getButton()== MouseEvent.BUTTON3&&!revealed[inBoxY()][inBoxX()]) {
					if(!unFlag[inBoxY()][inBoxX()]) {
						flagged[inBoxY()][inBoxX()] = true;
						unFlag[inBoxY()][inBoxX()] = true;
					}
					else {
						flagged[inBoxY()][inBoxX()] = false;
						unFlag[inBoxY()][inBoxX()] = false;
					}
				}
				if(e.getButton()== MouseEvent.BUTTON2) {
					for(int i=0;i<9;i++) {
						for(int j=0;j<16;j++) {
							revealed[i][j] = true;
							if(mines[i][j]&&flagged[i][j])
								flagged[i][j] = false;
						}
					}
					sol = true;
					newGame=true;
				}
				if(inBoxY()!=-1&&inBoxX()!=-1&&e.getButton()== MouseEvent.BUTTON1&&!flagged[inBoxY()][inBoxX()]) {
					revealed[inBoxY()][inBoxX()] = true;
					if(neighbours[inBoxY()][inBoxX()]==0&&!mines[inBoxY()][inBoxX()]) { // brute force neighbor reveal
						X.add(inBoxX());
						Y.add(inBoxY());
						while(!X.isEmpty()) {
							revealed[Y.get(0)][X.get(0)] = true;
							// y
							for(int i=1;Y.get(0)+i<9;i++) {
								
								if(neighbours[Y.get(0)+i][X.get(0)]==0&&!revealed[Y.get(0)+i][X.get(0)]) {
									X.add(X.get(0));
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)] = true;
									break;
								}
							}
							// -y
							for(int i=-1;Y.get(0)+i>=0;i--) {
								
								if(neighbours[Y.get(0)+i][X.get(0)]==0&&!revealed[Y.get(0)+i][X.get(0)]) {
									X.add(X.get(0));
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)] = true;
									break;
								}
							}
							// x
							for(int i=1;X.get(0)+i<16;i++) {
								
								if(neighbours[Y.get(0)][X.get(0)+i]==0&&!revealed[Y.get(0)][X.get(0)+i]) {
									X.add(X.get(0)+i);
									Y.add(Y.get(0));
								}
								else {
									revealed[Y.get(0)][X.get(0)+i] = true;
									break;
								}
							}
							// -x
							for(int i=-1;X.get(0)+i>=0;i--) {
								
								if(neighbours[Y.get(0)][X.get(0)+i]==0&&!revealed[Y.get(0)][X.get(0)+i]) {
									X.add(X.get(0)+i);
									Y.add(Y.get(0));
								}
								else {
									revealed[Y.get(0)][X.get(0)+i] = true;
									break;
								}
							}
							// x y
							for(int i=1;Y.get(0)+i<9&&X.get(0)+i<16;i++) {
								
								if(neighbours[Y.get(0)+i][X.get(0)+i]==0&&!revealed[Y.get(0)+i][X.get(0)+i]) {
									X.add(X.get(0)+i);
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)+i] = true;
									break;
								}
							}
							// -x y
							for(int i=-1,j=1;Y.get(0)+i>=0&&X.get(0)+j<16;i--,j++) {
								
								if(neighbours[Y.get(0)+i][X.get(0)+j]==0&&!revealed[Y.get(0)+i][X.get(0)+j]) {
									X.add(X.get(0)+j);
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)+j] = true;
									break;
								}
							}
							// x -y
							for(int i=1,j=-1;Y.get(0)+i<9&&X.get(0)+j>=02;i++,j--) {
								
								if(neighbours[Y.get(0)+i][X.get(0)+j]==0&&!revealed[Y.get(0)+i][X.get(0)+j]) {
									X.add(X.get(0)+j);
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)+j] = true;
									break;
								}
							}
							// -x -y
							for(int i=-1;Y.get(0)+i>=0&&X.get(0)+i>=0;i--) {
								
								if(neighbours[Y.get(0)+i][X.get(0)+i]==0&&!revealed[Y.get(0)+i][X.get(0)+i]) {
									X.add(X.get(0)+i);
									Y.add(Y.get(0)+i);
								}
								else {
									revealed[Y.get(0)+i][X.get(0)+i] = true;
									break;
								}
							}
							
							X.remove(0);
							Y.remove(0);
							//X.clear();
							//Y.clear();
						}
					}
					if(inBoxY()!=-1&&inBoxX()!=-1&&mines[inBoxY()][inBoxX()]) {
						x = inBoxX();
						y = inBoxY();
						for(int i=0;i<9;i++) {
							for(int j=0;j<16;j++) {
								if(mines[i][j])
									revealed[i][j] = true;
							}
						}
						newGame=true;
						happy = false;
					}
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		} 
		
		public int inBoxX() {
			for(int i=0;i<9;i++) {
				for(int j=0;j<16;j++) {
					if(mx >= spacing+j*size+2 && mx <= (j+1)*size-spacing+3 && my >= spacing+(i+1)*size+26 && my <= 26+(i+2)*size-spacing-1)
						return j;
				}
			}
			return -1;
		}
		
		public int inBoxY() {
			for(int i=0;i<9;i++) {
				for(int j=0;j<16;j++) {
					if(mx >= spacing+j*size+2 && mx <= (j+1)*size-spacing+3 && my >= spacing+(i+1)*size+26 && my <= 26+(i+2)*size-spacing-1)
						return i;
				}
			}
			return -1;
		}
		
		public boolean inSmiley() {
			if(Math.sqrt(Math.pow(Math.abs(mx-3-640),2)+Math.pow(Math.abs(my-26-40),2))<=30)
				return true;
			else
				return false;
		}
		
		public void reset() {
			newGame = false;
			sol = false;
			happy = true;
			for(int i=0;i<9;i++) {
				for(int j=0;j<16;j++) {
					neighbours[i][j]=0;
					if(rand.nextInt(100)<20)
						mines[i][j]=true;
					else
						mines[i][j]=false;
					revealed[i][j] = false;
					flagged[i][j]=false;
					unFlag[i][j]=false;
				}
			}
			
			for(int i=0;i<9;i++) {
				for(int j=0;j<16;j++) {
					for(int k=i-1;k<=i+1;k++) {
						for(int l=j-1;l<=j+1;l++) {
							if(k>=0&&l>=0&&k<9&&l<16&&(k!=i||l!=j)&&mines[k][l]==true)
								neighbours[i][j]++;
						}
					}
				}
			}
		}
	}
}
