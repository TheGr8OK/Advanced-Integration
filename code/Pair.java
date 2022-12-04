package code;

public class Pair implements Cloneable{
    int y;
    int x;

    public Pair(int y, int x){
        this.y=y;
        this.x =x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + y;
        result = prime * result + x;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (y != other.y)
            return false;
        if (x != other.x)
            return false;
        return true;
    }

    public Pair clone() throws CloneNotSupportedException{
        return (Pair) super.clone();
    }
}
