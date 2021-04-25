import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，
 * 此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件
 *
 * @author leven.chen
 */
public class CustomerClassLoader extends ClassLoader {


    public static void main(String[] args) {
        try {
            Class<?> helloClass = new CustomerClassLoader().findClass("Hello");
            Method helloMethod = helloClass.getMethod("hello");
            helloMethod.invoke(helloClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        URL resource = this.getClass().getResource("Hello.xlass");
        if (Objects.isNull(resource)) {
            throw new RuntimeException("Hello.xlass not exists");
        }
        String path = resource.getPath();
        try {
            byte[] bytes = readFileContent(path);
            return defineClass(name, decode(bytes), 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
    }

    private byte[] readFileContent(String path) throws IOException {
        File file = new File(URLDecoder.decode(path, "UTF-8"));
        if (file.isFile() && file.exists()) {
            try (FileChannel channel = new FileInputStream(file).getChannel()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
                channel.read(byteBuffer);
                return byteBuffer.array();
            }
        } else {
            throw new RuntimeException("failed to find path: " + path);
        }
    }

    /**
     * replace each byte with x->255-x
     *
     */
    private byte[] decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = (byte) (255 - bytes[i]);
            bytes[i] = (byte) ~bytes[i];
        }
        return bytes;
    }


}
