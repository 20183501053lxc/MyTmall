<%--
alipay.jsp :
与 register.jsp 相仿，alipay.jsp也包含了header.jsp, top.jsp， footer.jsp 等公共页面。
中间是确认支付业务页面 alipayPage.jsp
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<%@include file="include/header.jsp"%>
<%@include file="include/top.jsp"%>
<%@include file="include/cart/alipayPage.jsp"%>
<%@include file="include/footer.jsp"%>
