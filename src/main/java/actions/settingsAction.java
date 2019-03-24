package actions;

import Dao.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import model.signatureSettings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class settingsAction extends ActionSupport implements Preparable {

    // userId
    private String userId;
    // model get frim database By UserId
    signatureSettings signatureSettings;

    Map<String, String> positionMap;

    Map<String, String> alignementMap;

    // port
    String digitalSignaturePDFPluginPort = "8085";


    // - Image File From Html
     File userImage;
    // - Image File Name
     String userImageFileName;


    // check plugin On or Not
    public String executeSelectSettings() throws Exception {


        if (userId == null || userId.trim().isEmpty())
            return ERROR;

        // get model From database By UserId
        ResultSet resultSet = Database.selectByUserId(Integer.parseInt(userId));
        signatureSettings = new signatureSettings();
        if (resultSet.next()) {

            // set all variables
            signatureSettings.setDIGITAL_SIGNATURE_ALIAS(resultSet.getString("DIGITAL_SIGNATURE_ALIAS"));
            signatureSettings.setDIGITAL_SIGNATURE_ALIGNMENT(resultSet.getString("DIGITAL_SIGNATURE_ALIGNMENT"));
            signatureSettings.setDIGITAL_SIGNATURE_CLIENT_IP(resultSet.getString("DIGITAL_SIGNATURE_CLIENT_IP"));
            signatureSettings.setDIGITAL_SIGNATURE_IMAGE(resultSet.getString("DIGITAL_SIGNATURE_IMAGE"));
            signatureSettings.setDIGITAL_SIGNATURE_PAGENO(resultSet.getInt("DIGITAL_SIGNATURE_PAGENO"));
            signatureSettings.setDIGITAL_SIGNATURE_SERIAL_NUMBER(resultSet.getString("DIGITAL_SIGNATURE_SERIAL_NUMBER"));
            signatureSettings.setDIGITAL_SIGNATURE_POSITION(resultSet.getString("DIGITAL_SIGNATURE_POSITION"));
            signatureSettings.setID(resultSet.getInt("Id"));
            signatureSettings.setUSER_ID(Integer.parseInt(userId));
        } else {
            signatureSettings.setUSER_ID(Integer.parseInt(userId));
            signatureSettings.setID(0);
        }

        System.out.println(userId);

        return SUCCESS;
    }


    public String executeSaveOrUpdate() throws Exception {

        // set file path to image sigature image in model
        if (userImage != null) {

            String filePath = getTempFolderOfSavedImages().concat("userimages") + "\\" + userImageFileName;


            String savedPath = convertPath(filePath);

            File fileToCreate = new File(filePath);
            FileUtils.copyFile(userImage, fileToCreate); //copying source file to new file

            signatureSettings.setDIGITAL_SIGNATURE_IMAGE(savedPath);

        }


        // save Or Update
        int user_id = signatureSettings.getUSER_ID();
        ResultSet resultSet = Database.selectByUserId(user_id);
        // if exists update
        if (resultSet.next()) {

            // update database
            Database.updateIntoDatabase(signatureSettings);

        }
        // else create New
        else {
            Database.saveIntoDatabase(signatureSettings);
        }

        System.out.println("Done");
        return SUCCESS;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public model.signatureSettings getSignatureSettings() {
        return signatureSettings;
    }

    public void setSignatureSettings(model.signatureSettings signatureSettings) {
        this.signatureSettings = signatureSettings;
    }

    public Map<String, String> getPositionMap() {
        return positionMap;
    }

    public void setPositionMap(Map<String, String> positionMap) {
        this.positionMap = positionMap;
    }

    public Map<String, String> getAlignementMap() {
        return alignementMap;
    }

    public void setAlignementMap(Map<String, String> alignementMap) {
        this.alignementMap = alignementMap;
    }

    public String getDigitalSignaturePDFPluginPort() {
        return digitalSignaturePDFPluginPort;
    }

    public void setDigitalSignaturePDFPluginPort(String digitalSignaturePDFPluginPort) {
        this.digitalSignaturePDFPluginPort = digitalSignaturePDFPluginPort;
    }

    public File getUserImage() {
        return userImage;
    }

    public void setUserImage(File userImage) {
        this.userImage = userImage;
    }

    public String getUserImageFileName() {
        return userImageFileName;
    }

    public void setUserImageFileName(String userImageFileName) {
        this.userImageFileName = userImageFileName;
    }
//    ********************* actions **************
    // temp folder to save image

    private String getTempFolderOfSavedImages() {
        //base folder of saved signed and un signed files in client user machine
        String tempFolderPathOfFiles =
                System.getProperty("user.home") + File.separatorChar + "DigitalSignature" + File.separatorChar + "Images" +
                        File.separatorChar;
        File tempFolderOfFiles = new File(tempFolderPathOfFiles);
        if (!tempFolderOfFiles.exists()) {
            tempFolderOfFiles.mkdirs();
        }

        return tempFolderPathOfFiles;
    }

    private String convertPath(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0; i < stringBuilder.length(); i++) {

            if (stringBuilder.charAt(i) == '\\') {
                stringBuilder.replace(i, i + 1, "/");

            }
        }

        return stringBuilder.toString();


    }

    @Override
    public void prepare() throws Exception {

        System.out.println("Ya man ");
        // set buttom and hight
        positionMap = new HashMap<>();
        positionMap.put("1", "Buttom");
        positionMap.put("2", "Hight");


        alignementMap = new HashMap<>();
        alignementMap.put("1", "Right");
        alignementMap.put("2", "Left");
    }

}
