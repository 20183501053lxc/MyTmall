<%--
sortBar.jsp 即排序条，做了如下几个与数据相关的事情
1. 根据sort参数判断哪个排序按钮高亮
<td <c:if test="${'all'==param.sort||empty param.sort}">class="grayColumn"</c:if> ><a href="?cid=${c.id}&sort=all">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
<td <c:if test="${'review'==param.sort}">class="grayColumn"</c:if> ><a href="?cid=${c.id}&sort=review">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
<td <c:if test="${'date'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=date">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
<td <c:if test="${'saleCount'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=saleCount">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
<td <c:if test="${'price'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=price">价格<span class="glyphicon glyphicon-resize-vertical"></span></a></td>
2. 每个排序按钮提交到本页面，即/forecategory，并带上参数sort
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
    $(function(){
        $("input.sortBarPrice").keyup(function(){
            var num= $(this).val();
            if(num.length==0){
                $("div.productUnit").show();
                return;
            }

            num = parseInt(num);
            if(isNaN(num))
                num= 1;
            if(num<=0)
                num = 1;
            $(this).val(num);

            var begin = $("input.beginPrice").val();
            var end = $("input.endPrice").val();
            if(!isNaN(begin) && !isNaN(end)){
                console.log(begin);
                console.log(end);
                $("div.productUnit").hide();
                $("div.productUnit").each(function(){
                    var price = $(this).attr("price");
                    price = new Number(price);

                    if(price<=end && price>=begin)
                        $(this).show();
                });
            }

        });
    });
</script>
<div class="categorySortBar">

    <table class="categorySortBarTable categorySortTable">
        <tr>
            <%--
            jsp ${param.属性}用法:
            众所周知 如${id} 意思是取出某一范围中名称为id的变量，它的取值范围Page,Request,Session,Application。
            而${param.id}就不是从这四个范围取值的方式了，而是相当于 request.getParameter("id")。如同怎么得到从表单传递过来的值一样。
            描述：param 应该是el表达式中的东西，这种写法一般是可以得到请求的参数的值。
            --%>
            <td <c:if test="${'all'==param.sort||empty param.sort}">class="grayColumn"</c:if> ><a href="?cid=${c.id}&sort=all">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
            <td <c:if test="${'review'==param.sort}">class="grayColumn"</c:if> ><a href="?cid=${c.id}&sort=review">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
            <td <c:if test="${'date'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=date">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
            <td <c:if test="${'saleCount'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=saleCount">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
            <td <c:if test="${'price'==param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=price">价格<span class="glyphicon glyphicon-resize-vertical"></span></a></td>
        </tr>
    </table>

    <table class="categorySortBarTable">
        <tr>
            <td><input class="sortBarPrice beginPrice" type="text" placeholder="请输入"></td>
            <td class="grayColumn priceMiddleColumn">-</td>
            <td><input class="sortBarPrice endPrice" type="text" placeholder="请输入"></td>
        </tr>
    </table>

</div>
