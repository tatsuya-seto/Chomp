public class MyChomp {

    public static void main(String[] args) {
        new MyChomp();
    }

    public MyChomp(){

        int i=0;

        for (int r1 = 3; r1 >= 0; r1--) {
            for (int r2 = r1; r2 >= 0; r2--) {
                for (int r3 = r2; r3 >= 0; r3--) {

                    if(r1==0){
                        break;
                    }

                    System.out.println(r1 + "," + r2 + "," + r3);
                    i++;
                }
            }
        }

        System.out.println(i + " boards printed");

    }
}
