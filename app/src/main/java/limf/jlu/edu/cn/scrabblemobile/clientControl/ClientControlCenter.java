package limf.jlu.edu.cn.scrabblemobile.clientControl;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

import limf.jlu.edu.cn.scrabblemobile.Net.blockingqueue.ClientNet;
import limf.jlu.edu.cn.scrabblemobile.blockingqueue.GuiGetMsg;
import limf.jlu.edu.cn.scrabblemobile.blockingqueue.GuiPutMsg;
import limf.jlu.edu.cn.scrabblemobile.clientControl.blockingqueue.ClientCenterGetMsg;
import limf.jlu.edu.cn.scrabblemobile.clientControl.blockingqueue.ClientCenterPutMsg;


public class ClientControlCenter implements Runnable{
    private String tag = "clientControl";
    private static Logger logger = Logger.getLogger(ClientControlCenter.class);
    private final BlockingQueue<String> fromNet;
    private final BlockingQueue<String> toGui;
    private final BlockingQueue<String> fromGui;
    private final BlockingQueue<String> toNet;
    private boolean flag = true;
    private ThreadFactory threadForSocket;
    private ExecutorService pool;


    public ClientControlCenter() {
        this.fromNet = new LinkedBlockingQueue<>();
        toGui = new LinkedBlockingQueue<>();
        fromGui = new LinkedBlockingQueue<>();
        toNet = new LinkedBlockingQueue<>();
//        initialClient();
        logger.info(tag+" Initial clientControlCenter Complete!");
    }
    public void initialClient(){
        threadForSocket = new ThreadFactoryBuilder()
                .setNameFormat("Client-ControlCenter-pool-%d").build();
        pool = new ThreadPoolExecutor(10,50,0L,TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),threadForSocket,new ThreadPoolExecutor.AbortPolicy());
//        pool.execute(ClientNet.getInstance(fromNet,toNet));
//        pool.execute(Gui.getInstance(toGui,fromGui));
        logger.info(tag+" Initial Server Competed");
    }

    public void openNet(String ipAddr, int portNum,String username){
        try {
            pool.execute(ClientNet.getInstance(fromNet,toNet,ipAddr,portNum,username));
        }catch (Exception e){
            //pool.execute(LoginWindow.get());
        }
    }

    @Override
    public void run() {
        initialClient();
        pool.execute(new ClientCenterGetMsg(fromNet,toGui,fromGui,toNet));
        pool.execute(new ClientCenterPutMsg(fromNet,toGui,fromGui,toNet));
        //LoginWindow.get().setCenter(this);
        //pool.execute(LoginWindow.get());
        pool.execute(new GuiGetMsg(toGui));
        GuiPutMsg.getInstance(fromGui);

//        loginWindow = LoginWindow.get();
//        loginWindow.setClient(this);
//        Thread loginThread = new Thread(loginWindow);
//        loginThread.start();
    }


    public void shutdown(){
        flag = false;
    }

}
