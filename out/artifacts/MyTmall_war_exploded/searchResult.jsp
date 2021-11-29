<%--
与 register.jsp 相仿，searchResult.jsp 也包含了header.jsp, top.jsp, search.jsp， footer.jsp 等公共页面。
中间是搜索结果业务页面 searchResultPage.jsp

注： 在search.jsp中，又把参数keyword显示在输入框中

<input name="keyword" type="text" value="${param.keyword}" placeholder="时尚男鞋  太阳镜 ">
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<%@include file="include/header.jsp"%>
<%@include file="include/top.jsp"%>
<%@include file="include/search.jsp"%>
<%@include file="include/searchResultPage.jsp"%>
<%@include file="include/footer.jsp"%>
