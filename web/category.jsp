<%--
category.jsp也包含了header.jsp, top.jsp, simpleSearch.jsp， footer.jsp 等公共页面。
中间是分类业务页面 categoryPage.jsp
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<%@include file="include/header.jsp"%>
<%@include file="include/top.jsp"%>
<%@include file="include/search.jsp"%>
<%@include file="include/category/categoryPage.jsp"%>
<%@include file="include/footer.jsp"%>
