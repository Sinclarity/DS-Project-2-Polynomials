
// ********************************************************
// ExponentOutOfRangeException class for the ADT Polynomial.
// By Peter Schurhammer
// Date: 10/11/2020
// ********************************************************

public class ReferencePolynomial implements Polynomial {

    private Node head; //variable for head

    //ReferencePolynomial constructor
    public ReferencePolynomial() {
        head = new ReferencePolynomial.Node(0, 0, null);
    } //end constructor

    public int degree() { //finds degree of the the Node
        if (head == null) {  //checks to make sure there is a node
            return 0;
        } //end if
        return head.power; //return power at head
    } //end degree

    public double getCoefficient(int power)
        throws ExponentOutOfRangeException {
        if (power > 1000) {
            throw new ExponentOutOfRangeException("Maximum power cannot exceed 1000");  //catches the exception for power
        }
        Node curr = head;   //setting curr to head
        if (head == null) {
            return 0;
        } //end if
        if (head.power == power) { //check power at head
            return head.coefficient;
        } else {
            while (curr.next != null) {     //making sure there is a next Node
                if (curr.power == power) {
                    return curr.coefficient; //return coefficient at curr Node
                } //end if
                curr = curr.next;
            } //end while
        } //end else
        return 0;
    } //end getCoefficient

    public void setCoefficient(double newCoefficient, int power) //set coefficient method, no return
        throws ExponentOutOfRangeException{
        Node curr = this.head;  //variables
        Node prev = null;       //^^
        //temp variable for Node
        Node tempNode = new Node(newCoefficient, power, null);
        if (newCoefficient != 0) { //making sure newCoefficient isn't 0
            if (head == null || head.coefficient == 0) { //special case
                head = tempNode;
            } //end if
            if (power == head.power) { //special case if the powers are equal
                if (newCoefficient == 0) { //checking if newCoefficient is 0
                    head.next = head; //iteration for head
                } else {
                    head.coefficient = newCoefficient;
                } //end else
                return;
            } //end if
            if (power > head.power) { //special case if power > head.power
                head = tempNode;
                tempNode.next = curr;
            }
            boolean isComplete = false; //special case to get rid of trailing ints
            if (power < head.power) { //special case if power < head.power
                while (curr != null) { //while if curr isn't null
                    if (curr.power == power) {
                        curr.coefficient = newCoefficient;
                        isComplete = true; //special case check
                        break;
                    } else if (power > curr.power) { //if power > power at current Node
                        prev.next = tempNode;
                        tempNode.next = curr;
                        isComplete = true; //special case check
                        break;
                    } //end else if
                    prev = curr;
                    curr = curr.next; //iteration
                } //end while
                if (!isComplete) {
                    prev.next = tempNode; //iterations/moving Nodes
                    tempNode.next = curr; //^^
                } //end if
            } //end if
        } else {
            while (curr != null) { //while if curr isn't null
                if (curr.power == power) {
                    if (prev != null) {
                        prev.next = curr.next; //iteration
                    } else {
                        head = curr.next; //iteration
                    }
                    break;
                }
                prev = curr;
                curr = curr.next; //iteration
            } //end while
        } //end else
    } //end setCoefficient


    public ReferencePolynomial add(Polynomial p) {
        ReferencePolynomial sum = new ReferencePolynomial(); //creating temps
        ReferencePolynomial pRef = (ReferencePolynomial) p;  //^^

        Node Qcurr = this.head;   //variables
        Node Pcurr;               //^^

        while (Qcurr != null) { //while loop is Qcurr isn't null
            int Qpower = Qcurr.power;
            double Qcoefficient = Qcurr.coefficient;
            Pcurr = pRef.head;
            while (Pcurr != null) { //nested loop for Pcurr
                int Ppower = Pcurr.power;
                double Pcoefficient = Pcurr.coefficient;
                if (Qpower > Ppower) { //special case if Q is greater than P
                    sum.setCoefficient(Qcoefficient, Qpower);
                    break;
                } else if (Qpower == Ppower) { //comparing the 2 powers
                    sum.setCoefficient(Qcoefficient + Pcoefficient, Qpower);
                    break;
                } else {
                    Pcurr = Pcurr.next; //iteration
                    if (Pcurr == null) {
                        sum.setCoefficient(Qcoefficient, Qpower);
                    } //end if
                } //end else                                           //similar structure to mult but I just added
            } //end while                                              //the coefficients together and set it as sum
            Qcurr = Qcurr.next; //iteration
        } //end while
        Pcurr = pRef.head;
        while (Pcurr != null) {  //while loop if Pcurr isn't null
            int Ppower = Pcurr.power;                //variables for while loop
            double Pcoefficient = Pcurr.coefficient; //^^
            Qcurr = this.head;                       //^^
            while (Qcurr != null) { //nested while loop for Qcurr
                int Qpower = Qcurr.power;
                if (Ppower > Qpower || Ppower == 0) { //special case
                    sum.setCoefficient(Pcoefficient, Ppower); //setting sum
                    break;
                } else if (Ppower == Qpower) { //checking if Ppower is equal to Qpower
                    break;
                } else {
                    Qcurr = Qcurr.next; //iteration
                } //end else
            } //end while
            Pcurr = Pcurr.next;   //iteration
        } //end while
        return sum; //returning sum
    } //end add method

    public ReferencePolynomial mult(Polynomial p) throws ExponentOutOfRangeException { //mult method, returns product
        ReferencePolynomial product = new ReferencePolynomial();  //temp variables
        ReferencePolynomial pRef = (ReferencePolynomial) p;       //^^
        Node Qcurr = this.head; //variables
        Node Pcurr;             //^^

        while (Qcurr != null) {
            int Qpower = Qcurr.power; //variable for power of Q
            double Qcoefficient = Qcurr.coefficient;    //variables for while loop
                                                                     //cycles through Qcurr to multiply coefficients
            Pcurr = pRef.head;                                       //and adding powers, return as product
            while (Pcurr != null) {
                int Ppower = Pcurr.power; //variable for power of P
                double Pcoefficient = Pcurr.coefficient; //variables for nested while loop

                    Node headTemp = product.head;
                    boolean result = false; //special case catching
                    while (headTemp != null) {
                        if (headTemp.power == Qpower + Ppower) { //setting temp head's power = to Qpower + Ppower
                            result = true; //special case catch
                            break;
                        } //end if
                        headTemp = headTemp.next; //iteration
                    } //end while

                if (result) {
                    ReferencePolynomial temp = new ReferencePolynomial();
                    temp.setCoefficient(Qcoefficient * Pcoefficient, Qpower + Ppower); //setting temp
                    product = product.add(temp);                                                         //^^
                } else {
                    product.setCoefficient(Qcoefficient * Pcoefficient, Qpower + Ppower);
                } //end else
                Pcurr = Pcurr.next; //iteration
            } //end while
            Qcurr = Qcurr.next; //iteration
        } //end while
        return product; //returning product
    } //end mult method


    public void mult(double scalar) {   //magnitude method, no return
        Node curr = head;
        if (scalar == 0) {
            head = new Node(0, 0, null); //creating new Node
            head.next = null;  //next Node would be null, no next
        } else {
            while (curr != null) {  //checking if curr node is there and has values
                curr.coefficient *= scalar;
                curr = curr.next;
            } //end while
        } //end else
    } //end mult(double)

    public double evaluate(double x) { //recursive method for evaluate
        return recursiveEvaluate(head, x); //calling the recursive method
    } //end evaluate

    private double recursiveEvaluate(Node head, double x){ //method for evaluate that calls it recursively, return temp
        double temp = 0; //temp variable
        if(head.next != null){ //base case
            temp = recursiveEvaluate(head.next,x); //recursive call
        } //end if
        temp += Math.pow(x, head.power) * head.coefficient; //using math.power * the coefficient at head
        return temp;
    } //end recursiveEvaluate

    public void displayPolynomial() {
        Node curr = head; //setting curr to head
        if (head.power == 0 && head.coefficient == 0) { //special case for the first test
            System.out.println(0.0);                    //output for special case
        } else {
            while (curr != null) { //makes sure there is a Node
                if (curr.power == 0) {    //special case
                    System.out.println(curr.coefficient);
                    break;
                } else {
                    if (curr.next == null) {
                        System.out.println(curr.coefficient + "x^" + curr.power);           //formatting
                    } else {                                                                //^^
                        System.out.print(curr.coefficient + "x^" + curr.power + " + ");
                    }
                }
                curr = curr.next; //next iteration
            } //end while
        } //end else
    } //end displayPolynomial


    private static class Node { //Node class
        double coefficient;
        int power;              //Node variables
        Node next;

        //Node constructor
        public Node(double coefficient, int power, Node next) {
            this.coefficient = coefficient;
            this.power = power;
            this.next = next;
        } //end Node constructor
    } //end Node class
} //end ReferencePolynomial




