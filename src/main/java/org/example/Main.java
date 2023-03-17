package org.example;

import org.example.entity.User;
import org.example.repo.UserDBRepo;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        //app here
        try {
            testDB(props);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void testDB(Properties props) throws SQLException {
        System.out.println("test");
        UserDBRepo userRepo = new UserDBRepo(props);
        List<User> userList = userRepo.getAll();
        System.out.println("users: " + userList.size());
        for(User user: userList){
            System.out.println(user.getUserName() + " " + user.getPasswd());
        }
        User newUser = new User(LocalTime.now().toString(), LocalTime.now().toString());
        userRepo.add(newUser);
        User userToDel = new User(LocalTime.now().toString() + "toDel", LocalTime.now().toString());
        userRepo.add(userToDel);
        userRepo.remove(userToDel);
        newUser.setPasswd("abc");
        userRepo.modify(newUser);
    }

}