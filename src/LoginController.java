/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.HttpCookie;
/**
 *
 * @author 100116544
 */
public class LoginController {
    private final Connection con;
    
    public LoginController() throws Exception{
        this.con = Database.getConnection();
    }

    public void checkLoginDetails(String username, char[] password) throws Exception {
        int resultRows = 0;
        
        String dbPassword = new String(password);
        try {
            dbPassword = Database.passwordDigest(dbPassword);
        } catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        String sql = "SELECT userID, COUNT(*) FROM user "
                + "WHERE username = ? and password = ?";
        PreparedStatement st = this.con.prepareStatement(sql);
        st.setString(1, username);
        st.setString(2, dbPassword);
        
        ResultSet result = st.executeQuery();
        while(result.next()){
            resultRows = result.getInt(2);
        }

        if(resultRows == 1){
//            String id = Integer.toString(result.getInt("userID"));
//            HttpCookie login = new HttpCookie(username, id);
//            login.setMaxAge(30*60);
            System.out.println("You have logged in!");
        } else {
            System.out.println("Login failed.");
        }
    }
    
    
}
