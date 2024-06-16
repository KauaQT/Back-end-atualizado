package sptech.school.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {

    private static final String CLOUD_NAME = "drspp7cxv";
    private static final String API_KEY = "492451475797915";
    private static final String API_SECRET = "EAZyTr7ko-py_S1x5iiZ9CoVuVc";

    public static Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }

}
