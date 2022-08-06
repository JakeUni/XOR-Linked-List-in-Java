
//this is the node class
//i have used this to attempt to replicate memory
//the npx is the index of the prior and next element in the array
//the data is the name
public class Node {
    private int npx;
    String data;

    public Node(int npx, String data){
        this.npx = npx;
        this.data = data;
    }
    public int getNpx() {
        return npx;
    }
    public String getData(){
        return data;
    }
    public void setNpx(int npx) {
        this.npx = npx;
    }
}
