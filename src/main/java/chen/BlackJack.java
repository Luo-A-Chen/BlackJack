package chen;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BlackJack {
    //===状态字段
    ArrayList<Card> deck;
    Random random = new Random();

    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;
    boolean gameOver = false ;

    int boardWidth = 600;
    int boardHeight = 600;
    int cardWidth = 100;
    int cardHeight = 150;

    JFrame frame = new JFrame("BlackJack");
    JPanel panel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            if(gameOver){
                drawCard(g,hiddenCard,20,20);
            }else{
                drawCardBack(g,20,20);
            }

            for(int i = 0;i<dealerHand.size();i++){
                drawCard(g,dealerHand.get(i),cardWidth+25+(cardWidth+5)*i,20);
            }

            for(int i = 0;i<playerHand.size();i++){
                drawCard(g,playerHand.get(i),20+(cardWidth+5)*i,320);
            }

            if(gameOver){
                g.setFont(new Font("Arial",Font.BOLD,28)));
                g.setColor(Color.WHITE);
                g.drawString(getResult(),190,260);
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    
    private void drawCard(Graphics g,Card card,int x,int y){
        Image img = null;
        java.net.URL url = getClass().getResource(card.getImagePath());
        if(url != null){
            img = new ImageIcon(url).getImage();
        }
        if(img != null){
            g.drawImage(img,x,y,cardWidth,cardHeight,null);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(x,y,cardWidth,cardHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x,y,cardWidth,cardHeight);
            g.setFont(new Font("Arial",Font.BOLD,22));
            g.drawString(card.getValue(),x+12,y+35;
        }
    }

    private void drawCardBack(Graphics g, int x, int y) {
        Image img = null;
        java.net.URL url = getClass().getResource("/cards/back.png");
        if (url != null) {
            img = new ImageIcon(url).getImage();
        }
        if (img != null) {
            g.drawImage(img, x, y, cardWidth, cardHeight, null);
        } else {
            g.setColor(new Color(30, 80, 180));
            g.fillRect(x, y, cardWidth, cardHeight);
            g.setColor(Color.WHITE);
            g.drawRect(x, y, cardWidth, cardHeight);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("BACK", x + 22, y + 80);
        }
    }

    Black Jack(){
        startGame();

        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativaTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53 , 101 , 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitBUtton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        gamePanel.add(buttonPanel,BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener()){
            public void actionPerformed(ActionEvent e){
                playerHit();
                if(gameOver){
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        }
        
        stayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                playerStay();
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                gamePanel.repatin();
            }
        });

        frame.setVisible(true);
        gamePanel.repaint()
    }


    //总数
    public void buildDeck(){
        deck = new ArrayList<>();

        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"S","H","C","D"};

        for(int i = 0; i < values.length; i++){
            for(int j = 0; j < types.length; j++){
                deck.add(new Card(values[i],types[j]));
            }
        }
    }

    public void shuffleDeck(){
        for(int i = 0;i < deck.size();i++){
            int j = random.nextInt(deck.size());

            Card temp = deck.get(i);
            deck.set(i,deck.get(j));
            deck.set(j,temp);
        }
    }

    public void startGame(){
        buildDeck();
        shuffleDeck();

        dealerHand = new ArrayList<>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1: 0;
        dealerHand.add(card);

        playerHand = new ArrayList<>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i=0;i<2;i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

    }

    public int reducePlayAce(){
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -=10;
            playerAceCount-=1;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -=10;
            dealerAceCount-=1;
        }
        return dealerSum;
    }

    public void playrHit(){
        Card card = deck.remove(deck.size()-1);
        playerSum += card.getValue();
        playerAceCount += card.isAce() ? 1 : 0;
        playerHand.add(card);
        if(reducePlayAce()>21){
            gameOver = true;
        };
    }

    public void playerStay(){
        gameOver = true;
        while(dealerSum < 17){
            Card card = deck.remove(deck.size() - 1);
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);
        }
        reduceDealerAce();
    }

    public String getResult(){
        int p = reducePlayAce();
        int d = reduceDealerAce();
        if(p>21){
            return "You Bust!";
        }
        if(d>21){
            return "Dealer Bust!";
        }
        if(p == d){
            return "Tie!";
        }
        if(p>d){
            return "You Win!";
        }
        if(p<d){
            return "Dealer Win!";
        }
        return "You Lose!";
    }



    private class Crad{
        String value;
        String type;//S,H,C,D黑红梅方

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }

        public String toString(){
            return value +"-"+type;
        }

        public int getValue(){
            if("AJQK".contains(value)){
                if(value.equals("A")){
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce(){
            return value.equals("A");
        }

        public String getImagePath(){
            return "/cards/" + toString() + ".png";
        }

    }

}
