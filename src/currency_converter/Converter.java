package currency_converter;

//JavaFX imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//Java regular imports
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

//JSON parser imports
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Converter extends Application {

	private static String TITLE = "Currency Converter";
	private static String API_URL = "https://api.exchangerate.host/latest";
	private static String[] KEYS;
	private static String[] VALUES;
	private static String[][] CURRENCIES;
	private static String fromCURRENCY, toCURRENCY;

	//Main
	public static void main(String[] args) throws IOException, ParseException {
		JSONObject obj = getCurrencyData(API_URL);
		KEYS = getKeysFromJSON(obj);
		VALUES = getValuesFromJSON(obj);
		CURRENCIES = alphabetizeArray(zipArrays(KEYS, VALUES));
		
		launch(args);
	}
	
	//Fetch API
	public static JSONObject getCurrencyData(String api) throws IOException, ParseException {
		URL url = new URL(api);

		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		int responseCode = request.getResponseCode();
		
		if (responseCode != 200) {
		    throw new RuntimeException("HttpResponseCode: " + responseCode);
		} else {
		  
		    String inline = "";
		    Scanner scanner = new Scanner(url.openStream());
		  
		    while (scanner.hasNext()) {
		       inline += scanner.nextLine();
		    }
		    scanner.close();

		    JSONParser parser = new JSONParser();
		    JSONObject dataObject = (JSONObject) parser.parse(inline);

		    return (JSONObject) dataObject.get("rates");
		}
	}

	//Convert the currencies, ex:
	//	getRate("BRL", "CAD") ~~ 0.21
	//	getRate("CAD", "BRL") ~~ 3.82
	public static double getRate(String[][] currencyList, String currency0, String currency1) {
		double value0 = 0.0;
		double value1 = 0.0;
		for(int i = 0; i < currencyList.length; i++) {
			if(currency0.equals(currencyList[i][0])) {
				value0 = getDouble(currencyList[i][1]);
			}
			if(currency1.equals(currencyList[i][0])) {
				value1 = getDouble(currencyList[i][1]);
			}
		}
		double ratio0 = 1/value0;
		double ratio1 = 1/value1;
		return ratio0/ratio1;
	}
	
	//Add JSON keys to a String array
	@SuppressWarnings("unchecked")
	public static String[] getKeysFromJSON(JSONObject json) {
		Set<String> keys = json.keySet();
	    return keys.toArray(String[]::new);
	}
	
	//Add JSON values to a double array
	@SuppressWarnings({ "rawtypes" })
	public static String[] getValuesFromJSON(JSONObject json) {
		Collection rawValues = json.values();
		Object[] arrayO = rawValues.toArray();
		double[] arrayD = new double[arrayO.length];
		for(int i = 0; i < arrayO.length; i++) {
			arrayD[i] = Double.parseDouble(arrayO[i].toString());
		}
		return stringifyArray(arrayD);
	}
	
	//Create a 2d array from String arrays
	public static String[][] zipArrays(String[] array0, String[] array1){
		String[][] zip = new String[array0.length][2];
		for(int i = 0; i < array0.length; i++) {
			zip[i][0] = array0[i];
			zip[i][1] = array1[i];
		}
		return zip;
	}
	
	//Sorts a 2d String array in alphabetical order
	public static String[][] alphabetizeArray(String[][] array) {
		int size = array.length;
	    for(int i = 0; i<size-1; i++) {
	    	for (int j = i+1; j<size; j++) {
	    		if(array[i][0].compareTo(array[j][0])>0) {
	    			String temp0 = array[i][0];
	    			String temp1 = array[i][1];
	                array[i][0] = array[j][0];
	                array[i][1] = array[j][1];
	                array[j][0] = temp0;
	                array[j][1] = temp1;
	            }
	        }
	    }
	    return array;
	}
	
	//Converts double array into String array
	public static String[] stringifyArray(double[] arrayD) {
		String[] arrayS = new String[arrayD.length];
		for(int i = 0; i<arrayD.length; i++) {
			arrayS[i] = String.valueOf(arrayD[i]);
		}
		return arrayS;
	}
	
	//Converts double into String
	public static double getDouble(String string) {
		return Double.parseDouble(string);
	}
	
	public static String[] getKeysFrom(String[][] arr) {
		String[] array = new String[arr.length];
		for(int i = 0; i < arr.length; i++) {
			array[i] = arr[i][0];
		}
		return array;
	}
	
	//JavaFX window settings
	@Override
	public void start(Stage stage) throws Exception {		
		stage.setTitle(TITLE);
		
		HBox hbox0 = new HBox();
		hbox0.setPrefWidth(400);
		HBox hbox4 = new HBox();
		hbox4.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(new Group(hbox4), 250, 190);
		
		Label inputLabel = new Label("Amount: ");
		TextField inputField = new TextField();
		
		Label display = new Label("-");
		display.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		
		ComboBox<String> fromDropDown = new ComboBox<String>();
		fromDropDown.setPrefWidth(100);
	    fromDropDown.getItems().addAll(getKeysFrom(CURRENCIES));
	    fromDropDown.setOnAction((e) -> {
	    	fromCURRENCY = fromDropDown.getSelectionModel().getSelectedItem();
	    });
		
	    ComboBox<String> toDropDown = new ComboBox<String>();
	    toDropDown.setPrefWidth(100);
	    toDropDown.getItems().addAll(getKeysFrom(CURRENCIES));
	    toDropDown.setOnAction((e) -> {
	    	toCURRENCY = toDropDown.getSelectionModel().getSelectedItem();
	    });
	    
	    Button convertButton = new Button("Exchange");
	    convertButton.setOnAction((e) -> { 
	    	double rate = getRate(CURRENCIES, fromCURRENCY, toCURRENCY);
            String string = "" + String.format("%.3f", rate);
            display.setText(string);
	    });
	    
	    HBox hbox1 = new HBox();
	    hbox1.setSpacing(20);
	    hbox1.setAlignment(Pos.BASELINE_RIGHT);
	    hbox1.getChildren().add(inputLabel);
	    hbox1.getChildren().add(inputField);
		
		HBox hbox2 = new HBox();
		hbox2.setSpacing(20);
		hbox2.setAlignment(Pos.BASELINE_RIGHT);
		hbox2.getChildren().add(new Label("From: "));
        hbox2.getChildren().add(fromDropDown);
      
		HBox hbox3 = new HBox();
		hbox3.setSpacing(20);
		hbox3.setAlignment(Pos.BASELINE_RIGHT);
		hbox3.getChildren().add(new Label("To: "));
		hbox3.getChildren().add(toDropDown);
		
		VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.BASELINE_RIGHT);
        vbox.setBackground(null);
        vbox.getChildren().add(hbox1);
        vbox.getChildren().add(hbox2);
        vbox.getChildren().add(hbox3);
        vbox.getChildren().add(convertButton);
        vbox.getChildren().add(display);

        hbox0.getChildren().add(vbox);
        hbox0.setAlignment(Pos.CENTER);
        hbox0.setPrefWidth(250);
        
        BackgroundFill backgroundFill = new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        hbox0.setBackground(background);
        
        Group root = (Group)scene.getRoot();
        root.getChildren().add(hbox0);
        stage.setScene(scene);
        stage.show();
	}

}
