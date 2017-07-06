package sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by yunusbora on 8.08.2016.
 */
public class test {
    public static void main(String[] args0) {
        EnterAction action = null;
        try
        {
            FileInputStream fileIn = new FileInputStream("C:\\Users\\yunusbora\\Desktop\\Wallet\\src\\enterAction.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            action = (EnterAction) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized Action...");
        System.out.println("Type: " + action.type);
        System.out.println("Quantity: " + action.quantity);
        System.out.println("Operation: " + action.operation);
        System.out.println("Time: " + action.time);
        System.out.println("Date: " + action.date);
        System.out.println("Payment Date: " + action.paymentDate);
    }
}
