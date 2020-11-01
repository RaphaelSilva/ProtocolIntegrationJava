package br.com.pij.data;

import br.com.pij.data.entity.Message;
import br.com.pij.imp.LogFile;
import br.com.pij.util.DB;
import org.hibernate.Transaction;

import java.util.Date;

public class FrameDataMessage extends FrameData {
    private String Text;

    @Override
    public boolean Save(){
        var data = new String(this.getData());
        LogFile.getInstance().log("Save data %s", data);
        var message = new Message();
        message.setDate(new Date());
        message.setText(data);
        Transaction tr = null;
        try(var session = DB.getSessionFactory().openSession()){
            tr = session.beginTransaction();
            session.save(message);
            tr.commit();
            return true;
        }catch (Exception e){
            if(tr != null){
                tr.rollback();
            }
            LogFile.getInstance().log(e);
        }
        return false;
    }
}
