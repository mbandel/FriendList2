package com.example.maciek.friendlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SearchFriendActivity extends AppCompatActivity {

    private ListView userList;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    String get_firstname, get_lastname, get_email;
    User user;
    User userTest;
    ArrayList<User> userArrayList;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        connectionClass = new ConnectionClass();
        userList=(ListView)findViewById(R.id.userListId);
        result = (TextView) findViewById(R.id.result);
        user=new User();
        userTest=new User("Jan", "Kowalski","janek@wp.pl", "janek123");
        userArrayList = new ArrayList<User>();
       // userArrayList.add(userTest);

        SelectUsers selectUsers = new SelectUsers();
        selectUsers.execute("");

        UserAdapter adapter = new UserAdapter(this, userArrayList);
        userList.setAdapter(adapter);


    }


    public class SelectUsers extends AsyncTask<String,String,String>
    {
        String z="komunikat";



        @SuppressLint("NewApi")
        public Connection CONN() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnURL =String.format("jdbc:jtds:sqlserver://adamserver2137.database.windows.net:1433/Users;" +
                        "user=adamserver2137@adamserver2137;password=#zbigniewstonoga1;encrypt=true;" +
                        "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

                conn = DriverManager.getConnection(ConnURL);

            } catch (SQLException e) {
                e.printStackTrace();
                result.append("sqlex "+e.getMessage().toString());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                 result.append("classnotfound   "+e.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
                  result.append(" zwykly "+e.getMessage());
            }
            //Toast.makeText(getBaseContext(),""+wiad,Toast.LENGTH_LONG).show();
            return conn;
        }

        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            //result.setText("Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                userArrayList.add(userTest);
                Connection con = CONN();
                if (con == null) {
                    z = "Connection failed";
                    result.append("Connection failed");
                } else {

                    String query = " select * from user_db";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    user.setFirstName(rs.getString(2));
                    user.setLastName(rs.getString(3));
                    user.setEmail(rs.getString(4));
                    userArrayList.add(user);

                }

            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions: " + ex;
                result.setText(z);
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

}
