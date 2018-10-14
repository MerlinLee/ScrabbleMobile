package limf.jlu.edu.cn.scrabblemobile.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;

import limf.jlu.edu.cn.scrabblemobile.Models.Users;
import limf.jlu.edu.cn.scrabblemobile.R;
import limf.jlu.edu.cn.scrabblemobile.blockingqueue.GuiPutMsg;
import limf.jlu.edu.cn.scrabblemobile.clientControl.ClientControlCenter;
import limf.jlu.edu.cn.scrabblemobile.protocols.NonGamingProtocol.NonGamingProtocol;

public class LobbyActivity extends AppCompatActivity {
    private ListView listView;
    private ClientControlCenter center;
    private Users[] userList;
    private LobbyActivity lobbyActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            new Thread(center = new ClientControlCenter()).start();
        }catch (Exception e){
            showDialog("Try Again Please, my boy/girl!");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        listView = findViewById(R.id.list_userList);
        Intent intent_get = getIntent();
        String userName = intent_get.getStringExtra("name");
        String ip = intent_get.getStringExtra("ip");
        String portNumber = intent_get.getStringExtra("port");
        GuiController.get().setUserName(userName);
        GuiController.get().setLobbyActivity(this);
        initialConnection(userName,ip,Integer.parseInt(portNumber));
    }

    public void showDialog(String msg){
        System.out.println(msg);
    }

    public void updateUserList(Users[] userList){
        this.userList = userList;
        lobbyActivity=this;
        handler.post(runnableUI);
    }

    private void initialConnection(String username,String ip, int port){
        center.openNet(ip,port,username);
        GuiPutMsg.getInstance().putMsgToCenter(JSON.toJSONString(new NonGamingProtocol(
                "login",new String[]{username}
        )));
    }

    private Handler handler = new Handler();

    Runnable runnableUI = new Runnable() {
        @Override
        public void run() {
            ArrayList<Users> users = new ArrayList<>(Arrays.asList(userList));
            ArrayAdapter<Users> adapter = new ArrayAdapter<>(lobbyActivity,android.R.layout.simple_list_item_1,userList);
            listView.setAdapter(adapter);
        }
    };
}
