package top.twip.api.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class ImageUtil {
    private static final long MAX_FILE_SIZE = 1024L * 1024; // 1MB

    private static final int MAX_COMPRESSION_TIMES = 10;

    public ImageUtil() {
    }

    public static FileInputStream compressImage(FileInputStream fis) throws IOException {
        FileChannel channel = fis.getChannel();
        long fileSize = channel.size();
        // 大小合法直接返回
        if (fileSize < MAX_FILE_SIZE) {
            return fis;
        } else {
            FileInputStream tmp = fis;
            for (int time = 0; time < MAX_COMPRESSION_TIMES; time++) {
                // 减少10%大小
                tmp = lessImage(tmp);
                if (tmp.getChannel().size() < MAX_FILE_SIZE) {
                    return tmp;
                }
            }
        }
        throw new IOException("测试");
    }


    public static FileInputStream lessImage(FileInputStream fis) throws IOException {
        // 将FileInputStream转换为BufferedImage
        BufferedImage originalImage = ImageIO.read(new BufferedInputStream(fis));

        // 初始化输出流以便存储压缩后的图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 计算压缩比例
        double scale = 0.9;

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


        // 将压缩后的图片数据转回FileInputStream
        // 注意：实际上FileInputStream不能直接指向内存中的字节数组，因此这里会先将压缩后的图片写入临时文件，然后从临时文件读取
        File tempFile = File.createTempFile("compressed-", ".jpg");
        Files.write(tempFile.toPath(), baos.toByteArray());

        // 创建指向临时文件的FileInputStream
        // 返回压缩后的FileInputStream
        return new FileInputStream(tempFile);
    }
}
