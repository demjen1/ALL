package application;
	
import java.util.ArrayList;
import java.util.List;

//import application.Main.PlayGround.Miesto;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	Image board = new Image("img\\board.png");
	ImageView brd = new ImageView(board);
	
	
	
	double width = 400;
	double height = 600;
	
	double sirkaDosky = 400;
	double vyskaDosky = 400;
	
	// velkost kamena vyratana od velkosti dosky
	double sirkaKamena = sirkaDosky / 19.2;
	double vyskaKamena = vyskaDosky / 19.2;

	// stredova pozicia laveho horneho kamena
	double lavyHornyX = sirkaDosky / 32.2;
	double lavyHornyY = vyskaDosky / 32.2;
	
	char[][] pole = new char[19][19];
	ArrayList<ArrayList<Block>> hra = new ArrayList<ArrayList<Block>>();
	int tickTime = 0;
	boolean black = true;
	
	BorderPane root;
	FlowPane top;
	FlowPane bottom;
	PlayGround center;
	
	Button quit;
	Button load;
	Button save;
	Button next;
	Button previous;
	
	@Override
	public void start(Stage primaryStage) {
		
		
		
		
		
			BorderPane root = new BorderPane();
			
			center = new PlayGround();
			//center.setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));

			center.maxHeight(4*(height/6));
			
			
			
			top = new FlowPane();
			top.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
			top.setAlignment(Pos.CENTER);
			top.setHgap(10);
			top.setVgap(10);
			top.setMaxHeight(height/6);
			top.setMinHeight(height/6);
			
			
			
			bottom = new FlowPane();
			bottom.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
			bottom.setAlignment(Pos.CENTER);
			bottom.setHgap(10);
			bottom.setVgap(10);
			bottom.setMaxHeight(height/6);
			bottom.setMinHeight(height/6);
			
			root.setBottom(bottom);
			root.setTop(top);
			root.setCenter(center);
			
			load = new Button("load");
			save = new Button("save");
			next = new Button("Next map");
			previous = new Button("Previous map");
			quit = new Button("QUIT");
			
			
			quit.setOnAction(ev -> {System.exit(0);});
			Scene scene = new Scene(root,width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			scene.widthProperty().addListener(ev -> {
				double w = scene.getWidth();
				 width = w;
				 sirkaDosky = w;
				 sirkaKamena = sirkaDosky / 19.2;
				 lavyHornyX = sirkaDosky / 32.2;
				 center.setWidth(width);
					
				paint();
			});
			scene.heightProperty().addListener(ev -> {
				double h = scene.getHeight()/3;
				 height = h;
				vyskaDosky = h;
				 vyskaKamena = vyskaDosky / 19.2;
				 lavyHornyY = vyskaDosky / 32.2;
				 center.setHeight(sirkaDosky);
				paint();
			});
			paint();
		
	}
	public void paint() {
		//top
		//////////////////////////////
		
		top.getChildren().clear();
		Label time = new Label("Time: "+ Integer.toString(tickTime));
		Label player;
		if(black) {
			player = new Label("Current player: black");
		}
		else {
			player = new Label("Current player: black");
		}
		top.getChildren().addAll(time, player);
		//bottom
		//////////////////////////////
		bottom.setMaxHeight(height/6);
		bottom.setMinHeight(height/6);
		bottom.getChildren().clear();
		bottom.getChildren().addAll(save,load,next,previous,quit);
		
		//center
		///////////////////////////////
		//center.getChildren().clear();
		
		
		brd.setFitWidth(sirkaDosky);
		brd.setFitHeight(vyskaDosky);
		brd.maxHeight(4*(height/6));
		
		//center.getChildren().add(brd);
		/*
		for(int i = 0; i< hra.size(); i++) {
			for( int j = 0; j < hra.get(i).size(); j++) {
				Block x = hra.get(i).get(j);
				
				
				center.getChildren().add(x);
				x.paint();
			}
		}*/
		center.paint();
	}
		
		class PlayGround extends Canvas{
			
			
			//TODO
			
			public PlayGround() {
				setWidth(vyskaDosky);
				setHeight(sirkaDosky);
				for(int i = 0; i< 19; i++) {
					ArrayList<Block> tmp = new ArrayList<Block>();
					for(int j = 0; j< 19; j++) {
						tmp.add(new Block(i,j, "."));
					}
					hra.add(tmp);
				}
				this.setOnMouseClicked(ev -> {
					int x = (int) (ev.getX()/sirkaKamena);
					int y = (int) (ev.getY()/vyskaKamena);
					
					System.out.println(x);
					System.out.println(y);
					Block b = hra.get(x).get(y);
					if(!b.obsadene && !b.st.equals("x")) {
						if(black) {
							
							hra.get(x).get(y).st = "b";
							hra.get(x).get(y).obsadene = true;}
						else {
							hra.get(x).get(y).st = "w";
							hra.get(x).get(y).obsadene = true;
						}
						black = !black;
					}paint();
				});
				
			}
			
			
			public void paint() {
				GraphicsContext g = getGraphicsContext2D();
				
				g.setFill(Color.WHITE);
	            g.fillRect(0,0,width,sirkaDosky);
	            g.drawImage(board, 0,0,width,sirkaDosky);
	            for(int i = 0; i< hra.size(); i++) {
	            	for( int c = 0; c< hra.get(i).size(); c++) {
	            		Circle kr  = null;
	            		if(hra.get(i).get(c).st.equals("w")) {
	            			g.setFill(Color.WHITE);
	            			g.fillOval(i*sirkaKamena+2, c*vyskaKamena+2, sirkaKamena, vyskaKamena);
	            			
	            		}
	            		else if(hra.get(i).get(c).st.equals("b")) {
	            			g.setFill(Color.BLACK);
	            			g.fillOval(i*sirkaKamena+2, c*vyskaKamena+2, sirkaKamena, vyskaKamena);
	            			kr = new Circle(i*sirkaKamena, c*vyskaKamena, sirkaKamena/2);
	            			
	            		}
	            		
	            	}
	            }
			}
				
				
				//((Shape) this.getChildren().get(2)).setFill(Color.ALICEBLUE);
				
				
			}
			
		
		
	public static void main(String[] args) {
		launch(args);
	}
}
