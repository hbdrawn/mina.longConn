package com.hbdrawn.push;

public class PushClient4Test extends PushClient {

    public PushClient4Test(String ipAddress, int port) {
        super(ipAddress, port);
    }

    public static void main(String[] args) throws Exception {
        int countAll = Integer.valueOf(args[0]);
        System.out.println("最大连接用户数创建成功:" + countAll);
        int i = 0;
        for (i = 1; i <= countAll; i++) {
            PushClient4Test pushClient4Test = new PushClient4Test(args[1], 6200);
            pushClient4Test.startService();
            new Thread(pushClient4Test).start();
            //        pushClient4Test.login("15801207002", "15801207002");
            //            pushClient4Test.login("18701585849", "sim18701585849");
            System.out.println("第[ " + i + "]长连接已建立成功");
        }
        System.out.println("最大连接用户数创建成功:" + i);
    }

    @Override
    public String sendLocationListener(int coordtype) {
        // TODO Auto-generated method stub
        return "{\"loctype\":0, \"lon\":111,\"lat\":111,\"radius\":111}";
    }

    @Override
    public void afterSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void preStartService() throws Exception {
        // TODO Auto-generated method stub

    }

}
