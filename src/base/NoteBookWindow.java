package base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.callback.TextInputCallback;
import javax.swing.ActionMap;
import javax.swing.UIDefaults.ActiveValue;

import org.xml.sax.HandlerBase;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	Stage stage;

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();

	final HBox saveAndDelete = new HBox();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setDisable(false);
		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setDisable(false);
		Label label = new Label("Search :");
		TextField textField = new TextField();
		Button search = new Button("Search");
		Button clear = new Button("Clear Search");

		hbox.getChildren().addAll(buttonLoad, buttonSave, label, textField, search, clear);

		search.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				currentSearch = textField.getText();
				textAreaNote.setText("");
				updateListView(currentFolder,currentSearch);
			}			
		});

		clear.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				currentSearch = "";
				textField.setText("");
				textAreaNote.setText("");
				updateListView(currentFolder,null);
			}
		});

		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(stage);

				if(file != null){
					loadNoteBook(file);
				}
			}
		});

		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File to Store Your NoteBook!");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(stage);

				if(file != null){
					noteBook.save(file.getAbsolutePath());

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("Your file has been saved to file "+ file.getName());
					alert.showAndWait().ifPresent(rs -> {
						if(rs==ButtonType.OK){
							System.out.println("Pressed OK.");
						}
					});
				}
			}
		});

		return hbox;
	}

	private void loadNoteBook(File file){
		noteBook = new NoteBook(file.getAbsolutePath());
		saveAndDelete.setVisible(true);
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		for(Folder f: noteBook.getFolders()){
			foldersComboBox.getItems().add(f.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView(currentFolder,null);

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";
				TextNote tn;
				for(Folder f: noteBook.getFolders()){
					if(f.getName().equals(currentFolder)){
						for(Note note: f.getNotes()){
							if(note.getTitle().equals(title)){
								if(note instanceof TextNote){
									tn = (TextNote)note;
									content = tn.getContent();
									currentNote = tn.getTitle();
									break;
								}
							}
						}
						if(content.equals("")==false){
							break;
						}
					}
				}
				textAreaNote.setText(content);
			}
		});

		HBox topBar = new HBox();
		Button addFoler = new Button("Add a Folder");
		Button addNote = new Button("Add a Note");


		addFoler.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0){
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook");
				dialog.setContentText("Please enter the name you want to create:");

				Optional<String>  result = dialog.showAndWait();
				if(result.isPresent()){
					boolean ok = true;
					for(Folder f: noteBook.getFolders()){
						if(result.get().equals(f.getName())){
							ok = false;
							
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Warning");
							alert.setContentText("You already have a folder named with "+ result.get());
							alert.showAndWait();
						}
					}
					if(ok){
						noteBook.addFolder(result.get());
						foldersComboBox.getItems().add(result.get());
						foldersComboBox.setValue(result.get());
						updateListView(result.get(), null);
						foldersComboBox.setValue(result.get());
					}
				}else{
					Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Warning");
							alert.setContentText("Please input an valid folder name");
							alert.showAndWait();
				}
			}
		});

		addNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0){
				if(foldersComboBox.getSelectionModel().getSelectedItem().equals("-----")){
					Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Warning");
							alert.setContentText("Please choose a folder first!");
							alert.showAndWait();
				}else{
					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new note to the current folder");
					dialog.setContentText("Please enter the name of your note:");

					Optional<String> result = dialog.showAndWait();

					if(result.isPresent()){
						noteBook.createTextNote(currentFolder,result.get());
						Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful!");
							alert.setContentText("Insert note " + result.get() + " to folder " + currentFolder + " successfully!");
							alert.showAndWait();

						updateListView(currentFolder, null);
					}
				}
			}
		});

		topBar.getChildren().addAll(foldersComboBox, addFoler);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(topBar);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNote);


		return vbox;
	}

	private void updateListView(String currentFolderName,String criteria) {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		Folder currentFolder = null;
		for(Folder f: noteBook.getFolders()){
			if(f.getName().equals(currentFolderName)){
				currentFolder = f;
				break;
			}
		}
		if(currentFolder!=null){
			if(criteria==null){
				for(Note note: currentFolder.getNotes()){
					list.add(note.getTitle());
				}
			}else{
				for(Note note: currentFolder.searchNotes(criteria)){
					list.add(note.getTitle());
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		
		//Create the save and delete bar
		saveAndDelete.setPadding(new Insets(15, 12, 15, 12));
		saveAndDelete.setSpacing(10); // Gap between nodes
		
		Button saveNote = new Button("Save Note");
		saveNote.setPrefSize(100, 20);
		Button deleteNote = new Button("Delete Note");
		deleteNote.setPrefSize(100, 20);

		Image save = new Image("C:/Users/kelvin/workspace/tryjava/comp3021lab/comp3021lab/save.png");
		ImageView saveview = new ImageView(save);
		saveview.fitHeightProperty().bind(saveNote.heightProperty());
		saveview.fitWidthProperty().bind(saveview.fitHeightProperty());

		Image delete = new Image("C:/Users/kelvin/workspace/tryjava/comp3021lab/comp3021lab/delete.jfif");
		ImageView deleteview = new ImageView(delete);
		deleteview.fitHeightProperty().bind(saveNote.heightProperty());
		deleteview.fitWidthProperty().bind(deleteview.fitHeightProperty());

		saveAndDelete.getChildren().addAll(saveview,saveNote,deleteview,deleteNote);
		saveAndDelete.setVisible(true);

		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				if(currentFolder.equals("")||currentNote.equals("")){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder or a note");
					alert.showAndWait();
				}else{
					for(Note n : noteBook.searchNotes(currentNote)){
						if(n instanceof TextNote){
							TextNote nn = (TextNote)n;
							nn.setContent(textAreaNote.getText());
							break;
						}
					}
				}
			}
		});

		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				if(currentFolder.equals("")||currentNote.equals("")){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder or a note");
					alert.showAndWait();
				}else{
					for(Folder f : noteBook.getFolders()){
						if(f.getName().equals(currentFolder)){
							for(Note n : noteBook.searchNotes(currentNote)){
								f.getNotes().remove(n);
								break;
							}
						}							
					}
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Succeed");
					alert.setContentText("Removed");
					alert.showAndWait();
				}
			}
		});

		// 0 0 is the position in the grid
		grid.add(saveAndDelete,0, 0);
		grid.add(textAreaNote, 0, 1);
		
		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called �𦭛he most shocking play in NFL history�� and the Washington Redskins dubbed the �嚹hrowback Special��: the November 1985 play in which the Redskins�� Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award-winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything-until it wasn't. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant--a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether-- Daddy Was a Number Runner and Dorothy Allison�䏭 Bastard Out of Carolina, Jacqueline Woodson�䏭 Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood�𤴆he promise and peril of growing up�𤤗nd exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;
	}
}