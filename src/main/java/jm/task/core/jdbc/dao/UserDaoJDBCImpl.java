package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    final String TABLE_NAME = "users_table";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS `users_database`.`users_table` (
                `user_id` BIGINT(3) NOT NULL AUTO_INCREMENT,
                `user_name` VARCHAR(45) NOT NULL,
                `user_lastname` VARCHAR(45) NOT NULL,
                `user_age` SMALLINT(3) NOT NULL,                    
                PRIMARY KEY (`user_id`))
                """;

        try (Connection con = Util.getConnection(); PreparedStatement prst = con.prepareStatement(sql)) {
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS `users_database`.`users_table`";
        try (Connection con = Util.getConnection(); PreparedStatement prst = con.prepareStatement(sql)) {
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveUser(String name, String lastName, byte age) {

        try (Connection con = Util.getConnection();
             PreparedStatement prst
                     = con.prepareStatement("INSERT INTO users_table(user_name, user_lastname, user_age) " +
                     "VALUES (?, ?, ?)")) {

            prst.setString(1, name);
            prst.setString(2, lastName);
            prst.setInt(3, age);
            prst.executeUpdate();

            System.out.printf("User с именем – %s добавлен в базу данных",name);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeUserById(long id) {

        try (Connection con = Util.getConnection();
             PreparedStatement prst = con.prepareStatement("DELETE FROM `users_database`.`users_table` WHERE (`user_id` = ?)")) {
            prst.setLong(1, id);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        try (Connection con = Util.getConnection();
            PreparedStatement prst = con.prepareStatement("SELECT * FROM users_table")) {
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                Long user_id = rs.getLong("user_id");
                String user_name = rs.getString("user_name");
                String user_lastname = rs.getString("user_lastname");
                byte user_age = rs.getByte("user_age");
                userList.add(new User(user_id, user_name, user_lastname, user_age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM `users_database`.`users_table`";
        try (Connection con = Util.getConnection();
             PreparedStatement prst = con.prepareStatement(sql)) {
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
