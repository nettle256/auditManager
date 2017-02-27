package auditManager.Controller.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nettle on 2017/2/7.
 */
@Service
public class ImageTransfer {

    @Value("${photo.maxWidth}")
    private double maxWidth;

    /**
     * 按倍率缩小图片
     * @param srcImagePath 读取图片路径
     * @param toImagePath 写入图片路径
     * @throws IOException
     */
    public void reduceImage(String srcImagePath, String toImagePath) throws IOException {
        FileOutputStream out = null;
        try{
            //读入文件
            File file = new File(srcImagePath);
            // 构造Image对象
            BufferedImage src = javax.imageio.ImageIO.read(file);
            int width = src.getWidth();
            int height = src.getHeight();
            double ratio = width / maxWidth;
            if (ratio < 1) ratio = 1;
            int toWidth = (int) (width / ratio);
            int toHeight = (int) (height / ratio);
            System.out.printf("%d %d\n", width, height);
            System.out.printf("%f\n", ratio);
            System.out.printf("%d %d\n", toWidth, toHeight);
            // 缩小边长
            BufferedImage tag = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
            // 绘制 缩小后的图片
            tag.getGraphics().drawImage(src, 0, 0, toWidth, toHeight, null);
            out = new FileOutputStream(toImagePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(out != null){
                out.close();
            }
        }
    }
}
