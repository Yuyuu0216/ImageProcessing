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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSLFrame  extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelHue = new JPanel();
	JPanel cotrolPanelSat = new JPanel();
	JPanel cotrolPanelLum = new JPanel();
	JPanel cotrolPanelBin = new JPanel();
	JPanel cotrolPanelHSL = new JPanel();
	JPanel imagePanelOrg;   
	JPanel imagePanelHSL;   
	JButton btnShow;
	JSlider sliderHue;
	JSlider sliderSat;
	JSlider sliderLum;
	JLabel lbHue = new JLabel("Hue");
	JLabel lbSat = new JLabel("Saturation");
	JLabel lbLum = new JLabel("Luminance");
	JLabel lbHueBeging = new JLabel("-180");
	JLabel lbHueEnd = new JLabel("180");
	JLabel lbSatBeging = new JLabel("-100(%)") ;
	JLabel lbSatEnd =  new JLabel("100(%)") ;;
	JLabel lbLumBeging  = new JLabel("-100(%)") ;;
	JLabel lbLumEnd  = new JLabel("100(%)") ;;
	JTextField tfHueValue = new JTextField(3);
	JTextField tfSatValue = new JTextField(3);
	JTextField tfLumValue = new JTextField(3);
	int[][][] data;
	int height;
	int width;
	double Hue;
	double Sat;
	double Light;
	static BufferedImage imgOrg = null;  
	static BufferedImage imgHSL = null; 
	
	HSLFrame() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HW4: HSL Coversion");
		btnShow = new JButton("Show Original Image");
		tfHueValue.setText("0");
		tfSatValue.setText("0");
		tfLumValue.setText("0");
		tfHueValue.setEditable(false);
		tfSatValue.setEditable(false);
		tfLumValue.setEditable(false);
		
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6, 1));
		sliderHue = new JSlider(-180, 180, 0);
		cotrolPanelShow.add(btnShow);
		cotrolPanelHue.add(lbHueBeging);
		cotrolPanelHue.add(sliderHue);
		cotrolPanelHue.add(lbHueEnd);
		cotrolPanelHue.add(tfHueValue);
		cotrolPanelHue.add(lbHue);
		
		sliderSat = new JSlider(-100, 100, 0);
		cotrolPanelSat.add(lbSatBeging);
		cotrolPanelSat.add(sliderSat);
		cotrolPanelSat.add(lbSatEnd);
		cotrolPanelSat.add(tfSatValue);
		cotrolPanelSat.add(lbSat);
		
		sliderLum = new JSlider(-100, 100, 0);
		cotrolPanelLum.add(lbLumBeging);
		cotrolPanelLum.add(sliderLum);
		cotrolPanelLum.add(lbLumEnd);
		cotrolPanelLum.add(tfLumValue);
		cotrolPanelLum.add(lbLum);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelHue);
		cotrolPanelMain.add(cotrolPanelSat);
		cotrolPanelMain.add(cotrolPanelLum);
		cotrolPanelMain.add(cotrolPanelHSL);
		cotrolPanelMain.add(cotrolPanelBin);
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		imagePanelOrg = new ImagePanel();   
		imagePanelOrg.setBounds(0, 220, 700, 700);  
		getContentPane().add(imagePanelOrg);  
		imagePanelHSL = new ImagePanel();  
		imagePanelHSL.setBounds(750, 220, 700, 700);  
		getContentPane().add(imagePanelHSL);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				height = imgOrg.getHeight();
				width = imgOrg.getWidth();
				data = new int[height][width][3];
				for(int y = 0 ; y < height ; y++) {
					for(int x = 0 ; x < width ; x++) {
						int rgb = imgOrg.getRGB(x, y);
						data[y][x][0] = Util.getR(rgb);
						data[y][x][1] = Util.getG(rgb);
						data[y][x][2] = Util.getB(rgb);
						
					}
				}
				
				Graphics g = imagePanelOrg.getGraphics();  
				g.drawImage(imgOrg, 0, 0, null);  
			}
		});
	    
		sliderHue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				imgHSL = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
				tfHueValue.setText(Integer.toString(sliderHue.getValue()));
				Hue = Double.parseDouble(tfHueValue.getText());
				Sat = Double.parseDouble(tfSatValue.getText());
				Light = Double.parseDouble(tfLumValue.getText());
				for(int y = 0 ; y < height ; y++) {
					for(int x = 0 ; x < width ; x ++) {
						HSL newHsl = Util.RGB2HSL(data[y][x][0], data[y][x][1], data[y][x][2]);
						newHsl.h += Hue;
						RGB newRgb = Util.HSL2RGB(newHsl.h, newHsl.s, newHsl.l);
						int rgb = Util.makeColor(newRgb.red, newRgb.green, newRgb.blue);
						imgOrg.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanelOrg.getGraphics();  
				g.drawImage(imgOrg, 0, 0, null);  
			}
			
		});

		sliderSat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfSatValue.setText(Integer.toString(sliderSat.getValue()));
				Hue = Double.parseDouble(tfHueValue.getText());
				Sat = Double.parseDouble(tfSatValue.getText());
				Light = Double.parseDouble(tfLumValue.getText());
				for(int y = 0 ; y < height ; y++) {
					for(int x = 0 ; x < width ; x ++) {
						HSL newHsl = Util.RGB2HSL(data[y][x][0], data[y][x][1], data[y][x][2]);
						newHsl.s += Sat * 0.01;
						RGB newRgb = Util.HSL2RGB(newHsl.h, newHsl.s, newHsl.l);
						int rgb = Util.makeColor(newRgb.red, newRgb.green, newRgb.blue);
						imgOrg.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanelOrg.getGraphics();  
				g.drawImage(imgOrg, 0, 0, null);
			}
		});

		sliderLum.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfLumValue.setText(Integer.toString(sliderLum.getValue()));
				Hue = Double.parseDouble(tfHueValue.getText());
				Sat = Double.parseDouble(tfSatValue.getText());
				Light = Double.parseDouble(tfLumValue.getText());
				for(int y = 0 ; y < height ; y++) {
					for(int x = 0 ; x < width ; x ++) {
						HSL newHsl = Util.RGB2HSL(data[y][x][0], data[y][x][1], data[y][x][2]);
						newHsl.l += Light * 0.01;
						RGB newRgb = Util.HSL2RGB(newHsl.h, newHsl.s, newHsl.l);
						int rgb = Util.makeColor(newRgb.red, newRgb.green, newRgb.blue);
						imgOrg.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanelOrg.getGraphics();  
				g.drawImage(imgOrg, 0, 0, null);
			}
		});
	}

	void loadImg() {
		try {
			imgOrg = ImageIO.read(new File("Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
	}
	void loadData() {
		height = imgOrg.getHeight();
		width = imgOrg.getWidth();
		data = new int[height][width][3];
		for(int y = 0 ; y < height ; y++) {
			for(int x = 0 ; x < width ; x++) {
				int rgb = imgOrg.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
				
			}
		}
	}
	
	public static void main(String[] args) {
		HSLFrame frame = new HSLFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
