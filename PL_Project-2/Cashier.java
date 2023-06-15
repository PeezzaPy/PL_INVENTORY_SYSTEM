import java.util.Scanner;


public class Cashier {
    static Receipt[] totalReceipt = new Receipt[Main.MAX_INV];
    static Scanner console = new Scanner(System.in);
    static int totalMax, receiptMarker;

    public static void cashier() {
        Main.validInput = false;
        totalMax = -1;
        receiptMarker = -1;

        do {
            do {
                Terminal.clearScreen();
                Terminal.gotoxy(15,10); System.out.println("=-=-=-= CASHIER =-=-=-=");
                Terminal.gotoxy(15,13); System.out.println("(1) New Customer");
                Terminal.gotoxy(15,15); System.out.println("(0) Log out");
                Terminal.gotoxy(15,18); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=");
                Terminal.gotoxy(15,20); System.out.print("Select: ");

                if(console.hasNextInt()){
                    Main.choice = console.nextInt();
                    Main.validInput = true;
                }
                else {
                    console.next();     // consume the invalid input
                    Main.validInput = false;
                }
            } while (!Main.validInput);

            if (Main.choice == 1)
                Cashier.punch();
            else if (Main.choice == 0)
                Main.main(null);

        } while (Main.choice != 0);
    }


    public static void punch(){
        Inventory product = new Inventory();
        Receipt receipt = new Receipt();
        int inventoryPos, receiptPos;

        Terminal.clearScreen();
        Terminal.gotoxy(15,10); System.out.println("=-=-= PUNCH PRODUCT =-=-=");
        console.nextLine();                             
        Terminal.gotoxy(15,13); System.out.print("Product name: ");
        String productName = console.nextLine();
        receipt.productName = productName.toUpperCase();
        Terminal.gotoxy(15,15); System.out.print("Quantity: ");
        receipt.quantity = console.nextInt();

        product.name = receipt.productName;                     // pass receipt name to inventory name variable;
        inventoryPos = Main.locateProduct(product);             // locate the name to inventory

        if(inventoryPos == -1){                                 // if not exist
            Terminal.gotoxy(15,18); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=");
            Terminal.gotoxy(15,20); System.out.println("PRODUCT DOES NOT EXIST");
            receipt = new Receipt();                            // initialize again
            Main.console.nextLine();
        }
        else {
            if(Main.my_inv[inventoryPos].qty == 0 || Main.my_inv[inventoryPos].qty - receipt.quantity < 0){
                Terminal.gotoxy(15,18); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=");
                Terminal.gotoxy(15,20); System.out.println("INSUFFICIENT AMOUNT");
                receipt = new Receipt();        // initialize again
                console.nextLine();
            }
            else {
                receipt.price = Main.my_inv[inventoryPos].retail_price;
                receipt.totalPrice = receipt.quantity * receipt.price;

                if(Main.customerReceipt[0] == null){
                    addToReceipt(receipt);
                }
                else {
                    receiptPos = Main.locateProduct(receipt);
                    if(receiptPos == -1)
                        addToReceipt(receipt);
                    else {
                        Main.customerReceipt[receiptPos].quantity += receipt.quantity;
                        Main.customerReceipt[receiptPos].totalPrice += receipt.totalPrice;
                    }
                }
                
                Main.my_inv[inventoryPos].qty -= receipt.quantity;
                Main.my_inv[inventoryPos].sales_qty += receipt.quantity;
                Main.my_inv[inventoryPos].total_price = Main.my_inv[inventoryPos].qty * Main.my_inv[inventoryPos].orig_price;
                Main.my_inv[inventoryPos].total_sales_amount += Main.my_inv[inventoryPos].retail_price * receipt.quantity;
                Main.my_inv[inventoryPos].profit += Main.my_inv[inventoryPos].retail_price * receipt.quantity;
            }
        }       
        Terminal.gotoxy(20,24); System.out.println("Add product?");
        Terminal.gotoxy(18,25); System.out.println("[Y] Yes  [N] No");
        char choice = console.next().charAt(0);

        if(!receipt.productName.equalsIgnoreCase("N/a")){   
            totalMax++;                                                   
            totalReceipt[totalMax] = receipt;                             
        }

        if (choice == 'y' || choice == 'Y')
            Cashier.punch();
        else{
            if(totalMax > -1){                                            
                for(int i=0; i<=totalMax; i++)                            
                    DataManager.recordSales(totalReceipt[i]);             
                DataManager.save();                                       
                displayReceipt();                                     
            }
            initCustomerReceipt();                                             
            Cashier.cashier();                                          
        }
    }


    public static void addToReceipt(Receipt resibo){
        receiptMarker++;
        Main.customerReceipt[receiptMarker] = new Receipt(resibo.productName, resibo.quantity, resibo.price, resibo.totalPrice);
    }


    public static void displayReceipt(){
        int i;
        double totalPrice = 0.0;
        Terminal.clearScreen();                         
                
        Terminal.gotoxy(35,14); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-= RECEIPT =-=-=-=-=-=-=-=-=-=-=-=-=-=");
        Terminal.gotoxy(20,17); System.out.println("Product Name");
        Terminal.gotoxy(38,17); System.out.println("Price");
        Terminal.gotoxy(56,17); System.out.println("Quantity");
        Terminal.gotoxy(74,17); System.out.println("Total Amount");
        
        for(i=0; i<=receiptMarker; i++){
            Terminal.gotoxy(20,20+i); System.out.println(Main.customerReceipt[i].productName);
            Terminal.gotoxy(38,20+i); System.out.println(Main.customerReceipt[i].price);
            Terminal.gotoxy(56,20+i); System.out.println(Main.customerReceipt[i].quantity);
            Terminal.gotoxy(74,20+i); System.out.println(Main.customerReceipt[i].totalPrice);
            totalPrice += Main.customerReceipt[i].totalPrice;
        }
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        Terminal.gotoxy(20,21+i); System.out.println("Total Price: " + formattedTotalPrice);
        Terminal.gotoxy(35,24+i); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        Terminal.gotoxy(35,27+i); System.out.println("Press Enter to continue...");
        console.nextLine();
        console.nextLine();
    }


    public static void initCustomerReceipt(){
        for(int i=0; i<=receiptMarker; i++){
            Main.customerReceipt[i] = null;   
        }
    }
}
