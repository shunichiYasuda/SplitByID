package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AppController {
	File baseFile, selectedRecordFile, nonSelectedRecordFile, refferIDFile;
	String baseFileName, selectedFileName, nonSelectedFileName;
	String baseFieldRecord;
	boolean baseFileCheckFlag = true;
	boolean reffFileCheckFlag = true;
	String filePath = null;
	String sysEncode;
	List<String> baseRecordList = new ArrayList<String>();
	List<String> selectedRecordList = new ArrayList<String>();
	List<String> nonSelectedRecordList = new ArrayList<String>();
	List<String> refferIDList = new ArrayList<String>();
	String[] baseFieldArray; // ���̔z��̓t�B�[���h�������B
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
		fc.setTitle("������csv�t�@�C�����w�肵�Ă�������");
		baseFile = fc.showOpenDialog(log.getScene().getWindow());
		if(baseFile==null) {
			showAlert("�������t�@�C�����w�肳��Ă��܂���B");
			baseFileCheckFlag = true;
			return ;
		}else {
			baseFileCheckFlag = false;
		}
		log.appendText("���t�@�C����" + baseFile.getAbsolutePath() + "���ݒ肳��܂���\n");
		filePath = baseFile.getParent();
		baseFileName = getPreffix(baseFile.getAbsolutePath());
		//System.out.println(baseFileName);
		//
		String line = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(baseFile), "JISAutoDetect"));
			line = br.readLine();// �ŏ��̍s�̓t�B�[���h
			baseFieldArray = line.split(",");
			baseFieldRecord = line; //�t�B�[���h�����J���}��String �łƂ��Ă����B
			// �t�B�[���h�����R���{�{�b�N�X�ɓ����
			for (String s : baseFieldArray) {
				baseFieldCombo.getItems().add(s);
			}
			// ���t�@�C���̃��R�[�h��ۑ�����
			while ((line = br.readLine()) != null) {
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
		sysEncode = System.getProperty("file.encoding");
		FileChooser fc = new FileChooser();
		if(filePath != null) {
			fc.setInitialDirectory(new File(filePath));
		}
		fc.setTitle("�Q��ID���܂� csv �t�@�C�����w�肵�Ă�������");
		refferIDFile = fc.showOpenDialog(log.getScene().getWindow());
		if(refferIDFile == null) {
			showAlert("�Q��ID���܂񂾃t�@�C�����w�肵�Ă��������B");
			reffFileCheckFlag = true;
			return;
		}else {
			reffFileCheckFlag = false;
		}
		filePath = refferIDFile.getParent();
		log.appendText("�Q�ƃt�@�C����" + refferIDFile.getAbsolutePath() + "���ݒ肳��܂���\n");
		//
		String line = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(refferIDFile), "JISAutoDetect"));
			line = br.readLine();// �ŏ��̍s�̓t�B�[���h
			refferFieldArray = line.split(",");
			// �t�B�[���h�����R���{�{�b�N�X�ɓ����
			for (String s : refferFieldArray) {
				refferFieldCombo.getItems().add(s);
			}
			// �Q�ƃt�@�C���̃f�[�^���Ƃ��Ă����B���̎��_�ł͎Q�ƃL�[�͕�����Ȃ�
			while ((line = br.readLine()) != null) {
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
		//File���I������Ă��邩�ǂ����̃`�F�b�N
		if(baseFileCheckFlag) {
			showAlert("���t�@�C����I�����Ă��������B");
			return;
		}
		if(reffFileCheckFlag) {
			showAlert("�Q��ID���܂񂾃t�@�C����I�����Ă�������");
			return;
		}
		//�ڔ�����Q�b�g
		String addStr = suffixName.getText();
		if(addStr.equals("")) {
			showAlert("suffix ���ɕ�����̃t�@�C���ɂ���ڔ�������͂��Ă��������B");
			return;
		}
		//System.out.println("before: "+(String)baseFieldCombo.getValue());
		//�R���{�̒l
		String baseComboStr = (String)baseFieldCombo.getValue();
		String reffComboStr = (String)refferFieldCombo.getValue();
		if((baseComboStr == null) || (reffComboStr == null)) {
			showAlert("��r����L�[��I��ł��������B");
			return;
		}
		// ��r����t�B�[���h�̈ʒu
		int refferPos = 0;
		int basePos = 0;
		// ���t�@�C���̔�r�t�B�[���h��T��
		int index = 0;
		for (String s : baseFieldArray) {
			if (s.equals((String) baseFieldCombo.getValue())) {
				basePos = index;
			}
			index++;
		}
		// System.out.println("basePos ="+basePos);
		// �Q�ƃt�@�C���̔�r�t�B�[���h��T��
		index = 0;
		for (String s : refferFieldArray) {
			if (s.equals((String) refferFieldCombo.getValue())) {
				refferPos = index;
			}
			index++;
		}
		// System.out.println("refferPos ="+refferPos);
		// �Q�ƃt�B�[���h�ԍ��ɂ���l���r����B
		String key = null,keyReff= null;
		boolean hitFlag = false;
		for (String str : baseRecordList) {
			String[] baseArray = str.split(",");
			key = baseArray[basePos];
			hitFlag = false;
			for (String s : refferIDList) {
				String[] refferArray = s.split(",");
				keyReff = refferArray[refferPos];
				if (key.equals(keyReff)) {
					hitFlag = true;
				}
			} // end of for(String s:refferIdList)
			if (hitFlag) {
				selectedRecordList.add(str);
			} else {
				nonSelectedRecordList.add(str);
			}
		} // end of for(String str: baseRecodList)
		log.appendText("\nnum of base all:" + baseRecordList.size());
		log.appendText("\nnum of reffer :" + refferIDList.size());
		log.appendText("\nnum of selected :" + selectedRecordList.size());
		log.appendText("\nnum of noselected :" + nonSelectedRecordList.size());
		// �t�@�C���ɏ�������
		selectedRecordFile = new File(baseFileName+addStr+".csv");
		nonSelectedRecordFile = new File(baseFileName+"No"+addStr+".csv");
		// �����R�[�h���w�肷��
		try {
			PrintWriter printSelected = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedRecordFile), sysEncode)));
			PrintWriter printNonSelected = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nonSelectedRecordFile), sysEncode)));
			//�ŏ��̍s�Ɍ��̃t�B�[���h����������
			printSelected.println(baseFieldRecord);
			printNonSelected.println(baseFieldRecord);
			for(String s:selectedRecordList) {
				printSelected.println(s);
			}
			for(String s:nonSelectedRecordList) {
				printNonSelected.println(s);
			}
			//
			printSelected.close();
			printNonSelected.close();
			//
			String msg1 =new String("\n���t�@�C����"+(String)baseFieldCombo.getValue()+"��"+"�Q�ƃt�@�C����" + (String)refferFieldCombo.getValue() + "�����������R�[�h��\n");
			String msg2 = new String(selectedRecordFile.getAbsolutePath()+"\t�ɏ������܂�Ă��܂�\n");
			String msg3 = new String("�����łȂ����R�[�h��\n");
			String msg4 = new String(nonSelectedRecordFile.getAbsolutePath()+"\t�ɏ������܂�Ă��܂��B");
			log.appendText(msg1+msg2+msg3+msg4);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end of execAction()
	//
	private String getPreffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    } 
	    return fileName;
	}
	//
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("�t�@�C����I�����Ă�������");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); //�\��
	}
}
