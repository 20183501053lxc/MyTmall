package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PropertyValueDAO {
    public int getTotal(){
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select count(*) from propertyvalue";
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
    public void add(PropertyValue bean){
        String sql = "insert into propertyvalue values(null,?,?,?)";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());

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
    public void update(PropertyValue bean){
        String sql = "update propertyvalue set pid = ?,ptid = ?,value = ? where id = ?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.setInt(4,bean.getId());
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
            String sql = "delete from propertyvalue where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public PropertyValue get(int id){
        PropertyValue bean = new PropertyValue();
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from propertyvalue where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bean;
    }
    public PropertyValue get(int pid,int ptid){
        PropertyValue bean = null;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from propertyvalue where pid = ? and ptid = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,pid);
            ps.setInt(2,ptid);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                bean = new PropertyValue();
                int id = rs.getInt("id");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
            }
            rs.close();
            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bean;
    }
    public List<PropertyValue> list(){
        return list(0,Short.MAX_VALUE);
    }
    public List<PropertyValue> list(int start,int count){
        List<PropertyValue> beans = new LinkedList<>();
        String sql = "select * from propertyvalue order by id desc limit ?,?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,start);
            ps.setInt(2,count);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);

                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);

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
    public void init(Product p){
        List<Property> pts = new PropertyDAO().list(p.getCategory().getId());

        for(Property pt : pts){
            PropertyValue pv = get(p.getId(), pt.getId());
            if(null == pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);//指向当前对象的add方法
            }
        }
    }
    public List<PropertyValue> list(int pid){
        List<PropertyValue> beans = new LinkedList<>();
        String sql = "select * from propertyvalue where pid = ? order by ptid desc";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt("id");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);

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
}
