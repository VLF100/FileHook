package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import application.RecentFiles.OpenedFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

/**
 * Main class of the application.
 * Controls everything related to the display and user interface.
 *
 */
public class Main extends Application {

	
	private static Stage primaryStage;
	private static AnchorPane root;
	
	private static Stage nickModal;
	private static AnchorPane nickModalPane;

	//Parsing for scripts with ruby text
	public static boolean blueSkyMode = false;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("FileHook");
			HookController hook = new HookController();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/hook.fxml"));
			fxmlLoader.setController(hook);
			Main.root = (AnchorPane) fxmlLoader.load();

			Scene scene = new Scene(Main.root);
			primaryStage.setResizable(false);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			//Code to listen to enter key input
			scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
				Node numberField = Main.root.lookup("#numberlabel");
				if (!numberField.isFocused() && event.getCode() == KeyCode.ENTER)
					hook.hookClick(null);
			});

			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			
			Main.setTooltips();
			
			primaryStage.show();
			
			Main.primaryStage = primaryStage;
			
			RecentFiles.readFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	//Set tooltips of various elements
	public static void setTooltips(){
		Node nextArrow = Main.root.lookup("#nextScript");
		Tooltip.install(nextArrow, Main.generateStandardTooltip("Go to the next file in alphabetical order.")); 
		Node numberField = Main.root.lookup("#numberlabel");
		Tooltip.install(numberField, Main.generateStandardTooltip("Input number of line and press enter.")); 
		Node blueSkyMode = Main.root.lookup("#blueSkyMode");
		Tooltip.install(blueSkyMode, Main.generateStandardTooltip("Parsing for scripts with ruby text.")); 
		Node alwaysOnTopMode = Main.root.lookup("#alwaysOnTopMode");
		Tooltip.install(alwaysOnTopMode, Main.generateStandardTooltip("Keep the application on the foreground.")); 
	}
	
	//Function to generate tooltips with the same parameters
	private static Tooltip generateStandardTooltip(String text){
		Tooltip tooltip = new Tooltip(text);
		tooltip.setWrapText(true);
		tooltip.setMaxWidth(150);
		tooltip.setFont(new Font(13));
		return tooltip;
	}

	//Control of the filename label
	public static void showFileName(String name) {
		Node fileNameLabel = Main.root.lookup("#fileName");
		((Label) fileNameLabel).setText(name);
	}

	//Control of the labels related to the current line
	public static void setLine(int i, String string, int totalLines) {
		Node numberField = Main.root.lookup("#numberlabel");
		((TextField) numberField).setText(Integer.toString(i));
		Node lineLabel = Main.root.lookup("#lineLabel");
		((Label) lineLabel).setText(string);
		Node totalLabel = Main.root.lookup("#labelTotal");
		((Label) totalLabel).setText(i + "/" + totalLines);
	}

	//Function to obtain the contents of the number line label
	public static int getNumberLine() {
		Node numberField = Main.root.lookup("#numberlabel");
		try {
			return new Integer(((TextField) numberField).getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	//Control of selection script window
	public static File selectScript(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open script file...");
		return fileChooser.showOpenDialog(primaryStage);
	}
	
	//Control of the "recent list of files" side menu
	public static void showRecent(List<OpenedFile> recentFiles) {
		@SuppressWarnings("unchecked")
		ListView<OpenedFile> recentList = (ListView<OpenedFile>)Main.root.lookup("#recentList");
		ListView<OpenedFile> viewList = ((ListView<OpenedFile>) recentList);
		
		ObservableList<OpenedFile> wordsList = FXCollections.observableArrayList(RecentFiles.readFile()); 
		viewList.setItems(wordsList);
		//set action for when item selected with a double click
		
		viewList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {

				if (click.getClickCount() == 2) {
					OpenedFile currentItemSelected = viewList.getSelectionModel().getSelectedItem();
					HookController.openRecent(currentItemSelected);
				}
			}
		});
		
		Node recentPane = Main.root.lookup("#paneRecent");
		recentPane.setVisible(true);
	}
	public static void closeRecent() {
		Node recentPane = Main.root.lookup("#paneRecent");
		recentPane.setVisible(false);
	}

	//Control of the "delta features" side menu
	public static void showDelta() {
		Node recentPane = Main.root.lookup("#paneDelta");
		recentPane.setVisible(true);
	}
	public static void closeDelta() {
		Node recentPane = Main.root.lookup("#paneDelta");
		recentPane.setVisible(false);
	}

	public static void toogleAlwaysOnTop() {
		Node topFire = Main.root.lookup("#alwaysOnTopMode");
		primaryStage.setAlwaysOnTop(((CheckBox)topFire).isSelected());
	}

	public static void showNicknameModal() {
		if(nickModal == null){
			nickModal = new Stage();
			nickModal.setTitle("Introduce nickname to save the progress");
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/application/nicknamemodal.fxml"));
			fxmlLoader.setController(new ModalController());
			nickModalPane = null;
			
			try {
				nickModalPane = (AnchorPane) fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			Scene scene = new Scene(nickModalPane);
			scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			nickModal.setScene(scene);
			
			nickModal.setResizable(false);
			nickModal.initOwner(primaryStage);
			nickModal.initModality(Modality.APPLICATION_MODAL);
			
			Node nickField = nickModalPane.lookup("#nickField");
			Tooltip.install(nickField, Main.generateStandardTooltip("Must not be empty and lesser than 25 characters.")); 
            
		}
		
		double centerXPosition = primaryStage.getX() + primaryStage.getWidth()/2d;
        double centerYPosition = primaryStage.getY() + primaryStage.getHeight()/2d;
        
        nickModal.setOnShown(ev -> {
        	nickModal.setX(centerXPosition - nickModal.getWidth()/2d);
            nickModal.setY(centerYPosition - nickModal.getHeight()/2d);
            nickModal.show();
        });
        
		nickModal.showAndWait();
		
	}
	
	public static void closeNicknameModal() {
		nickModal.close();
	}

	public static String getNicknameModalField() {
		Node nickfield = nickModalPane.lookup("#nickField");
		return ((TextField) nickfield).getText();
	}

	public static void nicknameModalShowError() {
		System.out.println("in");
		Node nickfield = nickModalPane.lookup("#nickField");
		nickfield.getStyleClass().add("textfieldError");
		
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	nickfield.getStyleClass().remove("textfieldError");
		            }
		        }, 
		        3000 
		);
		
	}

}
