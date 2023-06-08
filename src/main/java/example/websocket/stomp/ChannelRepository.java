package example.websocket.stomp;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChannelRepository {

    private List<String> channelList = new ArrayList<>();

    public List<String> getChannelList() {
        return channelList;
    }

    public void addChannel(String channelId) {
        channelList.add(channelId);
    }
}
