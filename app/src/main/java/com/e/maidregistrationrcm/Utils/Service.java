package com.e.maidregistrationrcm.Utils;

import android.app.Activity;
import android.os.Environment;

import com.e.maidregistrationrcm.Activities.Aquila_reg;
import com.e.maidregistrationrcm.Activities.Devices;
import com.e.maidregistrationrcm.Activities.MainActivity;
import com.e.maidregistrationrcm.VOs.BlackListVO;
import com.e.maidregistrationrcm.VOs.DevicesVO;
import com.e.maidregistrationrcm.VOs.EmployerVO;
import com.e.maidregistrationrcm.VOs.GateVisitorVO;
import com.e.maidregistrationrcm.VOs.MaidVO;
import com.e.maidregistrationrcm.VOs.SystemVO;
import com.e.maidregistrationrcm.VOs.UserVO;
import com.e.maidregistrationrcm.VOs.VisitorVO;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Service {
    public static MainActivity mainActivity = new MainActivity();
    final public static String IP = "13.234.225.75";
    //final public static String IP = "192.168.2.113";
    final public static String VERSION = "2.1";
    public static int PORT = 7001;
    private static final JSONParser JSON_PARSER = new JSONParser();

    public static MaidVO getMaidInfo(final Integer maidId) throws Exception {

            MaidVO maidVO = null;

            JSONObject reqJsonObject = new JSONObject();
            reqJsonObject.put("DeviceID", Service.readDeviceId());
            reqJsonObject.put("Cmd", "getMaidInfo");
            reqJsonObject.put("MaidId", maidId);

            String jsonResponse = send(reqJsonObject.toJSONString());

            System.out.println("Json res -->"+jsonResponse);

            JSONParser jsonParser = new JSONParser();

            JSONObject resJsonObject = (JSONObject) jsonParser.parse(jsonResponse);

            Integer errorCode = Integer.parseInt(resJsonObject.get("ErrorCode").toString());
            String  errorMsg = resJsonObject.get("ErrorMsg").toString();
            if(errorCode != 0){
                throw new Exception(errorMsg);
            }

            maidVO = new MaidVO();
            Integer token = Integer.parseInt(resJsonObject.get("Token").toString().trim());
            maidVO.setToken(token);
            maidVO.setName(resJsonObject.get("Name").toString());
            maidVO.setMobile(resJsonObject.get("Mobile").toString());
            maidVO.setAddress(resJsonObject.get("Address").toString());
            maidVO.setAdharNo(resJsonObject.get("AdharNo").toString());
            maidVO.setOcupation(resJsonObject.get("ocupation").toString());
            maidVO.setImageRef(resJsonObject.get("imgref").toString());
            maidVO.setAccess(Integer.parseInt(resJsonObject.get("Permission").toString().trim()));
            int FOUNDINBL = Integer.parseInt(resJsonObject.get("founfinBl").toString().trim());
            maidVO.setInBL(FOUNDINBL);
            if(FOUNDINBL == 1){
                maidVO.setBanfrom(resJsonObject.get("banfrom").toString());
                maidVO.setBanTo(resJsonObject.get("banto").toString());
            }
            int maidIdK = Integer.parseInt(resJsonObject.get("MaidId").toString());
            maidVO.setMaidId(maidIdK);
            maidVO.setOcupation(resJsonObject.get("ocupation").toString());

            maidVO.setInTime((String) resJsonObject.get("InTime"));
            maidVO.setOutTime((String) resJsonObject.get("OutTime"));

            String array = resJsonObject.get("Employers").toString();
            System.out.println(array);
            ArrayList<EmployerVO> employerS = JSONtoArrayList((JSONArray) jsonParser.parse(array));
            maidVO.setEmployers(employerS);

            byte[] img1 = Service.getPhoto(maidVO.getImageRef());
            maidVO.setImage(img1);

            return maidVO;
    }

    public static MaidVO[] searchMaids(Integer token) throws Exception {
        List<MaidVO> list = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeviceID", Service.readDeviceId());
        jsonObject.put("Cmd", "searchMaids");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(token);
        jsonObject.put("tokensArray", jsonArray.toJSONString());

        String jsonResponse = send(jsonObject.toJSONString());

        JSONParser jsonParser = new JSONParser();
        JSONObject resJsonObject = (JSONObject) jsonParser.parse(jsonResponse);

        Integer errorCode = Integer.parseInt(resJsonObject.get("ErrorCode").toString());
        String  errorMsg = resJsonObject.get("ErrorMsg").toString();
        if(errorCode != 0){
            throw new Exception(errorMsg);
        }

        JSONArray maidArr = (JSONArray) resJsonObject.get("maids");
        Iterator itr = maidArr.iterator();
        while (itr.hasNext()) {
            JSONObject maidObj = (JSONObject) itr.next();
            MaidVO maidVO = new MaidVO();
            maidVO.setMaidId(new Integer(maidObj.get("Id").toString()));
            maidVO.setToken(new Integer(maidObj.get("Token").toString()));
            maidVO.setName(maidObj.get("Name").toString());
            list.add(maidVO);
        }

        return list.toArray(new MaidVO[list.size()]);
    }

    public static byte[] getPhoto(String imageName) throws Exception {
        byte[] imageData = null;
        Socket socket = null;
        try {

            socket = new Socket(IP, 7002);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("getImage");
            dataOutputStream.writeUTF(imageName);
            dataOutputStream.flush();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int length = dataInputStream.readInt();
            imageData = new byte[length];
            dataInputStream.readFully(imageData);
            dataInputStream.close();
            dataOutputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                socket.close();
            } catch (Exception ex) {
            }
        }

        return imageData;
    }

    @NotNull
    public static JSONArray toJSONArray(ArrayList<EmployerVO> arrayList) {

        JSONArray jsonArray = new JSONArray();
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println("loop is spinning");
            EmployerVO employerVO = (EmployerVO) iterator.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Name", employerVO.getName());
            jsonObject.put("Address", employerVO.getAddress());
            jsonObject.put("Mobile", employerVO.getMobile());
            jsonObject.put("mail",employerVO.getEmail());
            jsonArray.add(jsonObject);
        }
        System.out.println("JsonArray -->" + jsonArray.toJSONString());
        return jsonArray;
    }


    public static ArrayList<EmployerVO> JSONtoArrayList(JSONArray jsonArray) {
        ArrayList<EmployerVO> employers = new ArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            EmployerVO employerVO = new EmployerVO();
            employerVO.setName(jsonObject.get("Name").toString());
            employerVO.setMobile(jsonObject.get("Mobile").toString());
            employerVO.setAddress(jsonObject.get("Address").toString());
            employerVO.setEmail(jsonObject.get("mail").toString());
            employers.add(employerVO);
        }
        return employers;
    }

    public static Integer getMaidId(Integer token) throws Exception {
        Integer maidID = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeviceID", Service.readDeviceId());
        jsonObject.put("Cmd", "getMaidID");
        jsonObject.put("token", token);
        String reply = send(jsonObject.toJSONString());
        JSONParser jsonParser = new JSONParser();
        JSONObject resJsonObject = (JSONObject) jsonParser.parse(reply);
        Integer errorCode = Integer.parseInt(resJsonObject.get("ErrorCode").toString());
        String  errorMsg = resJsonObject.get("ErrorMsg").toString();
        if(errorCode != 0){
            throw new Exception(errorMsg);
        }

        maidID = Integer.parseInt(resJsonObject.get("MaidID").toString().trim());

        return maidID;
    }

    public static void deleteMaid(final int maidid) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(IP, 7001);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("DeviceID", Service.readDeviceId());
                    jsonObject.put("Cmd", "DeleteMaid");
                    jsonObject.put("MaidId", maidid);
                    dataOutputStream.writeUTF(jsonObject.toJSONString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public static UserVO login(String deviceId, String userName, String password, String version) throws Exception {

        UserVO userVO = null;

        JSONObject jsonObject = new JSONObject();
        //System.out.println(Service.readDeviceId(LogIn.this));
        jsonObject.put("Cmd", "Login");
        jsonObject.put("UserName", userName);
        jsonObject.put("Password", password);
        jsonObject.put("DeviceID", deviceId);
        jsonObject.put("Version",version);

        String reply = send(jsonObject.toJSONString());

        JSONParser jsonParser = new JSONParser();
        JSONObject resJsonObject = (JSONObject) jsonParser.parse(reply);
        Integer errorCode = Integer.parseInt(resJsonObject.get("ErrorCode").toString());
        String  errorMsg = resJsonObject.get("ErrorMsg").toString();
        if(errorCode != 0){
            throw new Exception(errorMsg);
        }

        userVO = new UserVO();
        userVO.setId(new Integer(resJsonObject.get("UserId").toString()));
        userVO.setRoleId(Integer.parseInt(resJsonObject.get("RoleId").toString().trim()));

        return userVO;
    }

    public static String[] getBuilding()throws Exception {
        String [] buildings = null;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Cmd","getBuildings");
                    Socket socket = new Socket(Service.IP, 7001);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(jsonObject.toJSONString());
                    dataOutputStream.flush();

                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String buldings = dataInputStream.readUTF();
                    JSONParser jsonParser = new JSONParser();
                    JSONObject replyobj = (JSONObject) jsonParser.parse(buldings);
                    String bulding = replyobj.get("Buildings").toString();
                    System.out.println(bulding);
                    buildings = bulding.split(",");
              return buildings;
    }

    public static void generateDeviceID() {
        FileOutputStream fileOutputStream = null;
        String Id = "" + System.currentTimeMillis();
        String DeviceId = Id.substring(Id.length()-5,Id.length());
        System.out.println(DeviceId);
        try {
            File f3 = new File(Environment.getExternalStorageDirectory() + "/DeviceId.txt");
            if (!f3.exists()) {
                System.out.println("Making DIR");
                fileOutputStream = new FileOutputStream(f3.getAbsolutePath());
                fileOutputStream.write(DeviceId.getBytes());
                System.out.println("fileMade");
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readDeviceId() {
        String id = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/DeviceId.txt");
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            id = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id.trim();
    }

    public static void writeUserInfo(UserVO userVO) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/UserConfig.txt");
            String info = userVO.getRoleId() + "," + userVO.getUserId();
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserVO readUserInfo() {
        UserVO UserVO = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/UserConfig.txt");
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            String s = new String(data);
            UserVO = new UserVO();
            String[] d = s.split(",");

            UserVO.setRoleId(new Integer(Integer.parseInt(d[0].trim())));
            UserVO.setUserId(d[1].trim());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return UserVO;
    }

    public static String send(String jsonRequest) throws Exception{
        String jsonResponse = null;
        Socket socket = null;
        try {
            socket = new Socket(IP, 7001);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(5000);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //dataOutputStream.writeInt(1);
            JSONObject jsonObject = (JSONObject) JSON_PARSER.parse(jsonRequest);
            jsonObject.put("Version",VERSION);
            jsonObject.put("DeviceID",readDeviceId());
            dataOutputStream.writeUTF(jsonObject.toJSONString());
            dataOutputStream.flush();

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            jsonResponse = dataInputStream.readUTF();
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();

            System.out.println("Server response --> "+jsonResponse);
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }finally {
            try {
                socket.close();
            }catch (Exception e){}
        }
        return  jsonResponse;
    }

    public static void writeIP(@NotNull String input, Activity activity) {

        //new FileOutputStream(IP_FILE_PATH);
        input.trim();
        try {
            FileOutputStream fos = activity.openFileOutput("IPadd.txt", activity.MODE_PRIVATE);
            fos.write(input.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendMails(int maidId){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","SendMails");
            jsonObject.put("MaidId",maidId);
            send(jsonObject.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static int confirmEntry(int token){
        int reply = -1;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","confirmEntry");
            jsonObject.put("token",token);

            String replystr = send(jsonObject.toJSONString());
            JSONParser jsonParser = new JSONParser();
            JSONObject replyobj = (JSONObject) jsonParser.parse(replystr);

            reply = Integer.parseInt(replyobj.get("entryfound").toString().trim());
        }catch (Exception e){
            e.printStackTrace();
        }
        return reply;
    }

    public static int saveVisitor(VisitorVO visitorVO, int type){
        JSONObject replyobj = null;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","saveVisitor");
            if(type == 0) {
                jsonObject.put("type",type);
                jsonObject.put("token",visitorVO.getToken());
                jsonObject.put("temp",visitorVO.getTemperature());
                jsonObject.put("o2level",visitorVO.getO2level());
            }else if(type == 1){
                jsonObject.put("type",type);
                jsonObject.put("token",visitorVO.getToken());
                jsonObject.put("temp",visitorVO.getTemperature());
                jsonObject.put("o2level",visitorVO.getO2level());
                jsonObject.put("name",visitorVO.getName());
                jsonObject.put("phone",visitorVO.getPhone());
                jsonObject.put("address",visitorVO.getAddress());
            }
            String reply = send(jsonObject.toJSONString());
            JSONParser jsonParser = new JSONParser();
            replyobj = (JSONObject) jsonParser.parse(reply);
            Aquila_reg.id = Integer.parseInt(replyobj.get("id").toString().trim());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(replyobj.get("ErrorCode").toString().trim());
    }

    public static SystemVO getSystemInfo() throws Exception {
        SystemVO systemVO = new SystemVO();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","getSystemInfo");
            jsonObject.put("DeviceID",readDeviceId());
            String reply = send(jsonObject.toJSONString());
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject1 = (JSONObject) jsonParser.parse(reply);
        JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonObject1.get("CPU").toString());
        Iterator iterator = jsonArray.iterator();
        System.out.println(jsonArray.size());
        HashMap hashMap = new HashMap();
        while (iterator.hasNext()){
            JSONObject jsonObjectr = (JSONObject) iterator.next();
            String cpu = (String)jsonObjectr.get("cpu");
            long time = Long.parseLong(jsonObjectr.get("time").toString().trim());
            System.out.println("-->"+time);
            hashMap.put(time,cpu);
        }
            System.out.println("final map"+hashMap);
            systemVO.setCupLog(hashMap);
            systemVO.setRAM(Integer.parseInt(jsonObject1.get("RAM").toString().trim()));
            systemVO.setStorage(Integer.parseInt(jsonObject1.get("Storage").toString().trim()));
            systemVO.setUPTime(jsonObject1.get("UPTime").toString());
            systemVO.setStartTime(jsonObject1.get("StartedAt").toString());
        return systemVO;
    }

    public static ArrayList<DevicesVO> getdevicesList() {
        ArrayList<DevicesVO> devicesVOS = new ArrayList();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","getdevices");
            jsonObject.put("DeviceID",Service.readDeviceId());
            String res = send(jsonObject.toJSONString());
            JSONParser jsonParser = new JSONParser();
            JSONObject replyobj = (JSONObject) jsonParser.parse(res);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(replyobj.get("devicesinfo").toString());
            System.out.println("res array --> "+replyobj.get("devicesinfo").toString());
            for(int i = 0;i<jsonArray.size();i++){
                JSONObject jsonObject1 = (JSONObject) jsonParser.parse(jsonArray.get(i).toString());
                DevicesVO devicesVO = new DevicesVO();
                devicesVO.setDeviceid(jsonObject1.get("deviceID").toString());
                devicesVO.setName(jsonObject1.get("name").toString());
                devicesVO.setStatus(jsonObject1.get("status").toString());
                devicesVOS.add(devicesVO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Iterator iterator = devicesVOS.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        return devicesVOS;
    }

    public static void saveDevices() {
        ArrayList<DevicesVO> devicesVOS = Devices.devicesVOS;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","saveDevices");
            jsonObject.put("DeviceID",readDeviceId());
            jsonObject.put("version",VERSION);
            JSONArray jsonArray = new JSONArray();

            for(int i = 0;i<devicesVOS.size();i++){
                JSONObject jsonObject1 = new JSONObject();
                DevicesVO devicesVO = devicesVOS.get(i);
                jsonObject1.put("id",devicesVO.getDeviceid().trim());
                jsonObject1.put("name",devicesVO.getName().trim());
                jsonObject1.put("status",devicesVO.getStatus().trim());
                jsonArray.add(jsonObject1);
            }
            jsonObject.put("devices",jsonArray);
            System.out.println("req--> "+jsonObject.toJSONString());
            send(jsonObject.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getnextInt(){
        String nexttoken =  "";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","getnexttoken");
            jsonObject.put("DeviceID",readDeviceId());
            jsonObject.put("version",VERSION);

            String relpy = send(jsonObject.toJSONString());

            JSONParser jsonParser = new JSONParser();

            JSONObject replyobj = (JSONObject) jsonParser.parse(relpy);
            nexttoken = replyobj.get("token").toString().trim();
        }catch (Exception e){
            e.printStackTrace();
        }
        return nexttoken;
    }
    public static ArrayList<BlackListVO> getAllBan(){
        ArrayList<BlackListVO> list = new ArrayList();
        try{
            JSONObject jsonObject = new JSONObject();
                jsonObject.put("Cmd","getAllbanMembers");
            jsonObject.put("DeviceID",Service.readDeviceId());
            jsonObject.put("version",VERSION);
            String r = send(jsonObject.toJSONString());
            JSONObject replyOBJ = (JSONObject) JSON_PARSER.parse(r);
            JSONArray jsonArray = (JSONArray) JSON_PARSER.parse(replyOBJ.get("banpeople").toString());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Iterator iterator = jsonArray.iterator();

            while (iterator.hasNext()){
                JSONObject j = (JSONObject) iterator.next();
                String type = j.get("type").toString().trim();

                if(type.equalsIgnoreCase("blacklist")){
                    String name = j.get("name").toString();
                    int token = Integer.parseInt(j.get("token").toString().trim());
                    int id = Integer.parseInt(j.get("id").toString().trim());
                    int maidid =  Integer.parseInt(j.get("maidid").toString().trim());
                    String  fromdatestr = j.get("banfrom").toString();
                    String  todatestr = j.get("banto").toString();

                    Date fromdate = simpleDateFormat.parse(fromdatestr);
                    Date todate =  simpleDateFormat.parse(todatestr);

                    BlackListVO blackListVO = new BlackListVO();
                    blackListVO.setId(id);
                    blackListVO.setToken(token);
                    blackListVO.setMaidId(maidid);
                    blackListVO.setFromDate(fromdate);
                    blackListVO.setToDate(todate);
                    blackListVO.setName(name);
                    blackListVO.setType(type);
                    list.add(blackListVO);
                }else {
                    int id = Integer.parseInt(j.get("id").toString().trim());
                    int token =  Integer.parseInt(j.get("token").toString().trim());
                    String  name = j.get("name").toString();

                    BlackListVO blackListVO = new BlackListVO();
                    blackListVO.setName(name);
                    blackListVO.setMaidId(id);
                    blackListVO.setToken(token);
                    blackListVO.setType(type);
                    list.add(blackListVO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public static String addToBlacklist(BlackListVO blackListVO){
        String res = "error";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Cmd","addtoblacklist");
            jsonObject.put("DeviceID",readDeviceId());
            jsonObject.put("Version",VERSION);
            jsonObject.put("maidId",getMaidId(blackListVO.getToken()));
            jsonObject.put("fromdate",simpleDateFormat.format(blackListVO.getFromDate()));
            jsonObject.put("todate",simpleDateFormat.format(blackListVO.getToDate()));
            jsonObject.put("id",blackListVO.getId());
            res = send(jsonObject.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static void saveGatevisitor(GateVisitorVO gateVisitorVO) {
        try{
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Cmd","addGateVisitor");
            jsonObject.put("DeviceID",readDeviceId());
            jsonObject.put("Name",gateVisitorVO.getName());
            jsonObject.put("Phone Number",gateVisitorVO.getMobile());
            jsonObject.put("Car Number",gateVisitorVO.getCarno());
            jsonObject.put("Bulding",gateVisitorVO.getBuilding());
            jsonObject.put("Reason",gateVisitorVO.getReason());
            jsonObject.put("Type",gateVisitorVO.getType());

            String res = send(jsonObject.toJSONString());
            JSONObject reply = (JSONObject) JSON_PARSER.parse(res);
            String img = "GV"+reply.get("id").toString().trim();
            Socket socket = new Socket(IP,7002);
            socket.setSoTimeout(10*1000);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("SaveImage");
            dataOutputStream.writeUTF(img);
            dataOutputStream.writeInt(gateVisitorVO.getImage().length);
            dataOutputStream.write(gateVisitorVO.getImage());

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            System.out.println(dataInputStream.readUTF());

            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}