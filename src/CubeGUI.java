import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;

public class CubeGUI {
	
	private boolean done = false;
	
	private JPanel contentPane;
	private JTextField textFieldFace1;
	private JTextField textFieldFace2;
	private JTextField textFieldFace3;
	private JTextField textFieldFace4;
	private JTextField textFieldFace5;
	private JTextField textFieldFace6;
	
	private int position = 0;
	private JLabel lblInstructions = new JLabel();

	private JFrame frame;
	private ArrayList<String> rotationstring = new ArrayList<String>();
	private static ArrayList<Object> stuff = new ArrayList<Object>();
	
	private final static BlockingQueue queue = new LinkedBlockingDeque();
	
	
	public static ArrayList<Object> getStuff(){
		main(new String[3]);
		try {
			Object j = queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stuff;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CubeGUI window = new CubeGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CubeGUI() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		round1();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void round1() {
		frame.setBounds(100, 100, 602, 452);
		JLabel OutputToUser = new JLabel("Hi and welcome to the CubeGUI. First, please enter your rotations.");
		OutputToUser.setBounds(48, 11, 380, 51);
		frame.getContentPane().add(OutputToUser);
		
		JLabel lblPrintRotations = new JLabel("No Rotations Entered Yet");
		lblPrintRotations.setBounds(23, 357, 407, 45);
		frame.getContentPane().add(lblPrintRotations);
		
		JButton btnRight = new JButton("Right");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rotationstring.add("right");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnRight.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Right.png")));
		btnRight.setBounds(10, 272, 209, 75);
		frame.getContentPane().add(btnRight);
		
		JButton btnLeft = new JButton("Left");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotationstring.add("left");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnLeft.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Left.png")));
		btnLeft.setBounds(219, 272, 209, 75);
		frame.getContentPane().add(btnLeft);
		
		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotationstring.add("up");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnUp.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Up.png")));
		btnUp.setBounds(10, 165, 209, 75);
		frame.getContentPane().add(btnUp);
		
		JButton btnDown = new JButton("Down");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotationstring.add("down");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnDown.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Down.png")));
		btnDown.setBounds(219, 165, 209, 75);
		frame.getContentPane().add(btnDown);
		
		JButton btnClockwise = new JButton("Clockwise");
		btnClockwise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rotationstring.add("clock");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnClockwise.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Clock.png")));
		btnClockwise.setBounds(10, 69, 209, 75);
		frame.getContentPane().add(btnClockwise);
		
		JButton btnCounterclockwise = new JButton("Counter-Clockwise");
		btnCounterclockwise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rotationstring.add("counter");
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnCounterclockwise.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/Counter.png")));
		btnCounterclockwise.setBounds(219, 69, 209, 75);
		frame.getContentPane().add(btnCounterclockwise);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rotationstring.size()==6) {
					stuff.add(rotationstring);
					frame.getContentPane().removeAll();
					round2();
					//frame.dispose();
					//CubeFaces item = new CubeFaces();
					//item.setVisible(true);
					//stuff.add(item.getNestedObjects());
					//queue.add(stuff);
				}
				else {
					OutputToUser.setText("You need exactly 6 rotations!");
					rotationstring.clear();
					lblPrintRotations.setText(rotationstring.toString());
				}
			}
		});
		btnDone.setBounds(466, 69, 110, 75);
		frame.getContentPane().add(btnDone);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotationstring.clear();
				lblPrintRotations.setText(rotationstring.toString());
			}
		});
		btnReset.setBounds(487, 0, 89, 23);
		frame.getContentPane().add(btnReset);
		
	}
	public void round2() {
		frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JLabel lblOutputToUser = new JLabel("<html>Please fill in the net</html>");
		lblOutputToUser.setBounds(318, 11, 106, 57);
		contentPane.add(lblOutputToUser);
		
		textFieldFace1 = new JTextField();
		textFieldFace1.setBounds(29, 41, 43, 27);
		contentPane.add(textFieldFace1);
		textFieldFace1.setColumns(10);
		
		textFieldFace2 = new JTextField();
		textFieldFace2.setColumns(10);
		textFieldFace2.setBounds(29, 115, 43, 27);
		contentPane.add(textFieldFace2);
		
		textFieldFace3 = new JTextField();
		textFieldFace3.setColumns(10);
		textFieldFace3.setBounds(99, 118, 43, 27);
		contentPane.add(textFieldFace3);
		
		textFieldFace4 = new JTextField();
		textFieldFace4.setColumns(10);
		textFieldFace4.setBounds(178, 118, 43, 27);
		contentPane.add(textFieldFace4);
		
		textFieldFace5 = new JTextField();
		textFieldFace5.setColumns(10);
		textFieldFace5.setBounds(249, 118, 43, 27);
		contentPane.add(textFieldFace5);
		
		textFieldFace6 = new JTextField();
		textFieldFace6.setColumns(10);
		textFieldFace6.setBounds(29, 182, 43, 27);
		contentPane.add(textFieldFace6);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(CubeGUI.class.getResource("/Image/SmallCubenet.png")));
		lblNewLabel.setBounds(10, 11, 309, 229);
		contentPane.add(lblNewLabel);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> faces = new ArrayList<Integer>();
				try {
					int t1 = Integer.parseInt(textFieldFace1.getText());
					int t2 = Integer.parseInt(textFieldFace2.getText());
					int t3 = Integer.parseInt(textFieldFace3.getText());
					int t4 = Integer.parseInt(textFieldFace4.getText());
					int t5 = Integer.parseInt(textFieldFace5.getText());
					int t6 = Integer.parseInt(textFieldFace6.getText());
					faces.add(t1);
					faces.add(t2);
					faces.add(t3);
					faces.add(t4);
					faces.add(t5);
					faces.add(t6);
					stuff.add(faces);
					frame.getContentPane().removeAll();
					round3();
				}
				catch(NumberFormatException badness) {
					lblOutputToUser.setText("<html>some fields are NaN. please fix</html>");
				}
			}
		});
		btnDone.setBounds(329, 197, 89, 23);
		contentPane.add(btnDone);
	}
	
	
	
	public void round3() {
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		ArrayList<String> colors = new ArrayList<String>();
		ArrayList<String> symbols = new ArrayList<String>();
		ArrayList<JLabel> imageOverlays = new ArrayList<JLabel>();
		for (int i = 0; i < 4; i++) {
			imageOverlays.add(new JLabel());
		}
		for (int i = 0 ; i < 13; i++) {
			colors.add("white");
			symbols.add("null");
		}
		
		frame.setBounds(100, 100, 908, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//ImageOverlays
		
		JLabel lblImageOverlay1 = new JLabel("");
		lblImageOverlay1.setBounds(30, 184, 90, 90);
		contentPane.add(lblImageOverlay1);
		imageOverlays.add(lblImageOverlay1);
		
		JLabel lblImageOverlay2 = new JLabel("");
		lblImageOverlay2.setBounds(130, 184, 90, 90);
		contentPane.add(lblImageOverlay2);
		imageOverlays.add(lblImageOverlay2);
		
		JLabel lblImageOverlay3 = new JLabel("");
		lblImageOverlay3.setBounds(30, 271, 90, 90);
		contentPane.add(lblImageOverlay3);
		imageOverlays.add(lblImageOverlay3);
		
		JLabel lblImageOverlay4 = new JLabel("");
		lblImageOverlay4.setBounds(130, 271, 90, 90);
		contentPane.add(lblImageOverlay4);
		imageOverlays.add(lblImageOverlay4);
		
		JLabel lblImageOverlay5 = new JLabel("");
		lblImageOverlay5.setBounds(30, 360, 90, 90);
		contentPane.add(lblImageOverlay5);
		imageOverlays.add(lblImageOverlay5);
		
		JLabel lblImageOverlay6 = new JLabel("");
		lblImageOverlay6.setBounds(130, 360, 90, 90);
		contentPane.add(lblImageOverlay6);
		imageOverlays.add(lblImageOverlay6);
		
		JLabel lblImageOverlay7 = new JLabel("");
		lblImageOverlay7.setBounds(30, 452, 90, 90);
		contentPane.add(lblImageOverlay7);
		imageOverlays.add(lblImageOverlay7);
		
		JLabel lblImageOverlay8 = new JLabel("");
		lblImageOverlay8.setBounds(130, 452, 90, 90);
		contentPane.add(lblImageOverlay8);
		imageOverlays.add(lblImageOverlay8);
		
		JLabel lblImageOverlayS = new JLabel("");
		lblImageOverlayS.setBounds(275, 452, 90, 90);
		contentPane.add(lblImageOverlayS);
		imageOverlays.add(lblImageOverlayS);
		
		//WIRES
		
		JLabel lblWire1 = new JLabel("Wire 1");
		lblWire1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblWire1.setIcon(createImageIcon(Color.WHITE, 40, 10));
		lblWire1.setBounds(31, 45, 90, 30);
		contentPane.add(lblWire1);
		labels.add(lblWire1);
		
		JLabel lblWire2 = new JLabel("Wire 2");
		lblWire2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWire2.setIcon(createImageIcon(Color.WHITE, 40, 10));
		lblWire2.setBounds(198, 45, 90, 30);
		contentPane.add(lblWire2);
		labels.add(lblWire2);
		
		JLabel lblWire3 = new JLabel("Wire 3");
		lblWire3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWire3.setIcon(createImageIcon(Color.WHITE, 40, 10));
		lblWire3.setBounds(31, 114, 90, 30);
		contentPane.add(lblWire3);
		labels.add(lblWire3);
		
		JLabel lblWire4 = new JLabel("Wire 4");
		lblWire4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWire4.setIcon(createImageIcon(Color.WHITE, 40, 10));
		lblWire4.setBounds(198, 114, 90, 30);
		contentPane.add(lblWire4);
		labels.add(lblWire4);
		
		//BUTTONS
		
		JLabel lblButton1 = new JLabel("B1");
		lblButton1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton1.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton1.setBounds(30, 184, 90, 90);
		contentPane.add(lblButton1);
		labels.add(lblButton1);
		
		JLabel lblButton2 = new JLabel("B2");
		lblButton2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton2.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton2.setBounds(130, 184, 90, 90);
		contentPane.add(lblButton2);
		labels.add(lblButton2);
		
		JLabel lblButton3 = new JLabel("B3");
		lblButton3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton3.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton3.setBounds(30, 271, 90, 90);
		contentPane.add(lblButton3);
		labels.add(lblButton3);
		
		JLabel lblButton4 = new JLabel("B4");
		lblButton4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton4.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton4.setBounds(130, 271, 90, 90);
		contentPane.add(lblButton4);
		labels.add(lblButton4);
		
		JLabel lblButton5 = new JLabel("B5");
		lblButton5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton5.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton5.setBounds(30, 360, 90, 90);
		contentPane.add(lblButton5);
		labels.add(lblButton5);
		
		JLabel lblButton6 = new JLabel("B6");
		lblButton6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton6.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton6.setBounds(130, 360, 90, 90);
		contentPane.add(lblButton6);
		labels.add(lblButton6);
		
		JLabel lblButton7 = new JLabel("B7");
		lblButton7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton7.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton7.setBounds(30, 452, 90, 90);
		contentPane.add(lblButton7);
		labels.add(lblButton7);
		
		JLabel lblButton8 = new JLabel("B8");
		lblButton8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblButton8.setIcon(createImageIcon(Color.WHITE,70,70));
		lblButton8.setBounds(130, 452, 90, 90);
		contentPane.add(lblButton8);
		labels.add(lblButton8);
		
		JLabel lblSubmitButton = new JLabel("BS");
		lblSubmitButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSubmitButton.setIcon(createImageIcon(Color.WHITE,70,70));
		lblSubmitButton.setBounds(275, 452, 90, 90);
		contentPane.add(lblSubmitButton);
		labels.add(lblSubmitButton);
		
		
		//COLOR CHANGERS
		
		JButton btnBlue = new JButton("BLUE");
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.BLUE,width, height));
				colors.set(position, "blue");
				lblInstructions.requestFocus();
			}
		});
		btnBlue.setBounds(298, 50, 89, 23);
		contentPane.add(btnBlue);
		
		JButton btnOrange = new JButton("ORANGE");
		btnOrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.ORANGE,width, height));
				colors.set(position, "orange");
				lblInstructions.requestFocus();
			}
		});
		btnOrange.setBounds(397, 50, 89, 23);
		contentPane.add(btnOrange);
		
		JButton btnGreen = new JButton("GREEN");
		btnGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.GREEN,width, height));
				colors.set(position, "green");
				lblInstructions.requestFocus();
			}
		});
		btnGreen.setBounds(298, 84, 89, 23);
		contentPane.add(btnGreen);
		
		JButton btnPurple = new JButton("PURPLE");
		btnPurple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.MAGENTA,width, height));
				colors.set(position, "purple");
				lblInstructions.requestFocus();
			}
		});
		btnPurple.setBounds(397, 84, 89, 23);
		contentPane.add(btnPurple);
		
		JButton btnRed = new JButton("RED");
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.RED,width, height));
				colors.set(position, "red");
				lblInstructions.requestFocus();
			}
		});
		btnRed.setBounds(298, 119, 89, 23);
		contentPane.add(btnRed);
		
		JButton btnWhite = new JButton("WHITE");
		btnWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 70;
				int height = 70;
				if (position<4) {
					width = 40;
					height = 10;
				}
				labels.get(position).setIcon(createImageIcon(Color.WHITE,width, height));
				colors.set(position, "white");
				lblInstructions.requestFocus();
			}
		});
		btnWhite.setBounds(397, 119, 89, 23);
		contentPane.add(btnWhite);
		
		JButton btnLeft = new JButton("LEFT");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labels.get(position).setFont(new Font("Tahoma", Font.PLAIN, 12));
				if (position>0)position-=1;
				labels.get(position).setFont(new Font("Tahoma", Font.BOLD, 12));
				lblInstructions.requestFocus();
			}
		});
		btnLeft.setBounds(539, 96, 89, 23);
		contentPane.add(btnLeft);
		
		JButton btnRight = new JButton("RIGHT");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labels.get(position).setFont(new Font("Tahoma", Font.PLAIN, 12));
				if (position<12)position+=1;
				labels.get(position).setFont(new Font("Tahoma", Font.BOLD, 12));
				lblInstructions.requestFocus();
			}
		});
		btnRight.setBounds(638, 96, 89, 23);
		contentPane.add(btnRight);
		
		
		
		lblInstructions = new JLabel("<html>Welcome to hell. Please input the colors of your wires, then buttons. Use like arrow keys and shit. S is for submit button.</html>");
		lblInstructions.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_RIGHT)btnRight.doClick();
				if (e.getKeyCode()==KeyEvent.VK_LEFT)btnLeft.doClick();
			}
		});
		lblInstructions.setBounds(539, 11, 208, 90);
		lblInstructions.setFocusable(true);
		contentPane.add(lblInstructions);
		
		
		
		
		
		
		JButton btnCharlie = new JButton("");
		btnCharlie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Charlie.png")));
					symbols.set(position, "charlie");
				}
				lblInstructions.requestFocus();
			}
		});
		btnCharlie.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Charlie.png")));
		btnCharlie.setBounds(275, 184, 80, 80);
		contentPane.add(btnCharlie);
		
		JButton btnCheckmark = new JButton("");
		btnCheckmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Checkmark.png")));
					symbols.set(position, "checkmark");
				}
				lblInstructions.requestFocus();
			}
		});
		btnCheckmark.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Checkmark.png")));
		btnCheckmark.setBounds(375, 184, 80, 80);
		contentPane.add(btnCheckmark);
		
		JButton btnCoffee = new JButton("");
		btnCoffee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Coffee.png")));
					symbols.set(position, "coffee");					
				}
				lblInstructions.requestFocus();
			}
		});
		btnCoffee.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Coffee.png")));
		btnCoffee.setBounds(475, 184, 80, 80);
		contentPane.add(btnCoffee);
		
		JButton btnEclipse = new JButton("");
		btnEclipse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Eclipse.png")));
					symbols.set(position, "eclipse");
				}
				lblInstructions.requestFocus();
			}
		});
		btnEclipse.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Eclipse.png")));
		btnEclipse.setBounds(575, 184, 80, 80);
		contentPane.add(btnEclipse);
		
		JButton btnFireball = new JButton("");
		btnFireball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Fireball.png")));
					symbols.set(position, "fireball");
				}
				lblInstructions.requestFocus();
			}
		});
		btnFireball.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Fireball.png")));
		btnFireball.setBounds(675, 184, 80, 80);
		contentPane.add(btnFireball);
		
		JButton btnFlag = new JButton("");
		btnFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Flag.png")));
					symbols.set(position, "flag");
				}
				lblInstructions.requestFocus();
			}
		});
		btnFlag.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Flag.png")));
		btnFlag.setBounds(775, 184, 80, 80);
		contentPane.add(btnFlag);
		
		JButton btnGlobe = new JButton("");
		btnGlobe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Globe.png")));
					symbols.set(position, "globe");
				}
				lblInstructions.requestFocus();
			}
		});
		btnGlobe.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Globe.png")));
		btnGlobe.setBounds(275, 278, 80, 80);
		contentPane.add(btnGlobe);
		
		JButton btnMeteor = new JButton("");
		btnMeteor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Meteor.png")));
					symbols.set(position, "meteor");
				}
				lblInstructions.requestFocus();
			}
		});
		btnMeteor.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Meteor.png")));
		btnMeteor.setBounds(375, 278, 80, 80);
		contentPane.add(btnMeteor);
		
		JButton btnOscar = new JButton("");
		btnOscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Oscar.png")));
					symbols.set(position, "oscar");
				}
				lblInstructions.requestFocus();
			}
		});
		btnOscar.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Oscar.png")));
		btnOscar.setBounds(475, 278, 80, 80);
		contentPane.add(btnOscar);
		
		JButton btnPepsi = new JButton("");
		btnPepsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pepsi.png")));
					symbols.set(position, "pepsi");
				}
				lblInstructions.requestFocus();
			}
		});
		btnPepsi.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pepsi.png")));
		btnPepsi.setBounds(575, 278, 80, 80);
		contentPane.add(btnPepsi);
		
		JButton btnPluto = new JButton("");
		btnPluto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pluto.png")));
					symbols.set(position, "pluto");
				}
				lblInstructions.requestFocus();
			}
		});
		btnPluto.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pluto.png")));
		btnPluto.setBounds(675, 278, 80, 80);
		contentPane.add(btnPluto);
		
		JButton btnRibbon = new JButton("");
		btnRibbon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Ribbon.png")));
					symbols.set(position, "ribbon");
				}
				lblInstructions.requestFocus();
			}
		});
		btnRibbon.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Ribbon.png")));
		btnRibbon.setBounds(775, 278, 80, 80);
		contentPane.add(btnRibbon);
		
		JButton btnRussianDoll = new JButton("");
		btnRussianDoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/RussianDoll.png")));
					symbols.set(position, "russiandoll");
				}
				lblInstructions.requestFocus();
			}
		});
		btnRussianDoll.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/RussianDoll.png")));
		btnRussianDoll.setBounds(275, 367, 80, 80);
		contentPane.add(btnRussianDoll);
		
		JButton btnSqueeze = new JButton("");
		btnSqueeze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Squeeze.png")));
					symbols.set(position, "squeeze");
				}
				lblInstructions.requestFocus();
			}
		});
		btnSqueeze.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Squeeze.png")));
		btnSqueeze.setBounds(375, 367, 80, 80);
		contentPane.add(btnSqueeze);
		
		JButton btnStar = new JButton("");
		btnStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Star.png")));
					symbols.set(position, "star");
				}
				lblInstructions.requestFocus();
			}
		});
		btnStar.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Star.png")));
		btnStar.setBounds(475, 367, 80, 80);
		contentPane.add(btnStar);
		
		JButton btnStonehenge = new JButton("");
		btnStonehenge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Stonehenge.png")));
					symbols.set(position, "stonehenge");
				}
				lblInstructions.requestFocus();
			}
		});
		btnStonehenge.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Stonehenge.png")));
		btnStonehenge.setBounds(575, 367, 80, 80);
		contentPane.add(btnStonehenge);
		
		JButton btnTunnel = new JButton("");
		btnTunnel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Tunnel.png")));
					symbols.set(position, "tunnel");
				}
				lblInstructions.requestFocus();
			}
		});
		btnTunnel.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Tunnel.png")));
		btnTunnel.setBounds(675, 367, 80, 80);
		contentPane.add(btnTunnel);
		
		JButton btnVortex = new JButton("");
		btnVortex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position>3) {
					imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Vortex.png")));
					symbols.set(position, "vortex");
				}
				lblInstructions.requestFocus();
			}
		});
		btnVortex.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Vortex.png")));
		btnVortex.setBounds(775, 367, 80, 80);
		contentPane.add(btnVortex);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stuff.add(colors);
				stuff.add(symbols);
				frame.getContentPane().removeAll();
				round4();
			}
		});
		btnDone.setBounds(775, 31, 89, 23);
		contentPane.add(btnDone);
		lblInstructions.requestFocus();
	}
	
	public void round4() {
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		ArrayList<Integer> symbolInts = new ArrayList<Integer>();
		ArrayList<JLabel> imageOverlays = new ArrayList<JLabel>();
		for (int i = 0 ; i < 16; i ++)symbolInts.add(100);
		position = 0;
		
		frame.setBounds(100, 100, 908, 647);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//ImageOverlays
		
		JLabel lblImageOverlay1 = new JLabel("");
		lblImageOverlay1.setBounds(30, 11, 90, 90);
		contentPane.add(lblImageOverlay1);
		imageOverlays.add(lblImageOverlay1);
		
		JLabel lblImageOverlay2 = new JLabel("");
		lblImageOverlay2.setBounds(120, 11, 90, 90);
		contentPane.add(lblImageOverlay2);
		imageOverlays.add(lblImageOverlay2);
		
		JLabel lblImageOverlay3 = new JLabel("");
		lblImageOverlay3.setBounds(210, 11, 90, 90);
		contentPane.add(lblImageOverlay3);
		imageOverlays.add(lblImageOverlay3);
		
		JLabel lblImageOverlay4 = new JLabel("");
		lblImageOverlay4.setBounds(300, 11, 90, 90);
		contentPane.add(lblImageOverlay4);
		imageOverlays.add(lblImageOverlay4);
		
		JLabel lblImageOverlay5 = new JLabel("");
		lblImageOverlay5.setBounds(390, 11, 90, 90);
		contentPane.add(lblImageOverlay5);
		imageOverlays.add(lblImageOverlay5);
		
		JLabel lblImageOverlay6 = new JLabel("");
		lblImageOverlay6.setBounds(480, 11, 90, 90);
		contentPane.add(lblImageOverlay6);
		imageOverlays.add(lblImageOverlay6);
		
		JLabel lblImageOverlay7 = new JLabel("");
		lblImageOverlay7.setBounds(570, 11, 90, 90);
		contentPane.add(lblImageOverlay7);
		imageOverlays.add(lblImageOverlay7);
		
		JLabel lblImageOverlay8 = new JLabel("");
		lblImageOverlay8.setBounds(660, 11, 90, 90);
		contentPane.add(lblImageOverlay8);
		imageOverlays.add(lblImageOverlay8);
		
		JLabel lblImageOverlay9 = new JLabel("");
		lblImageOverlay9.setBounds(30, 458, 90, 90);
		contentPane.add(lblImageOverlay9);
		imageOverlays.add(lblImageOverlay9);
		
		JLabel lblImageOverlay10 = new JLabel("");
		lblImageOverlay10.setBounds(120, 458, 90, 90);
		contentPane.add(lblImageOverlay10);
		imageOverlays.add(lblImageOverlay10);
		
		JLabel lblImageOverlay11 = new JLabel("");
		lblImageOverlay11.setBounds(210, 458, 90, 90);
		contentPane.add(lblImageOverlay11);
		imageOverlays.add(lblImageOverlay11);
		
		JLabel lblImageOverlay12 = new JLabel("");
		lblImageOverlay12.setBounds(300, 458, 90, 90);
		contentPane.add(lblImageOverlay12);
		imageOverlays.add(lblImageOverlay12);
		
		JLabel lblImageOverlay13 = new JLabel("");
		lblImageOverlay13.setBounds(390, 458, 90, 90);
		contentPane.add(lblImageOverlay13);
		imageOverlays.add(lblImageOverlay13);
		
		JLabel lblImageOverlay14 = new JLabel("");
		lblImageOverlay14.setBounds(480, 458, 90, 90);
		contentPane.add(lblImageOverlay14);
		imageOverlays.add(lblImageOverlay14);
		
		JLabel lblImageOverlay15 = new JLabel("");
		lblImageOverlay15.setBounds(570, 458, 90, 90);
		contentPane.add(lblImageOverlay15);
		imageOverlays.add(lblImageOverlay15);
		
		JLabel lblImageOverlay16 = new JLabel("");
		lblImageOverlay16.setBounds(660, 458, 90, 90);
		contentPane.add(lblImageOverlay16);
		imageOverlays.add(lblImageOverlay16);
		
		
		JButton btnLeft = new JButton("LEFT");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labels.get(position).setFont(new Font("Tahoma", Font.PLAIN, 12));
				if (position>0)position-=1;
				labels.get(position).setFont(new Font("Tahoma", Font.BOLD, 12));
				lblInstructions.requestFocus();
			}
		});
		btnLeft.setBounds(30, 313, 89, 23);
		contentPane.add(btnLeft);
		
		JButton btnRight = new JButton("RIGHT");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labels.get(position).setFont(new Font("Tahoma", Font.PLAIN, 12));
				if (position<labels.size()-1)position+=1;
				labels.get(position).setFont(new Font("Tahoma", Font.BOLD, 12));
				lblInstructions.requestFocus();
			}
		});
		btnRight.setBounds(121, 313, 89, 23);
		contentPane.add(btnRight);
		
		
		
		lblInstructions = new JLabel("<html>Input the 8 digit code on the left, and then bottom of your screen. Left is above, bottom is below.</html>");
		lblInstructions.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_RIGHT)btnRight.doClick();
				if (e.getKeyCode()==KeyEvent.VK_LEFT)btnLeft.doClick();
			}
		});
		lblInstructions.setBounds(37, 246, 208, 90);
		lblInstructions.setFocusable(true);
		contentPane.add(lblInstructions);
		
		
		
		
		
		
		JButton btnCharlie = new JButton("");
		btnCharlie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Charlie.png")));
				symbolInts.set(position, 9);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnCharlie.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Charlie.png")));
		btnCharlie.setBounds(275, 184, 80, 80);
		contentPane.add(btnCharlie);
		
		JButton btnCheckmark = new JButton("");
		btnCheckmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Checkmark.png")));
				symbolInts.set(position, 5);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnCheckmark.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Checkmark.png")));
		btnCheckmark.setBounds(375, 184, 80, 80);
		contentPane.add(btnCheckmark);
		
		JButton btnCoffee = new JButton("");
		btnCoffee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Coffee.png")));
				symbolInts.set(position, 6);					
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnCoffee.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Coffee.png")));
		btnCoffee.setBounds(475, 184, 80, 80);
		contentPane.add(btnCoffee);
		
		JButton btnEclipse = new JButton("");
		btnEclipse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Eclipse.png")));
				symbolInts.set(position, 7);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnEclipse.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Eclipse.png")));
		btnEclipse.setBounds(575, 184, 80, 80);
		contentPane.add(btnEclipse);
		
		JButton btnFireball = new JButton("");
		btnFireball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Fireball.png")));
				symbolInts.set(position, 8);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnFireball.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Fireball.png")));
		btnFireball.setBounds(675, 184, 80, 80);
		contentPane.add(btnFireball);
		
		JButton btnFlag = new JButton("");
		btnFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Flag.png")));
				symbolInts.set(position, 5);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnFlag.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Flag.png")));
		btnFlag.setBounds(775, 184, 80, 80);
		contentPane.add(btnFlag);
		
		JButton btnGlobe = new JButton("");
		btnGlobe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Globe.png")));
				symbolInts.set(position, 7);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnGlobe.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Globe.png")));
		btnGlobe.setBounds(275, 278, 80, 80);
		contentPane.add(btnGlobe);
		
		JButton btnMeteor = new JButton("");
		btnMeteor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Meteor.png")));
				symbolInts.set(position, 4);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnMeteor.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Meteor.png")));
		btnMeteor.setBounds(375, 278, 80, 80);
		contentPane.add(btnMeteor);
		
		JButton btnOscar = new JButton("");
		btnOscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Oscar.png")));
				symbolInts.set(position, 0);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnOscar.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Oscar.png")));
		btnOscar.setBounds(475, 278, 80, 80);
		contentPane.add(btnOscar);
		
		JButton btnPepsi = new JButton("");
		btnPepsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pepsi.png")));
				symbolInts.set(position, 6);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnPepsi.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pepsi.png")));
		btnPepsi.setBounds(575, 278, 80, 80);
		contentPane.add(btnPepsi);
		
		JButton btnPluto = new JButton("");
		btnPluto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pluto.png")));
				symbolInts.set(position, 3);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnPluto.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Pluto.png")));
		btnPluto.setBounds(675, 278, 80, 80);
		contentPane.add(btnPluto);
		
		JButton btnRibbon = new JButton("");
		btnRibbon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Ribbon.png")));
				symbolInts.set(position, 3);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnRibbon.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Ribbon.png")));
		btnRibbon.setBounds(775, 278, 80, 80);
		contentPane.add(btnRibbon);
		
		JButton btnRussianDoll = new JButton("");
		btnRussianDoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/RussianDoll.png")));
				symbolInts.set(position, 2);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnRussianDoll.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/RussianDoll.png")));
		btnRussianDoll.setBounds(275, 367, 80, 80);
		contentPane.add(btnRussianDoll);
		
		JButton btnSqueeze = new JButton("");
		btnSqueeze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Squeeze.png")));
				symbolInts.set(position, 4);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnSqueeze.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Squeeze.png")));
		btnSqueeze.setBounds(375, 367, 80, 80);
		contentPane.add(btnSqueeze);
		
		JButton btnStar = new JButton("");
		btnStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Star.png")));
				symbolInts.set(position, 4);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnStar.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Star.png")));
		btnStar.setBounds(475, 367, 80, 80);
		contentPane.add(btnStar);
		
		JButton btnStonehenge = new JButton("");
		btnStonehenge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Stonehenge.png")));
				symbolInts.set(position, 1);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnStonehenge.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Stonehenge.png")));
		btnStonehenge.setBounds(575, 367, 80, 80);
		contentPane.add(btnStonehenge);
		
		JButton btnTunnel = new JButton("");
		btnTunnel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Tunnel.png")));
				symbolInts.set(position, 1);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnTunnel.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Tunnel.png")));
		btnTunnel.setBounds(675, 367, 80, 80);
		contentPane.add(btnTunnel);
		
		JButton btnVortex = new JButton("");
		btnVortex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOverlays.get(position).setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Vortex.png")));
				symbolInts.set(position, 2);
				lblInstructions.requestFocus();
				btnRight.doClick();
			}
		});
		btnVortex.setIcon(new ImageIcon(CubeGUI.class.getResource("/KRAFontImages/Vortex.png")));
		btnVortex.setBounds(775, 367, 80, 80);
		contentPane.add(btnVortex);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!symbolInts.contains(100)) {
					stuff.add(symbolInts);
					queue.add(1);
					frame.dispose();
				}
				else {
					lblInstructions.setText("Fill all the things!");
				}
			}
		});
		btnDone.setBounds(775, 31, 89, 23);
		contentPane.add(btnDone);
		
		JLabel label1 = new JLabel("1");
		label1.setBounds(74, 95, 46, 14);
		contentPane.add(label1);
		labels.add(label1);
		
		JLabel label2 = new JLabel("2");
		label2.setBounds(140, 95, 46, 14);
		contentPane.add(label2);
		labels.add(label2);
		
		JLabel label3 = new JLabel("3");
		label3.setBounds(220, 95, 46, 14);
		contentPane.add(label3);
		labels.add(label3);
		
		JLabel label4 = new JLabel("4");
		label4.setBounds(300, 95, 46, 14);
		contentPane.add(label4);
		labels.add(label4);
		
		JLabel label5 = new JLabel("5");
		label5.setBounds(382, 95, 46, 14);
		contentPane.add(label5);
		labels.add(label5);
		
		JLabel label6 = new JLabel("6");
		label6.setBounds(448, 95, 46, 14);
		contentPane.add(label6);
		labels.add(label6);
		
		JLabel label7 = new JLabel("7");
		label7.setBounds(536, 100, 46, 14);
		contentPane.add(label7);
		labels.add(label7);
		
		JLabel label8 = new JLabel("8");
		label8.setBounds(592, 95, 46, 14);
		contentPane.add(label8);
		labels.add(label8);
		
		JLabel label9 = new JLabel("9");
		label9.setBounds(592, 195, 46, 14);
		contentPane.add(label9);
		labels.add(label9);
		
		JLabel label10 = new JLabel("10");
		label10.setBounds(536, 195, 46, 14);
		contentPane.add(label10);
		labels.add(label10);
		
		JLabel label11 = new JLabel("11");
		label11.setBounds(74, 195, 46, 14);
		contentPane.add(label11);
		labels.add(label11);
		
		JLabel label12 = new JLabel("12");
		label12.setBounds(140, 195, 46, 14);
		contentPane.add(label12);
		labels.add(label12);
		
		JLabel label13 = new JLabel("13");
		label13.setBounds(220, 195, 46, 14);
		contentPane.add(label13);
		labels.add(label13);
		
		JLabel label14 = new JLabel("14");
		label14.setBounds(300, 195, 46, 14);
		contentPane.add(label14);
		labels.add(label14);
		
		JLabel label15 = new JLabel("15");
		label15.setBounds(382, 195, 46, 14);
		contentPane.add(label15);
		labels.add(label15);
		
		JLabel label16 = new JLabel("16");
		label16.setBounds(448, 195, 46, 14);
		contentPane.add(label16);
		labels.add(label16);
		
		
		
		
		for (int i = 0 ; i < labels.size(); i++) {
			labels.get(i).setBounds(imageOverlays.get(i).getX()+25, imageOverlays.get(i).getY()+100, 45, 15);
		}
		
		lblInstructions.requestFocus();
	}
	public static ImageIcon createImageIcon(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect ( 0, 0, width, height);
        return new ImageIcon(image);
    }
}
