package org.example.repo;

import jdk.jshell.spi.ExecutionControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Registration;
import org.example.entity.Team;
import org.example.repo.generic.RegistrationRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RegistrationDBRepo implements RegistrationRepo {

    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public RegistrationDBRepo(Properties properties) {
        logger.info("creating TeamRepo");
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Registration> getAll() throws SQLException {
        logger.info("getting registrations from DB");
        Connection connection = dbUtils.getConnection();
        List<Registration> registrations = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from registrations")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer participantCode = resultSet.getInt("participant_code");
                    Integer probeCode = resultSet.getInt("probe_code");
                    registrations.add(new Registration(participantCode, probeCode));
                }
            } catch (SQLException e) {
                logger.error("RegistrationDB error: " + e.toString());
            }
        }
        return registrations;
    }

    @Override
    public void add(Registration obj) {
        logger.info("adding new registration");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into registrations(participant_code, probe_code) values(?, ?)")) {
            preparedStatement.setInt(1, obj.getParticipantCode());
            preparedStatement.setInt(2, obj.getProbeCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("RegistrationDB+ prepare statement error: " + e.toString());
        }
        logger.info("registration added");
    }

    @Override
    public void remove(Registration obj) {
        logger.info("deleting user " + obj.getParticipantCode().toString() + " " + obj.getProbeCode().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from registrations where participant_code = ? and probe_code = ?")) {
            preparedStatement.setInt(1, obj.getParticipantCode());
            preparedStatement.setInt(2, obj.getProbeCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("RegistrationDB prepare statement error-: " + e.toString());
        }
        logger.info("reg deleted");
    }

    @Override
    public void modify(Registration obj) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("never need to be used");
    }

    @Override
    public Registration search(Registration obj) {
        logger.info("searching for reg " + obj.getParticipantCode().toString() + " " + obj.getProbeCode().toString());
        try {
            for(Registration registration : getAll()){
                if(registration.equals(obj)) {
                    logger.info("--reg found");
                    return registration;
                }
            }
        }catch (Exception e){
            logger.error("--RegDB prepare statement error: " + e.getMessage());
        }
        logger.info("--Reg not found");
        return null;
    }
}
