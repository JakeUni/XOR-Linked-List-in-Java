import java.io.FileReader;
import java.io.IOException;

/**
 *this is my implementation of an xor linked list
 */
public class CW2Q4 {
    //this is an arrazy of the class type node
    //this class type stored the npx and the data
    private static Node[] node = new Node[10000];
    //this stores the location of th last element in the array as its index
    private static int headloc = 1;

    /**
     * this is the main function where everything is initialized
     * @param args
     * @throws IOException
     */
    public static void main(String[] args   ) throws IOException {
        CW2Q4 me = new CW2Q4();

       me.readAddAll("names.txt");
       me.insertAfter("ZACK","Sphteven");
       me.insertBefore("MICHAEL","ekaj");
       System.out.println(me.removeBefore("ZACK"));
       System.out.println(me.removeAfter("MICHAEL"));
       me.print();

      //  for (Node node:node) {
        //    if(node != null) {
          //      System.out.println(node.getData());
           // }
       // }
    }

    /**
     * this function will read in all the names from the file provided and add them to the xor linked list
     * @param file this is the file where the names are located
     */
   private void readAddAll(String file){
        try{
            //here i am making a new file reader to read in the file from the array
            FileReader fr;
            fr = new FileReader(file);
            int i;
            char c;
            //currName will store the name as its being constructed
            String currName = "";
            //while we are reading in from the file and it is not -1 than loop through and decide what to do with i
            while ((i = fr.read()) != -1) {
                //convert i to a char
                c = (char) i;
                if(c == '"'){
                    //we will ignore at " as this is irrelevant to us
                }else if(c == ','){
                    //if it is however of value , than we have finished the current word
                    //we call add word to add the word and restart currName.
                    addNode(currName);

                    currName = "";
                }else{
                    //if none of the above then just add into the list
                    currName = currName + c;
                }
            }
            //add the last node
            addNode(currName);
        }catch(java.io.FileNotFoundException e){
            System.out.println("Input file not found");
        }catch(java.io.IOException e){

        }

    }

    /**
     * this is just a print function to print out the array in order (reverse order )
     */
    private void print(){
        int temp;
        int current = headloc;
        int previous = node[headloc].getNpx();
        System.out.println(node[current].getData());
        while(previous!=0){
            System.out.println(node[previous].getData());
            temp = previous;
            previous = node[previous].getNpx() ^ current;
            current = temp;
        }
    }

    /**
     * this is the function which will input names into the linked list but not in the end of it
     * this inserts the string what after or before the string where depending on the value of dir
     * @param where - the string we are adding after or before
     * @param what - the String we are adding
     * @param dir - the direction, either after or before
     */
    private void insert(String where, String what, int dir){
        //get the corresponding addresses
        int[] addresses = findNode(where, dir);
        int originalA = addresses[0];
        int changeA = addresses[1];
        int newA = findFree();
        //if changeA == -1 than the name doesnt exist and we inform the user
        if(changeA == -1){
            System.out.println("Name not found");
        }else{
            //if the name does exist than we will create a new node at the next free address
            //we give this node the links that link to its next and previous calues
            int originalNtx = (node[originalA].getNpx() ^ changeA) ^ newA;
            node[newA] = new Node(originalA ^ changeA, what);
            node[originalA].setNpx(originalNtx);
            //if we added after the head than we need to change the head address
            if(originalA == headloc){
                headloc = newA;
            }
            //this is a check to see if we are trying to add before Mary
            //if we arent than there is a value to change
            if(!(node[changeA] == null)){
                node[changeA].setNpx((node[changeA].getNpx() ^ originalA) ^ newA);
            }
            //message to the user telling them the name was added
            System.out.println("Name Added");
        }
        }

        //These are the functions that were required to be made in the spec
        //I implemented this in a slightly differenet way
        private void insertAfter(String after , String newObj){
           insert(after,newObj,1);
        }
        private void insertBefore(String before , String newObj){
            insert(before,newObj,0);
        }
        private String removeAfter(String after){
            return remove(after,1);
        }
        private String removeBefore(String before){
            return remove(before,0);
        }

    /**This function is called by remove after and remove before
     * I pass dir to this function to represent after and before (1 and 0)
     * I use the findNode function to get the corresponding addresses needed for this function
     *
     * @param who - this is the name to be removed before or after
     * @param dir - this is the direction so 1 for after 0 for before
     * @return - returns the name removed
     */
    public static String remove(String who, int dir){
        int[] addresses = findNode(who, dir);
        int originalA = addresses[0];
        int removeA = addresses[1];
        //if the remove is -1 or if the remove is 0 than we will error message as the
        //user has tried to do something they shouldnt have done
        if(removeA == -1){
            return "Error name not found";
        }else if (removeA == 0) {
            return "Error no name at that location";
        }
        else{
            //removeA is the node that we want to remove as it is the one after "who"
            //originalA is our "who"s address so we need to change this
            //we change the link values of all the variables associated by using the xor function

            int changeA = node[removeA].getNpx() ^ originalA;
            String name = node[removeA].getData();
            //if we are remove the end of the array than we need to change the pointer to the head of the array to be
            //the element prior to it
            if(removeA == headloc){
                headloc = originalA;
            }
            //If the changeA node is not the 0th element in the array
            //than we want to change it
            if(!(node[changeA] == null)) {
                node[changeA].setNpx((removeA^node[changeA].getNpx())^originalA);
            }
            //update the original npx
            node[originalA].setNpx((removeA^node[originalA].getNpx())^changeA);
            //set the removedNodes address to be null so its space can be used agaim
            node[removeA] = null;
            //return the name removed
            return name;
        }
    }

    /**This function will return two addressses, (array indexes)
     * these indexes will be that of the searched name and either before or after that name
     * for example if "NAME" is MARY  and dir = 0 than we will output the address of MARY and the one prior to mary
     * if it were MARY and dir = 1 than we would return the addresses of MARY and the one after Mary
     *
     * @param name this is the name we want to do something after/before to
     * @param dir this is the direction, 1 for after 0 for before
     * @return we will return an integer array of two addresses
     */
    public static int[] findNode(String name , int dir){
        //we will start at the end of the array
        int start = headloc;
        int temp;
        //where stores the previous address
        int where = node[start].getNpx();
        //while it is not equal to the data that we want than we will iterate backwards through the array
        while(!node[start].getData().equals(name) && where > 0) {
            //this will where and start point to the addresses before them
            temp = where;
            where = start ^ node[where].getNpx();
            start = temp;


        }
        //we return -1 if the value of where is 0 which would result in a null value occurance
        //or if we never actually found the data and just finished the array
        if((where == 0) && !(node[start].getData().equals(name))){
            where = -1;
        }
        //if the direction is 1 than we want to get the index after the searched name
        //we do this by getting the NPX of the searched name and xoring it with the index prior to it

        if ((dir == 1) && where > 0){
             where = ( node[start].getNpx() ^ where);
        }
        //this is our return statement which will return the two addresses.
        int[] ret = {start, where};
        return ret;
    }

    /**
     * this is a very simple function which will just return the next available space in the array
     * this will return purely the next available index in the array
     * @return next free address
     */
    private int findFree(){
        int start = 1;
        //loop until a node is empty and return the value of this node
        //however if the loop value gets to 10000 (the end of the array)
        // we stop by returning -1
        while (node[start] != null){
            start++;
            if(start == 10000){
                System.out.println("full");
                return -1;
            }
        }
        return start;
    }

    /**This funciton wwill insert an item into the end of a list
     * we will get the next available memory address and assign it to hte address of the new name
     * if the new address is -1 than we dont want to do anything as this means our array is full
     * however if it isnt -1 we want to addding it along with the npx of head
     * head being the end of the array, this means the new name is in the end position
     * we now need to change the link of  whoever was in the last index previously
     * we do this by xoring the address of the old head with the new address and with the old heads link
     * @param Data - this is the actual data in our case the name
     */
    private void addNode(String Data){
            int address = findFree();
            if(address != -1){
                node[address] = new Node(headloc,Data);
                node[headloc].setNpx(address^ node[headloc].getNpx());
                headloc = address;
            }
    }
}
