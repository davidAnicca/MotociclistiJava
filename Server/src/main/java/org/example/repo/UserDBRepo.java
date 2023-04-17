package org.example.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.User;
import org.example.repo.generic.UserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepo implements UserRepo {

    private static final Logger logger = LogManager.getLogger();

    private final JdbcUtils dbUtils;

    public UserDBRepo(Properties properties) {
        logger.info("creating UserDBRepo");
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<User> getAll() throws SQLException {
        logger.info("getting users from DB");
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from users")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String userName = resultSet.getString("name");
                    String passwd = resultSet.getString("passwd");
                    users.add(new User(userName, passwd));
                }
            } catch (SQLException e) {
                logger.error("UserDB error: " + e.toString());
            }
        }
        return users;
    }

    @Override
    public void add(User obj) {
        logger.info("adding new user");
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into users(name, passwd) values(?, ?)")) {
            preparedStatement.setString(1, obj.getUserName());
            preparedStatement.setString(2, obj.getPasswd());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("UserDB prepare statement error: " + e.toString());
        }
        logger.info("user added");
    }

    @Override
    public void remove(User obj) {
        logger.info("deleting user " + obj.getUserName());
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from users where name = ?")) {
            preparedStatement.setString(1, obj.getUserName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("UserDB prepare statement error: " + e.toString());
        }
        logger.info("user deleted");
    }

    @Override
    public void modify(User obj) {
        logger.info("updating user " + obj.getUserName());
        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update users set passwd=? where name=?")) {
            preparedStatement.setString(1, obj.getPasswd());
            preparedStatement.setString(2, obj.getUserName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("--UserDB prepare statement error: " + e.toString());
        }
        logger.info("user updated");
    }

    @Override
    public User search(User obj) {
        logger.info("searching for user " + obj.getUserName());
        try {
            for(User user : getAll()){
                if(user.equals(obj)) {
                    logger.info("--user found");
                    return user;
                }
            }
        }catch (Exception e){
            logger.error("--UserDB prepare statement error: " + e.getMessage());
        }
        logger.info("--user not found");
        return null;
    }
}
