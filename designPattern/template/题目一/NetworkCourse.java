package com.gupao.vip.pattern.template.notextends;

/**
 * Created by qingbowu on 2019/3/18.
 */
public class NetworkCourse {

    private INetwork iNetwork;

    public NetworkCourse(INetwork iNetwork) {
        this.iNetwork = iNetwork;
    }

    protected final void createCourse(){
        //1、发布预习资料
        this.postPreResource();

        //2、制作PPT
        this.createPPT();

        //3、在线直播
        this.liveVideo();

        //4、提交课件、课堂笔记
        this.postNote();

        //5、提交源码
        this.postSource();

        //6、布置作业，有些课有作业，有些课没有作业
        //如果有作业则检查作业，没有作业直接完成
        if (this.iNetwork.needHomework()){
            this.iNetwork.checkHomework();
        }
    }

    protected  void checkHomework(){
        System.out.println("检查作业");
    }

    //钩子方法:实现流程的微调，业务中有需要可以加，没有也可以不加
    protected boolean needHomework() {
        return false;
    }

    protected final void postSource(){
        System.out.println("提交源码");
    }

    protected final void postNote(){
        System.out.println("提交课件，课堂笔记");
    }

    protected final void liveVideo(){
        System.out.println("授课直播");
    }


    protected final void postPreResource(){
        System.out.println("分发预习资料");
    }


    protected final void createPPT(){
        System.out.println("制作PPT");
    }

}
