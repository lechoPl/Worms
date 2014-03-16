package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import utility.Board;
import utility.Point;

public class MainFrame extends JFrame {
    
    private int M, N;
    
    private Board boardEngine;
    private BoardPanel board;
    
    
    private JButton StartButton = new JButton("Start");
    private JButton StopButton = new JButton("Stop");
    
    private JMenuBar menu = new JMenuBar();
    
    private ActionListener StartButtonList = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(board != null)
                board.start();
        }
    };
    
    private ActionListener StopButtonList = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(board != null)
                board.stop();
        }
    };
    
    
    
    private MouseInputListener mouseList = new MouseInputAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if(board == null || boardEngine == null) return;

                Point temp = (( BoardPanel )( e.getSource() )).getField( e.getX(), e.getY() );

                if(temp == null) return;

                board.addNewWorm(temp);
            }
        }
    };
    
    
    public MainFrame(int N, int M){
        super("Robaki");
        
        this.N = N;
        this.M = M;
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //------- SIZE ------------
        Dimension dScreanSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int width = (2*dScreanSize.width)/3;
        int height = (2*dScreanSize.height)/3;
        
        
        this.setSize(width, height);
        
        this.setLocation((dScreanSize.width - width)/2,
                (dScreanSize.height - height)/2);
        
        
        //-----------------------------
        
        boardEngine = new Board(this.M, this.N);
        board = new BoardPanel(boardEngine);
        board.addMouseListener(mouseList);
        
        setLayout(new BorderLayout());
        
        menu.add(StartButton);
        StartButton.addActionListener(StartButtonList);
        
        menu.add(StopButton);
        StopButton.addActionListener(StopButtonList);
        
        add(menu, BorderLayout.PAGE_START);
        add(board, BorderLayout.CENTER);        
        
        this.setVisible(true);
    }
}
