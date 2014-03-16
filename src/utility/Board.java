package utility;

import java.util.Random;
import java.util.Vector;

public class Board {
    public final int Height;
    public final int Width;
    
    Vector<Worm> worms = new Vector<Worm>();
    Vector<Thread> threads = new Vector<Thread>();
    
    boolean isStart;
    
    /**
     * pierwszy parametr to kolumna(x)
     * drugi parametr to wiersza(y)
     */
    private int[][] board;
    private static Random rand = new Random();
    
    public Board(int width, int height){
        isStart = false;
        
        this.Width = width;
        this.Height = height;
        
        board = new int[Width][Height];
        
        for(int x=0; x < Width; x++){
            for(int y=0; y<Height; y++){
                board[x][y] = 0;
            }
        }
        
        
        //liczba robakow width + height
        for(int i=0; i < Width+Height; i++){
            Point tempPoint;
            do{
                tempPoint = new Point(rand.nextInt(Width), rand.nextInt(Height));
            }while(board[tempPoint.x][tempPoint.y] != 0);
            
            Worm tempWorm = new Worm(tempPoint, this);
            
            worms.add(tempWorm);
        }
    }
    
    public void start(){
        isStart = true;
        
        for(int i=0; i < worms.size(); i++){
            Thread temp = new Thread(worms.elementAt(i));
            threads.add(temp);
            
            temp.start();
        }
    }
    
    public void stop(){
        isStart = false;
        
        for(int i=0; i < threads.size(); i++){
            threads.get(i).interrupt();
        }
    }
    
    
    
    /**
     * Pobieranie nastepnego pola do którego możemy sie przemiścić
     * @param p obecny pole
     * @param d kierunek
     * @return nastepne pole
     */
    public Point getNextField(Point p, DIR d){
        if(p == null) return null;
        
        int next_x, next_y;
        
        switch(d){
            case UP:
                next_y = (p.y == 0 ? next_y = Height-1 : p.y - 1);
                next_x = p.x;
                break;
                
            case RIGHT:
                next_y = p.y;
                next_x = (p.x == Width - 1 ? 0 : p.x + 1 );
                break;
                
            case DOWN: 
                next_y = (p.y == Height - 1 ? 0 : p.y + 1);
                next_x = p.x;
                break;
                
            case LEFT: 
                next_y = p.y;
                next_x = (p.x == 0 ? Width - 1 : p.x - 1 );
                break;
            default:
                next_x = next_y = -1;
        }
        
        return new Point(next_x, next_y);
    }
    
    /**
     * @param p wsp pola
     * @return wartosc pola(0 oznacza pole puste)
     */
    public int getField(Point p){
        return this.board[p.x][p.y]; 
    }
    
    /**
     * zajecie pola przez robaka o podanym id
     * @param val
     * @param id
     * @return 0 jesli pole bylo puste lub id robaka ktory ta przebywal
     */
    public void setFiled(Point p, int val){
        this.board[p.x][p.y] = val;
    }
    
    public Worm getWorm(int id){
        if(id < 0 || id >= worms.size()) return null;
        
        return worms.get(id);
    }
    
    public int getNumberOfWorms(){
        return worms.size();
    }
    
    public void addNewWorm(Point p) {
        synchronized(this){
            int val = this.getField(p);

            Worm tempWorm = new Worm(p, this);
            worms.add(tempWorm);

            Thread newThread = new Thread(tempWorm);
            threads.add(newThread);

            if(val != 0){
                Worm temp = this.getWorm(val - 1);

                if(temp != null)
                    temp.attack(p);
            }

            if(isStart){
                newThread.start();
            }
        }
    }
}
