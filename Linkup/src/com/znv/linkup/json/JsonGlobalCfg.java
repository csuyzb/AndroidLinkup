package com.znv.linkup.json;

public class JsonGlobalCfg {
    private int gsound;
    private int bgmusic;

    public int getGsound() {
        return gsound;
    }

    public void setGsound(int gsound) {
        this.gsound = gsound;
    }

    public int getBgmusic() {
        return bgmusic;
    }

    public void setBgmusic(int bgmusic) {
        this.bgmusic = bgmusic;
    }

    public int getPrompt() {
        return prompt;
    }

    public void setPrompt(int prompt) {
        this.prompt = prompt;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    private int prompt;
    private int refresh;
    private int addtime;
}
