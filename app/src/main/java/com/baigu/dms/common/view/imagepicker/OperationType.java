package com.baigu.dms.common.view.imagepicker;


public class OperationType {

    public static final int TYPE_UNKNOWN = -1;

    public static class ChatListOperationType {
        public static final int TYPE_SCAN = 1;
        public static final int TYPE_START_CHAT = 2;
        public static final int TYPE_ADD_FRIEND = 3;
        public static final int TYPE_INVITE_COLLEAGUE = 4;
    }

    public static class CircleListOperationType {
        public static final int TYPE_SCAN = 1;
        public static final int TYPE_ADD_CIRLE = 2;
    }

    public static class FileOperationType {
        public static final int TYPE_ALL = 1;
        public static final int TYPE_IMAGE = 2;
        public static final int TYPE_DOCUMENT = 3;
        public static final int TYPE_VIDEO = 4;
        public static final int TYPE_MUSIC = 5;
    }
}
