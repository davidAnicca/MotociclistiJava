package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.*;
import org.example.repo.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

public class Main {

    private final static Logger log= LogManager.getLogger();

    public static void main(String[] args) {

        log.debug("test debug");
        Properties props = new Properties();
        try {
            log.error("test - this is not an error!");
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            log.error("Cannot find bd.config " + e);
        }

        //app here

        tests(props);
        log.error("test - this is not an error!");
    }

    private static void tests(Properties props) {
//        try {
//            testUserDB(props);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            testTeamDB(props);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            testParticipantDB(props);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            testProbeDB(props);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            testRegDB(props);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

    }

    private static void testRegDB(Properties props) throws SQLException {
        System.out.println("test reg");
        RegistrationDBRepo registrationDBRepo = new RegistrationDBRepo(props);
        List<Registration> registrations = registrationDBRepo.getAll();
        System.out.println("reg: " + registrations.size());
        for (Registration registration : registrations)
            System.out.println(registration.getParticipantCode().toString() + " " + registration.getProbeCode().toString());

        Registration newReg = new Registration(1, 11);
        registrationDBRepo.add(newReg);
        Registration toDel = new Registration(44, 22);
        registrationDBRepo.add(toDel);
        registrationDBRepo.remove(toDel);
    }

    private static void testTeamDB(Properties props) throws SQLException {
        System.out.println("test teams");
        TeamsDBRepo teamsRepo = new TeamsDBRepo(props);
        List<Team> teams = teamsRepo.getAll();
        System.out.println("teams: " + teams.size());
        for (Team team : teams)
            System.out.println(team.getCode().toString() + " " + team.getName());

        Team newTeam = new Team(LocalTime.now().getHour() * 10 + LocalTime.now().getSecond(), LocalTime.now().toString());
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
        for (User user : userList) {
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

    private static void testParticipantDB(Properties props) throws SQLException {
        System.out.println("test participants");
        ParticipantDBRepo participantDBRepo = new ParticipantDBRepo(props);
        List<Participant> participantList = participantDBRepo.getAll();
        System.out.println("participants: " + participantList.size());
        for (Participant participant : participantList) {
            System.out.println(participant.getCode() + " " +
                    participant.getName() + " " +
                    participant.getTeamCode() + " " +
                    participant.getCapacity());
        }
        Participant newPart = new Participant(LocalTime.now().getSecond(), "666", 150, 66);
        participantDBRepo.add(newPart);
        Participant toDel = new Participant(99, "notHere", 150, 100);
        participantDBRepo.add(toDel);
        participantDBRepo.remove(toDel);
        newPart.setTeamCode(179);
        newPart.setName("new name");
        newPart.setCapacity(88);
        participantDBRepo.modify(newPart);
    }

    private static void testProbeDB(Properties props) throws SQLException {
        System.out.println("test probes");
        ProbesDBRepo probesRepo = new ProbesDBRepo(props);
        List<Probe> probes = probesRepo.getAll();
        System.out.println("probes: " + probes.size());
        for (Probe probe : probes)
            System.out.println(probe.getCod().toString() + " " + probe.getName());

        Probe newProbe = new Probe(LocalTime.now().getSecond(), LocalTime.now().toString());
        probesRepo.add(newProbe);
        Probe toDel = new Probe(-1, "shoudntBeHere");
        probesRepo.add(toDel);
        probesRepo.remove(toDel);
        newProbe.setName("new Name");
        probesRepo.modify(newProbe);
    }
}