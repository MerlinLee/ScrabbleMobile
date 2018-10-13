package limf.jlu.edu.cn.scrabblemobile;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnectThread implements Runnable {
    private Socket client;
    private String ipAddr;
    private int portNum;
    private OutputStream dataOutputStream;
    private InputStream dataInputStream;

    @Override
    public void run() {
        try {
            client = new Socket(ipAddr,portNum);
            if(client != null){
                dataOutputStream = client.getOutputStream();
                dataInputStream = client.getInputStream();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
