<%--
productsByCategory.jsp现实当前分类下的所有产品
通过forEach遍历c.products集合里的每个产品，并把产品标题，价格，图片，评价数，成交数打印出来
解释下：categorycount，这个是用于测试的，在访问地址的时候加这个参数
http://127.0.0.1:8080/tmall/forecategory?cid=74&categorycount=2
这样就只显示2个产品，仅供测试使用，未免有同学不理解，特拿出来讲解一下
<c:if test="${empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="100"/>
</c:if>
<c:if test="${!empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="${param.categorycount}"/>
</c:if>
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<c:if test="${empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="100"/>
</c:if>

<c:if test="${!empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="${param.categorycount}"/>
</c:if>

<div class="categoryProducts">
    <c:forEach items="${c.products}" var="p" varStatus="stc">
        <c:if test="${stc.count<=categorycount}">
            <div class="productUnit" price="${p.promotePrice}">
                <div class="productUnitFrame">
                    <a href="foreproduct?pid=${p.id}">
                        <img class="productImage" src="img/productSingle_middle/${p.firstProductImage.id}.jpg">
                    </a>
                    <span class="productPrice">¥<fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/></span>
                    <a class="productLink" href="foreproduct?pid=${p.id}">
                            ${fn:substring(p.name, 0, 50)}
                    </a>

                    <a  class="tmallLink" href="foreproduct?pid=${p.id}">天猫专卖</a>

                    <div class="show1 productInfo">
                        <span class="monthDeal ">月成交 <span class="productDealNumber">${p.saleCount}笔</span></span>
                        <span class="productReview">评价<span class="productReviewNumber">${p.reviewCount}</span></span>
                        <span class="wangwang">
					<a class="wangwanglink" href="#nowhere">
						<img src="img/site/wangwang.png">
					</a>

					</span>
                    </div>
                </div>
            </div>
        </c:if>
    </c:forEach>
    <div style="clear:both"></div>
</div>
