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

                    printOneMoves(r1, r2, r3);
                }
            }
        }

        System.out.println(i + " boards printed");

    }

    public void printOneMoves(int r1, int r2, int r3) {

        System.out.println("  ");
        System.out.println("All possible boards:");

        int[] rows = {r1, r2, r3};

        for (int row = 0; row < 3; row++) {
            int length = rows[row];

            if (length == 0) continue;

            for (int col = 1; col <= length; col++) {

                int nr1 = r1;
                int nr2 = r2;
                int nr3 = r3;

                int newVal = col - 1;

                if (row == 0) {  //eating in r1
                    nr1 = newVal;
                    nr2 = Math.min(nr2, newVal);
                    nr3 = Math.min(nr3, newVal);
                }
                else if (row == 1) { //eating in r2
                    nr2 = newVal;
                    nr3 = Math.min(nr3, newVal);
                }
                else { // eating in r3
                    nr3 = newVal;
                }

                // if this results in a valid non-increasing board
                if (nr1 >= nr2 && nr2 >= nr3) {
                    System.out.println(nr1 + "," + nr2 + "," + nr3);
                }
            }
        }

        System.out.println("___________________");
    }
}