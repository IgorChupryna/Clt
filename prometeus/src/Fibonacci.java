

public class Fibonacci {

    public Fibonacci() {
        super();
    }


    public long getNumber(int position){
        if (position <= 0) return -1;
        if (position == 1) return 1;
        if (position == 2) return 1;
        return getNumber(position - 1) + getNumber(position - 2);

    }




}