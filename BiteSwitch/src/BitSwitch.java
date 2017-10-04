import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Format;

public class BitSwitch {
    public static void main(String[] args) throws IOException {

        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        printArr(switchBite(createArray(), 34));

    }
    public static void printArr(Integer[] array){
        for (int i = 0; i < array.length; i++){
            System.out.println("Element i: " + array[i]);
        }
    }

    public static Integer[] createArray() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer lengthArray = Integer.parseInt(reader.readLine());
        Integer[] array = new Integer[lengthArray];

        for (int i = 0; i < array.length; i++) {

            array[i] = Integer.parseInt(reader.readLine());
        }
        return array;
    }

    public static Integer[] switchBite(Integer[] array, Integer bite) {
        Integer searchElement = ((array.length * 32) - bite) / 32;

        System.out.println(searchElement);

        if ((array.length - searchElement - 1) < array.length) {

            System.out.println(String.format("%32s", Integer.toBinaryString(array[searchElement])).replace(" ", "0"));


            String searchElementTwice = String.format("%32s", Integer.toBinaryString(array[searchElement])).replace(" ", "0");
            if (array[array.length - searchElement - 1]<0){
                System.out.println("11");
                searchElementTwice = "-"+searchElementTwice.substring(1,32);

            }

            Integer tmpBit = bite % 32;
            String bit;
            if (tmpBit != 0 && tmpBit != 1) {
                bit = searchElementTwice.substring(bite % 32 - 1, bite % 32);
                if (bit.equals("0")) {
                    bit = "1";
                } else {
                    bit = "0";
                }
                searchElementTwice = searchElementTwice.substring(0, bite % 32 - 1) + bit + searchElementTwice.substring(bite % 32, 32);
            } else if(tmpBit == 0) {
                bit = searchElementTwice.substring(32 - 1, 32);
                if (bit.equals("0")) {
                    bit = "1";
                } else {
                    bit = "0";
                }
                searchElementTwice = searchElementTwice.substring(0, 32 - 1) + bit;
            }else if(tmpBit == 1){
                bit = searchElementTwice.substring(0, 1);
                if (bit.equals("0")) {
                    System.out.println("pp");
                    bit = "-";
                } else if (bit.equals("-")){
                    System.out.println("tt");
                    bit = "0";
                }

                searchElementTwice =  bit + searchElementTwice.substring(1, 32);
            }


            array[array.length - searchElement - 1] = Integer.parseInt(searchElementTwice,2);


            //System.out.println(Integer.reverse(array[array.length - searchElement - 1]));
            //return result;
        } else {
            System.out.println("ArrayIndexOutOfBoundsException");
        }
        return array;
    }
}

