package barnes.chess.db.stats;

import barnes.chess.db.DB;
import barnes.chess.db.DBCreate;
import barnes.chess.db.DatabaseConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Friends {

    private ObservableList<FriendList> personData = FXCollections.observableArrayList();

    public static void main(String[] args){
        DBCreate db = new DBCreate();

        String query = "SELECT friendship.user2, userprofile.id, userprofile.nick FROM userprofile \n" +
                "INNER JOIN friendship ON userprofile.id = friendship.user1 \n" +
                "WHERE friendship.user2 = ";
        String query2 = "SELECT nick FROM userprofile WHERE id = ";

        Scanner scan = new Scanner(System.in);
        int id;
        while(true) {
            System.out.println("Of which player do you want to see friends?: ");
            id = scan.nextInt();
            if(id == 0){
                System.out.println("Bye");
                break;
            }
            try {
                Statement state = db.getConnection().createStatement();
                ResultSet res2 = state.executeQuery(query2 + id);
                while (res2.next()){
                    System.out.println("Player: " + res2.getString(1));
                }
                ResultSet res = state.executeQuery(query + id);
                while (res.next()) {
                    FriendList fr = new FriendList(res.getInt(1), res.getInt(2), res.getString(3));
                    System.out.println("id: " + res.getInt(1));
                    System.out.println("name: " + res.getString(3));
                    System.out.println("--------------");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
