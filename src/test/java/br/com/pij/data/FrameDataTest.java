package br.com.pij.data;

import br.com.pij.data.entity.Message;
import br.com.pij.data.entity.UserInfo;
import br.com.pij.util.DB;
import br.com.pij.util.MByte;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class FrameDataTest extends TestCase {

    public void testFrameDataInfo() {
        UUID uuid = UUID.nameUUIDFromBytes("Raphael Silva".getBytes());
        var name = uuid.toString();
        var age = 32;
        var weight = 75;
        int height = 180;
        var data = MByte.concatAll(new byte[]{
                (byte) age, (byte) weight, (byte) height, (byte) name.length()
        }, name.getBytes());
        var length = data.length + 4;
        var frame = FrameData.factory(length, FrameData.INFO);
        assertTrue(frame instanceof FrameDataInfo);
        frame.setData(data);
        frame.Process();
        try (var session = DB.getSessionFactory().openSession()) {
            var query = session.createQuery("from UserInfo where name = :name");
            System.out.println(name);
            query.setParameter("name", name);
            var user = (UserInfo) query.uniqueResult();
            System.out.println(user);
            assertNotNull(user);
            assertEquals("Name isn't equals", name, user.getName());
            assertEquals("Age isn't equals", age, user.getAge());
            assertEquals("Weight isn't equals", weight, user.getWeight());
            assertEquals("Height isn't equals", height, user.getHeight());
            assertEquals("Length isn't equals", name.length(), user.getLength());
        }
    }

    public void testFrameDataMessage() throws InterruptedException {
        UUID uuid = UUID.nameUUIDFromBytes("Raphael Silva".getBytes());
        var text = uuid.toString();
        var data = text.getBytes();
        var length = data.length + 4;
        var frame = FrameData.factory(length, FrameData.MESSAGE);
        assertTrue(frame instanceof FrameDataMessage);
        frame.setData(data);
        var initialDate = new Date();
        Thread.sleep(1000);
        frame.Process();
        try (var session = DB.getSessionFactory().openSession()) {
            var query = session.createQuery("from Message where text = :text");
            query.setParameter("text", text);
            var message = (Message) query.uniqueResult();
            assertNotNull(message);
            assertEquals("Text isn't equals", text, message.getText());
            assertTrue(message.getDate().compareTo(initialDate) >= 0);
            assertTrue(message.getDate().compareTo(new Date()) <= 0);
        }
    }
}