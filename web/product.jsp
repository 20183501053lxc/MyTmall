<%--
与 register.jsp 相仿，product.jsp也包含了header.jsp, top.jsp, simpleSearch.jsp， footer.jsp 等公共页面。
中间是产品业务页面 productPage.jsp
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<%@include file="include/header.jsp"%>
<%@include file="include/top.jsp"%>
<%@include file="include/simpleSearch.jsp"%>
<%@include file="include/product/productPage.jsp"%>
<%@include file="include/footer.jsp"%>
