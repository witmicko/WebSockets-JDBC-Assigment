package messages;

/**
 * Created by witmi on 26/10/2015.
 */
public class PaymentsMessage extends Message{
    private int accNUm;
    private double rate;
    private int years;
    private double ammount;


    public PaymentsMessage(int accNUm, double rate, int years, double ammount) {

        this.accNUm = accNUm;
        this.rate = rate;
        this.years = years;
        this.ammount = ammount;
    }

    public int getAccNUm() {
        return accNUm;
    }

    public double getRate() {
        return rate;
    }

    public int getYears() {
        return years;
    }

    public double getAmmount() {
        return ammount;
    }
}
