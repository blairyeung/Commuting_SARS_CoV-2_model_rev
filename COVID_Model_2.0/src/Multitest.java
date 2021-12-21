public class Multitest extends Thread{
    public static void main(String[] args) {
        Multitest t1 = new Multitest();
        Multitest t2 = new Multitest();
        t1.start();
        t2.start();
    }

    public Multitest(){

    }

    /*public void run(){
        System.out.println("Test 1");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Test 2");
    }*/

    public void run(int i1){
        System.out.println("Test 1");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Test 2");
    }
}
