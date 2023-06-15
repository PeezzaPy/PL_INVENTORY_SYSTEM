import java.util.Scanner;


public class Admin {
    static Scanner console = new Scanner(System.in);
    static String name, username, password;

    public static void admin(){
        Inventory product = new Inventory();
        int pos;

        do {
        //save_profit_history();
            do {
                Terminal.clearScreen();
                Terminal.gotoxy(15,10); System.out.println("=-=-= ADMIN INVENTORY =-=-=");
                Terminal.gotoxy(19,13); System.out.println("(1) Add Product");
                Terminal.gotoxy(19,15); System.out.println("(2) Display");
                Terminal.gotoxy(19,17); System.out.println("(3) Settings");
                Terminal.gotoxy(19,19); System.out.println("(0) Log out");
                Terminal.gotoxy(15,22); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                Terminal.gotoxy(19,24); System.out.print("Select: ");
                Authen.inputValidation();
            } while (!Main.validInput || Main.choice < 0 || Main.choice > 3);
            
            Terminal.clearScreen();
            if(Main.choice == 1){
                Terminal.gotoxy(15,10); System.out.println("=-=-= ADD PRODUCT =-=-=");
                product.category = Category.setGetCategory();
                System.out.println(product.category);
                Terminal.clearScreen();
                Terminal.gotoxy(15,10); System.out.println("=-=-= ADD PRODUCT =-=-=");
                Terminal.gotoxy(15,13); System.out.println("Category: " + product.category);
                Terminal.gotoxy(15,15); System.out.print("Product Name: ");
                String product_name = console.nextLine();
                product.name = product_name.toUpperCase();
                Terminal.gotoxy(15,17); System.out.print("Price (each): ");
                product.orig_price = console.nextDouble();
                Terminal.gotoxy(15,19); System.out.print("Quantity: ");
                product.qty = console.nextInt();

                // set total priice
                product.total_price = product.qty * product.orig_price;

                Terminal.gotoxy(15,21); System.out.print("Retail price (each): ");
                product.retail_price = console.nextDouble();    
                // Get the current date/time
                product.date = DateManager.setDate();
                // Set and get expiration date
                product.exp_date = DateManager.setGetExpirationDate(product.category);

                // locate if already exist or not
                pos = Main.locateProduct(product);
                if(pos == -1){
                    product.sales_qty = 0;
                    product.total_sales_amount = 0.0;
                    product.profit = product.total_price * -1;
                    
                    addProduct(product);
                }
                else       // if exist update the product 
                    updateProduct(product, pos);       

                Terminal.gotoxy(15,24); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-");
                Terminal.gotoxy(15,26); System.out.println("ADDED SUCCESSFULLY");
                console.nextLine();

                DataManager.recordProduct(product);         // recording data to history
                DataManager.save();                         // save

            }
            else if(Main.choice == 2)
                display();
            else if(Main.choice == 3)
                settingsMenu();     
            
        } while (Main.choice != 0);
        
        if(Main.choice == 0);
            Main.backToLogin = true;
    } 




    public static void addProduct(Inventory my_product){
        if(Main.isFull() == 1){
            Terminal.clearScreen();
            Terminal.gotoxy(15,10); System.out.println("ARRAY IS FULL");
            console.nextLine();
        }
        else {
            if(my_product.qty <= Main.MAX_PROD_STOCK){
                Main.marker++;
                Main.my_inv[Main.marker] = new Inventory(my_product.category, my_product.name, my_product.date, my_product.exp_date, my_product.orig_price, my_product.total_price, my_product.qty, my_product.retail_price, my_product.sales_qty, my_product.total_sales_amount, my_product.profit);
            }
            else {
                System.out.println("QUANTITY LIMIT EXCEEDED");
                console.nextLine();
            }
        }
    }   


    public static void updateProduct(Inventory my_product, int indexPos) {
        if((Main.my_inv[indexPos].qty + my_product.qty) > Main.MAX_PROD_STOCK){
            System.out.println("QUANTITY LIMIT EXCEEDED");
            console.nextLine();
        }
        else
            Main.my_inv[indexPos].date = my_product.date;
            Main.my_inv[indexPos].exp_date = my_product.exp_date;
            Main.my_inv[indexPos].orig_price = my_product.orig_price;
            Main.my_inv[indexPos].qty += my_product.qty;
            Main.my_inv[indexPos].total_price = my_product.orig_price * Main.my_inv[indexPos].qty;
            Main.my_inv[indexPos].retail_price = my_product.retail_price;
            Main.my_inv[indexPos].profit -= my_product.qty * my_product.orig_price;
        }


    public static void display(){
        int i;
        Terminal.clearScreen();

        if (Main.marker == -1) {    // empty
            Terminal.gotoxy(50, 13); System.out.println("| NOTHING IS IN THE INVENTORY | ");
            Terminal.designBox(48, 11, 5, 35);
            Terminal.gotoxy(50, 17); System.out.println("Press any key to continue...");
            console.nextLine();
        } 
        else {
            Terminal.gotoxy(17, 13); System.out.printf("MAX PRODUCT: %d", Main.MAX_INV);
            Terminal.gotoxy(17, 14); System.out.printf("MAX STOCK PER PRODUCT: %d", Main.MAX_PROD_STOCK);
            Terminal.gotoxy(86, 13); System.out.println("INVENTORY REPORT");
            Terminal.designBox(60, 12, 3, 69);
            Terminal.gotoxy(15, 17); System.out.println("No");
            Terminal.gotoxy(25, 17); System.out.println("Date");
            Terminal.gotoxy(38, 17); System.out.println("Product Name");
            Terminal.gotoxy(56, 17); System.out.println("Orig Price");
            Terminal.gotoxy(71, 17); System.out.println("Quantity");
            Terminal.gotoxy(84, 17); System.out.println("Total Amount");
            Terminal.gotoxy(102, 17); System.out.println("Retail Price");
            Terminal.gotoxy(120, 17); System.out.println("Sales Quantity");
            Terminal.gotoxy(139, 17); System.out.println("Total Sales Amount");
            Terminal.gotoxy(165, 17); System.out.println("Profit");

            // border
            for (i = 0; i <= Main.marker + 2; i++) {
                if (i == 1) {
                    for (int j = 13; j <= 178; j++) {
                        Terminal.gotoxy(j, 16); System.out.print("-");
                        Terminal.gotoxy(j, 18); System.out.print("-");
                        Terminal.gotoxy(j, 18 + Main.marker + 2); System.out.print("-");
                    }
                } else {
                    Terminal.gotoxy(13, 17 + i); System.out.print("|");
                    Terminal.gotoxy(19, 17 + i); System.out.print("|");
                    Terminal.gotoxy(34, 17 + i); System.out.print("|");
                    Terminal.gotoxy(53, 17 + i); System.out.print("|");
                    Terminal.gotoxy(68, 17 + i); System.out.print("|");
                    Terminal.gotoxy(81, 17 + i); System.out.print("|");
                    Terminal.gotoxy(98, 17 + i); System.out.print("|");
                    Terminal.gotoxy(117, 17 + i); System.out.print("|");
                    Terminal.gotoxy(136, 17 + i); System.out.print("|");
                    Terminal.gotoxy(159, 17 + i); System.out.print("|");
                    Terminal.gotoxy(178, 17 + i); System.out.print("|");
                }
            }

            // displaying product status
            for(i = 0; i <= Main.marker; i++) {
                Terminal.gotoxy(15, 19+i); System.out.printf("%d", i + 1);
                Terminal.gotoxy(22, 19+i); System.out.printf("%s", Main.my_inv[i].date);
                Terminal.gotoxy(38, 19+i); System.out.printf("%s", Main.my_inv[i].name);
                String formattedOrigPrice = String.format("%.2f", Main.my_inv[i].orig_price);
                Terminal.gotoxy(58, 19+i); System.out.printf(formattedOrigPrice);
                Terminal.gotoxy(73, 19+i); System.out.printf("%d", Main.my_inv[i].qty);
                String formattedTotalPrice = String.format("%.2f", Main.my_inv[i].total_price);
                Terminal.gotoxy(87, 19+i); System.out.printf(formattedTotalPrice);
                String formattedRetailPrice = String.format("%.2f", Main.my_inv[i].retail_price);
                Terminal.gotoxy(105, 19+i); System.out.printf(formattedRetailPrice);
                Terminal.gotoxy(124, 19+i); System.out.printf("%d", Main.my_inv[i].sales_qty);
                String formattedTotalSalesAmount = String.format("%.2f", Main.my_inv[i].total_sales_amount);
                Terminal.gotoxy(143, 19+i); System.out.printf(formattedTotalSalesAmount);
                String formattedProfit = String.format("%.2f", Main.my_inv[i].profit);
                Terminal.gotoxy(165, 19+i); System.out.printf(formattedProfit);
            }
            Terminal.gotoxy(20,23+i); System.out.println("Press Enter to continue...");
            console.nextLine();
        }
    }


    public static void settingsMenu(){
        do {
            do {
                Terminal.clearScreen();
                Terminal.gotoxy(15,10); System.out.println("=-=-= SETTINGS =-=-=");
                Terminal.gotoxy(15,13); System.out.println("(1) Cashier");
                Terminal.gotoxy(15,15); System.out.println("(2) Admin");
                Terminal.gotoxy(15,17); System.out.println("(3) Change Encryption Key");
                Terminal.gotoxy(15,19); System.out.println("(0) Exit");
                Terminal.gotoxy(15,22); System.out.println("=-=-=-=-=-=-=-=-=-=-");
                Terminal.gotoxy(15,24); System.out.print("Select: ");
                Authen.inputValidation();
            } while (!Main.validInput || Main.choice < 0 || Main.choice > 3);
            
            if(Main.choice != 0)
                settings(Main.choice);          // default to avoid duplicate exits  
        } while (Main.choice != 0);

        if(Main.choice == 0)
            Main.choice = -1;
    }


    public static void settings(int my_choice){
        do {
            boolean isChange = false;
            if(my_choice == 1 || my_choice == 2){
                do {
                    Terminal.clearScreen();
                    if(my_choice == 1){
                        Terminal.gotoxy(15,10); System.out.println("=-=-= CASHIER SETTINGS =-=-=");
                    }
                    else if(my_choice == 2){
                        Terminal.gotoxy(15,10); System.out.println("=-=-= ADMIN SETTINGS =-=-=");
                    }
                    // update info
                    Terminal.gotoxy(15,13); System.out.println("(1) Change Name");
                    Terminal.gotoxy(15,15); System.out.println("(2) Change Username");
                    Terminal.gotoxy(15,17); System.out.println("(3) Change Password");
                    Terminal.gotoxy(15,19); System.out.println("(0) Exit");
                    Terminal.gotoxy(15,22); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    Terminal.gotoxy(15,24); System.out.print("Select: ");
                    Authen.inputValidation();
                } while (!Main.validInput || Main.choice < 0 || Main.choice > 3);
            }

            if(Main.choice == 0){
                Main.choice = -1;
                return;
            }
            else {
                Terminal.clearScreen();
                if(my_choice == 1){                // cashier settings
                    name = Main.cashierAcc.getName();
                    username = Main.cashierAcc.getUsername();
                    password = Main.cashierAcc.getPassword();

                    if(Main.choice == 1){
                        Terminal.gotoxy(15,10); System.out.println("Current name: " + Main.cashierAcc.getName());
                        Terminal.gotoxy(15,12); System.out.print("New name: ");
                        name = console.nextLine();
                        if(name.equals(Main.cashierAcc.getName())){
                            Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,17); System.out.println("NEW NAME MUST NOT BE THE SAME AS CURRENT NAME");
                            console.nextLine();
                        }
                        else {
                            isChange = true;
                            Main.cashierAcc = new Account(name, username, password);
                        }
                    } 
                    else if(Main.choice == 2){
                        Terminal.gotoxy(15,10); System.out.println("Current Username: " + Main.cashierAcc.getUsername());
                        Terminal.gotoxy(15,12); System.out.print("New username: ");
                        username = console.nextLine();
                        if(username.equals(Main.cashierAcc.getUsername())){
                            Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,17); System.out.println("NEW USERNAME MUST NOT BE THE SAME AS CURRENT USERNAME");
                            console.nextLine();
                        }
                        else {
                            isChange = true;
                            Main.cashierAcc = new Account(name, username, password);
                        }
                    }
                    else if(Main.choice == 3){
                        Terminal.gotoxy(15,10); System.out.println("Current Password: " + Main.cashierAcc.getPassword());
                        Terminal.gotoxy(15,12); System.out.print("New password: ");
                        password = console.nextLine();
                        if(password.equals(Main.cashierAcc.getPassword())){
                            Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,17); System.out.println("NEW PASSWORD MUST NOT BE THE SAME AS CURRENT PASSWORD");
                            console.nextLine();
                        }
                        else {
                            isChange = true;
                            Main.cashierAcc = new Account(name, username, password);
                        }
                    }
                }
                else if(my_choice == 2){            // admin settings
                    name = Main.adminAcc.getName();
                    username = Main.adminAcc.getUsername();
                    password = Main.adminAcc.getPassword();

                    if(Main.choice == 1){
                        Terminal.gotoxy(15,10); System.out.println("Current name: " + Main.adminAcc.getName());
                        Terminal.gotoxy(15,12); System.out.print("New name: ");
                        name = console.nextLine();
                        if(name.equals(Main.adminAcc.getName())){
                            Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,17); System.out.println("NEW NAME MUST NOT BE THE SAME AS CURRENT NAME");
                            console.nextLine();
                        }
                        else {
                            isChange = true;
                            Main.adminAcc = new Account(name, username, password);
                        }
                    } 
                    else if(Main.choice == 2){
                        Terminal.gotoxy(15,10); System.out.println("Current Username: " + Main.adminAcc.getUsername());
                        Terminal.gotoxy(15,12); System.out.print("New username: ");
                        username = console.nextLine();
                        if(username.equals(Main.adminAcc.getUsername())){
                            Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,17); System.out.println("NEW USERNAME MUST NOT BE THE SAME AS CURRENT USERNAME");
                            console.nextLine();
                        }
                        else {
                            isChange = true;
                            Main.adminAcc = new Account(name, username, password);
                        }
                    }
                    else if(Main.choice == 3){
                        String temp_password;
                        Terminal.gotoxy(15,10); System.out.println("Current Password: " + Main.adminAcc.getPassword());
                        Terminal.gotoxy(15,12); System.out.print("New password: ");
                        password = console.nextLine();
                        Terminal.gotoxy(15,14);System.out.print("Re-enter password: ");
                        temp_password = console.nextLine();
                        if(password.equals(Main.adminAcc.getPassword())){
                            Terminal.gotoxy(15,17); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                            Terminal.gotoxy(15,19); System.out.println("NEW PASSWORD MUST NOT BE THE SAME AS CURRENT PASSWORD");
                            console.nextLine();
                        }
                        else {
                            if(password.equals(temp_password)){
                                isChange = true;
                                Main.adminAcc = new Account(name, username, password);
                            }
                            else {
                                Terminal.gotoxy(15,17); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                                Terminal.gotoxy(15,19); System.out.println("NEW PASSWORD DOES NOT MATCHED");
                                console.nextLine();
                            }
                        }
                    }
                }
                else if(my_choice == 3){
                    Terminal.gotoxy(15,10); System.out.println("Current encryption key: " + Security.getSecretKey());
                    Terminal.gotoxy(15,12); System.out.print("New encryption key: ");
                    int new_key = Integer.parseInt(console.nextLine());
                    if(new_key == Security.getSecretKey()){
                        Terminal.gotoxy(15,15); System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                        Terminal.gotoxy(15,17); System.out.println("NEW KEY MUST NOT BE THE SAME AS CURRENT KEY");
                        console.nextLine();
                    }
                    else {
                        // store previous
                        String old_admin_fp = Security.encrypt(Security.getAdminFileName(), Security.getSecretKey());
                        String old_cashier_fp = Security.encrypt(Security.getCashierFileName(), Security.getSecretKey());
                        Security.changeSecretKey(new_key);
                        // store new ones
                        String new_admin_fp = Security.encrypt(Security.getAdminFileName(), Security.getSecretKey());
                        String new_cashier_fp = Security.encrypt(Security.getCashierFileName(), Security.getSecretKey());
                        
                        Security.renameFile(Authen.account_dir + old_admin_fp + ".txt", Authen.account_dir + new_admin_fp + ".txt");
                        Security.renameFile(Authen.account_dir + old_cashier_fp + ".txt", Authen.account_dir + new_cashier_fp + ".txt");
                        
                        Authen.saveAccount();
                    }
                }

                if(Main.choice != 0 && isChange == true){
                    Terminal.gotoxy(15,17); System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                    Terminal.gotoxy(15,19); System.out.println("CHANGED SUCCESSFULLY");
                    Main.console.nextLine();
                    Authen.saveAccount();
                }

                if(my_choice == 3)
                    Main.choice = 0;
            }
        } while (Main.choice != 0);
    }
}
