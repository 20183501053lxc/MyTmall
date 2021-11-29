package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PropertyServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        String name = request.getParameter("name");
        //获取name和cid,查找对应的category,创建property对象,插入到数据库中即可
        Category category = categoryDAO.get(cid);
        Property property = new Property();
        property.setCategory(category);
        property.setName(name);
        propertyDAO.add(property);
        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer id = Integer.parseInt(request.getParameter("id"));

        Property property = propertyDAO.get(id);
        Integer cid = property.getCategory().getId();
        propertyDAO.delete(id);
        return "@admin_property_list?cid="+ cid;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer pid = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(pid);
        request.setAttribute("p",property);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        Property property = propertyDAO.get(id);
        property.setName(name);
        property.setCategory(category);
        propertyDAO.update(property);
        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        List<Property> list = propertyDAO.list(cid,page.getStart(),page.getCount());
        Category c = categoryDAO.get(cid);
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        request.setAttribute("page",page);
        request.setAttribute("c",c);
        request.setAttribute("ps",list);
        return "admin/listProperty.jsp";
    }



}
