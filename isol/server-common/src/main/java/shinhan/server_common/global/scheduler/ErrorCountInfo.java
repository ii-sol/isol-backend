package shinhan.server_common.global.scheduler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class ErrorCountInfo {
    private int limit;
    private int count;

    @Autowired
    public ErrorCountInfo(int limit, int count) {
        this.limit = limit;
        this.count = count;
    }

    public void incrementCount(){
        count++;
    }

}
