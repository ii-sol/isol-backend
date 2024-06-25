package shinhan.server_common.global.scheduler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Getter
public class ExecutionInfo {
    private int limit;
    private int count;

    @Autowired
    public ExecutionInfo(int limit, int count) {
        this.limit = limit;
        this.count = count;
    }

    public void incrementCount(){
        count++;
    }
}
