package sample;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by yunusbora on 26.07.2016.
 */
public class EnterAction implements Serializable {
    public String type;
    public String operation;
    public Double quantity;
    public LocalDate date;
    public LocalTime time;
    public int paymentDate;


}
