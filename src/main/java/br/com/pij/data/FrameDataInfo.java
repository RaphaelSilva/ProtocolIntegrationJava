package br.com.pij.data;

import br.com.pij.data.entity.Message;
import br.com.pij.data.entity.UserInfo;
import br.com.pij.imp.LogFile;
import br.com.pij.util.DB;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.Date;

public class FrameDataInfo extends FrameData {

    private UserInfo userInfo;

    @Override
    public boolean Process() {
        var data = this.getData();
        userInfo = new UserInfo();
        userInfo.setAge(Byte.toUnsignedInt(data[0]));
        userInfo.setWeight(Byte.toUnsignedInt(data[1]));
        userInfo.setHeight(Byte.toUnsignedInt(data[2]));
        userInfo.setLength(Byte.toUnsignedInt(data[3]));
        userInfo.setName(new String(Arrays.copyOfRange(data, 4, data.length)));
        Transaction tr = null;
        try(var session = DB.getSessionFactory().openSession()){
            tr = session.beginTransaction();
            session.save(userInfo);
            tr.commit();
            LogFile.getInstance().log("%s saved into DB", this.toString());
            return true;
        }catch (Exception e){
            if(tr != null){
                tr.rollback();
            }
            LogFile.getInstance().log(e);
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "FrameDataInfo{" +
                "userInfo=" + userInfo +
                '}';
    }
}
