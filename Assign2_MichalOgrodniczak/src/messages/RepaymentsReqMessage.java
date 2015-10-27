package messages;

/**
 * Created by witmi on 26/10/2015.
 */
public class RepaymentsReqMessage extends Message{
    private int    accountNumber;
    private double annualRate;
    private int    numOfYears;
    private double loanAmount;


    public RepaymentsReqMessage(int accountNumber, double rate, int years, double ammount) {

        this.accountNumber = accountNumber;
        this.annualRate = rate;
        this.numOfYears = years;
        this.loanAmount = ammount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public int getNumOfYears() {
        return numOfYears;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    @Override
    public String toString() {
        return "RepaymentsReqMessage{" +
                "accountNumber=" + accountNumber +
                ", annualRate=" + annualRate +
                ", numOfYears=" + numOfYears +
                ", loanAmount=" + loanAmount +
                '}';
    }
}