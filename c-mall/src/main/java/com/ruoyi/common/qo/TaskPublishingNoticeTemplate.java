package com.ruoyi.common.qo;

import java.util.HashMap;
import java.util.Map;

public class TaskPublishingNoticeTemplate {

    /**
     *  任务名称
     */
    private Map<String, String> thing1;
    /**
     *  任务发布人
     */
    private Map<String, String> name2;

    public Map<String, String> getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = getFormat(name2);
    }

    public Map<String, String> getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = getFormat(name3);
    }

    public Map<String, String> getThing4() {
        return thing4;
    }

    public void setThing4(String thing4) {
        this.thing4 = getFormat(thing4);
    }

    public Map<String, String> getDate5() {
        return date5;
    }

    public void setDate5(String date5) {
        this.date5 = getFormat(date5);
    }

    /**
     *  任务接收人
     */
    private Map<String, String> name3;

    /**
     *  任务描述
     */
    private Map<String, String> thing4;
    /**
     *  发布时间
     */
    private Map<String, String> date5;

    public Map<String, String> getThing1() {
        return thing1;
    }

    public void setThing1( String thing1) {
        this.thing1 = getFormat(thing1);
    }




    public HashMap<String, String> getFormat(String str) {
        return new HashMap<String, String>() {{
            put("value", str);
        }};
    }
}
