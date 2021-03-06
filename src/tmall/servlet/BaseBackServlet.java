package tmall.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tmall.dao.*;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseBackServlet extends HttpServlet {

    public abstract String add(HttpServletRequest request,HttpServletResponse response,Page page);
    public abstract String delete(HttpServletRequest request,HttpServletResponse response,Page page);
    public abstract String edit(HttpServletRequest request,HttpServletResponse response,Page page);
    public abstract String update(HttpServletRequest request,HttpServletResponse response,Page page);
    public abstract String list(HttpServletRequest request,HttpServletResponse response,Page page);


    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            /*获取分页信息*/
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(request.getParameter("page.start"));
            }catch (Exception e){

            }
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            }catch (Exception e){

            }
            Page page = new Page(start,count);
            /*借助反射，调用对应的方法*/
            /*借助反射，调用对应的方法*/
            String method = (String) request.getAttribute("method");
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
                    javax.servlet.http.HttpServletResponse.class,Page.class);
            String redirect = m.invoke(this,request, response,page).toString();
            /*根据方法的返回值，进行相应的客户端跳转，服务端跳转，或者仅仅是输出字符串*/
            if(redirect.startsWith("@"))
                response.sendRedirect(redirect.substring(1));
            else if(redirect.startsWith("%"))
                response.getWriter().print(redirect.substring(1));
            else
                request.getRequestDispatcher(redirect).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }
    public InputStream parseUpload(HttpServletRequest request, Map<String,String> params){
        InputStream is = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置上传文件的大小限制为10M(设置缓冲区的大小)
            factory.setSizeThreshold(1024*10240);
            /*
            List parseRequest(HttpServletRequest request)：解析request对象，
            并把表单中的每一个输入项包装成一个fileItem 对象，并返回一个保存了所有FileItem的list集合。
             */
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while(iter.hasNext()){
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    // item.getInputStream() 获取上传文件的输入流
                    //迭代获取输入流
                    is = item.getInputStream();
                } else {
                    //用于更新分类名字或者上面的更新图片用的
                    //如果不是图片的输入流，那么就将管理者输入的分类名称传进来
                    String paramName = item.getFieldName();
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                    params.put(paramName, paramValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }
}
