package tmall.servlet;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.comparator.*;
import tmall.dao.OrderDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForeServlet extends BaseForeServlet{
    /**
     * 首页方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String home(HttpServletRequest request, HttpServletResponse response, Page page){
        List<Category> cs = categoryDAO.list();
        //为这些分类填充产品集合，即为每个Category对象，设置products属性
        productDAO.fill(cs);
        //为这些分类填充推荐产品集合，即为每个Category对象，设置productsByRow属性
        productDAO.fillByRow(cs);
        request.setAttribute("cs",cs);

        return "home.jsp";
    }

    /**
     * 注册方法
     * 为什么要用 HtmlUtils.htmlEscape？ 因为注册的时候，ForeServlet.register()，就进行了转义，所以这里也需要转义。
     * 有些同学在恶意注册的时候，会使用诸如 <script>alert('papapa')</script> 这样的名称，
     * 会导致网页打开就弹出一个对话框。 那么在转义之后，就没有这个问题了。
     */
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);

        boolean exist = userDAO.isExist(name);
        if(exist){
            request.setAttribute("msg","用户名已存在，请输入其他用户名");
            return "register.jsp";
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDAO.add(user);
        return "@registerSuccess.jsp";
    }

    /**
     * 登录方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");
        User user = userDAO.get(name, password);
        if(null == user){
            request.setAttribute("msg","账号密码错误!");
            return "login.jsp";
        }
        request.getSession().setAttribute("user",user);
        return "@forehome";
    }

    /**
     * 退出登录方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        //删除用户对应的订单项集合
        request.getSession().removeAttribute("ois");
        return "@forehome";
    }

    /**
     * 获取产品详情页的产品方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        /**
         * 1. 获取参数pid
         * 2. 根据pid获取Product 对象p
         * 3. 根据对象p，获取这个产品对应的单个图片集合
         * 4. 根据对象p，获取这个产品对应的详情图片集合
         * 5. 获取产品的所有属性值
         * 6. 获取产品对应的所有的评价
         * 7. 设置产品的销量和评价数量
         * 8. 把上述取值放在request属性上
         * 9. 服务端跳转到 "product.jsp" 页面
         */
        Integer pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        //分别获取产品的单个图片和详情图片
        List<ProductImage> singleImg = productImageDAO.list(product, productImageDAO.type_single);
        List<ProductImage> detailImg = productImageDAO.list(product, productImageDAO.type_detail);
        product.setProductSingleImages(singleImg);
        product.setProductDetailImages(detailImg);

        List<PropertyValue> propertyValueList = propertyValueDAO.list(product.getId());

        List<Review> reviewList = reviewDAO.list(product.getId());
        productDAO.setSaleAndReviewNumber(product);

        request.setAttribute("reviews", reviewList);

        request.setAttribute("p", product);
        request.setAttribute("pvs", propertyValueList);
        return "product.jsp";
    }

    /**
     * 模态框的检查登录方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null!=user)
            return "%success";
        return "%fail";
    }

    /**
     * 模态框的登录方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userDAO.get(name,password);

        if(null==user){
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success";
    }

    /**
     * 分类页方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        /**
         * 1. 获取参数cid
         * 2. 根据cid获取分类Category对象 c
         * 3. 为c填充产品
         * 4. 为产品填充销量和评价数据
         * 5. 获取参数sort
         * 5.1 如果sort==null，即不排序
         * 5.2 如果sort!=null，则根据sort的值，从5个Comparator比较器中选择一个对应的排序器进行排序
         * 6. 把c放在request中
         * 7. 服务端跳转到 category.jsp
         */
        Integer cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        productDAO.fill(category);
        //为分类中的所有产品填充销量和评论数量
        productDAO.setSaleAndReviewNumber(category.getProducts());
        String sort = request.getParameter("sort");
        if(null != sort){
            //有排序元素
            switch (sort){
                case "review"://评论数量
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;
                case "saleCount" :
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        request.setAttribute("c",category);
        return "category.jsp";
    }

    /**
     * 搜索框函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String search(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 通过search.jsp或者simpleSearch.jsp提交数据到路径 /foresearch， 导致ForeServlet.search()方法被调用
         * 1. 获取参数keyword
         * 2. 根据keyword进行模糊查询，获取满足条件的前20个产品
         * 3. 为这些产品设置销量和评价数量
         * 4. 把产品结合设置在request的"ps"属性上
         * 5. 服务端跳转到 searchResult.jsp 页面
         */
        String keyword = request.getParameter("keyword");
        List<Product> ps = productDAO.search(keyword, 0, 20);
        productDAO.setSaleAndReviewNumber(ps);
        request.setAttribute("ps",ps);
        return "searchResult.jsp";
    }

    /**
     * 立即购买函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 通过上个步骤访问的地址 /forebuyone 导致ForeServlet.buyone()方法被调用
         * 1. 获取参数pid
         * 2. 获取参数num
         * 3. 根据pid获取产品对象p
         * 4. 从session中获取用户对象user
         *
         * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
         * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
         * a.1 基于用户对象user，查询没有生成订单的订单项集合
         * a.2 遍历这个集合
         * a.3 如果产品是一样的话，就进行数量追加
         * a.4 获取这个订单项的 id
         *
         * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
         * b.1 生成新的订单项
         * b.2 设置数量，用户和产品
         * b.3 插入到数据库
         * b.4 获取这个订单项的 id
         *
         * 最后， 基于这个订单项id客户端跳转到结算页面/forebuy
         */
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        int oiid = 0;//订单项对应对应d
        Product product = productDAO.get(pid);
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> os = orderItemDAO.listByUser(user.getId());
        for (OrderItem orderItem : os) {
            if(orderItem.getProduct().getId() == product.getId()){
                found = true;
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemDAO.update(orderItem);
                oiid = orderItem.getId();
                break;
            }
        }
        if(!found){
            //不存在对应的订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(product);
            orderItemDAO.add(orderItem);
            oiid = orderItem.getId();
        }
        return "@forebuy?oiid="+oiid;
    }

    /**
     * 结算页面方法
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 导致ForeServlet.buy()方法被调用
         * 1. 通过getParameterValues获取参数oiid
         * 为什么这里要用getParameterValues试图获取多个oiid，而不是getParameter仅仅获取一个oiid? 因为根据购物流程环节与表关系，
         * 结算页面还需要显示在购物车中选中的多条OrderItem数据，所以为了兼容从购物车页面跳转过来的需求，
         * 要用getParameterValues获取多个oiid
         * 2. 准备一个泛型是OrderItem的集合ois
         * 3. 根据前面步骤获取的oiids，从数据库中取出OrderItem对象，并放入ois集合中
         * 4. 累计这些ois的价格总数，赋值在total上
         * 5. 把订单项集合放在session的属性 "ois" 上
         * 6. 把总价格放在 request的属性 "total" 上
         * 7. 服务端跳转到buy.jsp
         */
        String[] oiids = request.getParameterValues("oiid");
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for (String soiid : oiids) {
            int oiid = Integer.parseInt(soiid);
            OrderItem orderItem = orderItemDAO.get(oiid);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            ois.add(orderItem);
        }
        //登录的时候记得去掉这个集合ois，否则退出登录时还是会有ois在
        request.getSession().setAttribute("ois", ois);
        request.setAttribute("total", total);
        return "buy.jsp";
    }

    /**
     * 加入购物车函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        /**
         * 上一步访问地址/foreaddCart导致ForeServlet.addCart()方法被调用
         * addCart()方法和立即购买中的 ForeServlet.buyone()步骤做的事情是一样的，区别在于返回不一样
         * 1. 获取参数pid
         * 2. 获取参数num
         * 3. 根据pid获取产品对象p
         * 4. 从session中获取用户对象user
         *
         * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
         * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
         * a.1 基于用户对象user，查询没有生成订单的订单项集合
         * a.2 遍历这个集合
         * a.3 如果产品是一样的话，就进行数量追加
         * a.4 获取这个订单项的 id
         *
         * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
         * b.1 生成新的订单项
         * b.2 设置数量，用户和产品
         * b.3 插入到数据库
         * b.4 获取这个订单项的 id
         *
         * 与ForeServlet.buyone() 客户端跳转到结算页面不同的是， 最后返回字符串"success"，表示添加成功
         */
        int pid = Integer.parseInt(request.getParameter("pid"));
        Integer num = Integer.parseInt(request.getParameter("num"));//订单项数量
        Product product = productDAO.get(pid);
        User user = (User) request.getSession().getAttribute("user");

        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        boolean found  = false;
        for (OrderItem orderItem : orderItems) {
            if(orderItem.getProduct().getId() == product.getId()){
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemDAO.update(orderItem);
                found = true;
                break;
            }
        }
        if(!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setNumber(num);
            //更新的时候会将orderItem的id也一并更新
            orderItemDAO.add(orderItem);
        }
        return "%success";
    }

    /**
     * 购物车详情页函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 访问地址/forecart导致ForeServlet.cart()方法被调用
         * 1. 通过session获取当前用户
         * 所以一定要登录才访问，否则拿不到用户对象
         * 2. 获取被这个用户关联的订单项集合 ois
         * 3. 把ois放在request中
         * 4. 服务端跳转到cart.jsp
         */
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        request.setAttribute("ois",ois);
        return "cart.jsp";
    }

    /**
     * 购物车中修改订单项数量的函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 点击增加或者减少按钮后，根据 cartPage.jsp 中的js代码，会通过Ajax访问/forechangeOrderItem路径，导致ForeServlet.changeOrderItem()方法被调用
         * 1. 判断用户是否登录
         * 2. 获取pid和number
         * 3. 遍历出用户当前所有的未生成订单的OrderItem
         * 4. 根据pid找到匹配的OrderItem，并修改数量后更新到数据库
         * 5. 返回字符串"success"
         */
        User user = (User) request.getSession().getAttribute("user");
        if(null == user){
            return "%fail";//用户未登录
        }

        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));

        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem orderItem : ois) {
            if(orderItem.getProduct().getId() == pid){
                orderItem.setNumber(number);
                orderItemDAO.update(orderItem);
                break;
            }
        }
        return "%success";
    }

    /**
     * 购物车中删除订单项的函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 1. 判断用户是否登录
         * 2. 获取oiid
         * 3. 删除oiid对应的OrderItem数据
         * 4. 返回字符串"success"
         */
        User user =(User) request.getSession().getAttribute("user");
        if(null==user)
            return "%fail";
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }

    /**
     * 用户结算订单，生成订单函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 提交订单访问路径 /forecreateOrder, 导致ForeServlet.createOrder 方法被调用
         * 1. 从session中获取user对象
         * 2. 获取地址，邮编，收货人，用户留言等信息
         * 3. 根据当前时间加上一个4位随机数生成订单号
         * 4. 根据上述参数，创建订单对象
         * 5. 把订单状态设置为等待支付
         * 6. 加入到数据库
         * 7. 从session中获取订单项集合 ( 在结算功能的ForeServlet.buy() 13行，订单项集合被放到了session中 )
         * 8. 遍历订单项集合，设置每个订单项的order，并更新到数据库
         * 9. 统计本次订单的总金额
         * 10. 客户端跳转到确认支付页forealipay，并带上订单id和总金额
         */
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois.isEmpty())
            return "@login.jsp";
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        Order order = new Order();
        //生成四位数的订单号
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setMobile(mobile);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setUserMessage(userMessage);
        order.setUser(user);
        order.setCreateDate(new Date());
        order.setStatus(OrderDAO.waitPay);//设置状态为等待支付

        orderDAO.add(order);
        int total = 0;//计算订单总金额
        for (OrderItem orderItem : ois) {
            orderItem.setOrder(order);
            orderItemDAO.update(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        return "@forealipay?oid="+order.getId()+"&total="+total;
    }

    /**
     * 调用支付方法(只是跳转到支付页面，没有实现支付)
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
        return "alipay.jsp";
    }

    /**
     * 支付成功函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        /**
         * 1.1 获取参数oid
         * 1.2 根据oid获取到订单对象order
         * 1.3 修改订单对象的状态和支付时间
         * 1.4 更新这个订单对象到数据库
         * 1.5 把这个订单对象放在request的属性"o"上
         * 1.6 服务端跳转到payed.jsp
         */
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setDeliveryDate(new Date());
        orderDAO.update(order);
        request.setAttribute("o",order);
        return "payed.jsp";
    }

    /**
     * 显示用户购买过的订单函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String bought(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * /forebought导致ForeServlet.bought()方法被调用
         * 1. 通过session获取用户user
         * 2. 查询user所有的状态不是"delete" 的订单集合os
         * 3. 为这些订单填充订单项
         * 4. 把os放在request的属性"os"上
         * 5. 服务端跳转到bought.jsp
         */
        User user = (User) request.getSession().getAttribute("user");
        List<Order> orderList = orderDAO.list(user.getId(), OrderDAO.delete);
        orderItemDAO.fill(orderList);
        request.setAttribute("os",orderList);
        return "bought.jsp";
    }

    /**
     * 点击确认收货后跳转页面的函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 2. ForeServlet.confirmPay()方法被调用
         * 2.1 获取参数oid
         * 2.2 通过oid获取订单对象o
         * 2.3 为订单对象填充订单项
         * 2.4 把订单对象放在request的属性"o"上
         * 2.5 服务端跳转到 confirmPay.jsp
         */
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        orderItemDAO.fill(order);
        request.setAttribute("o",order);
        return "confirmPay.jsp";
    }

    /**
     * 确认收货成功的函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 1.1 获取参数oid
         * 1.2 根据参数oid获取Order对象o
         * 1.3 修改对象o的状态为等待评价，修改其确认支付时间
         * 1.4 更新到数据库
         * 1.5 服务端跳转到orderConfirmed.jsp页面
         */
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitReview);
        orderDAO.update(order);
        return "orderConfirmed.jsp";
    }

    /**
     * 评价产品页面
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String review(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 1.1 获取参数oid
         * 1.2 根据oid获取订单对象o
         * 1.3 为订单对象填充订单项
         * 1.4 获取第一个订单项对应的产品,因为在评价页面需要显示一个产品图片，那么就使用这第一个产品的图片了
         * 1.5 获取这个产品的评价集合
         * 1.6 为产品设置评价数量和销量
         * 1.7 把产品，订单和评价集合放在request上
         * 1.8 服务端跳转到 review.jsp
         */
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        orderItemDAO.fill(order);
        List<OrderItem> ois = orderItemDAO.listByOrder(order.getId());
        OrderItem orderItem = ois.get(0);
        Product product = orderItem.getProduct();
        List<Review> reviewList = reviewDAO.list(product.getId());
        productDAO.setSaleAndReviewNumber(product);
        request.setAttribute("p",product);
        request.setAttribute("o",order);
        request.setAttribute("reviews",reviewList);
        return "review.jsp";
    }

    /**
     * 对订单做出评价函数
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page){
        /**
         * 1. ForeServlet.doreview()
         * 1.1 获取参数oid
         * 1.2 根据oid获取订单对象o
         * 1.3 修改订单对象状态
         * 1.4 更新订单对象到数据库
         * 1.5 获取参数pid
         * 1.6 根据pid获取产品对象
         * 1.7 获取参数content (评价信息)
         * 1.8 对评价信息进行转义，道理同注册ForeServlet.register()
         * 1.9 从session中获取当前用户
         * 1.10 创建评价对象review
         * 1.11 为评价对象review设置 评价信息，产品，时间，用户
         * 1.12 增加到数据库
         * 1.13.客户端跳转到/forereview： 评价产品页面，并带上参数showonly=true
         */
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.finish);
        orderDAO.update(order);

        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        String content = request.getParameter("content");//获取评价信息
        //对评价信息进行转义
        content = HtmlUtils.htmlEscape(content);
        User user = (User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setUser(user);
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setProduct(product);

        reviewDAO.add(review);

        return "@forereview?oid="+oid+"&showonly=true";
    }
}
