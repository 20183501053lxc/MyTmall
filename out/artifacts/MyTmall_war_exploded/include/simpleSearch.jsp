<%--
与首页的search.jsp不太一样的是，这个搜索栏要更简单一些，并且左右分开。
注： 这里${cs} 中用到的数据，是在代码讲解 - ForeServletFilter的48-52行代码
        List<Category> cs=(List<Category>) request.getAttribute("cs");
        if(null==cs){
            cs=new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
中取来的
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<div >
    <a href="${contextPath}">
        <img id="simpleLogo" class="simpleLogo" src="img/site/simpleLogo.png">
    </a>

    <form action="foresearch" method="post" >
        <div class="simpleSearchDiv pull-right">
            <input type="text" placeholder="平衡车 原汁机" name="keyword">
            <button class="searchButton" type="submit">搜天猫</button>
            <div class="searchBelow">
                <c:forEach items="${cs}" var="c" varStatus="st">
                    <c:if test="${st.count>=8 and st.count<=11}">
					<span>
						<a href="forecategory?cid=${c.id}">
                                ${c.name}
                        </a>
						<c:if test="${st.count!=11}">
                            <span>|</span>
                        </c:if>
					</span>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </form>
    <div style="clear:both"></div>
</div>
