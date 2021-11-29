package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReviewDAO {
    public int getTotal(){
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select count(*) from review";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                total = rs.getInt(1);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return total;
    }
    public int getTotal(int pid){
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select count(*) from review where pid = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                total = rs.getInt(1);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return total;
    }
    public void add(Review bean){
        String sql = "insert into review values(null,?,?,?,?)";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                bean.setId(id);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void update(Review bean){
        String sql = "update review set content = ?,uid = ?,pid = ?,createDate = ? where id = ?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5,bean.getId());

            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "delete from review where id = ?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Review get(int id){
        Review bean = new Review();
        try {
            String sql = "select * from review where id = ?";
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setProduct(product);
                bean.setUser(user);
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
    public List<Review> list(int pid){
        return list(pid,0,Short.MAX_VALUE);
    }
    public List<Review> list(int pid,int start,int count){
        List<Review> beans = new LinkedList<>();
        String sql = "select * from review where pid = ? order by id desc limit ?,?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review bean = new Review();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setProduct(product);
                bean.setUser(user);
                beans.add(bean);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return beans;
    }
    public boolean isExist(String content,int pid){
        String sql = "select * from review where content = ? and pid = ?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,content);
            ps.setInt(2,pid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
