import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class mapa extends Canvas{
	private int linhas;
	private int colunas;
	private int comando = -1;
	private int espacoHorizontal = 10;
	private int espacoVertical = 6;
	private int horizontal = 1600;
	private int vertical = 900;
	private int raio = 100;
	public graph grafo;
	private ArrayList<Integer> caminho = new ArrayList<Integer>();
	private boolean lendoArquivo = false;
	
	private int tempX = -1;
	private int tempY = -1;
	
	static public int tempDist = -1;
	
	
	public mapa(int linhas, int colunas) {
		this.linhas = linhas;
		this.colunas = colunas;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(espacoHorizontal, espacoVertical, horizontal, vertical);
		
		g.setColor(Color.BLACK);
		g.drawRect(espacoHorizontal-1, espacoVertical-1, horizontal+1, vertical+1);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		
		for(int i = 0; i < linhas; ++i) {
			for(int j = 0; j < colunas; ++j) {
				g.drawOval(j*160 + 40, i*150 + 30, raio, raio);
				int valor = this.numerarArea(j, i);
				String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
				g.drawString(s, j*160 + 65, i*150 + 95);
			}
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(1645, 50, 265, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 49, 266, 101);
		g.drawString("INÍCIO", 1695, 90);
		g.drawString("00", 1735, 140);
		
		g.setColor(Color.WHITE);
		g.fillRect(1645, 170, 265, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 169, 266, 101);
		g.drawString("FIM", 1720, 205);
		g.drawString("00", 1735, 260);
		
		g.setColor(Color.WHITE);
		g.fillRect(1645, 290, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 289, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 330);
		g.drawString("INICIAL", 1690, 380);
		g.drawString("000", 1725, 430);
		
		g.setColor(Color.WHITE);
		g.fillRect(1645, 460, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 459, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 500);
		g.drawString("FINAL", 1700, 550);
		g.drawString("000", 1725, 600);
		
		g.setColor(Color.WHITE);
		g.fillRect(1645, 630, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 629, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 670);
		g.drawString("TOTAL", 1689, 720);
		g.drawString("0000", 1710, 770);

		grafo = new graph();
	}
	
	public void verificarComando(int x, int y) {
		if(this.comando == 0){
			this.createHospital(x, y);
		}
		else if (this.comando == 1){
			this.apagarLocal(x, y);
		}
		else if (this.comando == 2){
			this.adicionarLocal(x, y);
		}
		else if (this.comando == 3){
			this.criarDistancia(x, y);
		}
		else if (this.comando == 4){
			this.apagarDistancia(x, y);
		}
		else if (this.comando == 5){
			this.acidente(x, y);
		}
	}
	
	private void createHospital(int x, int y) {
		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);

		if(grafo.isHospital(valor)) {
			this.mensagemErro("Essa área já é um hospital");
		}
		else {
			Graphics g = getGraphics();

			g.setColor(Color.RED);
			g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
			
			g.setColor(Color.BLACK);
			g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
			
			g.setFont(new Font("Arial", Font.BOLD, 40));
			String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
			g.drawString(s, x*160 + 65, y*150 + 95);
			
			grafo.adicionarHospital(valor);
		}
		
		if(grafo.isInvalido(valor))
			grafo.removerInvalido(valor);
		
		if(grafo.acidente == valor) {
			grafo.acidente = -1;
		}
	}
	
	private void apagarLocal(int x, int y) {
		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);
		
		Graphics g = getGraphics();
		g.setColor(Color.WHITE);

		g.fillRect((x*(horizontal/colunas))+espacoHorizontal,
				   (y*(vertical/linhas))+espacoVertical,
				   horizontal/colunas, vertical/linhas);
		
		grafo.setInvalido(valor);
		
		if(x > 0) { // Se existir esquerda
			g.fillRect((x*(horizontal/colunas))-14,
					   (y*(vertical/linhas))+10,
					   25, 100);
		}
		if(x < 9) { // Se existir direita
			g.fillRect((x*(horizontal/colunas))+150,
					   (y*(vertical/linhas))+10,
					   43, 100);
		}
		if(y > 0) { // Se existir em cima
			g.fillRect((x*(horizontal/colunas))+12,
					   (y*(vertical/linhas))-16,
					   105, 25);
		}
		if(y < 5) { // Se existir em baixo
			g.fillRect((x*(horizontal/colunas))+12,
					   (y*(vertical/linhas))+150,
					   105, 27);
		}
	}
	
	private void adicionarLocal(int x, int y) {
		Graphics g = getGraphics();

		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);
		
		if(valor == grafo.acidente)
			return;

		g.setColor(Color.BLACK);
		g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
		String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString(s, x*160 + 65, y*150 + 95);
		
		if(grafo.isHospital(valor))
			grafo.removerHospital(valor);
		
		if(grafo.isInvalido(valor))
			grafo.removerInvalido(valor);
	}
	
	private void criarDistancia(int x, int y) {
		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);
		
		Graphics g = getGraphics();
		
		if(this.tempX == -1) {	
			if(grafo.isInvalido(valor)) {
				this.mensagemErro("Essa é uma área inválida");
			}
			else {
				this.tempX = x;
				this.tempY = y;
				g.setColor(Color.YELLOW);
				g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
				
				g.setColor(Color.BLACK);
				g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
				
				g.setFont(new Font("Arial", Font.BOLD, 40));
				String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
				g.drawString(s, x*160 + 65, y*150 + 95);
			}
		}
		else {
			int valor2 = this.numerarArea(tempX, tempY);
			if(grafo.isInvalido(valor)) {
				this.mensagemErro("Essa é uma área inválida");
			}
			else if(valor2 == valor) {
				this.mensagemErro("Áreas devem ser diferentes");
			}
			else if (grafo.getDistancia(valor2, valor) != 0){
				this.mensagemErro("Essa distância já foi cadastrada");
			}
			else if(!grafo.verificarValidade(valor2, valor)) {
				this.mensagemErro("Áreas devem ser adjacentes (porém não na diagonal)");
			}
			else { // Tudo OK
				if(grafo.getDistancia(valor, valor2) == 0) {
					if(this.lendoArquivo == false)
						this.pedeValor();
					
					if(this.tempDist != -1) {
						this.desenhaSeta(valor, valor2, this.tempDist);
						grafo.setDistancia(valor2, valor, this.tempDist);
					}
				}
				else {
					int dist = grafo.getDistancia(valor, valor2);
					grafo.setDistancia(valor2, valor, dist);
					String s = String.valueOf(dist);
					if(valor2-valor == 10) { // Baixo-Cima depois
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Vertical.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				        }
				        catch (Exception e) {}
						
						g.drawImage(image, tempX*160 + 75, tempY*150 - 18, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(s, tempX*160 + 87, tempY*150 + 11);
					}
					else if(valor-valor2 == 1) { // Esquerda-Direita depois
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Horizontal.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, tempX*160 + 147, tempY*150 + 60, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(s,  tempX*160 + 166, tempY*150 + 81);
					}
					else if(valor2-valor == -10) { // Cima-Baixo depois
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Vertical.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, tempX*160 + 75, tempY*150 + 132, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(s, tempX*160 + 87, tempY*150 + 160);
					}
					else if(valor-valor2 == -1) { // Direita-Esquerda depois
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Horizontal.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, tempX*160 - 12, tempY*150 + 60, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(s,  tempX*160 + 8, tempY*150 + 82);
					}
				}
			}
			
			if(grafo.isHospital(valor2))
				g.setColor(Color.RED);
			else if(grafo.acidente == valor2) {
				g.setColor(Color.BLACK);
			}
			else
				g.setColor(Color.WHITE);
			
			g.fillOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			
			if(grafo.acidente == valor2)
				g.setColor(Color.WHITE);
			else 
				g.setColor(Color.BLACK);
			
			g.drawOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			String s = valor2 < 10 ? "0" + String.valueOf(valor2) : String.valueOf(valor2);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString(s, tempX*160 + 65, tempY*150 + 95);

			this.tempX = -1;
			this.tempY = -1;
			this.tempDist = -1;
		}
	}
	
	private void apagarDistancia(int x, int y) {
		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);
		
		Graphics g = getGraphics();
		
		if(this.tempX == -1) {	
			if(grafo.isInvalido(valor)) {
				this.mensagemErro("Essa é uma área inválida");
			}
			else {
				this.tempX = x;
				this.tempY = y;
				g.setColor(Color.YELLOW);
				g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
				
				g.setColor(Color.BLACK);
				g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
				
				g.setFont(new Font("Arial", Font.BOLD, 40));
				String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
				g.drawString(s, x*160 + 65, y*150 + 95);
			}
		}
		else {
			int valor2 = this.numerarArea(tempX, tempY);
			if(grafo.isInvalido(valor)) {
				this.mensagemErro("Essa é uma área inválida");
			}
			else if(valor2 == valor) {
				this.mensagemErro("Áreas devem ser diferentes");
			}
			else if (grafo.getDistancia(valor2, valor) == 0){
				this.mensagemErro("Essa distância ainda não foi cadastrada");
			}
			else if(!grafo.verificarValidade(valor2, valor)) {
				this.mensagemErro("Áreas devem ser adjacentes (porém não na diagonal)");
			}
			else { // Tudo OK
				g.setColor(Color.WHITE);
				grafo.setDistancia(valor2, valor, 0);
				int dist = grafo.getDistancia(valor, valor2);
				if(valor2-valor == 1) { // Se da direita para a esquerda
					g.fillRect((tempX*(horizontal/colunas))-14,
							   (tempY*(vertical/linhas))+10,
							   46, 100);
					if(dist != 0) {
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Direita.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, x*160 + 150, y*150 + 60, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(String.valueOf(dist), x*160 + 165, y*150 + 81);
					}
				}
				else if(valor2-valor == -1) { // Se da esquerda para a direita
					g.fillRect((tempX*(horizontal/colunas))+148,
							   (tempY*(vertical/linhas))+10,
							   45, 100);
					if(dist != 0) {
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Esquerda.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, x*160 - 15, y*150 + 60, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(String.valueOf(dist), x*160 + 5, y*150 + 82);
					}
				}
				else if(valor2-valor == 10) { // Se de baixo para cima
					g.fillRect((tempX*(horizontal/colunas))+12,
							   (tempY*(vertical/linhas))-17,
							   105, 44);
					if(dist != 0) {
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Baixo.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, x*160 + 75, y*150 + 132, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(String.valueOf(dist), x*160 + 87, y*150 + 155);
					}
				}
				else if(valor2-valor == -10) { // Se de cima para baixo
					g.fillRect((tempX*(horizontal/colunas))+12,
							   (tempY*(vertical/linhas))+132,
							   105, 47);
					if(dist != 0) {
						MediaTracker media = new MediaTracker(this);
						Image image = Toolkit.getDefaultToolkit().getImage("imgs/Cima.png");
						media.addImage(image, 0);
						try {
				             media.waitForID(0); 
				         }
				        catch (Exception e) {}
						
						g.drawImage(image, x*160 + 75, y*150 - 18, this);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Arial", Font.BOLD, 13));
						g.drawString(String.valueOf(dist), x*160 + 86, y*150 + 11);
					}
				}
			}
			
			if(grafo.isHospital(valor2))
				g.setColor(Color.RED);
			else if(grafo.acidente == valor2) {
				g.setColor(Color.BLACK);
			}
			else
				g.setColor(Color.WHITE);
			
			g.fillOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			
			if(grafo.acidente == valor2)
				g.setColor(Color.WHITE);
			else 
				g.setColor(Color.BLACK);

			g.drawOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			String s = valor2 < 10 ? "0" + String.valueOf(valor2) : String.valueOf(valor2);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString(s, tempX*160 + 65, tempY*150 + 95);
			
			this.tempX = -1;
			this.tempY = -1;
		}
	}
	
	public void novoMapa() {
		this.paint(getGraphics());
		this.grafo = new graph();
		caminho = new ArrayList<Integer>();
	}
	
	private void acidente(int x, int y) {
		Graphics g = getGraphics();
		
		x = transformarCoordenadaX(x);
		y = transformarCoordenadaY(y);
		int valor = this.numerarArea(x, y);
		
		if(grafo.acidente != -1) {
			int tempX = grafo.acidente%10;
			int tempY = grafo.acidente/10;
			
			int tempValor = this.numerarArea(tempX, tempY);
			
			g.setColor(Color.WHITE);
			g.fillOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			
			g.setColor(Color.BLACK);
			g.drawOval(tempX*160 + 40, tempY*150 + 30, raio, raio);
			String s = tempValor < 10 ? "0" + String.valueOf(tempValor) : String.valueOf(tempValor);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString(s, tempX*160 + 65, tempY*150 + 95);
		}

		g.setColor(Color.BLACK);
		g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
		
		g.setColor(Color.WHITE);
		g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
		String s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString(s, x*160 + 65, y*150 + 95);
		
		grafo.acidente = valor;
	}
	
	public void simulacao() {
		if(grafo.quantidadeHospitais() == 0) {
			this.mensagemErro("Não há hospitais");
			return;
		}
		
		if(grafo.acidente == -1) {
			this.mensagemErro("Não há acidentes");
			return;
		}
		
		Graphics g = getGraphics();
		g.setFont(new Font("Arial", Font.BOLD, 40));
		String s;
		
		// Limpar caminho antigo
		if(this.caminho.size() != 0) {
			for(int i = 3; i < caminho.size()-1; ++i) {
				if(caminho.get(i) != grafo.acidente) {
					int x = caminho.get(i)%10;
					int y = caminho.get(i)/10;
					int valor = caminho.get(i);
					g.setColor(Color.WHITE);
					g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
					
					g.setColor(Color.BLACK);
					g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
					
					s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
					g.drawString(s, x*160 + 65, y*150 + 95);
				}
			}
		}
		
		caminho = grafo.menorCaminho();
		
		if(caminho.get(0) == -1) {
			this.mensagemErro("Não há caminho entre o hospital e o acidente");
			return;
		}
		
		if(caminho.get(0) == -2) {
			this.mensagemErro("Não há caminho entre o acidente e o hospital");
			return;
		}
		
		int valor = caminho.get(2);
		g.setColor(Color.WHITE);
		g.fillRect(1645, 50, 265, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 49, 266, 101);
		g.drawString("INÍCIO", 1695, 90);
		s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
		g.drawString(s, 1735, 140);
		
		valor = caminho.get(caminho.size()-1);
		g.setColor(Color.WHITE);
		g.fillRect(1645, 170, 265, 100);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 169, 266, 101);
		g.drawString("FIM", 1720, 205);
		s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
		g.drawString(s, 1735, 260);
		
		valor = caminho.get(0);
		g.setColor(Color.WHITE);
		g.fillRect(1645, 290, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 289, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 330);
		g.drawString("INICIAL", 1690, 380);
		if(valor < 10)
			s = "00" + String.valueOf(valor);
		else if (valor >= 10 && valor < 100)
			s = "0" + String.valueOf(valor);
		else
			s = String.valueOf(valor);
		g.drawString(s, 1725, 430);
		
		valor = caminho.get(1);
		g.setColor(Color.WHITE);
		g.fillRect(1645, 460, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 459, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 500);
		g.drawString("FINAL", 1700, 550);
		if(valor < 10)
			s = "00" + String.valueOf(valor);
		else if (valor >= 10 && valor < 100)
			s = "0" + String.valueOf(valor);
		else
			s = String.valueOf(valor);
		g.drawString(s, 1725, 600);
		
		valor = caminho.get(0) + caminho.get(1);
		g.setColor(Color.WHITE);
		g.fillRect(1645, 630, 265, 150);
		g.setColor(Color.BLACK);
		g.drawRect(1644, 629, 266, 151);
		g.drawString("DISTÂNCIA", 1650, 670);
		g.drawString("TOTAL", 1689, 720);
		if(valor < 10)
			s = "000" + String.valueOf(valor);
		else if (valor >= 10 && valor < 100)
			s = "00" + String.valueOf(valor);
		else if(valor >= 100 && valor < 1000)
			s = "0" + String.valueOf(valor);
		else
			s = String.valueOf(valor);
		
		g.drawString(s, 1710, 770);
		
		for(int i = 3; i < caminho.size()-1; ++i) {
			if(caminho.get(i) != grafo.acidente) {
				int x = caminho.get(i)%10;
				int y = caminho.get(i)/10;
				valor = caminho.get(i);
				g.setColor(Color.GREEN);
				g.fillOval(x*160 + 40, y*150 + 30, raio, raio);
				
				g.setColor(Color.BLACK);
				g.drawOval(x*160 + 40, y*150 + 30, raio, raio);
				
				s = valor < 10 ? "0" + String.valueOf(valor) : String.valueOf(valor);
				g.drawString(s, x*160 + 65, y*150 + 95);
			}
		}
	}
	
	public void setComando(int c) {
		this.comando = c;
	}
	
	private int transformarCoordenadaX(int x) {
		return x/((horizontal + espacoHorizontal)/colunas);
	}
	
	private int transformarCoordenadaY(int y) {
		return y/((vertical + espacoVertical)/linhas);
	}
	
	private int numerarArea(int x, int y) {
		return x + 10*y;
	}
	
	private void mensagemErro(String s) {
		JLabel label = new JLabel(s);
		label.setFont(new Font("Arial", Font.BOLD, 18));
		JOptionPane.showMessageDialog(null,label,"ERRO",JOptionPane.WARNING_MESSAGE);
	}
	
	private void desenhaSeta(int valor, int valor2, int dist) {
		Graphics g = getGraphics();
		if(valor2-valor == 10) { // Seta para cima
			MediaTracker media = new MediaTracker(this);
			Image image = Toolkit.getDefaultToolkit().getImage("imgs/Cima.png");
			media.addImage(image, 0);
			try {
	             media.waitForID(0); 
	         }
	        catch (Exception e) {}
			
			g.drawImage(image, tempX*160 + 75, tempY*150 - 18, this);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 13));
			g.drawString(String.valueOf(this.tempDist), tempX*160 + 86, tempY*150 + 11);
		}
		else if(valor-valor2 == 1) { // Seta para direita
			MediaTracker media = new MediaTracker(this);
			Image image = Toolkit.getDefaultToolkit().getImage("imgs/Direita.png");
			media.addImage(image, 0);
			try {
	             media.waitForID(0); 
	         }
	        catch (Exception e) {}
			
			g.drawImage(image, tempX*160 + 150, tempY*150 + 60, this);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 13));
			g.drawString(String.valueOf(this.tempDist),  tempX*160 + 165, tempY*150 + 81);
		}
		else if(valor2-valor == -10) { // Seta para baixo
			MediaTracker media = new MediaTracker(this);
			Image image = Toolkit.getDefaultToolkit().getImage("imgs/Baixo.png");
			media.addImage(image, 0);
			try {
	             media.waitForID(0); 
	         }
	        catch (Exception e) {}
			
			g.drawImage(image, tempX*160 + 75, tempY*150 + 132, this);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 13));
			g.drawString(String.valueOf(this.tempDist), tempX*160 + 87, tempY*150 + 155);
		}
		else if(valor-valor2 == -1) { // Seta para esquerda
			MediaTracker media = new MediaTracker(this);
			Image image = Toolkit.getDefaultToolkit().getImage("imgs/Esquerda.png");
			media.addImage(image, 0);
			try {
	             media.waitForID(0); 
	         }
	        catch (Exception e) {}
			
			g.drawImage(image, tempX*160 - 15, tempY*150 + 60, this);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 13));
			g.drawString(String.valueOf(this.tempDist),  tempX*160 + 5, tempY*150 + 82);
		}
	}
	
	private void pedeValor() {
		JFrame f = new JFrame();
		f = new JFrame();
		JDialog d = new JDialog(f , "Distância", true);
		JLabel l = new JLabel("         Insira a distância");
		l.setFont(new Font("Arial", Font.BOLD, 18));
		JTextField t = new JTextField(2); 
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3,1));
        JLabel alert = new JLabel("");
        
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		int valor = Integer.parseInt(t.getText());
            		if(valor <= 0 || valor >= 10) {
            			alert.setText("             Número deve estar entre 1 e 9");
            		}
            		else {
            			mapa.tempDist = valor;
            			d.dispose();
            		}
        		} catch (NumberFormatException erro) {
        			alert.setText("            Deve ser um número (entre 1 e 9)");
        		}
            }
        };
        
        t.addActionListener(action);

        p.add(l, BorderLayout.CENTER); 
        p.add(t, BorderLayout.CENTER);
        p.add(alert, BorderLayout.CENTER);

        d.add(p); 

        d.setSize(250, 120);
        d.setLocationRelativeTo(null);
  
        d.setVisible(true);
	}
	
	public void open() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Apenas .pa2", "pa2"));
		int retorno = chooser.showOpenDialog(null);
		if (retorno == JFileChooser.APPROVE_OPTION) {
			this.novoMapa();
			this.lendoArquivo = true;
			File file = chooser.getSelectedFile();
			FileReader arq;
			try {
				arq = new FileReader(file);
				BufferedReader lerArq = new BufferedReader(arq);
				Scanner lerDados = new Scanner(lerArq);
				
				// Recria hospitais
				int quantidade = lerDados.nextInt();
				for(int i = 0; i < quantidade; ++i)  {
					int valor = lerDados.nextInt();
					this.createHospital((valor%10)*((horizontal + espacoHorizontal)/colunas),
										(valor/10)*((vertical + espacoVertical)/linhas));
				}
				
				// Recria áreas inválidas
				quantidade = lerDados.nextInt();
				for(int i = 0; i < quantidade; ++i)  {
					int valor = lerDados.nextInt();
					this.apagarLocal((valor%10)*((horizontal + espacoHorizontal)/colunas),
							(valor/10)*((vertical + espacoVertical)/linhas));
				}
				
				// Adiciona distâncias
				
				while(lerDados.hasNextInt()) {
					int a = lerDados.nextInt();
					int b = lerDados.nextInt();
					this.tempDist = lerDados.nextInt();

					this.criarDistancia((a%10)*((horizontal + espacoHorizontal)/colunas),
							(a/10)*((vertical + espacoVertical)/linhas));
					
					this.criarDistancia((b%10)*((horizontal + espacoHorizontal)/colunas),
							(b/10)*((vertical + espacoVertical)/linhas));
				}
				
				// closes
				lerDados.close();
				lerArq.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException erroAbertura) {
				erroAbertura.printStackTrace();
		    }
			catch (IllegalArgumentException erro) {
				erro.printStackTrace();
			}

		}
		
		this.lendoArquivo = false;
	}
}