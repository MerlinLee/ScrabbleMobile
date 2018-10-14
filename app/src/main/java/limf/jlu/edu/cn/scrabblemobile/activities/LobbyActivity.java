package limf.jlu.edu.cn.scrabblemobile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public void showInviteMessage(int inviterId, String inviterName) {
        handler.post(new RunnableShowInviteMessage(inviterId,inviterName));
    }

    Runnable runnableUI = new Runnable() {
        @Override
        public void run() {
            ArrayList<Users> users = new ArrayList<>(Arrays.asList(userList));
            List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
            SimpleAdapter adapter = new SimpleAdapter(lobbyActivity,data,R.layout.item,
                    new String[]{"id","name","win","status"},new int[]{R.id.userId,R.id.userName
            ,R.id.userWin,R.id.userStatus});
            for(Users user : users){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("id", user.getUserID());
                item.put("name", user.getUserName());
                item.put("win", user.getNumWin());
                item.put("status", user.getStatus());
                data.add(item);
            }
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new ItemClickEvent());
        }
    };

    private class RunnableShowInviteMessage implements Runnable{
        int inviterId;
        String inviterName;
        public RunnableShowInviteMessage(int inviterId, String inviterName) {
            this.inviterId = inviterId;
            this.inviterName = inviterName;
        }

        @Override
        public void run() {
            AlertDialog dialog = new AlertDialog.Builder(lobbyActivity)
                    .setTitle("Invite")//设置对话框的标题
                    .setMessage(inviterName+" ask you to join a game, yes or no?")//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("Refuse", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(lobbyActivity, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                            GuiController.get().sendInviteResponse(false,inviterId);
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(lobbyActivity, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                            GuiController.get().sendInviteResponse(true,inviterId);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    private class ItemClickEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            HashMap<String, Object> data = (HashMap<String, Object>) listView.getItemAtPosition(position);
            String personid = data.get("id").toString();
            Toast.makeText(getApplicationContext(), personid,Toast.LENGTH_SHORT).show();
        }
    }
}
