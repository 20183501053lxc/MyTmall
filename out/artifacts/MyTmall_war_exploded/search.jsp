<%--
每个页面都包含了搜索的jsp，首页和搜索结果页包含的是search.jsp，其他页面包含的是simpleSearch.jsp。
这两个页面都提供了一个form，提交数据keyword到foresearch这个路径
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<a href="${contextPath}">
    <img id="logo" src="img/site/logo.gif" class="logo">
</a>

<form action="foresearch" method="post" >
    <div class="searchDiv">
        <input name="keyword" type="text" value="${param.keyword}" placeholder="时尚男鞋  太阳镜 ">
        <button  type="submit" class="searchButton">搜索</button>
        <div class="searchBelow">
            <c:forEach items="${cs}" var="c" varStatus="st">
                <%--搜索框下面的推荐产品--%>
                <c:if test="${st.count>=5 and st.count<=8}">
						<span>
							<a href="forecategory?cid=${c.id}">
                                    ${c.name}
                            </a>
							<c:if test="${st.count!=8}">
                                <span>|</span>
                            </c:if>
						</span>
                </c:if>
            </c:forEach>
        </div>
    </div>
</form>
