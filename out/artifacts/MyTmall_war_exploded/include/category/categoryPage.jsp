<%--
categoryPage.jsp 里有3个内容
1. 显示当前分类图片
<img src="img/category/${c.id}.jpg">
2. 排序条 sortBar.jsp
3. 产品列表 productsByCategory.jsp
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<title>模仿天猫官网-${c.name}</title>
<div id="category">
    <div class="categoryPageDiv">
        <img src="img/category/${c.id}.jpg">
        <%@include file="sortBar.jsp"%>
        <%@include file="productsByCategory.jsp"%>
    </div>

</div>
