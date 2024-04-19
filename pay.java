import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;
import java.io.*;


public class pay implements Serializable {

    private double total;
    private String details;
    private String paymentID;
    enum paymentOptions {
        CREDITCARD, DEBITCARD, PAYPAL, CASH
    }
    enum paymentStatus {
        PENDING, COMPLETED, FAILED, CANCELED
    }
    private  paymentOptions option;
    private  paymentStatus status;


    public pay(String d, paymentStatus s, Double t, paymentOptions opt){
        this.paymentID=generatePaymentId();
        option=opt;
        total=t;
        status=s;
        details=d;
    }

    public pay() {
        total=0;
        details="no details";
    }

// Generate Unique ID
            private String generatePaymentId(){

                String id= UUID.randomUUID().toString();
                LocalDateTime time=LocalDateTime.now();
                DateTimeFormatter format= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedTime = time.format(format);

                paymentID=id+"_"+ formattedTime;
                return paymentID;
            }
            private String getPaymentID(){return paymentID;}


    public String paymentDetails(){
        return details;
    }

    public double paymentTotal(){
        return total;
    }

    public paymentStatus paymentStatus(){ return status; }

    public void UpdatePaymentStatus(paymentStatus s){ status=s; }

    public String toString(){
        return "Payment ID: ["+getPaymentID()+"]\nPayment details: "+paymentDetails()+"\nPayment Status: "+paymentStatus()+"\nPayment Total: "+paymentTotal();
    }



    public static void main(String[] args) throws Exception{

        Scanner kb = new Scanner(System.in);
        System.out.println("Enter 0 Exit, 1 to make a payment for your flight reservation and 2 to to Display payments details");

        int optt=kb.nextInt();
        switch (optt) {
            case 0:
                //exit from the system
                System.exit(0);
                break;


            case 1:{ //to make a record to the system manually
                        try {

                            // Read passenger details
                            System.out.println("Enter passenger's Details");
                            kb.nextLine();
                            String Details = kb.nextLine();

                            // Read Reservation total payment
                            System.out.println("Enter reservation's Total Payment");
                            Double total = kb.nextDouble();

                            // Read payment status
                            System.out.println("Enter passenger's Payment Status: \n-PENDING\n-COMPLETED\n-FAILED\n-CANCELED");
                            kb.nextLine();// read extra bits
                            String st = kb.nextLine();

                            // Read payment method
                            System.out.println("Choose one payment method: \n-CreditCard\n-DebitCard\n-paypal\n-cash");
                            String opt = kb.nextLine();

                            // Convert String into "enum" and match with the corresponding enum
                            paymentStatus status = paymentStatus.valueOf(st.toUpperCase());
                            paymentOptions option = paymentOptions.valueOf(opt.toUpperCase());


                            // create new obj
                            pay passenger = new pay(Details, status, total, option);
                            //Print Payment details in "paymentRecords.txt" File
                            File F = new File("C:/Users/maria_yiuhvtt/pay/paymentsRecords.txt");
                            ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(F));
                            ob.writeObject(passenger);
                            ob.close();

                            // Change payment status
                            System.out.println("Do You want to update payment's Status: Y Or N ");
                            String answer = kb.next();

                                    if (answer.compareToIgnoreCase("Y") == 0) {

                                        System.out.println("Enter the New Payment Status: \n-PENDING\\n-COMPLETED\\n-FAILED\\n-CANCELED\"");
                                        kb.nextLine();
                                        String newStatus = kb.nextLine();
                                        paymentStatus newst = paymentStatus.valueOf(newStatus.toUpperCase());
                                        passenger.UpdatePaymentStatus(newst);
                                    }
                            //print payment details
                            System.out.println(passenger.toString());

                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid choice, Please try again");
                        }

                break;}

            // to search by payment ID
            case 2:{

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter payment ID to search: ");
                String paymentId = scanner.nextLine(); // User enters the payment ID

                File file = new File("C:/Users/maria_yiuhvtt/pay/paymentsRecords.txt");
                try (ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file))) {
                    boolean found = false;

                    // the file contains multiple payment objects
                    while (true) {
                        try {
                            pay payment = (pay) obj.readObject();
                            if (payment.getPaymentID().equals(paymentId)) {
                                System.out.println("Payment found: \n" + payment);
                                found = true;
                                break;
                            }
                        } catch (EOFException e) {
                            break;
                        }
                    }
                    if (!found) { // Payment ID not found.
                        System.out.println("Payment ID not found.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                break;




            default: //if the user enter invalid input
                System.out.println("Invalid choice, please try again.");
                break;
            }
        }


    }
