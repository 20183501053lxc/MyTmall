<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%--
置顶导航页面
这里会根据用户是否登录，决定是否显示退出按钮，或者登录注册按钮，以及购物车中的商品数量
--%>
<nav class="top">
    <a href="${contextPath}">
        <span style="color:#C40000;margin:0px" class=" glyphicon glyphicon-home redColor"></span>
        天猫首页
    </a>

    <span>喵，欢迎来天猫</span>

    <c:if test="${!empty user}">
        <a href="login.jsp">${user.name}</a>
        <a href="forelogout">退出</a>
    </c:if>

    <c:if test="${empty user}">
        <a href="login.jsp">请登录</a>
        <a href="register.jsp">免费注册</a>
    </c:if>

    <span class="pull-right">
			<a href="forebought">我的订单</a>
			<a href="forecart">
			<span style="color:#C40000;margin:0px" class=" glyphicon glyphicon-shopping-cart redColor"></span>
			购物车<strong>${cartTotalItemNumber}</strong>件</a>
		</span>

</nav>

