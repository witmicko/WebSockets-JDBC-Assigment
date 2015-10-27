package messages;

import java.text.DecimalFormat;

/**
 * Created by Michal Ogrodniczak on 27/10/2015.
 * Repayment response message contains all information about loan repayment.
 * It also includes Welcome message to the requesting applicant.
 */
public class RepaymentsRespMessage extends TextMessage{

    private double monthlyPayment;
    private int    numOfPayments;
    private double totalPayment;
    private double interestPaid;


    /**
     * Constructor initiates and performs all calculations
     * @param applicant of the request, holder of an account in out bank database
     * @param repaymentsReqMessage reqyuest message with data necessary to calculate repayments
     */
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
        this.numOfPayments  = numOfPayments;
        this.totalPayment   = round(totalPayment);
        this.interestPaid   = round(interestPaid);

    }



    private static double round(double d){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(d));
    }


    public String prettyPrint() {
        return
                getMessage()                            + "\n" +
                "monthly payment: "    + monthlyPayment + "\n" +
                "number of payments: " + numOfPayments  + "\n" +
                "total payment: "      + totalPayment   + "\n" +
                "interest paid: "      + interestPaid;
    }
}
