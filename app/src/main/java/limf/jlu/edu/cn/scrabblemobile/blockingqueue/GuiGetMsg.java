package limf.jlu.edu.cn.scrabblemobile.blockingqueue;


import java.util.concurrent.BlockingQueue;

import limf.jlu.edu.cn.scrabblemobile.activities.GuiController;
import limf.jlu.edu.cn.scrabblemobile.utils.GuiListener;

public class GuiGetMsg implements Runnable{
    public GuiGetMsg(BlockingQueue<String> fromCenter) {
    this.fromCenter = fromCenter;
    //GuiListener.get().addBlockingQueue(fromCenter);
}
    private BlockingQueue<String> fromCenter;


    @Override
    public void run() {

        while (true){
            String temp;
            try {
                temp = fromCenter.take();
//                GuiController.get().receiveMsgFromCenter(temp);
                synchronized (GuiListener.get()){
                    GuiListener.get().addMessage(temp);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
