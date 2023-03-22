package org.example.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Participant;
import org.example.entity.Team;
import org.example.entity.User;
import org.example.repo.generic.ParticipantRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepo implements ParticipantRepo {

    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public ParticipantDBRepo(Properties properties) {
        logger.info("creating Participant Repo");
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Participant> getAll() throws SQLException {
        logger.info("getting partocopants from DB");
        Connection connection = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from participants")) {
            getParticipantInfo(participants, preparedStatement);
        }
        return participants;
    }

    @Override
    public void add(Participant obj) {
        logger.info("adding new participant");
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into participants(code, name, team_code, capacity) values(?, ?, ?, ?)")) {
            preparedStatement.setInt(1, obj.getCode());
            preparedStatement.setString(2, obj.getName());
            preparedStatement.setInt(3, obj.getTeamCode());
            preparedStatement.setInt(4, obj.getCapacity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("ParticipantDB prepare statement error+: " + e.toString());
        }
        logger.info("participant added");

    }

    @Override
    public void remove(Participant obj) {
        logger.info("deleting participant " + obj.getCode().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from participants where code = ?")) {
            preparedStatement.setInt(1, obj.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("ParticipantDB prepare statement error-: " + e.toString());
        }
        logger.info("participant deleted");

    }

    @Override
    public void modify(Participant obj) {
        logger.info("modify participant " + obj.getCode().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update participants set name=?, team_code=?, capacity=? where code=? ")) {
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setInt(2, obj.getTeamCode());
            preparedStatement.setInt(3, obj.getCapacity());
            preparedStatement.setInt(4, obj.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("--ParticipantDB prepare statement error: " + e.toString());
        }
        logger.info("participant updated");

    }

    @Override
    public Participant search(Participant obj) {
        logger.info("searching for participant " + obj.getCode().toString());
        try {
            for (Participant participant : getAll()) {
                if (participant.equals(obj)) {
                    logger.info("--participant found");
                    return participant;
                }
            }
        } catch (Exception e) {
            logger.error("--ParticipantDB prepare statement error: " + e.getMessage());
        }
        logger.info("--Participant not found");
        return null;
    }

    public List<Participant> findByTeam(Team team) throws SQLException {
        logger.info("getting participants by team");
        Connection connection = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from participants where team_code = ?")) {
            preparedStatement.setInt(1, team.getCode());
            getParticipantInfo(participants, preparedStatement);
        }
        return participants;
    }

    private void getParticipantInfo(List<Participant> participants, PreparedStatement preparedStatement) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            getInfo(participants, resultSet);
        } catch (SQLException e) {
            logger.error("ParticipantDB error: " + e.toString());
        }
    }

    static void getInfo(List<Participant> participants, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Integer code = resultSet.getInt("code");
            String name = resultSet.getString("name");
            Integer teamCode = resultSet.getInt("team_code");
            Integer capacity = resultSet.getInt("capacity");
            participants.add(new Participant(code, name, teamCode, capacity));
        }
    }
}
