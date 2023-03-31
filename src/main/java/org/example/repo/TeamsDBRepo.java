package org.example.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Participant;
import org.example.entity.Team;
import org.example.entity.User;
import org.example.repo.generic.TeamsRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TeamsDBRepo implements TeamsRepo {

    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public TeamsDBRepo(Properties properties) {
        logger.info("creating TeamRepo");
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Team> getAll() throws SQLException {
        logger.info("getting teams from DB");
        Connection connection = dbUtils.getConnection();
        List<Team> teams = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from teams")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer code = resultSet.getInt("code");
                    String name = resultSet.getString("name");
                    teams.add(new Team(code, name));
                }
            } catch (SQLException e) {
                logger.error("TeamDB error: " + e.toString());
            }
        }
        return teams;
    }

    @Override
    public void add(Team obj) {
        logger.info("adding new team");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into teams(code, name) values(?, ?)")) {
            preparedStatement.setInt(1, obj.getCode());
            preparedStatement.setString(2, obj.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("TeamDB prepare statement error: " + e.toString());
        }
        logger.info("team added");
    }

    @Override
    public void remove(Team obj) {
        logger.info("deleting user " + obj.getCode().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from teams where code = ?")) {
            preparedStatement.setInt(1, obj.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("TeamDB prepare statement error-: " + e.toString());
        }
        logger.info("team deleted");
    }

    @Override
    public void modify(Team obj) {
        logger.info("updating team " + obj.getCode().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update teams set name=? where code=?")) {
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setInt(2, obj.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("--TeamDB prepare statement error: " + e.toString());
        }
        logger.info("team updated");
    }

    @Override
    public Team search(Team obj) {
        logger.info("searching for team " + obj.getCode().toString());
        try {
            for(Team team : getAll()){
                if(team.equals(obj)) {
                    logger.info("--team found");
                    return team;
                }
            }
        }catch (Exception e){
            logger.error("--TeamDB prepare statement error: " + e.getMessage());
        }
        logger.info("--team not found");
        return null;
    }

    public Team findByName(String teamName) throws SQLException {
        List<Team> teams = getAll();
        for(Team team : teams){
            if(teamName.equals(team.getName())){
                return team;
            }
        }
        return teams.get(0);
    }
}
