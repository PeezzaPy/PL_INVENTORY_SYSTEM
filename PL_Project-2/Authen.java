import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Console;

public class Authen {
    final static String account_dir = "D:\\TUP SCHOOLWORKS\\2nd Year\\ACTIVITIES\\2ND SEM\\PROGRAMMING LANGUAGE\\PL-Project-Java\\PROJECT_INVENTORY\\PL_INVENTORY_SYSTEM\\PL_Project-2\\account\\";
    static Scanner console = new Scanner(System.in);
    static String name, username, password, ename, eusern, epass; 
    static String admin_fp, cashier_fp;


    static void inputValidation(){
        Main.validInput = false; 

        if(console.hasNextInt()){
            Main.choice = console.nextInt();
            Main.validInput = true;
            console.nextLine();
        }
        else {
            console.next();     // consume the invalid input
            Main.validInput = false;
        }
    }

    
    static int login(){ 
        Console cons;   
        do {
            Terminal.clearScreen();
            Terminal.gotoxy(15,10); System.out.println("=-=-= INVENTORY LOGIN =-=-=");
            Terminal.gotoxy(19,13); System.out.println("(1) Cashier");
            Terminal.gotoxy(19,15); System.out.println("(2) Admin");
            Terminal.gotoxy(19,17); System.out.println("(0) Exit");
            Terminal.gotoxy(15,20); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            Terminal.gotoxy(19,22); System.out.print("Select: ");
            Authen.inputValidation();  
        } while (!Main.validInput || Main.choice < 0 || Main.choice > 2);
        
        if(Main.choice == 0){
            Main.backToLogin = false;
            return 0;
        }

        Terminal.clearScreen();
        Terminal.gotoxy(15,10); System.out.println("=-=-=-= LOG IN =-=-=-=");
        Terminal.gotoxy(15,13); System.out.print("Enter username: ");
        username = console.nextLine();

        if((cons = System.console()) != null){
            Terminal.gotoxy(15,15);
            char[] charPass = cons.readPassword("Enter password: ");
            password = new String(charPass);
        }
        else {
            Terminal.gotoxy(15,15); System.out.print("Enter username: ");
            password = console.nextLine();
        }

        if(Main.choice == 1){          // cashier
            if(username.equals(Main.cashierAcc.getUsername())){
                if(password.equals(Main.cashierAcc.getPassword())){    
                    return 1;        // if found    
                }   
            }  
        }
        else if(Main.choice == 2){     // admin
            if(username.equals(Main.adminAcc.getUsername())){
                if(password.equals(Main.adminAcc.getPassword())){    
                    return 2;        // if found     
                }   
            }
        }
        return -1;               // status if not found 
    }

    
    

    public static void saveAccount(){      
        admin_fp = Security.encrypt(Security.getAdminFileName(), Security.getSecretKey());
        cashier_fp = Security.encrypt(Security.getCashierFileName(), Security.getSecretKey());
        
        // ADMIN ACCOUNT
        try (FileWriter writer = new FileWriter(account_dir + admin_fp + ".txt")){
            if(Main.adminAcc.getName()  != "N/A"){
                ename = Security.encrypt(Main.adminAcc.getName(), Security.getSecretKey());
                eusern = Security.encrypt(Main.adminAcc.getUsername(), Security.getSecretKey()); 
                epass = Security.encrypt(Main.adminAcc.getPassword(), Security.getSecretKey()); 
                writer.write(ename + '\n');
                writer.write(eusern + '\n');
                writer.write(epass + '\n');
            }
            writer.close();       
        } catch (IOException e){
        }
    
        // CASHIER ACCOUNT
        try (FileWriter writer = new FileWriter(account_dir + cashier_fp + ".txt")){
            if(Main.cashierAcc.getName() != "N/A"){
                ename = Security.encrypt(Main.cashierAcc.getName(), Security.getSecretKey());
                eusern = Security.encrypt(Main.cashierAcc.getUsername(), Security.getSecretKey()); 
                epass = Security.encrypt(Main.cashierAcc.getPassword(), Security.getSecretKey()); 
                writer.write(ename + '\n');
                writer.write(eusern + '\n');
                writer.write(epass + '\n');
            } 
            writer.close();           
        } catch (IOException e){
        } 
    }


    public static void retrieveAccount(){
        String data_line;
        admin_fp = Security.encrypt(Security.getAdminFileName(), Security.getSecretKey());
        cashier_fp = Security.encrypt(Security.getCashierFileName(), Security.getSecretKey());

        // ADMIN ACCOUNT
        try(BufferedReader reader = new BufferedReader(new FileReader(account_dir + admin_fp + ".txt"))){
            while((data_line = reader.readLine()) != null){
                if(data_line != "N/A"){
                    ename = data_line;   
                    eusern = reader.readLine();
                    epass = reader.readLine();
                    name = Security.decrypt(ename, Security.getSecretKey());
                    username = Security.decrypt(eusern, Security.getSecretKey());
                    password = Security.decrypt(epass, Security.getSecretKey());
                    Main.adminAcc = new Account(name, username, password);    
                }
                else
                    break;
            } 
            reader.close();
        } catch (IOException e){
        }

        // CASHIER ACCOUNT
        try(BufferedReader reader = new BufferedReader(new FileReader(account_dir + cashier_fp + ".txt"))){
            while((data_line = reader.readLine()) != null){
                if(data_line != "N/A"){
                    ename = data_line;   
                    eusern = reader.readLine();
                    epass = reader.readLine();
                    name = Security.decrypt(ename, Security.getSecretKey());
                    username = Security.decrypt(eusern, Security.getSecretKey());
                    password = Security.decrypt(epass, Security.getSecretKey());
                    Main.cashierAcc = new Account(name, username, password);
                }
                else 
                    break;
            }
            reader.close();
        } catch (IOException e){
        }
    }
}


