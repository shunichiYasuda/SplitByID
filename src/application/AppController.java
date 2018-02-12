package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AppController {
	File baseFile,selectedRcordFile,nonSelectedRecordFile,refferIDFile;
	List<String> baseRecordList = new ArrayList<String>();
	List<String> selectedRecordList = new ArrayList<String>();
	List<String> nonSelectedRecordList = new ArrayList<String>();
	List<String> refferIDList = new ArrayList<String>();
	String[] baseFieldArray; //この配列はフィールド名を持つ。
	String[] refferFieldArray;
	@FXML
	TextField suffixName;
	@FXML
	TextArea log;
	@FXML
	ComboBox<String> baseFieldCombo;
	@FXML
	ComboBox<String> refferFieldCombo;
	@FXML
	private void quitAction() {
		System.exit(0);
	}
	@FXML
	private void openAction() {
		FileChooser fc = new FileChooser();
		baseFile = fc.showOpenDialog(log.getScene().getWindow());
		log.appendText("元ファイルに"+baseFile.getAbsolutePath()+"が設定されました\n");
		//
		String line = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(baseFile), "JISAutoDetect"));
			line = br.readLine();//最初の行はフィールド
			baseFieldArray = line.split(",");
			//フィールド名をコンボボックスに入れる
			for(String s: baseFieldArray) {
				baseFieldCombo.getItems().add(s);
			}
			//元ファイルのレコードを保存する
			while((line=br.readLine())!= null) {
				baseRecordList.add(line);
			}
			
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void openRefferFile() {
		FileChooser fc = new FileChooser();
		refferIDFile =fc.showOpenDialog(log.getScene().getWindow());
		log.appendText("参照ファイルに"+refferIDFile.getAbsolutePath()+"が設定されました\n");	
		//
		String line = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(refferIDFile), "JISAutoDetect"));
			line = br.readLine();//最初の行はフィールド
			refferFieldArray = line.split(",");
			//フィールド名をコンボボックスに入れる
			for(String s: refferFieldArray) {
				refferFieldCombo.getItems().add(s);
			}
			//参照ファイルのデータをとっておく。この時点では参照キーは分からない
			while((line = br.readLine())!=null) {
				refferIDList.add(line);
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void execAction() {
		//比較するフィールドの位置
		int refferPos =0;
		int basePos = 0;
		//元ファイルの比較フィールドを探す
		int index =0;
		for(String s: baseFieldArray) {
			if(s.equals((String)baseFieldCombo.getValue())){
				basePos = index;
			}
			index++;
		}
		//System.out.println("basePos ="+basePos);
		//参照ファイルの比較フィールドを探す
		index = 0;
		for(String s: refferFieldArray) {
			if(s.equals((String)refferFieldCombo.getValue())){
				refferPos = index;
			}
			index++;
		}
		//System.out.println("refferPos ="+refferPos);
		//参照フィールド番号にある値を比較する。
		boolean hitFlag = false;
		for(String str: baseRecordList) {
			String[] baseArray = str.split(",");
			String key = baseArray[basePos];
			hitFlag = false;
			for(String s : refferIDList) {
				String[] refferArray = s.split(",");
				String keyReff = refferArray[refferPos];
				if(key.equals(keyReff)) {
					hitFlag = true;
				}
			} //end of for(String s:refferIdList)
			if(hitFlag) {
				selectedRecordList.add(str);
			}else {
				nonSelectedRecordList.add(str);
			}
		}//end of for(String str: baseRecodList)
		
		System.out.println("num of base all:"+baseRecordList.size());
		System.out.println("num of reffer :"+refferIDList.size());
		System.out.println("num of selected :"+ selectedRecordList.size());
		System.out.println("num of noselected :"+ nonSelectedRecordList.size());
	}// end of execAction()
}
