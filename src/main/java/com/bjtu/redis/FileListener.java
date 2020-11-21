package com.bjtu.redis;
import java.io.File;

import org.apache.commons.io.monitor.*;

public class FileListener implements FileAlterationListener {

    @Override
        public void onStart(FileAlterationObserver observer) {//文件初始化
           // System.out.println();
        }

        @Override
        public void onDirectoryCreate(File directory) {//有操作创建了新的文件夹
            //System.out.println("onDirectoryCreate:" + directory.getName());
        }

        @Override
        public void onDirectoryChange(File directory) {//有操作改变了新的文件夹
            //System.out.println("onDirectoryChange:" + directory.getName());
        }

        @Override
        public void onDirectoryDelete(File directory) {//有操作删除了文件夹
            //System.out.println("onDirectoryDelete:" + directory.getName());
        }

        @Override
        public void onFileCreate(File file) {//有操作新建了文件
            System.out.println("待议");
        }

        @Override
        public void onFileChange(File file) {//有文件改变



            System.out.println("onFileChange : " + file.getName());
        }

        @Override
        public void onFileDelete(File file) {//有文件删除
            System.out.println("待议");
        }

        @Override
        public void onStop(FileAlterationObserver observer) {
        }


    }