package tmall.servlet;

import tmall.bean.User;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 后台管理用户只有展示用户列表，因为用户信息的修改只能够用户自己修改
 * 增加交由前台用户注册功能
 * 删除不提供（用户信息是最重要的资料）
 * 修改不提供，应该由前台用户自己完成
 */
public class UserServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
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
        List<User> list = userDAO.list(page.getStart(), page.getCount());
        int total = userDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("us", list);
        request.setAttribute("page", page);

        return "admin/listUser.jsp";
    }
}
