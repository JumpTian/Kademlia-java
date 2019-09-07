package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

/**
 * @author JumpTian
 */
@Getter
public class FindParam {

    private final Node.Id key;

    private FindParam(Builder builder) {
        this.key = builder.key;
    }

    public static class Builder {
        private Node.Id key;

        public Builder key(Node.Id key) {
            this.key = key;
            return this;
        }

        public FindParam build() {
            return new FindParam(this);
        }
    }
}
