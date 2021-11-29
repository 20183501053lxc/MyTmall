package tmall.bean;

import java.util.Date;
import java.util.List;

public class Product {
    //与数据库中的表的字段先有一一对应的关系
    private String name;
    private String subTitle;
    private float orignalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;
    private Category category;
    private int id;
    private ProductImage firstProductImage;//从productSingleImages集合中取出第一个来，用于显示这个产品的默认图片
    private List<ProductImage> productImages;
    private List<ProductImage> productSingleImages;//单个产品图片集合
    private List<ProductImage> productDetailImages;//产品详情集合
    //reviewCount表示评论数量
    private int reviewCount;
    private int saleCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public float getOrignalPrice() {
        return orignalPrice;
    }

    public void setOrignalPrice(float orignalPrice) {
        this.orignalPrice = orignalPrice;
    }

    public float getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(float promotePrice) {
        this.promotePrice = promotePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductImage getFirstProductImage() {
        return firstProductImage;
    }

    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public List<ProductImage> getProductSingleImages() {
        return productSingleImages;
    }

    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public List<ProductImage> getProductDetailImages() {
        return productDetailImages;
    }

    public void setProductDetailImages(List<ProductImage> productDetailImage) {
        this.productDetailImages = productDetailImage;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public Product() {
    }

    public Product(String name, String subTitle, float orignalPrice, float promotePrice, int stock, Date createDate, Category category, int id, ProductImage firstProductImage, List<ProductImage> productImages, List<ProductImage> productSingleImages, List<ProductImage> productDetailImages, int reviewCount, int saleCount) {
        this.name = name;
        this.subTitle = subTitle;
        this.orignalPrice = orignalPrice;
        this.promotePrice = promotePrice;
        this.stock = stock;
        this.createDate = createDate;
        this.category = category;
        this.id = id;
        this.firstProductImage = firstProductImage;
        this.productImages = productImages;
        this.productSingleImages = productSingleImages;
        this.productDetailImages = productDetailImages;
        this.reviewCount = reviewCount;
        this.saleCount = saleCount;
    }
}
