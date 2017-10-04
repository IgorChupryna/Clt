import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Chupryna-IV on 14.06.16.
 */
public class Main {
    public static void main(String[] args){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 6;i++){
            list.add(i);
        }
        int x = 0;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()){
            x+=it.next();
        }
        System.out.println(x);
    }
}
