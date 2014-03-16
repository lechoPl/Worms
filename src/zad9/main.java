package zad9;

import gui.MainFrame;

public class main {

    public static void main(String[] args) {
        //339 x 680 MAX
        int N = 10, M = 10;
        
        if(args.length == 2){
            int temp;
            
            try{
                temp = new Integer(args[0]);
                N = temp;
            }catch(NumberFormatException ex) {}
            
            try{
                temp = new Integer(args[1]);
                M = temp;
            }catch(NumberFormatException ex) {}
        }
        
        new MainFrame(N, M);
    }
}
