package GrpcClient;
import java.util.Scanner;
import com.my.grpc.User;
import com.my.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
public class Client {

    public static void main(String[] args) {

        ManagedChannel channel =ManagedChannelBuilder.forAddress("localhost", 4000).usePlaintext().build();

        //stubs
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        String studentname, email, password;
        int phone, reg_no;
        boolean isLoggedIn = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Type 1 for Registration");
        System.out.println(" Type 2 for Log In");
        System.out.println(" Type 3 for Log Out");
        int choice = input.nextInt();
        if(choice == 1)
        {
            System.out.println("Register Here ");
            System.out.print("Student Name: ");
            studentname = input.next();
            System.out.print("Registration No: ");
            reg_no = input.nextInt();
            System.out.print("Phone: ");
            phone = input.nextInt();
            System.out.print("Email: ");
            email = input.next();
            System.out.print("Password: ");
            password = input.next();
            User.Registration registration = User.Registration.newBuilder().setStudentname(studentname).setRegNo(reg_no).setPhone(phone).setEmail(email).setPassword(password).build();
            User.apiResponse response = userStub.register(registration);
            if(response.getResponseCode() == 200){
                isLoggedIn = true;
            }
            System.out.println(response.getResponseMessage());
        }
        else if(choice == 2)
        {
            System.out.println("Log In Here ");
            System.out.print("Email: ");
            studentname = input.nextLine();
            input.nextLine();

            System.out.print("Password: ");
            password = input.nextLine();
            User.LoginRequest loginReq = User.LoginRequest.newBuilder().setStudentname(studentname).setPassword(password).build();
            User.apiResponse response = userStub.login(loginReq);
            System.out.println(response.getResponseMessage());
        }
        else if(choice == 3)
        {
            if(isLoggedIn)
            {
                System.out.println("log out");
            }
            else
            {
                System.out.println("Please Log In First!");
            }
        }
        else
        {
            System.out.println("Wrong choice!");
            System.out.println("Please Type Between 1, 2 and 3");
        }
    }
}
