package barnes.chess.db.stats;

import barnes.chess.db.DBCreate;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class Friends extends Application {

    public static void main(String[] args){
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        JTable tab;
        Object[][] data = new Object[10][3];
        Object[] columnName = {"Nick", "Active"};
        Random rand = new Random();
        int i;

        DBCreate db = new DBCreate();

        String query = "SELECT friendship.user2, userprofile.id, userprofile.nick FROM userprofile \n" +
                "INNER JOIN friendship ON userprofile.id = friendship.user1 \n" +
                "WHERE friendship.user2 = ";
        String query2 = "SELECT nick FROM userprofile WHERE id = ";

        Scanner scan = new Scanner(System.in);
        int id;
        JButton next = new JButton("next");
        JButton prev = new JButton("prev");
        while(true) {
            Statement state = db.getConnection().createStatement();
            System.out.println("Of which player do you want to see friends?: ");
            id = scan.nextInt();
            if(id == 0){
                System.out.println("Bye");
                break;
            }
            i = 0;
            try {
                next.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        /*countRight++;
                        for(int )*/
                    }
                });
                prev.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });


                ResultSet res2 = state.executeQuery(query2 + id);
                while (res2.next()){
                    System.out.println("Player: " + res2.getString(1));
                }
                ResultSet res = state.executeQuery(query + id);
                while (res.next() || i != 10) {
                    FriendList fr = new FriendList(res.getInt(1), res.getInt(2), res.getString(3));
                    data[i][0] = res.getString(3);
                    data[i++][1] = rand.nextBoolean();
                    System.out.println("id: " + res.getInt(1));
                    System.out.println("name: " + res.getString(3));
                    System.out.println("--------------");
                }
                break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        JFrame jf = new JFrame();
        jf.setSize(300, 170);

        tab = new JTable(data, columnName);
        JScrollPane scrlPane = new JScrollPane(tab);
        tab.setPreferredScrollableViewportSize(new Dimension(250, 100));
        jf.getContentPane().add(scrlPane);
        jf.setVisible(true);
    }
}
