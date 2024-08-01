package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
public class EventBusTest {
    EventBus eventBus = new DefaultEventBus("1");
    EventBus eventBus2 = new DefaultEventBus("2");

    @Before
    public void setUp() {
        initEventBus(eventBus);
        initEventBus(eventBus2);

//        initEventBus(eventBus2);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        TimeUnit.MILLISECONDS.sleep(50);
    }

    private void initEventBus(EventBus eventBus) {
        String id = eventBus.getId();
        EventBrokerClientMessage eventBrokerClientMessage = this.createEventBrokerClientMessage(id);
        eventBus.setEventBrokerClientMessage(eventBrokerClientMessage);

        eventBus.setSubscribeExecutorStrategy(SubscribeExecutorStrategy.defaultInstance());
        eventBus.setSubscriberInvokeCreator(SubscriberInvokeCreator.defaultInstance());
        eventBus.setEventBusMessageCreator(EventBusMessageCreator.defaultInstance());
        eventBus.setEventBusListener(EventBusListener.defaultInstance());
        eventBus.setExecutorRegion(ExecutorRegionKit.getExecutorRegion());

        // 注册订阅者
        eventBus.register(new CustomEvent());

        Set<String> topic = eventBus.listTopic();
        eventBrokerClientMessage.setEventTopicMessage(new EventTopicMessage(topic));

        EventBusRegion.addLocal(eventBus);

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            ThrowKit.ofRuntimeException(e);
        }
    }

    private EventBrokerClientMessage createEventBrokerClientMessage(String appId) {
        String appName = "testAppName-" + appId;
        String tag = "testTag";
        String typeName = "LOGIC";

        return new EventBrokerClientMessage(appName, tag, typeName, appId);
    }

    @Test
    public void fireAny() {
        MyMessage message = new MyMessage();
        message.setName("fireAny");

        this.eventBus.fireAnySync(message);
        Assert.assertEquals(3, CustomEvent.myMessageLong.sum());

        System.out.println();
        this.eventBus.fireAny(message);

        sleep();
        Assert.assertEquals(6, CustomEvent.myMessageLong.sum());
    }

    @Test
    public void testOther() {
        MyMessage message = new MyMessage();
        message.setName("ok");

        EventBusMessage eventBusMessage = this.eventBus.createEventBusMessage(message);
        eventBusMessage.setThreadIndex(1);

        this.eventBus.fire(eventBusMessage);
//        this.eventBus.fire(message);
        System.out.println();
//        this.eventBus.fire(message);

    }

    @Test
    public void fireMe() {
        MyMessage message = new MyMessage();
        message.setName("fireMe");

        this.eventBus.fireMeSync(message);

        Assert.assertEquals(3, CustomEvent.myMessageLong.sum());

        this.eventBus.fireMe(message);

        sleep();
        Assert.assertEquals(6, CustomEvent.myMessageLong.sum());
    }

    @Test
    public void fireLocal() {
        MyMessage message = new MyMessage();
        message.setName("fireLocal");

        this.eventBus.fireLocalSync(message);
        Assert.assertEquals(6, CustomEvent.myMessageLong.sum());

        this.eventBus.fireLocal(message);

        sleep();
        Assert.assertEquals(12, CustomEvent.myMessageLong.sum());
    }

    @Test
    public void fire() {
        MyMessage message = new MyMessage();
        message.setName("ok");

        MyRecord myRecord = new MyRecord(100);

        this.eventBus.fireSync(message);
        this.eventBus.fire(message);

        this.eventBus.fireSync(myRecord);
        this.eventBus.fire(myRecord);

        sleep();
        Assert.assertEquals(12, CustomEvent.myMessageLong.sum());
        Assert.assertEquals(8, CustomEvent.myRecordLong.sum());
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}