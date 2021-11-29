package tmall.dao;

import tmall.bean.Category;
import tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO {
    //获取总数
    public int getTotal(){
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select count(*) from category";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                total = rs.getInt(1);//获取最大的那个id就是总数
            }
            /**
             * 正确关闭资源的顺序
             */
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return total;
    }
    //查询所有
    public List<Category> list(){
        return list(0, Short.MAX_VALUE);
    }
    //分页查询
    public List<Category> list(int start,int end){
        List<Category> list = new LinkedList<>();
        String sql = "select * from category order by id desc limit ?,?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,start);
            ps.setInt(2,end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Category bean = new Category();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                bean.setId(id);
                bean.setName(name);
                list.add(bean);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    public Category get(int id){
        Category bean = null;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from category where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                bean = new Category();
                String name = rs.getString(2);
                bean.setName(name);
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
    public void update(Category bean){
        try {
            Connection c = DBUtil.getConnection();
            String sql = "update category set name = ? where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,bean.getName());
            ps.setInt(2,bean.getId());
            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void delete(int id){
        try {
            Connection c = DBUtil.getConnection();
            String sql = "delete from category where id = "+id;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void add(Category bean){
        try {
            Connection c = DBUtil.getConnection();
            String sql = "insert into category values(null,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,bean.getName());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);//获取结果集的第一列的id
                bean.setId(id);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
