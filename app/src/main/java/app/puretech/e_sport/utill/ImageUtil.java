package app.puretech.e_sport.utill;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by dinesh on 14-02-2018.
 */

@SuppressWarnings("All")

public class ImageUtil {

    private static ImageLoader imageLoader;

    /* set school image */
    public static ImageLoader displayImageSchool(ImageView view, String path,
                                                 ImageLoadingListener listener) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.clearMemoryCache();
        try {
            if (path.contains("http")) {
                imageLoader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);
            } else {
                String str = DataManager.urlSchoolPic + path;
                imageLoader.displayImage(str, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);

            }
        } catch (Exception e) {

            e.printStackTrace();
            imageLoader.clearMemoryCache();
        }
        return imageLoader;
    }

    /* set trainer image*/
    public static ImageLoader displayImageTrainer(ImageView view, String path,
                                                  ImageLoadingListener listener) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.clearMemoryCache();
        try {
            if (path.contains("http")) {
                imageLoader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);
            } else {
                String str = DataManager.urlTrainerPic + path;
                imageLoader.displayImage(str, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);

            }
        } catch (Exception e) {

            e.printStackTrace();
            imageLoader.clearMemoryCache();
        }
        return imageLoader;
    }

    /* set parent image */
    public static ImageLoader displayImageParent(ImageView view, String path,
                                                 ImageLoadingListener listener) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.clearMemoryCache();
        try {
            if (path.contains("http")) {
                imageLoader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);
            } else {
                String str = DataManager.urlParentPic + path;
                imageLoader.displayImage(str, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);

            }
        } catch (Exception e) {

            e.printStackTrace();
            imageLoader.clearMemoryCache();
        }
        return imageLoader;
    }

    /* set trainer gallary */
    public static ImageLoader displayImageTrainerGallary(ImageView view, String path,
                                                         ImageLoadingListener listener) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.clearMemoryCache();
        try {
            if (path.contains("http")) {
                imageLoader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);
            } else {
                String str = DataManager.urlTrainerGallary + path;
                imageLoader.displayImage(str, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);

            }
        } catch (Exception e) {

            e.printStackTrace();
            imageLoader.clearMemoryCache();
        }
        return imageLoader;
    }

    public static ImageLoader displayImage(ImageView view, String path,
                                           ImageLoadingListener listener) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        imageLoader.clearMemoryCache();
        try {
            if (path.contains("http")) {
                imageLoader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);
            } else {
                String str = DataManager.url + path;
                imageLoader.displayImage(str, view, DEFAULT_DISPLAY_IMAGE_OPTIONS,
                        listener);

            }
        } catch (Exception e) {

            e.printStackTrace();
            imageLoader.clearMemoryCache();
        }
        return imageLoader;
    }

    public static void displayRoundImage(ImageView view, String path,
                                         ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS,
                    listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    public static void loadImage(String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    // TODO Change default image
    private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .displayer(new FadeInBitmapDisplayer(100, true, false, false));
//            .showImageForEmptyUri(R.drawable.img_load)
//            .showImageOnLoading(R.drawable.img_load)
//            .showImageOnFail(R.drawable.img_load).cacheOnDisk(true)
//            .cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

    private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .build();
    private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .displayer(new RoundedBitmapDisplayer(300)).build();
}
