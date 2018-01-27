package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	private static AnchorPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("FileHook");
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
	
	public static void showRecent(RecentFiles recentFiles) {
		Node recentList = root.lookup("#recentList");
		@SuppressWarnings("unchecked")
		ListView<String> viewList = ((ListView<String>)recentList);
		viewList.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		           String currentItemSelected = viewList.getSelectionModel()
		                                                    .getSelectedItem();
		           HookController.openRecent(currentItemSelected);
		        }
		    }
		});
		if(viewList.getItems()!=null)
			viewList.getItems().clear();
		if(recentFiles!=null && recentFiles.getList()!=null)
				for(String[] line_path: recentFiles.getList())
					viewList.getItems().add(line_path[0]+" : "+line_path[1]);
		Node recentPane = root.lookup("#paneRecent");
		recentPane.setVisible(true);
	}
	
	public static void closeRecent() {
		Node recentPane = root.lookup("#paneRecent");
		recentPane.setVisible(false);
	}
	
	
}
