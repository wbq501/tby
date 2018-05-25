package com.baigu.dms.common.view.imagepicker;
/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class FileChooser {

    public static class FileType {
        public static final int TYPE_IMAGE = 1;
        public static final int TYPE_DOCUMENT = 2;
        public static final int TYPE_VIDEO = 3;
        public static final int TYPE_MUSIC = 4;
    }

    public static class FileTypeBean {
        public int type;
        public int drawableResId;
        public int nameResId;

        public FileTypeBean(int type, int drawableResId, int nameResId) {
            this.type = type;
            this.drawableResId = drawableResId;
            this.nameResId = nameResId;
        }
    }
}
