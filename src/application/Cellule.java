package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cellule extends ImageView{
	
	private static Image rouge = new Image("jetonRouge.png");
	private static Image jaune = new Image("jetonJaune.png");
	
	
	//vide = 0 ; rouge = 1 ; jaune = 2;
	private int statut;
	
	public Cellule(){
		this.statut = 0;
	}
	
	public void set(int j){
		this.setImage(j == 1 ? rouge : jaune);
		this.statut = j;
	}
	
	
	public int getStatut(){
		return statut;
	}
	
}