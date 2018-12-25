package com.mb;

/**
 * @author za-yangjinghua
 */
public class Hello implements HelloMBean  {

    public void sayHello() {
        System.out.println("hello, world"+this.name);
    }

    public int add(int x, int y) {
        return x + y;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public synchronized void setCacheSize(int size) {
        this.cacheSize = size;
        System.out.println("Cache size now " + this.cacheSize);
    }
    private String name = "Reginald";
    private int cacheSize = DEFAULT_CACHE_SIZE;
    private static final int
            DEFAULT_CACHE_SIZE = 200;
}
