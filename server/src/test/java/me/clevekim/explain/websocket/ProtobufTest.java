package me.clevekim.explain.websocket;

import lombok.extern.slf4j.Slf4j;
import me.clevekim.explain.websocket.protobuf.WebSocketSampleProto;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by younghwan.kim@linecorp.com on 2019-05-13
 */
@Slf4j
public class ProtobufTest {

    @Test
    public void SampleCreatTest() {

        WebSocketSampleProto.Sample sample = WebSocketSampleProto.Sample.newBuilder()
                .setName("Test").build();

        Assert.assertTrue("Test".equals(sample.getName()));
    }

    @Test
    public void SampleSerailizeTest() {

        WebSocketSampleProto.Sample sample = WebSocketSampleProto.Sample.newBuilder().setName("Test").build();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            sample.writeTo(byteArrayOutputStream);

            byte[] serialized = byteArrayOutputStream.toByteArray();
            Assert.assertNotNull(serialized);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void SampleDeserialziTest() {

        WebSocketSampleProto.Sample sample = WebSocketSampleProto.Sample.newBuilder().setName("Test").build();

        byte[] serialized = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            sample.writeTo(byteArrayOutputStream);

            serialized = byteArrayOutputStream.toByteArray();
            Assert.assertNotNull(serialized);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        try {
            WebSocketSampleProto.Sample sample1 = WebSocketSampleProto.Sample.parseFrom(serialized);
            Assert.assertTrue(sample.getName().equals(sample1.getName()));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
