import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AffineFrame_hw extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelBackColor = new JPanel();;
	JPanel cotrolPanelTranslate = new JPanel();;
	JPanel cotrolPanelScale = new JPanel();
	JPanel cotrolPanelRotate = new JPanel();
	JPanel cotrolPanelShear = new JPanel();;
	ImagePanel imagePanel;
	JButton btnShow;
	JButton btnTranslate;
	JButton btnScale;
	BufferedImage img;
	BufferedImage newImg;
	JTextField tfDeltaX = new JTextField(5);
	JTextField tfDeltaY = new JTextField(5);
	JTextField tfAmpX = new JTextField(5);
	JTextField tfAmpY = new JTextField(5);
	JLabel lbDeltaY = new JLabel("y軸位移");
	JLabel lbDeltaX = new JLabel("x軸位移");
	JLabel lbAmpX = new JLabel("x軸倍率");
	JLabel lbAmpY = new JLabel("y軸倍率");
	final int[][][] data;
	int height;
	int width;

	AffineFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		tfDeltaX.setText("0");
		tfDeltaY.setText("0");
		tfAmpX.setText("1.0");
		tfAmpY.setText("1.0");
		setTitle("Affine Transform Homework");
		try {
			 img = ImageIO.read(new File("plate.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		for(int y = 0 ; y < height ; y++) {
			for(int x = 0 ; x < width ; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		
		btnShow = new JButton("顯示");
		btnTranslate = new JButton("平移");
		btnScale = new JButton("放大/縮小");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(1, 7));
		cotrolPanelShow.add(btnShow);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelBackColor);
		cotrolPanelTranslate.add(lbDeltaX);
		cotrolPanelTranslate.add(tfDeltaX);
		cotrolPanelTranslate.add(lbDeltaY);
		cotrolPanelTranslate.add(tfDeltaY);
		cotrolPanelTranslate.add(btnTranslate);
		cotrolPanelMain.add(cotrolPanelTranslate);
		cotrolPanelScale.add(lbAmpX);
		cotrolPanelScale.add(tfAmpX);
		cotrolPanelScale.add(lbAmpY);
		cotrolPanelScale.add(tfAmpY);
		cotrolPanelScale.add(btnScale);
		cotrolPanelMain.add(cotrolPanelScale);
		cotrolPanelMain.add(cotrolPanelRotate);
		cotrolPanelMain.add(cotrolPanelShear);
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.setBounds(0, 0, 1200, 150);
		getContentPane().add(cotrolPanelMain);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(0, 180, 1500, 1500);
		getContentPane().add(imagePanel);
		
		
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					img = ImageIO.read(new File("plate.png"));
				} catch (IOException e) {
					System.out.println("IO exception");
				}
				
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img,  0, 0, null);
			}
		});
		
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			Graphics g = imagePanel.getGraphics();
			int deltaX = Integer.parseInt(tfDeltaX.getText());
			int deltaY = Integer.parseInt(tfDeltaY.getText());
			imagePanel.paintComponent(g);
			g.drawImage(img,  deltaX, deltaY, null);
			}
		});
		
		btnScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double ax = Double.parseDouble(tfAmpX.getText());
				double ay = Double.parseDouble(tfAmpY.getText());
				int newWidth = (int)(ax * data[0].length);
				int newHeight = (int)(ay * data.length);
				newImg = new BufferedImage(newWidth, newHeight,BufferedImage.TYPE_INT_ARGB);
				double[][] matrix = {{ 1 / ax, 0.0, 0.0 },
									 { 0.0, 1 / ay, 0.0 },
									 { 0.0, 0.0, 1}};
				int[][][] newData = new int[newHeight][newWidth][3];
				for(int y = 0 ; y < newData.length ; y++) {
					for(int x = 0 ; x < newData[0].length ;x ++) {
						double[] position1 = { x, y, 1.0};
						double[] position2 = Util.affine(matrix, position1);
						int rgb = img.getRGB((int)(position2[0]), (int)(position2[1]));
						newImg.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(newImg, 0, 0, null);
			}
				});
	}

	
	public static void main(String[] args) {
		AffineFrame_hw frame = new AffineFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
