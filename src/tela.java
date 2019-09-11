import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;

public class tela extends JFrame{	
	private mapa m;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JButton [] b = new JButton[7];

	public tela() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		prepara_mapa(6, 10);
		createMenuBar();
		prepara_tela();
	}
	
	private void prepara_mapa(int linhas, int colunas) {
		m = new mapa(linhas, colunas);
		m.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(x > 10 && y > 10 && x < 1610 && y < 906)
					m.verificarComando(x, y);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
	private void prepara_tela() {
		setTitle("Lista 2 - Projeto de Algoritmos");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		prepara_top();
		prepara_bottom();
		
		add(topPanel, BorderLayout.PAGE_START);
		add(bottomPanel, BorderLayout.CENTER);
	}
	
	private void prepara_top() {
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(1600, 910));
		topPanel.setLayout(new BorderLayout());
		topPanel.add(m, BorderLayout.CENTER);
	}
	
	private void prepara_bottom() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 7));
		
		for(int i = 0; i < 7; ++i) {
			b[i] = new JButton();
			b[i].setSize(new Dimension(200,200));
			b[i].addActionListener(new Listener());
			b[i].setBorder(new LineBorder(Color.BLACK));
			b[i].setBackground(Color.WHITE);
			b[i].setContentAreaFilled(false);
			b[i].setOpaque(true);
			b[i].setFont(new Font("Arial", Font.BOLD, 20));
			b[i].setActionCommand(String.valueOf(i));
			bottomPanel.add(b[i]);
		}
		
		b[0].setText("Criar Hospital");
		b[1].setText("Apagar Local");
		b[2].setText("Adicionar Local");
		b[3].setText("Criar distância");
		b[4].setText("Apagar distância");
		b[5].setText("Acidente");
		b[6].setText("Traçar Rota");
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("Arquivo");
		
		JMenuItem newMenuItem = new JMenuItem("New", new ImageIcon("imgs/newfile.png"));
		newMenuItem.setActionCommand("new");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newMenuItem.addActionListener(new MenuListener());
		
		JMenuItem openMenuItem = new JMenuItem("Open", new ImageIcon("imgs/openfile.png"));
		openMenuItem.setActionCommand("open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		openMenuItem.addActionListener(new MenuListener());
		
		JMenuItem saveMenuItem = new JMenuItem("Save", new ImageIcon("imgs/savefile.png"));
		saveMenuItem.setActionCommand("save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveMenuItem.addActionListener(new MenuListener());
		
		JMenuItem exitMenuItem = new JMenuItem("Exit", new ImageIcon("imgs/exit.png"));
        exitMenuItem.addActionListener((event) -> System.exit(0));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
	}
	
	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			int command = Integer.parseInt(e.getActionCommand());
			for(int i = 0; i < 6; ++i) {
				if(i == command) {
					b[i].setBackground(Color.YELLOW);
				}
				else
					b[i].setBackground(Color.WHITE);
			}
			
			if(command != 6)
				m.setComando(command);
			else
				m.simulacao();
		} 
	}
	
	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			String command = e.getActionCommand();
			if(command.equals("new")) {
				m.novoMapa();
			}
			else if(command.equals("open")) {
				m.open();
			}
			else if(command.equals("save")) {
				m.grafo.save();
			}
		} 
	}

}

// http://zetcode.com/tutorials/javaswingtutorial/menusandtoolbars/