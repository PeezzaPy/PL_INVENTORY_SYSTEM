import java.util.Scanner;


class Main {
    final static int MAX_INV = 100;
    final static int MAX_PROD_STOCK = 50;
    public static int i, marker, receiptMarker, choice, loginChoice;
    static boolean validInput, backToLogin;
    static Inventory[] my_inv = new Inventory[MAX_INV];
    static Receipt[] customerReceipt = new Receipt[MAX_INV];
    static Scanner console = new Scanner(System.in);
    static Account adminAcc = new Account();
    static Account cashierAcc = new Account();


    public static void main(String[] args){   
        init(); 
        Authen.retrieveAccount();
        DataManager.retrieve();
        DataManager.delExpiredProduct();

        while(true){
            startMenu();
            do {
                switch(Authen.login()){
                    case 0: break;
                    case 1: Cashier.cashier();
                            break;
                    case 2: Admin.admin();
                            break;
                    
                    default: backToLogin = true;
                             System.out.println("\n\nINVALID USERNAME/PASSWORD \n");
                             console.nextLine();
                }
            } while(backToLogin == true);
        }   
    }

    public static void startMenu(){
        do {
            Terminal.clearScreen();
            Terminal.gotoxy(15,10); System.out.println("=-=-= WELCOME BACK =-=-=");
            Terminal.gotoxy(19,13); System.out.println("(1) Login");
            Terminal.gotoxy(19,15); System.out.println("(0) Exit");
            Terminal.gotoxy(15,18); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-");
            Terminal.gotoxy(19,20); System.out.print("Select: ");
            Authen.inputValidation();  
        } while (!validInput || choice < 0 || choice > 1);

        if(choice == 0){
            Terminal.clearScreen();
            System.exit(0);
        }
    }


    public static void init(){
        marker = -1;
    }


    public static int isFull(){
        if(marker == MAX_INV-1)
            return 1;
        else
            return 0;
    }


    // Method Overload
    public static int locateProduct(Inventory my_product){
        for(i=0; i<=marker; i++){
            if(my_inv[i].name.equalsIgnoreCase(my_product.name))
                return i;
        }
        return -1;
    }
    // Method Overload
    public static int locateProduct(Receipt my_product){
        for(i=0; i<=Cashier.receiptMarker; i++){
            if(customerReceipt[i].productName.equalsIgnoreCase(my_product.productName))
                return i;
        }
        return -1;
    }
}