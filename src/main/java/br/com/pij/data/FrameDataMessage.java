package br.com.pij.data;

import br.com.pij.data.entity.Message;
import br.com.pij.imp.LogFile;
import br.com.pij.util.DB;
import org.hibernate.Transaction;

import java.util.Date;

public class FrameDataMessage extends FrameData {
    private Message message;

    @Override
    public boolean Process() {
        var data = new String(this.getData());
        message = new Message();
        message.setDate(new Date());
        message.setText(data);
        Transaction tr = null;
        try (var session = DB.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.save(message);
            tr.commit();
            LogFile.getInstance().log("%s saved into DB", this.toString());
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            LogFile.getInstance().log(e);
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "FrameDataMessage{message=" + message + '}';
    }
}
