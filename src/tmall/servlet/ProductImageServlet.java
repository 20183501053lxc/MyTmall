package tmall.servlet;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ProductImageServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        InputStream is = null;
        HashMap<String,String> params = new HashMap<>();
        is = parseUpload(request,params);//该方法会将jsp页面传入的参数处理之后一并放入到param中
        String type = params.get("type");
        Integer pid = Integer.parseInt(params.get("pid"));

        //在数据库中插入对应的bean对象
        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(productDAO.get(pid));
        productImageDAO.add(productImage);

        //获取上传的图片,并将它存放在指定文件夹中
        String filename = productImage.getId()+".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        //根据上传图片的类型分别进行赋值
        if(ProductImageDAO.type_single.equals(productImage.getType())){
            imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
            imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        }else{
            imageFolder= request.getSession().getServletContext().getRealPath("img/productDetail");
        }
        File file =  new File(imageFolder,filename);
        file.getParentFile().mkdirs();
        //复制文件
        try {
            //available():返回与之关联的文件的字节数
            if(null != is && 0 != is.available()){
                try(FileOutputStream fos = new FileOutputStream(file)){
                    byte[] bytes = new byte[1024*1024];
                    int len = 0;
                    while (-1 != (len = is.read(bytes))){
                        fos.write(bytes, 0,len);
                    }
                    fos.flush();
                    //通过如下代码，把文件保存为jpg格式
                    BufferedImage image = ImageUtil.change2jpg(file);
                    ImageIO.write(image,"jpg",file);
                    if(ProductImageDAO.type_single.equals(productImage.getType())){
                        File f_small = new File(imageFolder_small, filename);
                        File f_middle = new File(imageFolder_middle, filename);

                        ImageUtil.resizeImage(file, 56, 56, f_small);
                        ImageUtil.resizeImage(file, 217, 190, f_middle);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_productImage_list?pid="+pid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage productImage = productImageDAO.get(id);
        productImageDAO.delete(id);

        //根据图片的类型删除对应的图片
        //如果是单个图片，那么删除3张正常，中等，小号图片
        //如果是详情图片，那么删除一张图片
        if(ProductImageDAO.type_single.equals(productImage.getType())){
            String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");

            File f_single = new File(imageFolder_single,productImage.getId()+".jpg");
            f_single.delete();
            File f_small = new File(imageFolder_small,productImage.getId()+".jpg");
            f_small.delete();
            File f_middle =new File(imageFolder_middle,productImage.getId()+".jpg");
            f_middle.delete();
        }else{
            String imageFolder_detail= request.getSession().getServletContext().getRealPath("img/productDetail");
            File f_detail =new File(imageFolder_detail,productImage.getId()+".jpg");
            f_detail.delete();
        }
        return "@admin_productImage_list?pid="+productImage.getProduct().getId();
    }

    //edit和update返回值为null,因为图片没有修改和更新
    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        Integer pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        List<ProductImage> singleList = productImageDAO.list(product, productImageDAO.type_single);
        List<ProductImage> detailList = productImageDAO.list(product, productImageDAO.type_detail);
        request.setAttribute("p",product);
        request.setAttribute("pisSingle",singleList);
        request.setAttribute("pisDetail",detailList);
        return "admin/listProductImage.jsp";
    }
}
