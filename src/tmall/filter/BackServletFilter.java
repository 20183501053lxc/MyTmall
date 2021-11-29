package tmall.filter;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import sun.nio.cs.ext.MacCentralEurope;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BackServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /**
         * request.getRequestURL() 返回全路径
         * request.getRequestURI() 返回除去host（域名或者ip）部分的路径
         * request.getContextPath()返回工程名部分，如果工程映射为/，此处返回则为空
         * request.getServletPath()返回除去host和工程名部分的路径
         */
        //获取application对象
        ServletContext application = request.getServletContext();
        //获取项目根路径
        String contextPath = application.getContextPath();
        //获取除去host（域名或者ip）部分的路径
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        if(uri.startsWith("/admin_")){
            String servletPath = StringUtils.substringBetween(uri,"_","_")+"Servlet";
            String method = StringUtils.substringAfterLast(uri,"_");
            request.setAttribute("method",method);
            servletRequest.getRequestDispatcher("/"+servletPath).forward(request,response);
            return;
        }
        //如果不是admin路径下的请求，则继续放行至下一个监听
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
