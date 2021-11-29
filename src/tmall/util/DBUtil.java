package tmall.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Author: lxc001
 * @Date: 2021/11/20 15:32
 * @Description: 工具类，用于加载类驱动，获取数据库的连接
 */
public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3307;
    static String database = "tmall";
    static String encoding = "UTF-8";
    static String loginName = "root";
    static String password = "Lxc@990605";
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s",ip,port,database,encoding);
        return DriverManager.getConnection(url,loginName,password);
    }

//    public static Connection getConnection() throws SQLException{
//        Connection conn = null;
//        Properties ps = new Properties();
//        InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("druid.properties");
//        DataSource dataSource = null;
//        try {
//            System.out.println(is);
//            System.out.println(ps);
//            ps.load(is);
//            dataSource = DruidDataSourceFactory.createDataSource(ps);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        conn = dataSource.getConnection();
//        return conn;
//    }
    public static void main(String[] args) throws Exception {
        //测试
//        System.out.println(getConnection());
        System.out.println("开始连接");
        System.out.println(getConnection());
        System.out.println("连接成功");
    }
}
