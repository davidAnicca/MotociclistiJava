package org.example;

import org.example.entity.Team;
import org.example.entity.User;
import org.example.repo.TeamsDBRepo;
import org.example.repo.UserDBRepo;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
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

        tests(props);

    }

    private static void tests(Properties props) {
        try {
            testUserDB(props);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            testTeamDB(props);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private static void testTeamDB(Properties props) throws SQLException {
        System.out.println("test teams");
        TeamsDBRepo teamsRepo = new TeamsDBRepo(props);
        List<Team> teams = teamsRepo.getAll();
        System.out.println("teams: " + teams.size());
        for(Team team: teams)
            System.out.println(team.getCode().toString() + " " + team.getName());

        Team newTeam = new Team(LocalTime.now().getHour()*10+LocalTime.now().getSecond(), LocalTime.now().toString());
        teamsRepo.add(newTeam);
        Team teamToDel = new Team(-1, "shoudntBeHere");
        teamsRepo.add(teamToDel);
        teamsRepo.remove(teamToDel);
        newTeam.setName("new Name");
        teamsRepo.modify(newTeam);
    }

    private static void testUserDB(Properties props) throws SQLException {
        System.out.println("test users");
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