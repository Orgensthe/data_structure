/*
 * © 2019 CSE2010 HW #2
 *
 * You can freely modify this class except the signatures of the public methods!!
 */

import java.util.Arrays;
import java.util.stream.Collectors;

class Term {
    /*
     * Public field is a bad idea. But, for the sake of simplicity .....
     */
    public double   coeff;  // coefficient
    public int      expo;   // exponent

    public Term(double coeff, int expo) {
        this.coeff = coeff;
        this.expo = expo;
    }

    public static int compare(Term t1, Term t2) {
        if (t1.expo > t2.expo)
            return 1;
        if (t1.expo < t2.expo)
            return -1;
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Term))
            return false;
        final Term term = (Term) o;
        return Double.compare(term.coeff, coeff) == 0 &&
            expo == term.expo;
    }
}

/*
 * class DLinkedPolynomial
 */
public class DLinkedPolynomial implements Polynomial {

    private DLinkedList<Term> list = null;

    public DLinkedPolynomial() {
        list  = new DLinkedList<Term>();
    }

    @Override
    public int getDegree() {
        int maxExpo = 0;
        Node<Term> current = list.getFirstNode();
        while(current != null && current.getInfo()!=null){
            
            if(current.getInfo().expo > maxExpo){
                maxExpo = current.getInfo().expo;
            }
            current = current.getNext();
        }

        return maxExpo;
    }

    @Override
    public double getCoefficient(final int expo) {
       
        Node<Term> current = list.getFirstNode();
        double result = 0;
        
        while(current != null&& current.getInfo()!=null){
            
            if(current.getInfo().expo == expo){
                result = current.getInfo().expo;
                break;
            }
            current = current.getNext();
        }

        return result;
    }

    @Override
    public Polynomial add(final Polynomial p) {
        
        
        DLinkedPolynomial result = new DLinkedPolynomial();
        DLinkedPolynomial target = (DLinkedPolynomial) p;
        Node<Term> current = list.getFirstNode();

        while(current!=null&&current.getInfo()!=null){
            result.addTerm(current.getInfo().coeff,current.getInfo().expo);
            current = current.getNext();
        }
        current = target.list.getFirstNode();
        
        while(current!=null&&current.getInfo()!=null){
            result.addTerm(current.getInfo().coeff,current.getInfo().expo);
            current = current.getNext();
        }
        return result;
        
    }

    @Override
    public Polynomial mult(final Polynomial p) {
        DLinkedPolynomial result = new DLinkedPolynomial();
        DLinkedPolynomial target = (DLinkedPolynomial) p;
        Node<Term> currentOfThis = list.getFirstNode();
        Node<Term> currentOfTarget = target.list.getFirstNode();

        while(currentOfThis!=null && currentOfThis.getInfo()!=null){
            while(currentOfTarget!=null && currentOfTarget.getInfo()!=null){
                
                result.addTerm( currentOfThis.getInfo().coeff*currentOfTarget.getInfo().coeff , currentOfThis.getInfo().expo+currentOfTarget.getInfo().expo);


                currentOfTarget = currentOfTarget.getNext();
            }
            currentOfThis = currentOfThis.getNext();
            currentOfTarget = target.list.getFirstNode();
        }

        return result;
    }

    @Override
    public void addTerm(final double coeff, int expo) {
        
        Node<Term> current = list.getFirstNode();

        
        while( current != null&& current.getInfo()!=null){
            
            if(current.getInfo().expo == expo){
                Term info = new Term(current.getInfo().coeff+coeff, expo);
                current.setInfo(info);
                return;
            }else if(current.getInfo().expo < expo){
                Term info = new Term(coeff, expo);
                Node<Term> tempNode = new Node(info, null, null);
                list.addBefore(current,tempNode);
                return;
            }
            current = current.getNext();
        }

        Term info = new Term(coeff, expo);
        Node<Term> tempNode = new Node(info, null, null);
        list.addLast(tempNode);
       

    }

    @Override
    public void removeTerm(final int expo) {
       
        Node<Term> current = list.getFirstNode();
        while(current != null&& current.getInfo()!=null){
            
            if(current.getInfo().expo == expo){
                list.remove(current);
                return;
            }
            current = current.getNext();
        }


        throw  new NoSuchTermExistsException();
       
        
    }

    @Override
    public double evaluate(final double val) {

        double result = 0.0;
        double tempVal = 1;
        Node<Term> current = list.getFirstNode();
        
        while(current != null && current.getInfo()!=null){
            for(int i =0 ;i< current.getInfo().expo; i++){
                tempVal = tempVal*val;
            }
            
            
            tempVal = tempVal * current.getInfo().coeff;
            result += tempVal;
            current = current.getNext();
            tempVal = 1;
            
        }

        return result;


    }

    @Override
    public int termCount() {
        int result = list.size();
        return result;
    }

    @Override
    public String toString() {
        if (list.isEmpty())
            return "Empty Polynomial";
        else {
            String[] terms = new String[termCount()];
            int i = 0;
            Node<Term> current = list.getFirstNode();
            do {
                terms[i++] = current.getInfo().coeff + "x^" + current.getInfo().expo;
                current = list.getNextNode(current);
            } while (current != null);
            return Arrays.stream(terms).collect(Collectors.joining("+"));
        }
    }

}
