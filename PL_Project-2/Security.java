import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Security {
    final private static String key_dir = "D:\\TUP SCHOOLWORKS\\2nd Year\\ACTIVITIES\\2ND SEM\\PROGRAMMING LANGUAGE\\PL-Project-Java\\PROJECT_INVENTORY\\PL_INVENTORY_SYSTEM\\Key\\key.txt";
    final private static String admin_fp = "adminacc";
    final private static String cashier_fp = "cashieracc";
    public static String previous_key;


    public static String encrypt(String text, int key){
        String encryptedText = "";
        for(int i=0; i<text.length(); i++){
            char ch = text.charAt(i);
            ch += key;
            encryptedText += ch;
        }
        return encryptedText;
    }


    public static String decrypt(String encryptedText, int key){
        String decryptedText = "";
        for(int i=0; i<encryptedText.length(); i++){
            char ch = encryptedText.charAt(i);
            ch -= key;
            decryptedText += ch;
        }
        return decryptedText;
    }

    
    public static void changeSecretKey(int my_secretKey){ 
        try(FileWriter writer = new FileWriter(key_dir)){
            writer.write(Integer.toString(my_secretKey));  
            writer.close();      
        } catch (IOException e){
        }
    }


    public static int getSecretKey(){
        try(BufferedReader reader = new BufferedReader(new FileReader(key_dir))){
            String data = reader.readLine();
            reader.close();
            return Integer.parseInt(data);

        } catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }


    public static void renameFile(String old_filename, String new_filename){
        File old_fp = new File(old_filename);
        File new_fp = new File(new_filename);

        if(!old_fp.exists()){
            try{
                old_fp.createNewFile();
            } catch (Exception e) {
                System.out.println("Exception occurred during file creating: " + e.getMessage());
                Terminal.clearScreen();
            }          
        }

        try {
            old_fp.renameTo(new_fp);
        } catch (Exception e){
            System.out.println("Exception occurred during file renaming: " + e.getMessage());
            Terminal.clearScreen();
        }
    }


    public static String getAdminFileName(){
        return admin_fp;
    }

    public static String getCashierFileName(){
        return cashier_fp;
    }
}
