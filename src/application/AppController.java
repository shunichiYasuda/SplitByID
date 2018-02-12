package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AppController {
	File baseFile,selectedRcordFile,nonSelectedRecordFile,refferIDFile;
	List<String> baseRecordList = new ArrayList<String>();
	List<String> selectedRecordList = new ArrayList<String>();
	List<String> nonSelectedRecordList = new ArrayList<String>();
	List<String> refferIDList = new ArrayList<String>();
	@FXML
	TextField suffixName;
	@FXML
	TextArea log;
	@FXML
	private void quitAction() {
		System.exit(0);
	}
	@FXML
	private void openAction() {
		
	}
	@FXML
	private void execAction() {
		
	}
}
