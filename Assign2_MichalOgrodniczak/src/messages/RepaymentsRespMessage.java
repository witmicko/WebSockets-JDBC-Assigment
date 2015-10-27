package messages;

import java.text.DecimalFormat;

/**
 * Created by witmi on 27/10/2015.
 */
public class RepaymentsRespMessage extends TextMessage{

    private double monthlyPayment;
    private int    numOfPayments;
    private double totalPayment;
    private double interestPaid;

    public RepaymentsRespMessage(String applicant, RepaymentsReqMessage repaymentsReqMessage) {
        super("Hello " + applicant + " your loan repayments are following:");

        double monthlyRate = repaymentsReqMessage.getAnnualRate() / 1200;
        int numOfPayments      = repaymentsReqMessage.getNumOfYears() * 12;
        double loanAmount  = repaymentsReqMessage.getLoanAmount();
        double monthlyPayment =
                (monthlyRate +
                (monthlyRate / (Math.pow((1 + monthlyRate), numOfPayments) - 1))) *
                 loanAmount;
        double totalPayment = monthlyPayment * numOfPayments;
        double interestPaid = totalPayment - loanAmount;

        this.monthlyPayment = round(monthlyPayment);
        this.totalPayment   = round(totalPayment);
        this.interestPaid   = round(interestPaid);

    }



    private static double round(double d){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(d));
    }


    public String prettyPrint() {
        return
                getMessage()                         + "\n" +
                "monthly payment: " + monthlyPayment + "\n" +
                "total payment: "   + totalPayment   + "\n" +
                "interest paid: "   + interestPaid;
    }
}
