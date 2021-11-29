package tmall.dao;

import tmall.bean.Category;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO {
    //基本增删改查操作
    public int getTotal(){//获取用户总数
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select count(*) from user ";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                total = rs.getInt(1);//获取最大的那个id就是总数
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return total;
    }
    //查询所有
    public List<User> list(){
        return list(0, Short.MAX_VALUE);
    }
    //分页查询
    public List<User> list(int start,int end){
        List<User> list = new LinkedList<>();
        String sql = "select * from user order by id desc limit ?,?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,start);
            ps.setInt(2,end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                User user = new User();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String password = rs.getString("password");
                user.setId(id);
                user.setName(name);
                user.setPassword(password);
                list.add(user);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    public User get(int id){
        User bean = null;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from user where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bean = new User();
                String name = rs.getString("name");
                bean.setName(name);
                String password = rs.getString("password");
                bean.setPassword(password);
                bean.setId(id);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bean;
    }
    //非CURD操作
    public boolean isExist(String name){
        User user = get(name);
        return user != null;
    }
    public User get(String name){
        User user = null;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from user where name = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setName(name);
                String password = rs.getString("password");
                user.setPassword(password);
                user.setId(rs.getInt("id"));
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
    public User get(String name, String password) {
        User bean = null;
        String sql = "select * from user where name = ? and password = ?";
        try {
            Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs =ps.executeQuery();

            if (rs.next()) {
                bean = new User();
                int id = rs.getInt("id");
                bean.setName(name);
                bean.setPassword(password);
                bean.setId(id);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    public void add(User bean) {

        String sql = "insert into user values(null ,? ,?)";
        try  {
            Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getPassword());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
