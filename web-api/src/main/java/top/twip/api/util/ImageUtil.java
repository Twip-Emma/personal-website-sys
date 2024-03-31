package top.twip.api.util;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class ImageUtil {
    // 最大压缩次数
    private static final int MAX_COMPRESSION_TIMES = 10;

    /**
     * 循环压缩图片
     * @param fis 要压缩的图片流
     * @return 压缩后的图片流
     */
    public static FileInputStream compressImage(FileInputStream fis) throws IOException {
        // 将FileInputStream转换为BufferedImage
        BufferedImage originalImage = ImageIO.read(new BufferedInputStream(fis));

        // 初始化输出流以便存储压缩后的图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 开始压缩循环
        for (int i = 0; i < MAX_COMPRESSION_TIMES && !(originalImage.getWidth() <= 1) && !(originalImage.getHeight() <= 1); i++) {
            // 计算压缩比例
            double scale = 0.5;

            // 调整图片大小
            BufferedImage scaledImage = new BufferedImage(
                    (int) Math.round(originalImage.getWidth() * scale),
                    (int) Math.round(originalImage.getHeight() * scale),
                    originalImage.getType());

            // 使用重采样方法进行缩放
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
            g2d.dispose();

            // 将压缩后的图片写出到输出流
            ImageIO.write(scaledImage, "jpg", baos);

            // 用新压缩过的图片替换原来的图片
            originalImage = scaledImage;
        }

        // 将压缩后的图片数据转回FileInputStream
        // 注意：实际上FileInputStream不能直接指向内存中的字节数组，因此这里会先将压缩后的图片写入临时文件，然后从临时文件读取
        File tempFile = File.createTempFile("compressed-", ".jpg");
        Files.write(tempFile.toPath(), baos.toByteArray());

        // 创建指向临时文件的FileInputStream
        FileInputStream compressedFis = new FileInputStream(tempFile);

        // 返回压缩后的FileInputStream
        return compressedFis;
    }
}
