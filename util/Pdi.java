package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {

	public static Image escalaDeCinza(Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);
					double media = (corA.getRed()+corA.getBlue()+corA.getGreen()) / 3;
					Color corN = new Color(media, media, media ,corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image escalaDeCinzaPonderada(Image imagem, int pcr, int pcg, int pcb) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);
					
					double media = ((corA.getRed()*pcr) + (corA.getBlue()*pcb) + (corA.getGreen()*pcg)) / 100;
					
					Color corN = new Color(media, media, media ,corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image negativa(Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);
					
					double r  = 1 - corA.getRed();
					double g  = 1 - corA.getGreen();
					double b  = 1 - corA.getBlue();
					
					Color corN = new Color(r, g, b ,corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image limiarizacao(Image imagem, double limite) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);
					
					double r;
					double g;
					double b;
					
					if(1 - corA.getRed() >= limite) {
						r = 1;
					}else {
						r = 0;
					}
					
					if(1 - corA.getGreen() >= limite) {
						g = 1;
					}else {
						g = 0;
					}
					
					if(1 - corA.getBlue() >= limite) {
						b = 1;
					}else {
						b = 0;
					}
					
					
					Color corN = new Color(r, g, b,corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image reducaoDeRuidoEmX(Image imagem, boolean mediana) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			if(mediana) {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color topLeft = pr.getColor(i-1, j-1);
						Color bottomLeft = pr.getColor(i+1, j-1);
						Color topRight = pr.getColor(i-1, j+1);
						Color bottomRight = pr.getColor(i+1, j+1);
						
						ArrayList<Color> cores = new ArrayList<Color>() {{
						    add(center);
						    add(topLeft);
						    add(bottomLeft);
						    add(topRight);
						    add(bottomRight);
						}};

						Color corN = medianaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}else {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color topLeft = pr.getColor(i-1, j-1);
						Color bottomLeft = pr.getColor(i+1, j-1);
						Color topRight = pr.getColor(i-1, j+1);
						Color bottomRight = pr.getColor(i+1, j+1);
						
						ArrayList<Color> cores = new ArrayList<Color>() {
							private static final long serialVersionUID = 1L;
						{
						    add(center);
						    add(topLeft);
						    add(bottomLeft);
						    add(topRight);
						    add(bottomRight);
						}};

						Color corN = mediaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}
			
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image reducaoDeRuidoEmCruz(Image imagem, boolean mediana) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			if(mediana) {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color top = pr.getColor(i-1, j);
						Color bottom = pr.getColor(i+1, j);
						Color left = pr.getColor(i, j-1);
						Color right = pr.getColor(i, j+1);
						
						ArrayList<Color> cores = new ArrayList<Color>() {{
						    add(center);
						    add(top);
						    add(bottom);
						    add(left);
						    add(right);
						}};

						Color corN = medianaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}else {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color top = pr.getColor(i-1, j);
						Color bottom = pr.getColor(i+1, j);
						Color left = pr.getColor(i, j-1);
						Color right = pr.getColor(i, j+1);
						
						ArrayList<Color> cores = new ArrayList<Color>() {{
						    add(center);
						    add(top);
						    add(bottom);
						    add(left);
						    add(right);
						}};

						Color corN = mediaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}
			
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image reducaoDeRuido3x3(Image imagem, boolean mediana) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			if(mediana) {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color top = pr.getColor(i-1, j);
						Color bottom = pr.getColor(i+1, j);
						Color left = pr.getColor(i, j-1);
						Color right = pr.getColor(i, j+1);
						Color topLeft = pr.getColor(i-1, j-1);
						Color bottomLeft = pr.getColor(i+1, j-1);
						Color topRight = pr.getColor(i-1, j+1);
						Color bottomRight = pr.getColor(i+1, j+1);
						
						
						ArrayList<Color> cores = new ArrayList<Color>() {{
						    add(top);
						    add(bottom);
						    add(left);
						    add(right);
						    add(center);
						    add(topLeft);
						    add(bottomLeft);
						    add(topRight);
						    add(bottomRight);
						}};

						Color corN = medianaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}else {
				for(int i=1; i<w-1; i++) {
					for(int j=1; j<h-1; j++) {
						
						Color center = pr.getColor(i, j);
						Color top = pr.getColor(i-1, j);
						Color bottom = pr.getColor(i+1, j);
						Color left = pr.getColor(i, j-1);
						Color right = pr.getColor(i, j+1);
						Color topLeft = pr.getColor(i-1, j-1);
						Color bottomLeft = pr.getColor(i+1, j-1);
						Color topRight = pr.getColor(i-1, j+1);
						Color bottomRight = pr.getColor(i+1, j+1);
						
						
						ArrayList<Color> cores = new ArrayList<Color>() {{
						    add(top);
						    add(bottom);
						    add(left);
						    add(right);
						    add(center);
						    add(topLeft);
						    add(bottomLeft);
						    add(topRight);
						    add(bottomRight);
						}};


						Color corN = mediaCores(cores);
						
						pw.setColor(i, j, corN);
					}
				}
			}
			
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public static Image adicao(Image imagem1, Image imagem2, double percent1, double percent2) {
		try {
			int w1 = (int)imagem1.getWidth();
			int h1 = (int)imagem1.getHeight();
			int w2 = (int)imagem2.getWidth();
			int h2 = (int)imagem2.getHeight();
			
			int w = Math.min(w1, w2);
			int h = Math.min(h1, h2);
			
			
			PixelReader pr1 = imagem1.getPixelReader();
			PixelReader pr2 = imagem2.getPixelReader();
			
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
		
			
			for(int i=1; i<w-1; i++) {
				for(int j=1; j<h-1; j++) {
					Color corImg1 = pr1.getColor(i, j);
					Color corImg2 = pr2.getColor(i, j);
					
					double r = (corImg1.getRed() * percent1) + (corImg2.getRed() * percent2);
					double g = (corImg1.getGreen() * percent1) + (corImg2.getGreen() * percent2);
					double b = (corImg1.getBlue() * percent1) + (corImg2.getBlue() * percent2);
					
					r = r > 1 ? 1 : r;
					g = g > 1 ? 1 : g;
					b = b > 1 ? 1 : b;
					
					Color newColor = new Color(r, g, b, corImg1.getOpacity());
					pw.setColor(i, j, newColor);
				}
			}
			
			return wi;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Image subtracao(Image imagem1, Image imagem2) {
		try {
			int w1 = (int)imagem1.getWidth();
			int h1 = (int)imagem1.getHeight();
			int w2 = (int)imagem2.getWidth();
			int h2 = (int)imagem2.getHeight();
			
			int w = Math.min(w1, w2);
			int h = Math.min(h1, h2);
			
			
			PixelReader pr1 = imagem1.getPixelReader();
			PixelReader pr2 = imagem2.getPixelReader();
			
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
		
			
			for(int i=1; i<w-1; i++) {
				for(int j=1; j<h-1; j++) {
					Color corImg1 = pr1.getColor(i, j);
					Color corImg2 = pr2.getColor(i, j);
					
					double r = corImg1.getRed() - corImg2.getRed();
					double g = corImg1.getGreen() - corImg2.getGreen();
					double b = corImg1.getBlue() - corImg2.getBlue();
					
					r = r < 0 ? 0 : r;
					g = g < 0 ? 0 : g;
					b = b < 0 ? 0 : b;
					
					Color newColor = new Color(r, g, b, corImg1.getOpacity());
					pw.setColor(i, j, newColor);
				}
			}
			
			return wi;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	

	public static Color mediaCores(ArrayList<Color> colors) {
		int size = colors.size();
		
		double r = 0;
		double g = 0;
		double b = 0;
		double opacity = 0;
		
		for (Color color : colors) {
			r += color.getRed();
			g += color.getGreen();
			b += color.getBlue();
			opacity += color.getOpacity();
		}
		
		Color media = new Color(r/size, g/size, b/size, opacity/size);
		
		return media;
	}
	
	public static Color medianaCores(ArrayList<Color> colors) {
		int size = colors.size();
		
		ArrayList<Double> r = new ArrayList<>();
		ArrayList<Double> g = new ArrayList<>();
		ArrayList<Double> b = new ArrayList<>();
		ArrayList<Double> opacity = new ArrayList<>();
		
		
		for (Color color : colors) {
			r.add(color.getRed());
			g.add(color.getGreen());
			b.add(color.getBlue());
			opacity.add(color.getOpacity());
		}
		
		
		Color mediana = new Color(mediana(r), mediana(g), mediana(b), mediana(opacity));
		
		return mediana;
	}
	
	public static double mediana(ArrayList<Double> numeros) {
		double mediana;
		int size = numeros.size();
		
		if (size == 1) return numeros.get(0);
		
		Collections.sort(numeros);
		if(size % 2 == 0) {
			int metade = size/2;
			
			mediana = (numeros.get(metade) + numeros.get(metade + 1))/2;
		} else {
			int metade = size/2;
			
			mediana = numeros.get(metade); 
		}
		
		return mediana;
	}



	public static void getGrafico(Image imagem,BarChart<String, Number> grafico){
		try {
			
			int[] hist = histogramaUnico(imagem);
			int[] histR = histograma(imagem,0);
			int[] histG = histograma(imagem,1);
			int[] histB = histograma(imagem,2);
			
			XYChart.Series vlr = new XYChart.Series();
			XYChart.Series vlrR = new XYChart.Series();
			XYChart.Series vlrG = new XYChart.Series();
			XYChart.Series vlrB = new XYChart.Series();
			
			for (int i=0; i<255; i++) {
				vlr.getData().add(new XYChart.Data(i+"", hist[i]));
				
				vlrR.getData().add(new XYChart.Data(i+"", histR[i]));
				vlrG.getData().add(new XYChart.Data(i+"", histG[i]));
				vlrB.getData().add(new XYChart.Data(i+"", histB[i]));
			}
			
			grafico.getData().addAll(vlrR, vlrG, vlrB);
			
			for(Node n:grafico.lookupAll(".default-color0.chart-bar")) {
				n.setStyle("-fx-bar-fill: red;");
			}
			for(Node n:grafico.lookupAll(".default-color1.chart-bar")) {
				n.setStyle("-fx-bar-fill: green;");
			}
			for(Node n:grafico.lookupAll(".default-color2.chart-bar")) {
				n.setStyle("-fx-bar-fill: blue;");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static int[] histogramaUnico(Image imagem) {
		try {
			int[] histograma = new int[255];
			
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i, j);
					
					int red = ((int)(corA.getRed()*255)) -1;
					int blue = ((int)(corA.getBlue()*255)) -1;
					int green = ((int)(corA.getGreen()*255)) -1;
					
				    histograma[red < 0 ? 0: red] += 1;
				    histograma[blue < 0 ? 0: blue] += 1;
				    histograma[green < 0 ? 0: green] += 1;
				}
			}
						
			return histograma;	
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static int[] histograma(Image imagem, int tipo) {
		try {
			int[] histograma = new int[255];
			
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			
			if(tipo == 0) {
				for(int i=0; i<w; i++) {
					for(int j=0; j<h; j++) {
						Color corA = pr.getColor(i, j);	
						int red = ((int)(corA.getRed()*255)) -1;
					    histograma[red < 0 ? 0: red] += 1;

					}
				}
				
			}else if(tipo == 1) {
				for(int i=0; i<w; i++) {
					for(int j=0; j<h; j++) {
						Color corA = pr.getColor(i, j);
					
						int green = ((int)(corA.getGreen()*255)) -1;
					    histograma[green < 0 ? 0: green] += 1;
					}
				}
			}else {
				for(int i=0; i<w; i++) {
					for(int j=0; j<h; j++) {
						Color corA = pr.getColor(i, j);

						int blue = ((int)(corA.getBlue()*255)) -1;
					    histograma[blue < 0 ? 0: blue] += 1;
					}
				}
			}
						
			return histograma;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static int[] histogramaAcumulado(int[] histograma) {
		int[] histogramaAcumulado = new int[255];
		int last = 0;
		
		for(int i = 0; i < histograma.length; i++) {
			histogramaAcumulado[i] = last + histograma[i];
			
			last = histogramaAcumulado[i];
		}
		
		return histogramaAcumulado;
	}

	public static int quantidadeTons(int[] histograma) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int qntTons = 0;
	
		
		for(int i = 0; i < histograma.length; i++) {
			
			if(!list.contains(histograma[i])) {
				qntTons ++;
			}
			
			list.add(histograma[i]);
		}
		
		return qntTons;
	}
	
	public static int pontoMinimo(int[] histograma) {
		int minimo = 0;
		
		for(int i = 0; i < histograma.length; i++) {
			if(histograma[i] != 0) {
				if(minimo == 0) {
					minimo = histograma[i];
				}
				
				if(minimo > histograma[i]) {
					minimo = histograma[i];
				}
			}
		}
		
		return minimo;
	}
	
	public static Image equalizacaoHistograma(Image imagem, boolean valid) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			
			int[] histR = histograma(imagem,0);
			int[] histG = histograma(imagem,1);
			int[] histB = histograma(imagem,2);
			
			int[] histAcR = histogramaAcumulado(histR);
			int[] histAcG = histogramaAcumulado(histG);
			int[] histAcB = histogramaAcumulado(histB);
			
			
			int qntR = 255;
			int qntG = 255;
			int qntB = 255;
			
			int minR = 0;
			int minG = 0;
			int minB = 0;
			
			
			if(valid) {
				qntR = quantidadeTons(histR);
				qntG = quantidadeTons(histG);
				qntB = quantidadeTons(histB);
				
				minR = pontoMinimo(histR);
				minG = pontoMinimo(histG);
				minB = pontoMinimo(histB);
			}
			
			
			double n = w*h;
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					
					Color cor = pr.getColor(i, j);
					
					double acR = histAcR[(int)(cor.getRed()*255)];
					double acG = histAcR[(int)(cor.getGreen()*255)];
					double acB = histAcR[(int)(cor.getBlue()*255)];
					
					double colorR = (minR + (((qntR-1)/ n) *acR))/255;
					double colorG = (minG + (((qntG-1)/ n) *acG))/255;
					double colorB = (minB + (((qntB-1)/ n) *acB))/255;
					
					Color corN = new Color(colorR, colorG, colorB,cor.getOpacity());
					
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	public static Image filtroNaSelecao(Image imagem, int p1x, int p1y, int p2x, int p2y) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			
			int pyMaior;
			int pyMenor;
			
			int pxMaior;
			int pxMenor;
			
			
			if(p1y > p2y) {
				pyMaior = p1y;
				pyMenor = p2y;
			}else {
				pyMaior = p2y;
				pyMenor = p1y;
			}
			
			if(p1x > p2x) {
				pxMaior = p1x;
				pxMenor = p2x;
			}else {
				pxMaior = p2x;
				pxMenor = p1x;
			}
						
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corOriginal = pr.getColor(i, j);
										
					if((i <= pxMaior && i >= pxMenor) && (j <= pyMaior && j >= pyMenor)) {
						double r  = 1 - corOriginal.getRed();
						double g  = 1 - corOriginal.getGreen();
						double b  = 1 - corOriginal.getBlue();
						
						Color corN = new Color(r, g, b ,corOriginal.getOpacity());
						pw.setColor(i, j, corN);
					}else {
						pw.setColor(i, j, corOriginal);
					}
					
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image marcarSelecao(Image imagem, int p1x, int p1y, int p2x, int p2y) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			
			int pyMaior;
			int pyMenor;
			
			int pxMaior;
			int pxMenor;
			
			
			if(p1y > p2y) {
				pyMaior = p1y;
				pyMenor = p2y;
			}else {
				pyMaior = p2y;
				pyMenor = p1y;
			}
			
			if(p1x > p2x) {
				pxMaior = p1x;
				pxMenor = p2x;
			}else {
				pxMaior = p2x;
				pxMenor = p1x;
			}
						
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corOriginal = pr.getColor(i, j);
					Color corN = new Color(1, 0, 0 , 1);
					
					
					if((i == pxMaior || i == pxMenor) && (j <= pyMaior && j >= pyMenor)) {
						pw.setColor(i, j, corN);
						
					}else if((j == pyMaior || j == pyMenor) && (i <= pxMaior && i >= pxMenor)){
						pw.setColor(i, j, corN);
						
					}else {
						pw.setColor(i, j, corOriginal);
					}
					
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	
	public static Image inverterImagem(Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(w -1 - i, h -1 - j);
					pw.setColor(i, j, corA);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static Image inverterImagemEm4(Image imagem, boolean tl, boolean tr, boolean bl, boolean br) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			int wInvert = w/2;
			int hInvert = h/2;
			
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					
					Color corA = pr.getColor(i, j);
					
					if(tl) {
						
						if(i < wInvert && j < hInvert) {
							corA = pr.getColor(wInvert - i, hInvert - j);
						}
					}
					
					if(tr) {
						if(i > wInvert && j < hInvert) {
							corA = pr.getColor(w + wInvert - i, hInvert - j);
						}
					}
					
					if(bl) {
						if(i < wInvert && j > hInvert) {
							corA = pr.getColor(wInvert - i, h + hInvert - j);
						}
					}
					
					if(br) {
						if(i > wInvert && j > hInvert) {
							corA = pr.getColor(w + wInvert - i, h + hInvert - j);
						}
					}
					
					
					pw.setColor(i, j, corA);
				}
			}
			
			return wi;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}