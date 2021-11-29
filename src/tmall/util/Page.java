package tmall.util;

public class Page {
    private int start;//开始位置
    private int count;//每页显示的数量
    private int total;//总共有多少条数据
    private String param;//参数
    public Page(int start,int count){
        super();
        this.start = start;
        this.count = count;
    }
    /**
     * getTotalPage 根据 每页显示的数量count以及总共有多少条数据total，计算出总共有多少页
     * getLast 计算出最后一页的数值是多少
     * isHasPreviouse 判断是否有前一页
     * isHasNext 判断是否有后一页
     */
    public boolean isHasPreviouse(){
        if(start == 0){
            return false;
        }
        return true;
    }
    public boolean isHasNext(){
        if(start == getLast()){
            return false;
        }
        return true;
    }
    public int getLast(){
        int last;
        // 假设总数是50，是能够被5整除的，那么最后一页的开始就是45
        if(total % count == 0){
            last = total - count;
        }else {
            last = total - total % count;
        }
        last = last < 0 ? 0 : last;
        return last;
    }
    public int getTotalPage(){
        int totalPage;
        // 假设总数是50，是能够被5整除的，那么就有10页
        if (0 == total % count)
            totalPage = total /count;
            // 假设总数是51，不能够被5整除的，那么就有11页
        else
            totalPage = total / count + 1;

        if(0==totalPage)
            totalPage = 1;
        return totalPage;

    }
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
