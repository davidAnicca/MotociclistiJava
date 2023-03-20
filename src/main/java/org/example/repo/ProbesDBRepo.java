package org.example.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Probe;
import org.example.entity.Team;
import org.example.repo.JdbcUtils;
import org.example.repo.generic.ProbesRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProbesDBRepo implements ProbesRepo {

    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public ProbesDBRepo(Properties properties){
        logger.info("create probes repo");
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Probe> getAll() throws SQLException {
        logger.info("getting probes from DB");
        Connection connection = dbUtils.getConnection();
        List<Probe> probes = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from probes")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer code = resultSet.getInt("code");
                    String name = resultSet.getString("name");
                    probes.add(new Probe(code, name));
                }
            } catch (SQLException e) {
                logger.error("ProbeDB error: " + e.toString());
            }
        }
        return probes;
    }

    @Override
    public void add(Probe obj) {
        logger.info("adding new probe");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into probes(code, name) values(?, ?)")) {
            preparedStatement.setInt(1, obj.getCod());
            preparedStatement.setString(2, obj.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("ProbeDB prepare statement error: " + e.toString());
        }
        logger.info("probe added");
    }

    @Override
    public void remove(Probe obj) {
        logger.info("deleting PROBE " + obj.getCod().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from probes where code = ?")) {
            preparedStatement.setInt(1, obj.getCod());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("ProbeDB prepare statement error-: " + e.toString());
        }
        logger.info("probe deleted");
    }

    @Override
    public void modify(Probe obj) {
        logger.info("updating probe " + obj.getCod().toString());
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update probes set name=? where code=?")) {
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setInt(2, obj.getCod());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("--ProbeDB prepare statement error: " + e.toString());
        }
        logger.info("probe updated");
    }

    @Override
    public Probe search(Probe obj) {
        logger.info("searching for probe " + obj.getCod().toString());
        try {
            for(Probe probe : getAll()){
                if(probe.equals(obj)) {
                    logger.info("--probe found");
                    return probe;
                }
            }
        }catch (Exception e){
            logger.error("--ProbeDB prepare statement error?: " + e.getMessage());
        }
        logger.info("--probe not found");
        return null;
    }
}
