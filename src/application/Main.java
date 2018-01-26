package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	private static AnchorPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			HookController hook = new HookController(primaryStage);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/hook.fxml"));
	        fxmlLoader.setController(hook);
			AnchorPane root = (AnchorPane) fxmlLoader.load();
	        Main.root = root;
			        
			
	        Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void showFileName(String name){
		Node fileNameLabel = root.lookup("#fileName");
		((Label)fileNameLabel).setText(name);
	}

	public static void setLine(int i, String string) {
		Node numberField = root.lookup("#numberlabel");
		((TextField)numberField).setText(Integer.toString(i));
		Node lineLabel = root.lookup("#lineLabel");
		((Label)lineLabel).setText(string);
	}

	public static int getNumberLine() {
		Node numberField = root.lookup("#numberlabel");
		try {
			return new Integer(((TextField)numberField).getText());
		} catch (NumberFormatException e){
			return -1;
		}
	}
}
