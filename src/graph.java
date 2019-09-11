import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class graph {
	private int [][] distancia = new int [60][60];
	private ArrayList<Integer> hospitais = new ArrayList<Integer>();
	private ArrayList<Integer> invalidos = new ArrayList<Integer>();
	public int acidente = -1;
	
	public graph() {
		for(int i = 0; i < 60; ++i)
			for(int j = 0; j < 60; ++j)
				distancia[i][j] = 0; // 0 = não conectado
	}
	
	/***************   Usado por "Criar Hospital"   ***************/
	
	public boolean isHospital(Integer h) {
		return this.hospitais.contains(h);
	}
	
	public void adicionarHospital(Integer h) {
		this.hospitais.add(h);
	}
	
	public void removerHospital(Integer h) {
		this.hospitais.remove(h);
	}
	
	/***************   Usado por "Apagar Local"    ***************/

	public void setInvalido(Integer area) {
		int x = this.converterParaX(area);
		int y = this.converterParaY(area);

		if(x > 0) { // Se existir esquerda
			this.setDistancia(area, area-1, 0);
			this.setDistancia(area-1, area, 0);
		}
		if(x < 9) { // Se existir direita
			this.setDistancia(area, area+1, 0);
			this.setDistancia(area+1, area, 0);
		}
		if(y > 0) { // Se existir em cima
			this.setDistancia(area, area-10, 0);
			this.setDistancia(area-10, area, 0);
		}
		if(y < 5) { // Se existir em baixo
			this.setDistancia(area, area+10, 0);
			this.setDistancia(area+10, area, 0);
		}
		
		if(this.isHospital(area)) {
			this.hospitais.remove(area);
		}
		
		if(this.acidente == area) {
			this.acidente = -1;
		}
		
		this.invalidos.add(area);
	}
	
	public boolean isInvalido(Integer a) {
		return this.invalidos.contains(a);
	}
	
	public void removerInvalido(Integer a) {
		this.invalidos.remove(a);
	}
	
	public void setDistancia(int src, int dest, int dist) {
		distancia[src][dest] = dist;
	}
	
	public int getDistancia(int src, int dest) {
		return distancia[src][dest];
	}
	
	public boolean verificarValidade(int src, int dest) {
		int srcX = this.converterParaX(src);
		int srcY = this.converterParaY(src);
		int destX = this.converterParaX(dest);
		int destY = this.converterParaY(dest);
		
		if(Math.abs(srcX-destX) <= 1 &&
		   Math.abs(srcY-destY) <= 1 && src != dest &&
		   Math.abs(srcX-destX) + Math.abs(srcY-destY) <= 1)
			return true;
		
		return false;
	}
	
	private int converterParaX(int area) {
		return area%10;
	}
	
	private int converterParaY(int area) {
		return area/10;
	}
	
	public ArrayList<Integer> menorCaminho() {
		ArrayList<Integer> hospAcidente = new ArrayList<Integer>();
		int tempDist = 999999999;
		
		// Detecta o hospital mais perto
		for(int i = 0; i < hospitais.size(); ++i) {
			ArrayList<Integer> temp = this.HospAcidente(hospitais.get(i));
			if(temp.get(0) < tempDist) {
				tempDist = temp.get(0);
				hospAcidente = temp;
			}
		}

		// Se não há caminho do hospital para o acidente
		if(tempDist == 999999999) {
			ArrayList<Integer> semCaminho = new ArrayList<Integer>();
			semCaminho.add(-1);
			return semCaminho;
		}

		ArrayList<Integer> acidHospital = this.AcidHospital(acidente);
		
		if(acidHospital.get(0) == -1) {
			ArrayList<Integer> semCaminho = new ArrayList<Integer>();
			semCaminho.add(-2);
			return semCaminho;
		}
		
		ArrayList<Integer> completePath = new ArrayList<Integer>();
		completePath.add(hospAcidente.get(0)); // completePath[0] = distancia hospital-acidente
		completePath.add(acidHospital.get(0)); // completePath[1] = distancia acidente-hospital
		
		for(int i = 1; i < hospAcidente.size(); ++i) {
			completePath.add(hospAcidente.get(i));
		}
		
		for(int i = 2; i < acidHospital.size(); ++i) {
			completePath.add(acidHospital.get(i));
		}
		
		return completePath;
	}
	
	private ArrayList<Integer> HospAcidente(int src) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int dist[] = new int[60];
        int pred[] = new int[60];
        Boolean sptSet[] = new Boolean[60];
        
        for (int i = 0; i < 60; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            pred[i] = -1;
            sptSet[i] = false; 
        } 

        dist[src] = 0;
        pred[src] = src;
        
        for (int count = 0; count < 60 - 1; count++) { 
            int u = minDistance(dist, sptSet); 

            sptSet[u] = true; 
 
            for (int v = 0; v < 60; v++) 
                if (!sptSet[v] && distancia[u][v] != 0 &&  
                   dist[u] != Integer.MAX_VALUE && dist[u] + distancia[u][v] < dist[v]) {
                	 dist[v] = dist[u] + distancia[u][v];
                	 pred[v] = u;
                } 
        }
        
        if(dist[this.acidente] == Integer.MAX_VALUE) {
        	path.add(1000000000);
        	return path;
        }
        
        int p = this.acidente;
        while(p != src) {
    		path.add(p);
    		p = pred[p];
    	}
    	
    	path.add(src);
    	path.add(dist[this.acidente]);
    	
    	Collections.reverse(path);
        
		return path;
	}
	
	private ArrayList<Integer> AcidHospital(int src) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int dist[] = new int[60];
        int pred[] = new int[60];
        Boolean sptSet[] = new Boolean[60];
        
        for (int i = 0; i < 60; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            pred[i] = -1;
            sptSet[i] = false; 
        } 

        dist[src] = 0;
        pred[src] = src;
        
        for (int count = 0; count < 60 - 1; count++) { 
            int u = minDistance(dist, sptSet); 

            sptSet[u] = true; 
 
            for (int v = 0; v < 60; v++) 
                if (!sptSet[v] && distancia[u][v] != 0 &&  
                   dist[u] != Integer.MAX_VALUE && dist[u] + distancia[u][v] < dist[v]) {
                	 dist[v] = dist[u] + distancia[u][v];
                	 pred[v] = u;
                } 
        }
        
        int p = -1;
        int menorDist = 999999999;
        
        for(int i = 0; i < hospitais.size(); ++i) {
        	if(dist[hospitais.get(i)] < menorDist) {
        		menorDist = dist[hospitais.get(i)];
        		p = hospitais.get(i);
        	}
        }
        
        if(p == -1) {
        	path.add(-1);
        	return path;
        }
        
        while(p != src) {
    		path.add(p);
    		p = pred[p];
    	}
    	
    	path.add(src);
    	path.add(menorDist);
    	
    	Collections.reverse(path);
        
		return path;
	}
	
	private int minDistance(int dist[], Boolean sptSet[]) { 
        int min = Integer.MAX_VALUE, min_index = -1; 
  
        for (int v = 0; v < 60; v++) 
            if (sptSet[v] == false && dist[v] <= min) { 
                min = dist[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    }
	
	public int quantidadeHospitais() {
		return this.hospitais.size();
	}
	
	public Integer hospitalAt(int i) {
		return this.hospitais.get(i);
	}
	
	public int quantidadeInvalidos() {
		return this.invalidos.size();
	}
	
	public Integer invalidosAt(int i) {
		return this.invalidos.get(i);
	}
	
	public void save() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Apenas .pa2", "pa2"));
		int retorno = chooser.showSaveDialog(null);
		if (retorno == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				file = new File(file.getPath() + ".pa2");
				file.createNewFile();
				FileWriter arq = new FileWriter(file.getPath());
				PrintWriter gravarArq = new PrintWriter(arq);
				
				// Grava hospitais
				gravarArq.println(this.hospitais.size());
				for(int i = 0; i < this.hospitais.size(); ++i)
					gravarArq.println(this.hospitais.get(i));
				
				// Grava inválidos
				gravarArq.println(this.invalidos.size());
				for(int i = 0; i < this.invalidos.size(); ++i)
					gravarArq.println(this.invalidos.get(i));
				
				// Grava grafo
				for(int i = 0; i < 60; ++i) {
					for(int j = 0; j < 60; ++j) {
						if(distancia[i][j] != 0) {
							gravarArq.println(i + " " + j + " " + distancia[i][j]);
						}
					}
				}
				
				gravarArq.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}