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

//        initEventBus(eventBus2);
    }

    private void initEventBus(EventBus eventBus) {

        eventBus.setSubscribeExecutorSelector(SubscribeExecutorSelector.defaultInstance());
        eventBus.setSubscriberInvokeCreator(SubscriberInvokeCreator.defaultInstance());
        eventBus.setEventBusMessageCreator(EventBusMessageCreator.defaultInstance());
        eventBus.setEventBusListener(EventBusListener.defaultInstance());

        // 注册订阅者
        eventBus.register(new CustomEvent());

        EventBusRegion.addLocal(eventBus);

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void fire() throws InterruptedException {
        MyMessage message = new MyMessage();
        message.setName("ok");

        EventBusMessage eventBusMessage = this.eventBus.createEventBusMessage(message);
        eventBusMessage.setThreadIndex(1);

        this.eventBus.fire(eventBusMessage);
//        this.eventBus.fire(message);
        System.out.println();
//        this.eventBus.fire(message);



        if (true) {
            TimeUnit.MILLISECONDS.sleep(50);
            return;
        }

        MyRecord myRecord = new MyRecord(100);

        System.out.println("hell");

        this.eventBus.fire(message);
        this.eventBus.fire(myRecord);

        this.eventBus.fireMe(myRecord);
        this.eventBus.fireMeSync(myRecord);

        this.eventBus.fireLocalNeighbor(myRecord);
        this.eventBus.fireLocalNeighborSync(myRecord);

        this.eventBus.fireLocal(myRecord);
        this.eventBus.fireLocalSync(myRecord);

        TimeUnit.MILLISECONDS.sleep(50);
    }
}