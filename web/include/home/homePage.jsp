<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<title>模仿天猫官网</title>
<%--该页面的包含关系很多，建议查看网页笔记理清包含关系--%>
<div class="homepageDiv">
    <%--分类和轮播--%>
    <%@include file="categoryAndcarousel.jsp"%>
    <%--主题的17种分类以及每种分类对应的5个产品--%>
    <%@include file="homepageCategoryProducts.jsp"%>
</div>