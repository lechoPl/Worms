package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;
import utility.Board;
import utility.Point;

public class BoardPanel extends JPanel 
                        implements ActionListener{
    private Board board;
    private int sizeSquare;
    
    private int positionX = 0;
    private int positionY = 0;
    
    private Color CBoard = new Color(185,138,91);
    private Vector<Color> CWorms = new Vector<Color>();
    
    private Random rand = new Random();
    
    private javax.swing.Timer BoardTimer = new javax.swing.Timer(200, null);
    
    public BoardPanel(Board b){
        super();
        BoardTimer.addActionListener(this);
        
        board = b;
        
        for(int i=0; i<b.getNumberOfWorms();i++){
            int R = rand.nextInt(256);
            int G = rand.nextInt(256);
            int B = rand.nextInt(256);
            
            CWorms.add(new Color(R,G,B));
        }
        
        BoardTimer.start();
    }
    
    @Override
    public void paint(Graphics graphics){
        sizeSquare = (this.getSize().width < this.getSize().height ?
                this.getSize().width / board.Width :
                this.getSize().height / board.Height);
        
        if(sizeSquare * board.Width > this.getSize().width)
            sizeSquare = this.getSize().width / board.Width;
        
        if(sizeSquare * board.Height > this.getSize().height)
            sizeSquare = this.getSize().height / board.Height;
        
        positionX  = (this.getSize().width - board.Width * sizeSquare)/2;
        positionY  = (this.getSize().height - board.Height * sizeSquare)/2;
        
        super.paint(graphics);
        
        drawBoard(graphics);
        drawWorms(graphics);
    }
    
    void drawBoard(Graphics graphics){
        graphics.setColor(CBoard);
        graphics.fillRect(positionX,positionY, 
                sizeSquare*board.Width,  sizeSquare*board.Height);
        
        graphics.setColor(Color.black);
        graphics.drawRect(positionX,positionY, 
                sizeSquare*board.Width,  sizeSquare*board.Height);
        
        //rysowanie lini pionowych
        for(int i = 1; i < board.Width; ++i){
            graphics.setColor(Color.black);
            
            //rysowanie lini x
            int x1 = positionX + i * sizeSquare;
            int x2 = x1;
            int y1 = positionY;
            int y2 = positionY + sizeSquare * (board.Height);
            graphics.drawLine(x1, y1, x2, y2);
        }
        
        //rysowanie lini poziomych
        for(int i = 1; i < board.Height; ++i){
            graphics.setColor(Color.black);
            
            //rysowanie lini x
            int x1 = positionX;
            int x2 = positionX + sizeSquare * board.Width;
            int y1 = positionY + i * sizeSquare;
            int y2 = y1;
            graphics.drawLine(x1, y1, x2, y2);
        }
    }
    
    void drawWorms(Graphics graphics){
            int temp = (int)(sizeSquare * 0.1);
    
            for(int x = 0; x < board.Width; x++){
                for(int y = 0; y < board.Height; y++){
                    if(board.getField(new Point(x,y)) != 0){
                        graphics.setColor(CWorms.get(board.getField(new Point(x,y)) -1));

                        graphics.fillOval((sizeSquare * x) + positionX + temp/2,
                            (sizeSquare * y + temp/2) + positionY, sizeSquare - temp,
                            sizeSquare - temp);
                    }
                }
            }

            for(int id = 0; id < board.getNumberOfWorms(); id++){
                if(board.getWorm(id) == null) continue;

                Point tempPoint = board.getWorm(id).getHead();
                if(tempPoint == null) continue;
                
                //graphics.setColor(Color.black);
                graphics.setColor(CWorms.get(board.getField(new Point(tempPoint.x,tempPoint.y)) -1));

                graphics.fillRect((sizeSquare * tempPoint.x) + positionX + temp/2,
                            (sizeSquare * tempPoint.y + temp/2) + positionY, sizeSquare - temp,
                            sizeSquare - temp);

            }
    }
    
    public void start(){
        //BoardTimer.start();
        board.start();
    }
    
    public void stop(){
        board.stop();
        //BoardTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }
    
    public Point getField(int x, int y){
        if(x < positionX || y < positionY)
            return null;
        
        x = (x - positionX) / sizeSquare;
        y = (y - positionY) / sizeSquare;
        
        if(x >= board.Width || y >= board.Height)
            return null;
        
        return new Point(x, y);
    }
    
    public void addNewWorm(Point p){
        
        int R = rand.nextInt(256);
        int G = rand.nextInt(256);
        int B = rand.nextInt(256);
            
        CWorms.add(new Color(R,G,B));
        
        
        board.addNewWorm(p);
    }
}
