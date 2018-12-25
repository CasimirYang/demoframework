package com.mb;

public interface HelloMBean {

    public void sayHello();
    public int add(int x, int y);

    public String getName();
    public void setName(String name);

    public int getCacheSize();
    public void setCacheSize(int size);
}
