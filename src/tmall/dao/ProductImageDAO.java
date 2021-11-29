package tmall.dao;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProductImageDAO {
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    public int getTotal(){
        int total = 0;
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from productimage";
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

    public void add(ProductImage bean){
        String sql = "insert into productimage values(null,?,?)";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,bean.getProduct().getId());
            ps.setString(2,bean.getType());
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
    public void update(ProductImage bean){

    }
    public void delete(int id){
        try {
            Connection c = DBUtil.getConnection();
            String sql = "delete from productimage where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ps.execute();

            ps.close();
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ProductImage get(int id){
        ProductImage bean = new ProductImage();
        try {
            Connection c = DBUtil.getConnection();
            String sql = "select * from productimage where id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                Product product = new ProductDAO().get(pid);
                bean.setProduct(product);
                bean.setType(type);
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
    public List<ProductImage> list(Product p,String type){
        return list(p,type,0,Short.MAX_VALUE);
    }
    public List<ProductImage> list(Product p,String type,int start,int count){
        List<ProductImage> beans = new LinkedList<>();
        String sql = "select * from productimage where pid = ? and type = ? order by id desc limit ?,?";
        try {
            Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,p.getId());
            ps.setString(2,type);
            ps.setInt(3,start);
            ps.setInt(4,count);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ProductImage bean = new ProductImage();
                int id = rs.getInt(1);
                bean.setId(id);
                bean.setType(type);
                bean.setProduct(p);

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
