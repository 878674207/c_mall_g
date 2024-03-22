package com.ruoyi.common.qo;

import java.util.HashMap;
import java.util.Map;

public class InstitutionEntryNoticeTemplate {

    /**
     *  提示消息
     */
    private Map<String, String> thing1;
    /**
     *  网址
     */
    private Map<String, String> character_string2;
    /**
     *  账户
     */
    private Map<String, String> character_string3;

    /**
     *  密码
     */
    private Map<String, String> number4;
    /**
     *  日期
     */
    private Map<String, String> time5;

    public Map<String, String> getThing1() {
        return thing1;
    }

    public void setThing1( String thing1) {
        this.thing1 = getFormat(thing1);
    }

    public Map<String, String> getCharacter_string2() {
        return character_string2;
    }

    public void setCharacter_string2(String character_string2) {
        this.character_string2 = getFormat(character_string2);
    }

    public Map<String, String> getCharacter_string3() {
        return character_string3;
    }

    public void setCharacter_string3(String character_string3) {
        this.character_string3 = getFormat(character_string3);
    }

    public Map<String, String> getNumber4() {
        return number4;
    }

    public void setNumber4( String number4) {
        this.number4 = getFormat(number4);
    }

    public Map<String, String> getTime5() {
        return time5;
    }

    public void setTime5( String time5) {
        this.time5 = getFormat(time5);
    }


    public HashMap<String, String> getFormat(String str) {
        return new HashMap<String, String>() {{
            put("value", str);
        }};
    }
}
