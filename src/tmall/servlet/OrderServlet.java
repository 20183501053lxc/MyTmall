package tmall.servlet;

import tmall.bean.Order;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 因为订单的增加和删除，都是在前台进行的。 所以OrderServlet提供的是list方法和delivery(发货)方法
 */
public class OrderServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }
    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderDAO.get(id);
        order.setDeliveryDate(new Date());
        order.setStatus(orderDAO.waitConfirm);//发货,因此从发货状态变成待收货状态
        orderDAO.update(order);
        return "@admin_order_list";
    }
    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Order> orderList = orderDAO.list(page.getStart(), page.getCount());
        orderItemDAO.fill(orderList);
        int total = userDAO.getTotal();//处理分页
        page.setTotal(total);
        request.setAttribute("page",page);
        request.setAttribute("os",orderList);
        return "admin/listOrder.jsp";
    }
}
