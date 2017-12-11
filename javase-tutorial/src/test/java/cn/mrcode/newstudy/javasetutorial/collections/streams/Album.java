package cn.mrcode.newstudy.javasetutorial.collections.streams;

import java.util.List;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/09     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/9 10:13
 * @date 2017/12/9 10:13
 * @since 1.0.0
 */
public class Album {
    private String name;
    private List<Track> tracks;

    public Album(String name, List<Track> tracks) {
        this.name = name;
        this.tracks = tracks;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
