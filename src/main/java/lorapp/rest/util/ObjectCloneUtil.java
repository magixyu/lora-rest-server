package lorapp.rest.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class ObjectCloneUtil implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(ObjectCloneUtil.class);

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepClone(T instance) {
        logger.info("Try to clone deep object: " + instance.hashCode() + ", class: " + instance.getClass());

        T dstInstance = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(instance);

            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);

            Object dstObj = ois.readObject();
            if (instance.getClass().isInstance(dstObj)) {
                dstInstance = (T) dstObj;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }finally {
            release(bos);
            release(oos);
            release(bis);
            release(ois);
        }
        return dstInstance;
    }

    private static void release(Closeable cb) {
        if (cb != null) {
            try {
                cb.close();
            } catch (Exception e2) {
            }
        }
    }
}