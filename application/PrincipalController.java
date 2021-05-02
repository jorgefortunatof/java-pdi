package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.*;


public class PrincipalController {
	
	
	@FXML private ToggleGroup vizinhos;
	@FXML private ToggleGroup tipoVizinhos;
	
	@FXML private Pane color;
	
	@FXML private Label lblR;
	@FXML private Label lblG;
	@FXML private Label lblB;
	
	@FXML private Label lblErroMediaPonderada;
	
	@FXML private ImageView imageView1;
	@FXML private ImageView imageView2;
	@FXML private ImageView imageView3;

	@FXML private TextField txtR;
	@FXML private TextField txtG;
	@FXML private TextField txtB;
	
	@FXML private Slider limite;
	
	@FXML private Slider percentImage1;
	@FXML private Slider percentImage2;
	
	@FXML private CheckBox limiarComEscalaDeCinza;
	
	
	@FXML private Label p1x;
	@FXML private Label p1y;
	@FXML private Label p2x;
	@FXML private Label p2y;
	private boolean isP1 = true;

	
	@FXML private CheckBox invertTL;
	@FXML private CheckBox invertTR;
	@FXML private CheckBox invertBL;
	@FXML private CheckBox invertBR;
	
	
	
	@FXML private Label quadradoMensagem;
	
	
	
	private Image image1;
	private Image image2;
	private Image image3;
	
	private File f;
	
	
	private File selecionarImagem() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				"Imagens", "*.jpg", "*.JPG", "*.png", ".PNG", "*.gif", ".GIF", "*.bmp", "*.BMP", "*.jpeg", "*.JPEG"));
		
		File imgSelec = fileChooser.showOpenDialog(null);
		
		try {
			if(imgSelec != null) {
				return imgSelec;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private void verificaCor(Image img, int x, int y) {
		
		try {
		 Color cor = img.getPixelReader().getColor(x-1, y-1);
		 
		 int red = (int) (cor.getRed()*255);
		 int blue = (int) (cor.getBlue()*255);
		 int green = (int) (cor.getGreen()*255);
 
		 lblR.setText("R: " + red);
		 lblB.setText("B: " + blue);
		 lblG.setText("G: " + green);
		 
		 color.setStyle("-fx-background-color: rgb("+red+","+green+","+blue+");");
		}catch(Exception e) {
//			e.printStackTrace();
		}
	}
	
	private void atualizaImagem3() {
		imageView3.setImage(image3);
		imageView3.setFitHeight(image3.getHeight());
		imageView3.setFitWidth(image3.getWidth());
	}
	
	
	@FXML public void abreModalHistograma(ActionEvent event) {
		try {
			Stage stage = new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ModalHistograma.fxml"));
			Parent root = loader.load();
			
			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			stage.initOwner(((Node)event.getSource()).getScene().getWindow());
			stage.show();
			
			ModalHistogramaController controller = (ModalHistogramaController)loader.getController();
			
			
			if (image1 != null) {
				Pdi.getGrafico(image1, controller.grafico1);
			}
			
			if (image2 != null) {
				Pdi.getGrafico(image2, controller.grafico2);
			}

			if (image3 != null) {
				Pdi.getGrafico(image3, controller.grafico3);
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML public void abreImagem1() {
		f = selecionarImagem();
		if(f != null) {
			image1 = new Image(f.toURI().toString());
			imageView1.setImage(image1);
			imageView1.setFitHeight(image1.getHeight());
			imageView1.setFitWidth(image1.getWidth());	
		}
	}
	
	@FXML public void abreImagem2() {
		f = selecionarImagem();
		if(f != null) {
			image2 = new Image(f.toURI().toString());
			imageView2.setImage(image2);
			imageView2.setFitHeight(image2.getHeight());
			imageView2.setFitWidth(image2.getWidth());	
		}
	}
	
	
	

	@FXML public void rasterImg(MouseEvent evt) {
		ImageView iv = (ImageView)evt.getTarget();
		
		if(iv.getImage() != null) {
			verificaCor(iv.getImage(), (int)evt.getX(), (int)evt.getY());
		}
	}
	
	@FXML public void salvar(){
		if (image3 != null){
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("Imagem", "*.png")); 	

			fileChooser.setInitialDirectory(new File("/Users/jorge/Desktop/"));
			
			File file = fileChooser.showSaveDialog(null);
			if (file !=null){
				BufferedImage bImg = SwingFXUtils.fromFXImage(image3, null);
				try {
					ImageIO.write(bImg, "PNG", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("Não há nenhuma imagem modificada");
		}
  }

	
	
	@FXML public void escalaDeCinzaMedia() {
		if(image1 != null) {
			image3 = Pdi.escalaDeCinza(image1);
			atualizaImagem3();	
		}
	}
	
	@FXML public void escalaDeCinzaMediaPonderada() {
		lblErroMediaPonderada.setText("");
		
		if(image1 != null) {
			int r = 0;
			int g = 0;
			int b = 0;
			
			if(txtR.getText().trim().length() > 0) {
				r = Integer.parseInt(txtR.getText());
			}
			if(txtG.getText().trim().length() > 0) {
				g = Integer.parseInt(txtG.getText());
			}
			if(txtB.getText().trim().length() > 0) {
				b = Integer.parseInt(txtB.getText());
			}
			
			
			if(r+g+b != 100) {
				lblErroMediaPonderada.setText("A soma deve ser igual à 100");
			}else {
				image3 = Pdi.escalaDeCinzaPonderada(image1, r, g, b);
				atualizaImagem3();	
			}
		}
	}

	
	
	@FXML public void negativa() {
		if(image1 != null) {
			image3 = Pdi.negativa(image1);
			atualizaImagem3();	
		}
	}

	@FXML public void limiarizacao() {
		if(image1 != null) {
			
			if(limiarComEscalaDeCinza.isSelected()) {
				image3 = Pdi.escalaDeCinza(image1);
				image3 = Pdi.limiarizacao(image3, limite.getValue()/255);
				
			}else {
				image3 = Pdi.limiarizacao(image1, limite.getValue()/255);
				
			}
			atualizaImagem3();					
		}
	}

	@FXML public void removeRuido() {
		RadioButton vizinho = (RadioButton)vizinhos.getSelectedToggle();
		RadioButton tipoVizinho = (RadioButton)tipoVizinhos.getSelectedToggle();
		
		if(image1 == null) {
			return;
		}
		
		
		switch(vizinho.getText()) {
			case "Vizinhos X":
				System.out.println("Vizinhos X");
				if(tipoVizinho.getText().equalsIgnoreCase("mediana")) {
					System.out.println("mediana");
					image3 = Pdi.reducaoDeRuidoEmX(image1, true);
				}else {
					System.out.println("media");
					image3 = Pdi.reducaoDeRuidoEmX(image1, false);
				}
				break;
				
			case "Vizinhos Cruz":
				System.out.println("Vizinhos Cruz");
				if(tipoVizinho.getText().equalsIgnoreCase("mediana")) {
					System.out.println("mediana");
					image3 = Pdi.reducaoDeRuidoEmCruz(image1, true);
				}else {
					System.out.println("media");
					image3 = Pdi.reducaoDeRuidoEmCruz(image1, false);
				}
				break;
				
			case "Vizinhos 3X3":
				System.out.println("Vizinhos 3X3");
				if(tipoVizinho.getText().equalsIgnoreCase("mediana")) {
					System.out.println("mediana");
					image3 = Pdi.reducaoDeRuido3x3(image1, true);
				}else {
					System.out.println("media");
					image3 = Pdi.reducaoDeRuido3x3(image1, false);
				}
				break;
		}
		
		atualizaImagem3();
	}

	
	
	@FXML public void adicao() {
		if(image1 != null && image2 != null) {
			image3 = Pdi.adicao(image1, image2, percentImage1.getValue()/100, percentImage2.getValue()/100);
			atualizaImagem3();
		}
	}
	
	@FXML public void subtracao() {
		if(image1 != null && image2 != null) {
			image3 = Pdi.subtracao(image1, image2);
			atualizaImagem3();
		}
	}
	
	
	
	@FXML public void equalizacaoHistograma() {
		if(image1 != null) {
			image3 = Pdi.equalizacaoHistograma(image1, true);
			atualizaImagem3();	
		}
	}
	
	@FXML public void equalizacaoHistograma255() {
		if(image1 != null) {
			image3 = Pdi.equalizacaoHistograma(image1, false);
			atualizaImagem3();	
		}
	}
	
	
	
	@FXML public void selecionaPonto(MouseEvent evt) {
		if(isP1) {
			this.p1x.setText((int)evt.getX() + "");
			this.p1y.setText((int)evt.getY() + "");
			this.isP1 = false;
		}else {
			this.p2x.setText((int)evt.getX() + "");
			this.p2y.setText((int)evt.getY() + "");
			this.isP1 = true;
		}
	}
	
	@FXML public void limparSelecao() {
		this.p1x.setText("x");
		this.p1y.setText("y");
		
		this.p2x.setText("x");
		this.p2y.setText("y");
		
		this.isP1 = true;
	}
	
	@FXML public void marcarSelecao() {
		try {
			if(image1 != null) {
				int p1x = Integer.parseInt(this.p1x.getText());
				int p1y = Integer.parseInt(this.p1y.getText());
				
				int p2x = Integer.parseInt(this.p2x.getText());
				int p2y = Integer.parseInt(this.p2y.getText());	
				
				
				image3 = Pdi.marcarSelecao(image1, p1x, p1y, p2x, p2y);
				atualizaImagem3();	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML public void filtroNaSelecao() {
		try {
			if(image1 != null) {
				int p1x = Integer.parseInt(this.p1x.getText());
				int p1y = Integer.parseInt(this.p1y.getText());
				
				int p2x = Integer.parseInt(this.p2x.getText());
				int p2y = Integer.parseInt(this.p2y.getText());	
				
				
				image3 = Pdi.filtroNaSelecao(image1, p1x, p1y, p2x, p2y);
				atualizaImagem3();	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@FXML public void inverterImagem() {
		if(image1 != null) {
			image3 = Pdi.inverterImagem(image1);
			atualizaImagem3();	
		}
	}
	
	@FXML public void inverterImagemLados() {
		if(image1 != null) {
			image3 = Pdi.inverterImagemEm4(image1, 
					this.invertTL.isSelected(), this.invertTR.isSelected(), 
					this.invertBL.isSelected(), this.invertBR.isSelected());
			atualizaImagem3();	
		}
	}



	@FXML public void marcarDiagonal() {
		if(image1 != null) {
			image3 = Pdi.marcarDiagonal(image1);
			atualizaImagem3();	
		}
	}
	
	@FXML public void filtroDiagonal() {
		if(image1 != null) {
			image3 = Pdi.filtroDiagonal(image1);
			atualizaImagem3();	
		}
	}



	@FXML public void analisaQuadrado() {
		boolean aberto = Pdi.analisaQuadrado(image1);
		
		if(aberto) {
			quadradoMensagem.setText("O quadrado é aberto.");
		}else {
			quadradoMensagem.setText("O quadrado é fechado.");
		}
		
	}
}
