package limf.jlu.edu.cn.scrabblemobile.blockingqueue;


import java.util.concurrent.BlockingQueue;

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
//                synchronized (GuiListener.get()){
//                    GuiListener.get().addMessage(temp);
//                }
                System.out.println(temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
