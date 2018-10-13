package limf.jlu.edu.cn.scrabblemobile;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import limf.jlu.edu.cn.scrabblemobile.protocols.NonGamingProtocol.NonGamingProtocol;

public class MainActivity extends AppCompatActivity {
    private EditText eT_username;
    private EditText eT_ip;
    private EditText eT_port;
    private Button btn_login;
    private TextView textView_username;
    private TextView textView_ip;
    private TextView textView_port;
    private MyBtnClicker myBtnClicker = new MyBtnClicker();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eT_username = findViewById(R.id.editText_username);
        eT_ip = findViewById(R.id.editText_ip);
        eT_port = findViewById(R.id.editText_port);
        textView_username = findViewById(R.id.textView_username);
        textView_ip = findViewById(R.id.textView_ip);
        textView_port = findViewById(R.id.textView_port);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(myBtnClicker);
    }


    private class MyBtnClicker implements View.OnClickListener{
        private SocketClient socketClient;
        @Override
        public void onClick(View v) {
            SocketClientAddress clientAddress = new SocketClientAddress(eT_ip.getText().toString(),eT_port.getText().toString());
            socketClient = new SocketClient(clientAddress);
            connectServer();
        }

        private void connectServer(){
            socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
                @Override
                public void onConnected(SocketClient client) {
                    socketClient.sendString(JSON.toJSONString(new NonGamingProtocol("login",new String[]{"Merlin"})));
                }

                @Override
                public void onDisconnected(SocketClient client) {

                }

                @Override
                public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                    String responseMsg = responsePacket.getMessage();
                    showDialog(responseMsg);
                }
            });
            socketClient.connect();
        }
    }

    public void showDialog(String msg){
        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                .setTitle("Login Success!")
                .setMessage(msg)
                .create();
        alertDialog1.show();
    }
}
