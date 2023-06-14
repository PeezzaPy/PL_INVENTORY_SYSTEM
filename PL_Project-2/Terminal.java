public class Terminal {

    static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }  


    static void designBox(int x, int y, int max_width, int max_length) {
        for (int i = 0; i < max_width; i++) {
            for (int j = 0; j < max_length; j++) {
                if (i == 0 || i == max_width - 1) {
                    if (j == 0) {
                        if (i == 0) {
                            gotoxy(x + j, y + i);
                            System.out.print((char) 201);
                        } else if (i == max_width - 1) {
                            gotoxy(x + j, y + i);
                            System.out.print((char) 200);
                        }
                    } else if (j == max_length - 1) {
                        if (i == 0) {
                            gotoxy(x + j, y + i);
                            System.out.print((char) 187);
                        } else if (i == max_width - 1) {
                            gotoxy(x + j, y + i);
                            System.out.print((char) 188);
                        }
                    } else {
                        gotoxy(x + j, y + i);
                        System.out.print((char) 205);
                    }
                } else {
                    if (j == 0 || j == max_length - 1) {
                        gotoxy(x + j, y + i);
                        System.out.print((char) 179);
                    }
                }
            }
        }
    }


    static void gotoxy(int x, int y){
        System.out.print("\033[" + y + ";" + x + "H");
}
}
