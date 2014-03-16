package utility;

import java.util.Random;
import java.util.Vector;

public class Worm implements Runnable{
    private static int NumberOfWorms = 1;
    
    public final int id;
    private final Board board;
    
    
    private Point head;
    private Vector<Point> tail;
    private DIR currentDir;
    
    private static Random rand  = new Random();
    
    public Worm(Point position, Board board){
        synchronized(board){
            id = NumberOfWorms;
            ++NumberOfWorms;

            this.board = board;

            head = position;
            
            board.setFiled(head, id);
            
            tail = new Vector<Point>();

            currentDir = DIR.values()[rand.nextInt(4)];
        }
    }
    
    /**
     * Okreslanie kierunku ruchu
     * @return nowy kierunek
     */
    public DIR getDirOfMove(){
        int t=1;
        int temp = 8*t;
        
        int randomInt = rand.nextInt(temp);
        
        //zachowanie kierunku
        if(randomInt < temp * (3.0/4.0)) return currentDir;
        
        //w lewo
        if(randomInt == temp * (3.0/4.0)){
            switch(currentDir){
                case DOWN:
                    return DIR.RIGHT;
                case RIGHT:
                    return DIR.UP;                    
                case UP:
                    return DIR.LEFT;
                case LEFT:
                    return DIR.DOWN;
            }
        }else{
            switch(currentDir){
                case DOWN:
                    return DIR.LEFT;
                case LEFT:
                    return DIR.UP;
                case UP:
                    return DIR.RIGHT;
                case RIGHT:
                    return DIR.DOWN;
            }
        }
        
        return null;
    }
    
    /**
     * wykoananie ruchu
     * @param d kierunek w ktorym sie porusza
     * @param p 
     */
    public void Move(DIR d){
        int val;
        if(head == null) return;
        
        synchronized(board){
            tail.add(head);

            /* przesuniecie glowy na dane pole
             * sprawdzenie co bylo na danym polu
             * -puste:
             *      zatrzymanie watku(zerowanie)
             * -attack:
             *      na wlasny ogon -> (zerowanie)
             *      na innego robaka -> dodatkowy ruch
             */
            
            currentDir = d;
            Point tempHead = board.getNextField(head, d);
            
            val = board.getField(tempHead);
            board.setFiled(tempHead, id);
            
            boolean BonusMove = false;
            
            if(val == 0){
                Point temp = tail.remove(0);
                    
                board.setFiled(temp, 0);
            }else{
                if(val == id){
                    this.attack(tempHead);
                }else{
                    /*
                     * val - 1 bo robaki liczymy od 1 a przechowywane sa w
                     * vectorze liczone od 0
                     * val to id zatokowanego robaka
                     */
                    Worm temp = board.getWorm(val - 1);

                    if(temp != null) temp.attack(tempHead);
                    
                    //BonusMove = true;
                }
            }
            
            head = tempHead;
            
            if(BonusMove){
                this.Move(getDirOfMove());
            }
        }
    }
    
    /**
     * Atakowanie przez innego robaka w punkcie p
     * @param p miejsce ataku
     */
    public void attack(Point p) {
        synchronized(board){
            if(p.equals(head)){
                head = null;
                
                for(int i = 0; i < tail.size(); i++){
                    if(board.getField(tail.get(i)) == id)
                        board.setFiled(tail.get(i), 0);
                }
                
                tail.removeAllElements();
                
                return;
            }else{   
                //obcinanie ogona
                int temp = -1;
                
                for(int i=0; i < tail.size(); i++){
                    if(tail.get(i).equals(p)){
                        temp = i;
                        break;
                    }
                }
                
                for(int i=0; i < temp ;i++){
                    //czysczenie pol planszy
                    if(board.getField(tail.get(i)) == id){
                        board.setFiled(tail.get(i),0);
                    }
                }
                
                Vector<Point> tempTail = new Vector<Point>();

                for(int i = temp + 1; i < tail.size(); i++){
                    tempTail.add(tail.get(i));
                }

                tail = tempTail;
                
            }
        }
    }
    
    public void life() throws InterruptedException{
        while(head != null){
            //zatrzymywanie watku(symulacja zerowania)
            int time = rand.nextInt(901) + 100;
            
            Thread.sleep(time);
            //poruszenie sie
            this.Move(this.getDirOfMove());
        }
    }

    @Override
    public void run() {
        try {
            this.life();
        } catch (InterruptedException ex) {}
    }
    
    public Point getHead(){
        return head;
    }
}
