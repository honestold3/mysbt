package org.wq.thread;

/**
 * Created by wq on 5/18/14.
 */
public class TestAnonymousInterClass {

    public static void main(String[] args) {

        test1();
        System.out.println("-------------------");
        test2();
        System.out.println("-------------------");
        test3("kankan");
    }

    public static void test1(){
        Out inner = new Out("aa"){
            void call(){
                System.out.println("my call...");
            }
        };
        inner.todo();
    }

    public static void test2(){
        new Out("aa").todo();

        Runnable r = new Runnable(){
            @Override
            public void run() {
                System.out.println("haha");
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public static void test3(final String str){ //拷贝引用，为了避免引用值发生改变，例如被外部类的方法修改等，
    // 而导致内部类得到的值不一致，于是用final来让该引用不可改变。
        final String str1 = "kankan1";
        Out inner = new Out("aa"){
            //public Out(){} //匿名内部类中是不能定义构造函数的
            //static String dd = "dd"; 匿名内部类中不能存在任何的静态成员变量和静态方法
            void call(){
                System.out.println(str1);
                System.out.println(str);
            }
        };
        inner.todo();
    }
}

class Out {

    String hehe;

    public Out(String str){
        this.hehe = str;
    }
    void init(){
        System.out.println("init.."+this.hehe);
    }

    void call(){
        System.out.println("default.."+this.hehe);
    }

    void close(){
        System.out.println("end..."+this.hehe);
    }

    void todo(){
        init();
        call();
        close();
    }
}
