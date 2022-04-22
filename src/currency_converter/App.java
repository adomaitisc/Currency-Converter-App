package currency_converter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	// http://tutorials.jenkov.com/javafx/index.html
	// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=1

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Button button = new Button();
		button.setText("Click me");
		
		Label label = new Label();
		label.setText("My Label");
	
		TextField textField = new TextField();
		textField.setPromptText("Last name");
		textField.setMaxWidth(200);
		
		CheckBox checkBox = new CheckBox();
		checkBox.setText("Check me");
		
		RadioButton radioButton = new RadioButton();
		radioButton.setText("Radio me");

		VBox verticalBox = new VBox();
		verticalBox.setSpacing(20);
		verticalBox.setAlignment(Pos.BASELINE_CENTER);

		verticalBox.getChildren().add(button);
		verticalBox.getChildren().add(label);
		verticalBox.getChildren().add(textField);
		verticalBox.getChildren().add(checkBox);
		verticalBox.getChildren().add(radioButton);
			
		Scene scene = new Scene(verticalBox, 400, 400);
		primaryStage.setScene(scene);

		primaryStage.setTitle("My App");
		
		primaryStage.show();
	}

}
