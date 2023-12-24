package com.iohao.game.action.skeleton.eventbus;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-12-24
 */
public class EventBusTest {
    EventBus eventBus = new EventBus("1");
    EventBus eventBus2 = new EventBus("2");

    @Before
    public void setUp() {
        initEventBus(eventBus);

        initEventBus(eventBus2);
    }

    private void initEventBus(EventBus eventBus) {
        CustomEvent customEvent = new CustomEvent();
        eventBus.register(customEvent);

        EventBusRegion.add(eventBus);
    }

    @Test
    public void fire() throws InterruptedException {
        System.out.println("hell");

        MyMessage message = new MyMessage();
        message.setName("ok");

        this.eventBus.fire(message);

        this.eventBus.fire(new MyRecord(100));

        TimeUnit.MILLISECONDS.sleep(50);
    }
}