package org.example.services;

import org.example.entity.Participant;
import org.example.entity.Probe;
import org.example.entity.Team;
import org.example.entity.User;
import org.example.repo.ParticipantDBRepo;
import org.example.repo.ProbesDBRepo;
import org.example.repo.TeamsDBRepo;
import org.example.repo.UserDBRepo;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Service {
    private Properties constring;
    private UserDBRepo userRepo;
    private ProbesDBRepo probeRepo;
    private TeamsDBRepo teamRepo;
    private ParticipantDBRepo participantRepo;

    public Service(Properties properties) {
        constring = properties;
        this.userRepo = new UserDBRepo(constring);
        this.probeRepo = new ProbesDBRepo(constring);
        this.teamRepo = new TeamsDBRepo(constring);
        this.participantRepo = new ParticipantDBRepo(constring);
    }

    public boolean checkUser(String userName, String passwd) {
        User user = userRepo.search(new User(userName, ""));
        if (user == null)
            return false;
        return user.getPasswd().equals(passwd);
    }

    public List<Probe> getProbes() throws SQLException {
        return probeRepo.getAll();
    }

    public int getParticipantsCount(int probeCode) {
        return probeRepo.getParticipants(new Probe(probeCode, "")).size();
    }

    public List<Participant> getParticipantsByTeam(String team) throws SQLException {
        return participantRepo.findByTeam(new Team(0, team));
    }

    public List<Team> getTeams() throws SQLException {
        return teamRepo.getAll();
    }

    public void addParticipant(String name, String team, String capacity) throws SQLException {
        int maxCode = 0;
        maxCode = participantRepo.maxCode(maxCode);
        int participantCode = maxCode + 1;
        int teamCode = teamRepo.findByName(team).getCode();
        participantRepo.add(new Participant(participantCode,
                name,
                teamCode,
                Integer.getInteger(capacity)));
    }
}
