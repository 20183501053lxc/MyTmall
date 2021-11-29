package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.PropertyValue;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ProductServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        Product product = new Product();
        /**
         * 这里因为没设置category导致dao层有空指针异常，排错了好久= =
         */
        product.setCategory(category);
        product.setName(name);
        product.setSubTitle(subTitle);
        product.setOrignalPrice(orignalPrice);
        product.setPromotePrice(promotePrice);
        product.setStock(stock);
        product.setCreateDate(new Date());
        productDAO.add(product);
        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        //没传cid参数进来，需要根据删除的product获取cid
//        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Integer id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        Integer cid = product.getCategory().getId();
        productDAO.delete(product.getId());
        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        request.setAttribute("p",product);
        return "admin/editProduct.jsp";
    }
    //编辑属性值方法和更新属性值方法
    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.get(id);
        //获取对应产品的属性值列表
        List<PropertyValue> pvs = propertyValueDAO.list(product.getId());
        request.setAttribute("p",product);
        request.setAttribute("pvs",pvs);
        return "admin/editProductValue.jsp";
    }
    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pvid = Integer.parseInt(request.getParameter("pvid"));
        String value = request.getParameter("value");//文本框的值
        PropertyValue propertyValue = propertyValueDAO.get(pvid);
        propertyValue.setValue(value);
        propertyValue.setId(pvid);
        propertyValueDAO.update(propertyValue);
        return "%success";
    }
    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));//获取分类的id
        Integer id = Integer.parseInt(request.getParameter("id"));//获取产品的id
        Product product = productDAO.get(id);
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        product.setName(name);
        product.setSubTitle(subTitle);
        product.setOrignalPrice(orignalPrice);
        product.setPromotePrice(promotePrice);
        product.setStock(stock);
        productDAO.update(product);
        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        List<Product> list = productDAO.list(cid, page.getStart(), page.getCount());
        int total = productDAO.getTotal(cid);//获取该分类产品下的总数
        page.setTotal(total);
        //拼接字符串"&cid="+c.getId()，设置给page对象的Param值。 因为产品分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
        page.setParam("&cid="+category.getId());
        request.setAttribute("c",category);
        request.setAttribute("ps",list);
        request.setAttribute("page",page);
        return "admin/listProduct.jsp";
    }
}
