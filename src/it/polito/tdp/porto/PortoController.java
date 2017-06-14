package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	
    	txtResult.clear();
		
    	try {
    		
			Author autore = boxPrimo.getValue() ;
			
			if( autore == null ) {
				txtResult.setText("Scegliere il primo autore.");
				return;
			}
			
			model.creaGrafo();
			
			List<Author> coautori = model.trovaCoautori(autore);
			
			for(Author coautore : coautori) {
				txtResult.appendText(coautore+"\n");
			}
			
	    	boxSecondo.getItems().clear();
	    	boxSecondo.getItems().addAll(model.trovaNonCoautori(autore));
	    	boxSecondo.setDisable(false);
		
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    
    	try {
    		
			Author autore1 = boxPrimo.getValue() ;
			Author autore2 = boxSecondo.getValue() ;
			
			if( autore1==null || autore2==null ) {
				txtResult.appendText("Errore: selezionare due autori\n");
				return ;
			}
			
			List<Paper> articoli = model.sequenzaArticoli(autore1, autore2) ;
			
			txtResult.appendText("\nArticoli che collegano "+autore1.toString()+" e "+autore2.toString()+":\n");
			for(Paper p: articoli) 
				txtResult.appendText(p.toString()+"\n");
			
		} catch (RuntimeException re) {
				txtResult.setText(re.getMessage());
		}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
        
        boxSecondo.setDisable(true);
    }

	public void setModel(Model model) {
		this.model = model;
		boxPrimo.getItems().addAll(model.getAllAutori());
	}
}
