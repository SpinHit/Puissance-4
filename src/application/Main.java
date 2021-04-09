package application;
	
// Librairies
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

	private static int nombreTour;
	
	private Cellule[][] Cellules;
	
	public void start(Stage primaryStage) {
		try {
			
			
			//Le plateau de jeu:
			//comporte 6 cellules en largeur
			//comporte 7 cellules en hauteur
			//commen le nom l'indique c'est un puissance 4 alors on doit aligner 4 jetons de même couleur pour gagner la partie
			
			// La couleur de fond:
			Color couleurFond = Color.BLACK;
			
			// La fenêtre 
			Group root = new Group();
			Scene scene = new Scene(root,1100,600 + 0);
			scene.setFill(couleurFond);
			
			
			// initialisation de La grill--e:
			Rectangle r = new Rectangle(0, 0, 700, 600);                                          //couleur RoyalBlue
			LinearGradient lg = new LinearGradient(0,0,1,1, true, CycleMethod.NO_CYCLE, new Stop(0.99, Color.rgb(65, 105, 225)));
			r.setFill(lg);
			root.getChildren().addAll(r);
			
           // cercles noirs pour la grille
			for(int i = 0 ; i < 6 ; i++){
				for (int j = 0 ; j < 7 ; j++){
					Circle cercleNoir = new Circle(5 +45 + 100*j, 5+45 + 100*i, 45);
					cercleNoir.setFill(couleurFond);
					cercleNoir.radiusProperty().bind(r.heightProperty().divide(12).subtract(5));
					cercleNoir.centerXProperty().bind(r.widthProperty().divide(7).multiply(j+0.5));
					cercleNoir.centerYProperty().bind(r.heightProperty().divide(6).multiply(i+0.5));
					root.getChildren().add(cercleNoir);
				}
			}
			
			
			// La création des Cellules:
			Cellules = new Cellule[7][6];	
			for(int i = 0 ; i < 6 ; i++){
				for (int j = 0 ; j < 7 ; j++){
					Cellules[j][i] = new Cellule();
					Cellules[j][i].layoutXProperty().bind(r.widthProperty().divide(7).multiply(j));
					Cellules[j][i].layoutYProperty().bind(r.heightProperty().divide(6).multiply(i));
					Cellules[j][i].fitHeightProperty().bind(r.heightProperty().divide(6));
					Cellules[j][i].fitWidthProperty().bind(r.widthProperty().divide(7));
					
					root.getChildren().add(Cellules[j][i]);
				}
			}
			
			
			
			// Les textes:
			
			Label victoire = new Label("");
			victoire.setTextFill(Color.WHITE);
			victoire.setFont(Font.font(scene.getHeight() / 20));
			victoire.setLayoutX(750);
			victoire.setVisible(false);
			

			Label joueur = new Label("Rouge joue");
			joueur.setTextFill(Color.WHITE);
			joueur.setFont(Font.font(scene.getHeight() / 20));
			joueur.setLayoutX(750);
			
			
			Label tour = new Label("Tour n'1");
			tour.setTextFill(Color.WHITE);
			tour.setFont(Font.font(scene.getHeight() / 25));
			tour.setLayoutX(1000);
			
			root.getChildren().addAll(tour, joueur, victoire);
			
	
			//  selectionner :
			
			Rectangle r2 = new Rectangle(0,0,10,10);
			r2.heightProperty().bind(r.heightProperty());
			r2.widthProperty().bind(r.widthProperty());
			r2.setFill(Color.TRANSPARENT);
			root.getChildren().addAll(r2);
			
		
			nombreTour = 1;
			
			// click:
			r2.setOnMouseClicked(e -> {			
				
				int colonne = (int)(e.getX() / (r.getWidth() / 7));
				
				// Le placement du jeton:
				if(Cellules[colonne][0].getStatut() == 0 && !victoire.isVisible()){
					
					int rang = 6-1;
					while(Cellules[colonne][rang].getStatut() != 0){
						rang--;
					}
					Cellules[colonne][rang].set(nombreTour%2==1 ? 1 : 2);
					
					
					//définir les condition pour la victoire:
					
					
					// a qui le tour ? :
					int couleur = (nombreTour%2==1 ? 1 : 2);
					
					//nombre alignés maximal: 
					int max = 0;
					int x; int y;
					int somme;
					
					
					// Le check verticale:
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && Cellules[x][y].getStatut() == couleur){ y--; somme++;}
					y = rang;
					while(y < 6 && Cellules[x][y].getStatut() == couleur){ y++; somme++;}
					if(somme > max) max= somme;
					
					// Le check horizontale:
					x = colonne; y = rang; somme=-1;
					while(x >= 0 && Cellules[x][y].getStatut() == couleur){ x--; somme++;}
					x = colonne;
					while(x < 7 && Cellules[x][y].getStatut() == couleur){ x++; somme++;}
					if(somme > max) max= somme;
					
					
					// Le check de la diagonale en Haut a gauche -- en bas a droite
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && x >= 0 && Cellules[x][y].getStatut() == couleur){ y--; x--; somme++;}
					x = colonne; y = rang;
					while(y < 6 && x < 7 && Cellules[x][y].getStatut() == couleur){ y++; x++; somme++;}
					if(somme > max) max= somme;
					
					// Le check de la diagonale en Haut a droite -- en bas a gauche
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && x < 7 && Cellules[x][y].getStatut() == couleur){ y--; x++; somme++;}
					x = colonne; y = rang;
					while(y < 6 && x >= 0 && Cellules[x][y].getStatut() == couleur){ y++; x--; somme++;}
					if(somme > max) max= somme;
				//	Victoire
					if(max >= 4){
						joueur.setVisible(false);
						victoire.setVisible(true);
						victoire.setText((couleur == 1 ? "Rouge" : "Jaune") + " gagne !!!");
						nombreTour--;
					}
					
					nombreTour++;
					
					 // égaliter
					if(nombreTour > 7*6 && max < 4){
						joueur.setVisible(false);
						victoire.setVisible(true);
						victoire.setText("EGALITE !!!");
						nombreTour--;
					}
					// tour n'...
					tour.setText("Tour n'" + nombreTour);
					joueur.setText((nombreTour%2 == 1 ? "Rouge" : "Jaune") + " joue");
					
				}
				
				
			});
			
			
			primaryStage.setTitle("Puissance 4                                                   Iliass Ben ammar 18908184");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}