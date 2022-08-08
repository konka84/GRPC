package user;

import com.my.grpc.User;
import com.my.grpc.userGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserService extends userGrpc.userImplBase {
    String url = "jdbc:mysql://localhost:3306/konka_grpc";
    String user = "root";
    String pass = "root1234";

    @Override
    public void register(User.Registration request, StreamObserver<User.apiResponse> responseObserver) {
        //super.register(request, responseObserver);
        System.out.println("Inside register");
        String studentname = request.getStudentname();
        int reg_no = request.getRegNo();
        int phone = request.getPhone();
        String email = request.getEmail();
        String password = request.getPassword();
        User.apiResponse.Builder response = User.apiResponse.newBuilder();
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            System.out.println("DB Connection Is Successful");
            String query = "INSERT INTO `konka_grpc`.`student_list` (`Student_Name`, `Registration`,`Phone`, `Email`,`Password`) VALUES (\'"+studentname+ "\',\'"+ reg_no + "\',\'"+ phone+ "\', \'"+ email + "\',\'" + password +"\')";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM konka_grpc.student_list where Student_Name=\""+ studentname + "\" and password = \""+password+"\"");
            if(resultSet.next()){
                response.setResponseCode(400).setResponseMessage("User Already Exists!");
            }
            else
            {
                statement.executeUpdate(query);
                response.setResponseCode(200).setResponseMessage("User Registration is Successful");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(User.LoginRequest request, StreamObserver<User.apiResponse> responseObserver) {
        System.out.println("Inside login");

        String studentname = request.getStudentname();
        String password = request.getPassword();
        User.apiResponse.Builder response = User.apiResponse.newBuilder();
        try{
            Connection connection = DriverManager.getConnection(url, user, pass);
            System.out.println("DB Connection Is Successful");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM konka_grpc.student_list where Student_Name = \""+studentname+"\"");

            if (resultSet.next())
            {
                String dbstudentname = resultSet.getString("Student_Name");
                String dbPassword = resultSet.getString("Password");

                if(studentname.equals(dbstudentname) && password.equals(dbPassword)){
                    response.setResponseCode(200).setResponseMessage("Successfully Logged in");
                }
                else {
                    response.setResponseCode(400).setResponseMessage("Invalid Student Name or Password");
                }
            }
            else
            {
                response.setResponseCode(401).setResponseMessage("Invalid User!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.empty request, StreamObserver<User.apiResponse> responseObserver) {

    }


    /*
    @Override
    public void login(User.LoginRequest request, StreamObserver<User.apiResponse> responseObserver) {
        System.out.println("Inside login");

        String username = request.getUsername();
        String password = request.getPassword();

        User.apiResponse.Builder response = User.apiResponse.newBuilder();
        if(username.equals(password)) {

            // return success message

            response.setResponseCode(0).setResponseMessage("Successfully logged in");

        }
        else {
            response.setResponseCode(100).setResponseMessage("Invalid password");
        }



        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }


    @Override
    public void logout(User.empty request, StreamObserver<User.apiResponse> responseObserver) {
        super.logout(request, responseObserver);
    }
    */

}
