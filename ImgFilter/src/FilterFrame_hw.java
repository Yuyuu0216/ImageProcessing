
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FilterFrame_hw extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelLP = new JPanel();
	JPanel cotrolPanelHP = new JPanel();
	ImagePanel imagePanel;
	ImagePanel imagePanel2;
	JButton btnShow;
	JButton btnLP = new JButton("Low-Pass(Blur)");
	JButton btnHP = new JButton("High-Pass(Sharp)");
	int[][][] data;
	int[][][] newData;
	int height;
	int width;
	BufferedImage img = null;
	BufferedImage imgNew = null;

	FilterFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HW 5: Image Filters 2023/04/06");
		try {
			img = ImageIO.read(new File("Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		btnShow = new JButton("Show");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		cotrolPanelShow.add(btnShow);
		cotrolPanelShow.add(btnLP);
		cotrolPanelShow.add(btnHP);
		cotrolPanelMain.add(cotrolPanelShow);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(20, 220, 700, 700);
		getContentPane().add(imagePanel);
		imagePanel2 = new ImagePanel();
		imagePanel2.setBounds(650, 220, 700, 700);
		getContentPane().add(imagePanel2);
		
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		newData = new int[height][width][3];
		for(int y = 0 ; y < height ; y++) {
			for(int x = 0 ; x < width ; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
				
			}
		}

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = imagePanel.getGraphics();  
				g.drawImage(img, 0, 0, null);  
				//ok

			}
		});

		btnLP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imgNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				for(int y = 0 ; y < height -1  ; y++) {
					for(int x = 0 ; x < width -1  ; x++) {
						if(x <= 0 || x >= width || y <= 0 || y >= height) {
							imgNew.setRGB(x, y, img.getRGB(x, y));
						}
						else{
							newData[y][x][0]= (data[y - 1][x - 1][0] + data[y - 1][x][0] + data[y - 1][x + 1][0] +
									data[y][x - 1][0] + data[y][x][0] + data[y][x + 1][0] +
									data[y + 1][x - 1][0] + data[y + 1][x][0] + data[y + 1][x + 1][0]) / 9;
							newData[y][x][1]= (data[y - 1][x - 1][1] + data[y - 1][x][1] + data[y - 1][x + 1][1] +
									data[y][x - 1][1] + data[y][x][1] + data[y][x + 1][1] +
									data[y + 1][x - 1][1] + data[y + 1][x][1] + data[y + 1][x + 1][1]) / 9;
							newData[y][x][2]= (data[y - 1][x - 1][2] + data[y - 1][x][2] + data[y - 1][x + 1][2] +
									data[y][x - 1][2] + data[y][x][2] + data[y][x + 1][2] +
									data[y + 1][x - 1][2] + data[y + 1][x][2] + data[y + 1][x + 1][2]) / 9;
							int newrgb = Util.makeColor(newData[y][x][0], newData[y][x][1], newData[y][x][2]);
							imgNew.setRGB(x, y, newrgb);
						}
					}
				}
				//put your code here 
				Graphics g = imagePanel2.getGraphics();  
				g.drawImage(imgNew, 0, 0, null);
			}
		});
		
		btnHP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imgNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				
				for(int y = 0 ; y < height -1  ; y++) {
					for(int x = 0 ; x < width -1  ; x++) {
						if(x <= 0 || x >= width || y <= 0 || y >= height) {
							imgNew.setRGB(x, y, img.getRGB(x, y));
						}
						else{
							newData[y][x][0]= (-data[y - 1][x - 1][0] - data[y - 1][x][0] - data[y - 1][x + 1][0] -
									data[y][x - 1][0] + (data[y][x][0]*8) - data[y][x + 1][0] -
									data[y + 1][x - 1][0] - data[y + 1][x][0] - data[y + 1][x + 1][0]) / 9 + data[y][x][0];
							newData[y][x][1]= (-data[y - 1][x - 1][1] - data[y - 1][x][1] - data[y - 1][x + 1][1] -
									data[y][x - 1][1] + (data[y][x][1]*8) - data[y][x + 1][1] -
									data[y + 1][x - 1][1] - data[y + 1][x][1] - data[y + 1][x + 1][1]) / 9 + data[y][x][1];
							newData[y][x][2]= (-data[y - 1][x - 1][2] - data[y - 1][x][2] - data[y - 1][x + 1][2] -
									data[y][x - 1][2] + (data[y][x][2]*8) - data[y][x + 1][2] -
									data[y + 1][x - 1][2] - data[y + 1][x][2] - data[y + 1][x + 1][2]) / 9 + data[y][x][2];
							int newrgb = Util.makeColor(Util.checkPixelBounds(newData[y][x][0]), Util.checkPixelBounds(newData[y][x][1]), Util.checkPixelBounds(newData[y][x][2]));
							imgNew.setRGB(x, y, newrgb);
						}
					}
				}
				//put your code here 
				Graphics g = imagePanel2.getGraphics();  
				g.drawImage(imgNew, 0, 0, null);
			}
				//put your code here
				
		});
	}// end of the constructor

	public static void main(String[] args) {
		FilterFrame_hw frame = new FilterFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
